package com.yz.core.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 校验管理
 * @author cyf
 *
 */
@Configuration
public class IHandlerAutoConfiguration {

	/**
	 * 检测链 
	 * @return
	 */
	@Bean(name="wechatHandlerChain")
	public CheckHandlerChain handlerChain() {
		CheckHandlerChain handlerChain = new CheckHandlerChain();
		//按顺序添加检测
		handlerChain.addHandler(wechatLoginCheckHandler());//身份信息校验
		handlerChain.addHandler(userStatusCheckHandler());//用户状态校验
		//handlerChain.addHandler(userStatusCheckHandler());//用户状态校验
		handlerChain.addHandler(tokenCheckHandler());//用户状态校验
		return handlerChain;
	}
	
	
	@Bean(name="wechatLoginCheckHandler")
	public LoginCheckHandler wechatLoginCheckHandler() {
		return new WechatLoginCheckHandler();
	}
	
	@Bean
	public UserStatusCheckHandler userStatusCheckHandler() {
		return new UserStatusCheckHandler();
	}
	@Bean
	public TokenCheckHandler tokenCheckHandler() {
		return new TokenCheckHandler();
	}
}
