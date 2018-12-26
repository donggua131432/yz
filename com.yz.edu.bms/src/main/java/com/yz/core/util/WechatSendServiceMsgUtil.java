package com.yz.core.util;

import java.util.HashMap;
import java.util.Map;

import com.yz.model.WechatMsgVo;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;

/**
 * 微信发送客服信息工具类
 * @author lx
 * @date 2018年4月11日 上午10:38:15
 */
public class WechatSendServiceMsgUtil {

	
	  public static void wechatSendServiceMsg(String openId,String msgContent){
			//推送客服微信公众号信息
			WechatMsgVo msgVo = new WechatMsgVo();
			msgVo.setTouser(openId);
			Map<String, String> contentMap = new HashMap<>();
			contentMap.put("content", msgContent);
			msgVo.setContentData(contentMap);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
	  }
}
