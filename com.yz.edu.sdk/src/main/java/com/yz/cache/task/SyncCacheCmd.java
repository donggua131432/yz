package com.yz.cache.task;

import java.util.Date;

import com.yz.cache.common.YzCacheBean;
 

/**
 * 
 * @author Administrator
 *
 */
public class SyncCacheCmd implements java.io.Serializable {

	private String nameSpace;// 缓存的命名空间 map结构 key

	private String key; // 当前更新的key

	private String redisName; // 制定redis的实例名称

	private Date createDate; // 创建时间

	private String cacheRelation; // cache 相关
	
	private String cacheHandler;  // 缓存处理类 
	
	private int cacheExpire; //缓存失效时间
	
	private Object param ; // 输入参数
	
	
	public SyncCacheCmd()
	{
		
	}

	public SyncCacheCmd(String key, YzCacheBean cache) {
		this.key = key;
		this.nameSpace = cache.getMapKey();
		this.redisName = cache.getRedisName();
		this.createDate = new Date();
		this.cacheRelation = cache.getCacheRelation();
	}
	
	
	public void setCacheExpire(int cacheExpire) {
		this.cacheExpire = cacheExpire;
	}
	
	public int getCacheExpire() {
		return cacheExpire;
	}
	
	public void setParam(Object param) {
		this.param = param;
	}
	
	public Object getParam() {
		return param;
	}
	
	public void setCacheHandler(String cacheHandler) {
		this.cacheHandler = cacheHandler;
	}
	
	public String getCacheHandler() {
		return cacheHandler;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRedisName() {
		return redisName;
	}

	public void setRedisName(String redisName) {
		this.redisName = redisName;
	}

	public Date getCreateDate() {
		return createDate == null ? null : (Date) createDate.clone();
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate == null ? null : (Date) createDate.clone();
	}

	public String getCacheRelation() {
		return cacheRelation;
	}

	public void setCacheRelation(String cacheRelation) {
		this.cacheRelation = cacheRelation;
	}
}