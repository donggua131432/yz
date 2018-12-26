package com.yz.cache.handler;

import com.alibaba.fastjson.JSON;
import com.yz.constants.CommonConstants;
import com.yz.redis.RedisService;
import com.yz.util.ClassUtil;
import com.yz.util.StringUtil;

import org.springframework.stereotype.Component;

@Component(value = CommonConstants.COMMON_CACHE_HANDLER)
/**
 * bcc 网关缓存处理类
 * 
 * @author Administrator
 *
 */
public class BccCacheHandler extends BaseRedisCacheHandler {

	@Override
	public void setCache(String redisName, String key, Object result, int cacheExpire) {
		RedisService.getRedisService(redisName).putObject(key, result, cacheExpire);
		logger.info("setCache:{}", key);
	}

	@Override
	public Object getCache(String redisName, String key, Class<?> cls) {
		String str = RedisService.getRedisService(redisName).get(key);
		if (StringUtil.isBlank(str)) {
			return null;
		}
		if (ClassUtil.isBaseDataType(cls)) {
			return str;
		}
		logger.info("getCache:{}", key);
		Object obj = JSON.parse(String.valueOf(str));
		logger.info("getCache:{}", key);
		return obj;

	}

}
