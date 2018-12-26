package com.yz.job.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yz.constants.WechatMsgConstants;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.job.dao.GwWechatPublicMapper;
import com.yz.job.disruptor.CollectMsgSendRespCmd;
import com.yz.job.disruptor.CollectMsgSendDisruptor;
import com.yz.job.model.GwWechatMsgTemplate;
import com.yz.model.NameValuePari;
import com.yz.model.WechatMsgVo;
import com.yz.redis.RedisService;
import com.yz.redis.hook.RedisOpHook; 
import com.yz.util.JsonUtil;
import com.yz.util.RegUtils;
import com.yz.util.StringUtil;

import redis.clients.jedis.Jedis;

/**
 * 微信公共发信息
 * @author lx
 * @date 2018年4月10日 上午11:52:26
 */
@Service
public class WechatSendMessageService {

	private static Logger logger = LoggerFactory.getLogger(WechatSendMessageService.class);
	
	//客服信息url
	private static String SERVICE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
	//模板信息url
	private static String SERVICE_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
	
	
	@Autowired
	private GwWechatPublicMapper publicMapper;
	
	@Value("${wehchat_pub.pub_id}")
	private String pubId; // 推送微信消息的公众号
	
	@Autowired
	private WeChatAccessTokenService weChatAccessTokenService;

	@Autowired
	private CollectMsgSendDisruptor msgSend;
	
	//发消息
	public void sendWechatMsg(WechatMsgVo msg,GwWechatMsgTemplate template)
	{
	    String token = RedisService.getRedisService().get(pubId	,new RedisOpHook<String>() {
				@Override
				public String execute(Jedis redis, Object param) {
					String pubId =String.valueOf(param);
					redis.set(pubId,weChatAccessTokenService.getAccessToken(pubId));
					redis.expire(pubId, 7200);
					return pubId;
				}
		} );
	    
		if(StringUtil.hasValue(msg.getTemplateId())){ //模板信息
			sendTemplateMsg(msg,token,template);
		}else{ //客服信息
			sendServiceMsg(msg, token); 
		}
		
	}
	//模板消息
	@SuppressWarnings({ "unchecked"})
	public void sendTemplateMsg(WechatMsgVo msg,String token,GwWechatMsgTemplate template){
		Map<String, Object> map = new HashMap<String, Object>();
		if(null != template){
			//替换模板数据
			String dataResult = RegUtils.findAndReplace("\\$\\{(.+?)\\}", template.getDataTemplate(), paramName -> {
				   String value = paramName.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("\\}", "").trim();
				   Optional<NameValuePari>optional =  msg.getData().stream().filter(v->StringUtil.equalsIgnoreCase(value, v.getKey())).findFirst();
				   return optional.orElse(new NameValuePari()).getValue();
		    });
			logger.error("transform...result{}",dataResult);
			map.put("touser", msg.getTouser());
			map.put("template_id", template.getTemplateCode());
			
			if (!msg.isIfUseTemplateUlr()) { // 不使用模板的url
				map.put("url", msg.getExt1());
			} else {
				map.put("url", template.getUrl());
			}
			map.put("data", JSON.parse(dataResult));
			logger.info("sendWechatMsg:param:{}",JsonUtil.object2String(map));
			String result = HttpUtil.sendPost(SERVICE_TEMPLATE_URL + token, JsonUtil.object2String(map),null);
			logger.info("sendResult:{}",result);
			if (StringUtil.hasValue(result)) {
				Map<String, Object> res = JsonUtil.str2Object(result, Map.class);

				String resCode = String.valueOf(res.get("errcode"));

				if (res != null && WechatMsgConstants.WECHAT_POST_STATUS_SUCCESS.equals(resCode)) { // 发送成功
					logger.info("send template to touser wechat msg success.........{}", msg.getTouser());
				} else {
					//处理异常
					if (null != msg.getPostBackData()) {
						CollectMsgSendRespCmd info = new CollectMsgSendRespCmd(msg.getPostBackData().get("mtpId"));
						info.setReceiverId(msg.getPostBackData().get("receiverId"));
						info.setReceiverName(msg.getPostBackData().get("receiverName"));
						info.setSender(msg.getPostBackData().get("sender"));
						msgSend.collectMsgResp(info);
					}
					logger.info("send template to touser wechat msg faile.........{}", msg.getTouser());
				}
			}
		}else{
			logger.info("send template to touser no template.........{}",msg.getTouser());
		}
		
	}
	//客服消息
	@SuppressWarnings("unchecked")
	public void sendServiceMsg(WechatMsgVo msg,String token) {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("touser", msg.getTouser());
		map.put("msgtype", "text");

		map.put("text", msg.getContentData());

		String url = SERVICE_MSG_URL + token;

		String result = HttpUtil.sendPost(url, JsonUtil.object2String(map),null);
		Map<String, Object> res = JsonUtil.str2Object(result, Map.class);
		logger.info("msgResult:{}",result);
		
		String resCode = String.valueOf(res.get("errcode"));

		if (res != null && "0".equals(resCode)) { // 发送成功
			logger.info("send service to touser wechat msg success.........{}", msg.getTouser());
		} else {
			logger.info("send service to touser wechat msg faile.........{}", msg.getTouser());
		}
	}
	public GwWechatMsgTemplate getWechatMsgTemplate(String templateId){
		return publicMapper.selectMsgTemplate(templateId);	
	}
	
}
