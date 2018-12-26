package com.yz.cache.common;
 

import org.apache.ibatis.mapping.BoundSql;
 

/**
 * @desc cache 上下文环境 
 * @author Administrator
 *
 */
public class CacheContent {

	private ThreadLocal<BoundSql> _localBoundSql = new ThreadLocal<>();

	private ThreadLocal<YzCacheBean> _localCacheBean = ThreadLocal.withInitial(YzCacheBean::new);

	private CacheContent() {  
	}

	private static class CacheContentHolder {
		private static CacheContent _instance = new CacheContent();
	}

	/**
	 * 
	 * @return
	 */
	public static CacheContent getInstance() {
		return CacheContentHolder._instance;
	}

	public void setBoundSql(BoundSql sql) {
		this._localBoundSql.set(sql);
	}

	public void setCacheBean(YzCacheBean cacheBean) {
		this._localCacheBean.set(cacheBean);
	}

	public BoundSql getBoundSql() {
		return this._localBoundSql.get();
	}

	public YzCacheBean getCacheBean() {
		return this._localCacheBean.get();
	}
	
	public void clear()
	{
		this._localBoundSql.remove();
		this._localCacheBean.remove();
	}

}
