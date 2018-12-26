package com.yz.model;


/**
 * 
 * @author lingdian
 *
 */
public class YzServiceInfo implements java.io.Serializable, Comparable<YzServiceInfo> 
{
	
	private Long id ; 

	private String host; // host 
	
	private String name; // gateway 调用方法名称

	private String interfaceName; // 接口 全路径

	private String interfaceMethod;// 接口方法

	private String appName;// 应用名称

	private String methodRemark; // 方法备注

	private String appType; // app类型

	private boolean needLogin;// 是否需要登录

	private boolean needTrans; // 是否需要交易
	
	private boolean needToken ; // 是否需要token验证 

	private String version = "1.0"; // 版本
	
	private boolean local = false; // 本地服务 

	private boolean useCache; //是否启用缓存配置
	
	private String cacheKey; // cache.key 
	
	private String cacheRule;// cache.key.rule
	
	private int cacheExpire; // cache 过期时间
	
	private String redisName; // redis 名称 
	
	private String cacheHandler;//cacheHandler 缓存处理程序
	
	private int timeout = 3000;  // 调用超时时间
	
	private boolean async = false; // 是否异步调用 
	 
	private boolean receiveSent = false ; // 是否接受异步回调结果
	  
	public boolean isReceiveSent() {
		return receiveSent;
	}

	public void setReceiveSent(boolean receiveSent) {
		this.receiveSent = receiveSent;
	}
 
	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public void setCacheExpire(int cacheExpire) {
		this.cacheExpire = cacheExpire;
	}
	
	public int getCacheExpire() {
		return cacheExpire;
	}
	 
	
    public boolean isNeedToken() {
		return needToken;
	}

	public void setNeedToken(boolean needToken) {
		this.needToken = needToken;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
    
    public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
 
	
	public void setCacheHandler(String cacheHandler) {
		this.cacheHandler = cacheHandler;
	}
	
	public String getCacheHandler() {
		return cacheHandler;
	}
	
	public void setLocal(boolean local) {
		this.local = local;
	}
	
	
	public boolean isLocal() {
		return local;
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}

	public boolean isNeedTrans() {
		return needTrans;
	}

	public void setNeedTrans(boolean needTrans) {
		this.needTrans = needTrans;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getInterfaceMethod() {
		return interfaceMethod;
	}

	public void setInterfaceMethod(String interfaceMethod) {
		this.interfaceMethod = interfaceMethod;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMethodRemark() {
		return methodRemark;
	}

	public void setMethodRemark(String methodRemark) {
		this.methodRemark = methodRemark;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public boolean isNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getRedisName() {
		return redisName;
	}

	public void setRedisName(String redisName) {
		this.redisName = redisName;
	}


	@Override
	public boolean equals(Object obj) {
		return obj != null && (super.equals(obj) || this.hashCode() == obj.hashCode());
	}

	@Override
	public int compareTo(YzServiceInfo o) {
		return id>o.getId() ? 1 : -1;
	}
}
