package com.yz.cache.handler;

/**
 * @desc 缓存的处理类
 * @author Administrator
 *
 */
public interface RedisCacheHandler {

	/**
	 * @desc 将数据缓存方法
	 * @param redisName redis指定数据源
	 * @param key Redis cache
	 * @param result 存入Redis的原数据
	 * @param cacheExpire 缓存失效时间 单位为妙
	 */
	public void setCache(String redisName,String key,Object result,int cacheExpire);
	
	
	/**
	 * @desc 根据key取出相应的缓存值
	 * @param redisName redis指定数据源
	 * @param key RedisKey
	 * @return 
	 */
	public Object getCache(String redisName,String key,Class<?>cls);
	
}
