package com.yz.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.exception.SystemException;
import com.yz.util.StringUtil;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author lingdian
 *
 */
public class YzRedisFactory {

	private static String DEFAULT_REDIS_POOL = "default_redis_pool";

	private static Logger logger = LoggerFactory.getLogger(YzRedisFactory.class);

	private static Map<String, YzJedisPool> jedisCacheMap = new HashMap<>();

	private static Map<String, RedisService> redisServiceCache = new ConcurrentHashMap<>();

	private YzRedisFactory() {

	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private static class MhsRedisFactoryHodler {
		private static YzRedisFactory instance = new YzRedisFactory();
	}

	/**
	 * 
	 * @return
	 */
	public static YzRedisFactory getInstance() {
		return MhsRedisFactoryHodler.instance;
	}

	/**
	 * @desc 废弃  直接调用 RedisService.getRedisService()
	 * @return
	 */
	@Deprecated
	public RedisService getRedisService() {
		if (redisServiceCache.containsKey(DEFAULT_REDIS_POOL)) {
			return redisServiceCache.get(DEFAULT_REDIS_POOL);
		}
		YzJedisPool pool = getDefaultRedisPool();
		RedisService redisService = RedisService.newRedisService(pool);
		redisServiceCache.putIfAbsent(DEFAULT_REDIS_POOL, redisService);
		return redisService;
	}
	
	
	/**
	 * @desc 废弃  直接调用 RedisService.getRedisService(name)
	 * @return
	 */
	@Deprecated
	public RedisService getRedisService(String name) {
		if (redisServiceCache.containsKey(name)) {
			return redisServiceCache.get(name);
		}
		YzJedisPool pool = getRedisPool(name);
		RedisService redisService = RedisService.newRedisService(pool);
		redisServiceCache.putIfAbsent(name, redisService);
		return redisService;
	}

	/**
	 * 
	 * @param config
	 */
	public void init(YzRedisBean config) {
		List<YzRedisInfo> redisInfos = config.getRedisInfos();
		if (redisInfos != null && !redisInfos.isEmpty()) {
			JedisPoolConfig conf = new JedisPoolConfig();
			conf.setLifo(true);
			conf.setMaxIdle(config.getMaxIdle());
			conf.setMinIdle(config.getMinIdle());
			conf.setMaxTotal(config.getMaxActive());
			conf.setBlockWhenExhausted(false);
			conf.setTestOnBorrow(config.isTestOnBorrow());
			conf.setTestOnReturn(config.isTestOnReturn());
			conf.setTestOnCreate(true);
			conf.setMaxWaitMillis(config.getMaxWait());
			for (YzRedisInfo redisInfo : redisInfos) {
				initRedisPool(conf, redisInfo, config.getTimeout());
			}
		}
	}

	/**
	 * 
	 * @param config
	 * @param redisInfo
	 * @param timeOut
	 */
	private void initRedisPool(GenericObjectPoolConfig config, YzRedisInfo redisInfo, int timeOut) {
		YzJedisPool jedisPool = new YzJedisPool(config, redisInfo.getHost(), redisInfo.getPort(), timeOut,
				redisInfo.getAuthKey());
		jedisPool.setDefault(redisInfo.isDefault());
		jedisPool.setName(redisInfo.getName());
		String name = redisInfo.getName();
		logger.info("initRedisPool:{}", name);
		jedisCacheMap.put(name, jedisPool);
	}

	/**
	 * 
	 * @param name
	 */
	private YzJedisPool getRedisPool(String name) {
		for (YzJedisPool redisPool : jedisCacheMap.values()) {
			if (StringUtil.equals(name, redisPool.getName())) {
				return redisPool;
			}
		}
		return getDefaultRedisPool();
	}

	/**
	 * 
	 * @return
	 */
	private YzJedisPool getDefaultRedisPool() {
		for (YzJedisPool redisPool : jedisCacheMap.values()) {
			if (redisPool.isDefault()) {
				return redisPool;
			}
		}
		logger.error("not found defaultRedisPool");
		throw new SystemException("config error ,please check config !");
	}

	/**
	 * 
	 */
	@PreDestroy
	public static void destory() {
		for (YzJedisPool jedisPool : jedisCacheMap.values()) {
			if (jedisPool != null && !jedisPool.isClosed()) {
				jedisPool.close();
			}
		} 
	} 
}
