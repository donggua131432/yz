package com.yz.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
	/**
	 * 是否检测登录
	 * @return
	 */
	public boolean checkLogin() default true;
	/**
	 * 是否检测权限
	 * @return
	 */
	public boolean checkRule() default true;
	
	/**
	 * 访问该方法所需权限编码
	 * @return
	 */
	public String[] value() default "";

}
