package com.yz.cache.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yz.constants.CommonConstants;
 

/**
 * 
 * @desc yzCache 配置 
 * @author Administrator
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface YzCache {

    public boolean useCache()default false; //是否启用缓存
    
    public String cachePrefix()default ""; //缓存前缀 
	
	public String cacheKey()default ""; //缓存的key支持正则表达式 
	
	public String cacheRule() default "commonCacheRule" ; //缓存key的规则
	
	public String cacheHandler() default CommonConstants.APP_CACHE_HANDLER; // 缓存的具体处理程序
	
	public int expire()default 3600; //默认过期时间 单位秒
	
	public String redisName()default "common";//对应的redis连接
	
    public boolean sync()default true;//同步 
    
    public String cacheRelation()default ""; //关联cache的方法
}
