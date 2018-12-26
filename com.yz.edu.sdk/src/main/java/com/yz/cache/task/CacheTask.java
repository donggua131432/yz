package com.yz.cache.task;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WorkHandler; 
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yz.cache.handler.RedisCacheHandler;
import com.yz.redis.RedisService;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.RegUtils;
import com.yz.util.StringUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Administrator
 *
 */
public class CacheTask {

	private Logger logger = LoggerFactory.getLogger(CacheTask.class);

	private ExecutorService executor = null;

	private Disruptor<SyncCacheCmd> disruptor = null;

	/** 
	 * 
	 * 
	 */
	private CacheTask() {
		executor = Executors.newSingleThreadExecutor(r -> {
			Thread thread = new Thread(r);
			thread.setName("CacheTaskThread");
			return thread;
		});
		disruptor = new Disruptor<>(SyncCacheCmd::new, 128, executor, ProducerType.SINGLE,
				new SleepingWaitStrategy());
		startTask();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			disruptor.shutdown();
			executor.shutdown();
		}));
	}

	private enum CacheTaskHolder {
		_instance;
		CacheTask task = new CacheTask();

		public CacheTask getTask() {
			return task;
		}

	}

	/**
	 * @desc 返回CacheTask实例
	 * @return
	 */
	public static CacheTask getInstance() {
		return CacheTaskHolder._instance.getTask();
	}

	/**
	 * 
	 * @param key
	 * @param cache
	 */
	public void addCacheTask(SyncCacheCmd info) {
		this.disruptor.getRingBuffer().publishEvent(new EventTranslator<SyncCacheCmd>() {
			@Override
			public void translateTo(SyncCacheCmd event, long sequence) {
				try {
					BeanUtils.copyProperties(event, info);
				} catch (IllegalAccessException | InvocationTargetException e) {
					logger.error(ExceptionUtil.getStackTrace(e));
				}
			}
		});
		logger.info("addCacheTask.info=>{}", JsonUtil.object2String(info));
	}

	/**
	 * 
	 * 
	 * @desc 启动任务调度命令
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void startTask() {
		disruptor.handleEventsWith(new CacheEventHandler());
		disruptor.handleExceptionsWith(new IgnoreExceptionHandler());
		disruptor.start();
	}

	/**
	 * @desc 根据缓存策略更新相应的缓存
	 * @param info
	 */
	public void syncCache(SyncCacheCmd info) {
		String cacheHandler = info.getCacheHandler();
		if (StringUtil.isNotBlank(cacheHandler)) {
			RedisCacheHandler handler = ApplicationContextUtil.getBean(info.getCacheHandler(), RedisCacheHandler.class);
			handler.setCache(info.getRedisName(), info.getKey(), info.getParam(),info.getCacheExpire());
			return;
		}
		String relation = info.getCacheRelation();
		if (StringUtil.isNotBlank(relation)) {
			String[] rule = StringUtil.split(relation);
			Set<String> keys = RedisService.getRedisService(info.getRedisName()).hkeys(info.getNameSpace());
			if (rule != null && keys != null) {
				List<String> delKeys = this.searchKey(rule, keys);
				if (!delKeys.isEmpty()) {
					String[] delArr = new String[delKeys.size()];
					delKeys.toArray(delArr);
					logger.info("modifyCache:{},redisKey:{}", info.getNameSpace(), delArr);
					RedisService.getRedisService(info.getRedisName()).hdel(info.getNameSpace(), delArr);
				}
			}
		} else {
			RedisService.getRedisService(info.getRedisName()).del(info.getNameSpace());
		}
	}

	/**
	 * 
	 * @param rule
	 * @param keys
	 * @return
	 */
	private List<String> searchKey(String[] rules, Set<String> keys) {
		Iterator<String> iter = keys.iterator();
		List<String> result = new ArrayList<>();
		while (iter.hasNext()) {
			String key = iter.next();
			for (String rule : rules) {
				if (RegUtils.isMatch(rule, key)) {
					result.add(key);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 
	 * @author lingdian
	 *
	 */
	private class CacheEventHandler implements EventHandler<SyncCacheCmd>, WorkHandler<SyncCacheCmd> {

		@Override
		public void onEvent(SyncCacheCmd event) throws Exception {
			logger.info("onEvent.invoker,envetInfo:{}", JsonUtil.object2String(event));
			syncCache(event);
		}

		@Override
		public void onEvent(SyncCacheCmd event, long sequence, boolean endOfBatch) throws Exception {
			this.onEvent(event);
		}

	}

}
