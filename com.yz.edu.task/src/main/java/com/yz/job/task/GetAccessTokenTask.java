package com.yz.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.common.YzTaskContext;
import com.yz.job.service.JDAccessTokenService;
import com.yz.job.service.WeChatAccessTokenService;
import com.yz.redis.hook.RedisOpHookHolder;
import com.yz.task.YzTaskConstants;


@Component(value = "getAccessTokenTask")
@YzJob(taskDesc = YzTaskConstants.YZ_ACCESS_TOKEN_TASK, cron = "0 */1 * * * ?", shardingTotalCount = 1,log = true)
/**
 * 
 * @desc 获取微信推送消息的OpenId的Token
 * @author Administrator
 *
 */
public class GetAccessTokenTask extends AbstractSimpleTask {

	@Value("${wehchat_pub.pub_id}")
	private String pubId; // 推送微信消息的公众号

	@Autowired
	private WeChatAccessTokenService weChatAccessTokenService;
	
	@Autowired
	private JDAccessTokenService jdAccessTokenService;

	@Override
	public void executeOther() {
		//微信token
	    long ttl = getRedisService().ttl(pubId);
		if (ttl <= 600) // token 如果存活时间小于10分钟 重新生成accesstoken
		{
			String accessToken = weChatAccessTokenService.getAccessToken(pubId);
			YzTaskContext.getTaskContext().addEventDetail(accessToken, "获取accessToken【"+accessToken+"】");
			getRedisService().setex(pubId, accessToken, 7200);//2小时的有效期
		}
		//微信jsapi
		long jsapittl = getRedisService().ttl("jsapiTicket" );
		if (jsapittl <= 600) {
			String token =getRedisService().get(pubId, RedisOpHookHolder.GET_WECHAT_TOKEN_HOOK);
			String jsapiTicket = weChatAccessTokenService.getJsapiTicket(token);
			YzTaskContext.getTaskContext().addEventDetail(jsapiTicket, "获取jsapiTicket【"+jsapiTicket+"】");
			getRedisService().setex("jsapiTicket", jsapiTicket, 7200);//2小时的有效期
		}
		//京东token(商品池,商品)
		long jdttl = getRedisService().ttl("jdAccessToken");
		if(jdttl <= 600){ //token 如果存活时间小于10分钟 重新生成accesstoken
			String accessToken = jdAccessTokenService.getAccessToken();
			YzTaskContext.getTaskContext().addEventDetail(accessToken, "获取jdAccessToken【"+accessToken+"】");
			getRedisService().setex("jdAccessToken", accessToken, 82800); //24小时的有效期
		}
		//实体卡的token (下单时用到)
		long jdEntityCardttl = getRedisService().ttl("jdEntityCardToken");
		if(jdEntityCardttl <= 600){
			String accessToken = jdAccessTokenService.getEntityCardAccessToken();
			YzTaskContext.getTaskContext().addEventDetail(accessToken, "获取jdEntityCardToken【"+accessToken+"】");
			getRedisService().setex("jdEntityCardToken", accessToken, 82800);
		}
		
	}
}
