package com.yz.job.sub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yz.constants.RedisKeyConstants;
import com.yz.job.service.JDAccessTokenService;
import com.yz.job.service.WeChatAccessTokenService;
import com.yz.model.GetTokenChannel;
import com.yz.redis.RedisService; 
import com.yz.redis.hook.RedisOpHookHolder;
import com.yz.sub.JedisPubSub;
import com.yz.sub.JedisPubSubHandler;

@Component(value = "getTokenPubHandler")
@JedisPubSub(enable = true, channel = RedisKeyConstants.GET_JD_OR_WECHAT_TOKEN_CHANNEL, target = GetTokenChannel.class)
/**
 * @desc 订阅获取Token信息
 * @author Administrator
 *
 */
public class GetTokenPubHandler extends JedisPubSubHandler<GetTokenChannel> {

	@Value("${wehchat_pub.pub_id}")
	private String pubId; // 推送微信消息的公众号

	@Autowired
	private WeChatAccessTokenService weChatAccessTokenService;

	@Autowired
	private JDAccessTokenService jdAccessTokenService;

	@Override
	public void execute(GetTokenChannel token) {
		if (RedisService.getRedisService().incr(String.valueOf(token.getId()), 5) == 1) {
			int tokenType = token.getTokenType();
			switch (tokenType) {
			case RedisOpHookHolder.GET_WECHAT_TOKEN_TYPE:
				String accessToken = weChatAccessTokenService.getAccessToken(pubId);
				RedisService.getRedisService().setex(pubId, accessToken, 7200);// 2小时的有效期
				break;
			case RedisOpHookHolder.GET_WECHAT_JSAPITICKET_TYPE:
				String tokenStr = RedisService.getRedisService().get(pubId, RedisOpHookHolder.GET_WECHAT_TOKEN_HOOK);
				String jsapiTicket = weChatAccessTokenService.getJsapiTicket(tokenStr);
				RedisService.getRedisService().setex("jsapiTicket", jsapiTicket, 7200);// 2小时的有效期
				break;
			case RedisOpHookHolder.GET_JD_TOKEN_TYPE:
				accessToken = jdAccessTokenService.getAccessToken();
				RedisService.getRedisService().setex("jdAccessToken", accessToken, 82800); // 24小时的有效期
				break;
			default:
				break;
			}
		}
	}

}
