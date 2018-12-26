package com.yz.core.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Token {
	/**
	 * 操作类型 
	 * @Flag.Save - 存入Token
	 * @Flag.Remove - 验证并移除Token
	 * @return
	 */
	public Flag action();
	/**
	 * 功能组编号，
	 * @return
	 */
	public String groupId();
	
	
	
	public static enum Flag {
		Save,
		Remove;
	}
	
}
