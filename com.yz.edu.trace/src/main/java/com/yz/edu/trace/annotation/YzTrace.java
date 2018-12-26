package com.yz.edu.trace.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.yz.util.StringUtil;
 

/**
 * @desc 方法体级别的元注释 
 *       用以标志方法是否创建TraceSpan
 * @author lingdian 
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YzTrace {
 
	public String remark() default StringUtil.EMPTY; // 方法备注
	
}