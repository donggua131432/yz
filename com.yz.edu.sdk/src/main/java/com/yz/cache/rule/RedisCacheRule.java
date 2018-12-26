package com.yz.cache.rule;

import java.util.Map;

import com.yz.constants.CommonConstants; 

/**
 * @desc 缓存bcc gate 调用缓存
 * @desc 通过CacheKey ，CacheRule 获取到相应的CacheKey
 * @desc RedisCache的规则
 * @author Administrator
 *
 */
public interface RedisCacheRule extends CommonConstants {

	/**
	 * @desc 根据cacheKey获取CacheKey
	 * @param cacheKey
	 * @param header
	 * @param body
	 * @return
	 */
	public String getCacheName(String cacheKey,Map<String,Object> mhsMap);
}
