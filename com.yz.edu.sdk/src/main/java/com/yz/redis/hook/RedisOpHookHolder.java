package com.yz.redis.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.constants.RedisKeyConstants;
import com.yz.model.GetTokenChannel; 
/**
 * 
 * RedisHookFactory
 * 
 * @author Administrator
 *
 */
public interface RedisOpHookHolder {

	static Logger logger = LoggerFactory.getLogger(RedisOpHookHolder.class);

	public static int RedisHookFactory = 0;

	public static int GET_WECHAT_TOKEN_TYPE = 0;

	public static int GET_WECHAT_JSAPITICKET_TYPE = 1;

	public static int GET_JD_TOKEN_TYPE = 2;
	 
	public static RedisOpHook<GetTokenChannel> GET_WECHAT_TOKEN_HOOK = new DefaultRedisOpHook(GET_WECHAT_TOKEN_TYPE,RedisKeyConstants.GET_WECHAT_TOKEN_CHANNEL);

	public static RedisOpHook<GetTokenChannel> GET_JD_TOKEN_HOOK =new DefaultRedisOpHook(GET_JD_TOKEN_TYPE,RedisKeyConstants.GET_JD_TOKEN_CHANNEL);
	
	public static RedisOpHook<GetTokenChannel> GET_WECHAT_JSAPITICKET_HOOK =new DefaultRedisOpHook(GET_WECHAT_JSAPITICKET_TYPE,RedisKeyConstants.GET_WECHAT_TOKEN_CHANNEL);
}
