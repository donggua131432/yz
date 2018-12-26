package com.yz.cache.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.cache.common.CacheContent;
import com.yz.cache.common.YzCacheBean;
import com.yz.util.StringUtil;

 

/**
 * 
 * @author Administrator
 *
 */
public abstract class BaseRedisCacheHandler implements RedisCacheHandler {

	protected  Logger logger = LoggerFactory.getLogger(this.getClass());

	
	/**
	 * 
	 * 
	 * @return
	 */
	public YzCacheBean getCacheBean()
	{
		YzCacheBean bean = CacheContent.getInstance().getCacheBean();
		return bean;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getExpire()
	{
		return this.getCacheBean().getExpire();
	}
	
	
	/**
	 * 
	 * @param bean
	 * @return
	 */
	public String getCachePrefix()
	{
		YzCacheBean bean = this.getCacheBean();
		String cachePrefix = bean.getCachePrefix();
		if(StringUtil.isBlank(cachePrefix))
		{
			return bean.getMapKey();
		}
		return cachePrefix;
	}
}
