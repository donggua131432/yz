package com.yz.cache.common;

/**
 * 
 * @author Administrator
 *
 */
public class YzCacheBean implements java.io.Serializable {

	private String cacheRelation; // cache 关联表达式 多个用逗号隔开

	private boolean useCache; // 是否启用缓存

	private String mapKey; // 缓存的前缀

	private String cacheKey; // 缓存的key支持正则表达式

	private String cacheRule; // 缓存key的规则

	private String cacheHandler; // 缓存的具体处理程序

	private String redisName;// 对应的redis连接

	private Class<?> returnCls; // 返回结果类

	private String cachePrefix;// 缓存前缀
	
	private int expire; //缓存过期时间 单位s

	private boolean sync = false;// 同步
	
	
	
	public void setExpire(int expire) {
		this.expire = expire;
	}
	
	public int getExpire() {
		return expire;
	}

	public void setCachePrefix(String cachePrefix) {
		this.cachePrefix = cachePrefix;
	}

	public String getCachePrefix() {
		return cachePrefix;
	}

	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}

	public String getMapKey() {
		return mapKey;
	}

	public void setReturnCls(Class<?> returnCls) {
		this.returnCls = returnCls;
	}

	public Class<?> getReturnCls() {
		return returnCls;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String getCacheRule() {
		return cacheRule;
	}

	public void setCacheRule(String cacheRule) {
		this.cacheRule = cacheRule;
	}

	public String getCacheHandler() {
		return cacheHandler;
	}

	public void setCacheHandler(String cacheHandler) {
		this.cacheHandler = cacheHandler;
	}

	public String getRedisName() {
		return redisName;
	}

	public void setRedisName(String redisName) {
		this.redisName = redisName;
	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

	public void setCacheRelation(String cacheRelation) {
		this.cacheRelation = cacheRelation;
	}

	public String getCacheRelation() {
		return cacheRelation;
	}
}
