package com.yz.edu.epl.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yz.edu.constant.ObservatoryStarConstant.ObserStarEnum;


/**
 * 
 * 
 * @author lingdian 
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface EplListener {

	public String name(); // 名称
	
    public ObserStarEnum[] alarms(); /// 预警规则
}
