package com.yz.redis.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.yz.model.GetTokenChannel; 
import com.yz.util.JsonUtil;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author Administrator
 *
 */
public class DefaultRedisOpHook implements RedisOpHook<GetTokenChannel> {

	static Logger logger = LoggerFactory.getLogger(DefaultRedisOpHook.class);

	private int tokenType;
	
	private String channel ;

	public DefaultRedisOpHook(int tokenType,String channel) {
		this.tokenType = tokenType;
		this.channel = channel;
	}

	@Override
	public GetTokenChannel execute(Jedis redis, Object param) {
		GetTokenChannel channel = new GetTokenChannel();
		channel.setTokenType(tokenType);
		channel.setParam(param);
		redis.publish(this.channel , JsonUtil.object2String(channel));
		sleep(200);
		return null;
	}

}
