package com.yz.sub;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yz.constants.CommonConstants;
import com.yz.util.StringUtil;

 

/**
 * Redis sub 订阅
 * @author Administrator
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface JedisPubSub  
{
	public int seq() default 1; // 序列 
	
	public boolean enable() default true;	//是否启用 
	   
    public String channel()default StringUtil.EMPTY;	//订阅频道
    
    public String patterns()default StringUtil.EMPTY;  //订阅模式
    
    public String convert() default CommonConstants.COMMON_CONVERT;
    
    public Class<?>target(); //目标转化类
    
    public byte pubType() default CommonConstants.REDIS_TYPE_SUB; //pub 类型  1 sub 2 pub 
}
