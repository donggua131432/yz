package com.yz.constants;

/**
 * @ProjectName mhsapp
 * @Author James
 * @Email 313635448@qq.com
 * @Date 08/07/2017
 * @Time 15:31
 */
public interface CommonConstants {
    
    public static final int OSS_ACTION_UPLOAD = 0;
    
    public static final int OSS_ACTION_COPY = 1;
    
    public static final int OSS_ACTION_DEL = 2;
    
    public static final int APP_CACHE_TTL = 60*60*48; //默认存活周期 48小时 
 

    public static final String COMMON_CONVERT = "commonConvert";
    
    public static final String COMMON_CACHE_RULE="commonCacheRule";
    
    public static final String COMMON_CACHE_HANDLER="bccCacheHandler";
    
    public static final String APP_CACHE_HANDLER="appCacheHandler";
    
    public static final String PARAMETER_CACHE_HANDLER="parameterCacheHandler";
    
    public static final String MEMORY_CACHE_HANDLER="memoryCacheHandler";
    
    public static final byte REDIS_TYPE_PUB = 2; // 发布
    
    public static final byte REDIS_TYPE_SUB = 1; // 订阅模式 
    
    public static final String REDIS_NAME_DEFAULT="default";  
}
