package com.yz.job.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yz.constants.GwConstants;
import com.yz.http.HttpUtil;
import com.yz.job.dao.GwWechatPublicMapper;
import com.yz.job.model.GwWechatPublic;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
/**
 * @desc 获取wechat accessToken 服务
 * @author Administrator
 *
 */
public class WeChatAccessTokenService {
	
	private static final Logger logger = LoggerFactory.getLogger(WeChatAccessTokenService.class);
	
	@Autowired
	private GwWechatPublicMapper pubMapper;
	
	/**
	 * @desc 获取微信accessToken 
	 * @param pubId
	 * @return
	 */
	public String getAccessToken(String pubId)
	{
		GwWechatPublic pub  = this.getPubInfo(pubId);
		Map<String,String>param = Maps.newLinkedHashMap();
		param.put("grant_type", GwConstants.WECHAT_GRANT_TYPE);
		param.put("appid", pub.getAppId());
		param.put("secret", pub.getAppSecret());
		String result = HttpUtil.sendGet(GwConstants.ACCESS_TOKEN_URL, param,null);
		result = (String) JsonUtil.str2Object(result, Map.class).get("access_token");
		logger.info("pubId:{},result:{}",pubId,JsonUtil.object2String(param));
		return result;
	}
	/**
	 * 获取微信 jsapiTicket
	 * @param accessToken
	 * @return
	 */
	public String getJsapiTicket(String accessToken) {
		if (StringUtil.hasValue(accessToken)) {
			String url = GwConstants.JSAPI_TICKET_URL.replace("ACCESSTOKEN", accessToken);

			String result = HttpUtil.sendGet(url,null);

			logger.debug("---------------------  微信公众号获取jsapiTicket返回结果：" + result);

			result = (String) JsonUtil.str2Object(result, Map.class).get("ticket");

			return result;
		}
		return null;

	}

	
	/**
	 * 
	 * @param pubId
	 * @return
	 */
	public GwWechatPublic getPubInfo(String pubId) {
		return pubMapper.getPublicInfo(pubId);
	} 
}
