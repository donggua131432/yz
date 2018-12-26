package com.yz.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface YzService {

	public String methodName(); //调用方法名称
	
	public String sysBelong();// 应用名称 
	
	public String methodRemark()default ""; //方法备注
	
	public String appType() default "2"; //app类型
	
	public String version() default "1.0";//服务版本号
	
	public boolean useCache()default false; //是否启用缓存
	
	public String cacheKey()default ""; //缓存的key支持正则表达式 
	
	public int cacheExpire()default 300; //缓存的key支持正则表达式 
	
	public String cacheRule() default "commonCacheRule" ; //缓存key的规则
	
	public String cacheHandler() default "bccCacheHandler"; // 缓存的具体处理程序
	
	public String redisName()default "common";//对应的redis连接
	
	public boolean needLogin() default false;//是否需要登录
	
	public boolean needTrans() default false;//是否需要登录
	
	public boolean needToken() default false; // 是否需要token
	
	public int timeout() default 30000; //超时时间30000ms
	
	public boolean async()default false;  // 是否异步执行
	
	public boolean receiveSent()default false; // 是否异步执行
}
