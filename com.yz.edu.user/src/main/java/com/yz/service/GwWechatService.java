package com.yz.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yz.exception.IRuntimeException;
import com.yz.http.HttpUtil;
import com.yz.interceptor.HttpTraceInterceptor;
import com.yz.model.CommunicationMap;
import com.yz.model.GwWechatPublic;
import com.yz.redis.RedisService;
import com.yz.util.CodeUtil;
import com.yz.util.JsonUtil;
import com.yz.util.MapUtil;
import com.yz.util.StringUtil;

@Service
public class GwWechatService {

	private static final Logger log = LoggerFactory.getLogger(GwWechatService.class);

	@Value("${wehchat_pub.userinfo_url}")
	private String userInfoUrl;

	@Value("${wehchat_pub.auth_token_url}")
	private String authTokenUrl;

	@Value("${wehchat_pub.pub_id}")
	private String pubId;

	@Autowired
	private GwWechatPublicService wechatPublicService;
	
	/**
	 * 获取微信用户信息
	 * 
	 * @param code
	 * @return
	 */
	public CommunicationMap getUserInfo(String code) throws IRuntimeException {
		String enc = "utf-8";

		CommunicationMap authInfo = getAuthInfo(code);

		if (authInfo == null) {
			return null;
		}

		String authToken = authInfo.getString("access_token");

		String openId = authInfo.getString("openid");

		StringBuffer sb = new StringBuffer(userInfoUrl);

		try {
			sb.append("?access_token=").append(URLEncoder.encode(authToken, enc));
			sb.append("&openid=").append(URLEncoder.encode(openId, enc));
			sb.append("&lang=zh_CN");
		} catch (UnsupportedEncodingException e) {
			return null;
		}

		String url = sb.toString();
		log.info("========获取微信用户信息请求地址："+url);
		log.info("开始获取微信用户信息------------------");
		long startTime=System.currentTimeMillis();   //获取开始时间
		String result = HttpUtil.sendGet(url,HttpTraceInterceptor.TRACE_INTERCEPTOR);
		long endTime=System.currentTimeMillis(); //获取结束时间
		log.info("结束获取微信用户信息------------------用时:"+(endTime-startTime)+"ms");
		CommunicationMap resultMap = processResult(result);

		return resultMap;
	}

	/**
	 * 获取微信用户OpenId
	 * 
	 * @param code
	 * @return
	 */
	public String getOpenId(String code) throws IRuntimeException {
		CommunicationMap authInfo = getAuthInfo(code);
		return authInfo == null ? null : authInfo.getString("openid");
	}

	/**
	 * 获取授权信息
	 * 
	 * @param code
	 * @return
	 */
	private CommunicationMap getAuthInfo(String code) {
		String enc = "utf-8";

		GwWechatPublic pubInfo = wechatPublicService.getPubInfo(pubId);
		if (null != pubInfo) {
			StringBuffer sb = new StringBuffer(authTokenUrl);

			try {

				sb.append("?appid=").append(URLEncoder.encode(pubInfo.getAppId(), enc));
				sb.append("&secret=").append(URLEncoder.encode(pubInfo.getAppSecret(), enc));
				sb.append("&code=").append(URLEncoder.encode(code, enc));
				sb.append("&grant_type=authorization_code");
			} catch (UnsupportedEncodingException e) {
				return null;
			}

			String url = sb.toString();
			log.debug("开始获取授权信息------------------");
			long startTime = System.currentTimeMillis(); // 获取开始时间
			String result = HttpUtil.sendGet(url,HttpTraceInterceptor.TRACE_INTERCEPTOR);
			long endTime = System.currentTimeMillis(); // 获取结束时间
			log.debug("结束获取授权信息------------------用时:" + (endTime - startTime) + "ms");
			return processResult(result);
		}
		return null;
	}

	/**
	 * 处理微信返回数据
	 * @param rtStr
	 * @return
	 */
	private CommunicationMap processResult(String rtStr) {
		if (StringUtil.isEmpty(rtStr)) {
			log.error("================== 微信公众号授权返回信息为空");
			return null;
		}

		CommunicationMap c = JsonUtil.str2Object(rtStr, CommunicationMap.class);
		if (c == null) {
			log.error("================== 微信公众号授权返回信息为空");
		} else if (c.containsKey("errcode")) {
			String errorCode = c.getString("errcode");
			String errmsg = c.getString("errmsg");
			log.error("================== 微信公众号授权失败：" + errorCode + " - {" + errmsg + "}");
		} else {
			log.info("================== 微信公众号授权返回信息："+rtStr);
			return c;
		}
		
		return null;
	}
	

	/**
	 * 微信jsapi签名
	 * 
	 * @param pubId
	 *            公众号ID
	 * @param url
	 *            前端调取地址
	 * @return
	 */
	public Map<String, String> jsapiSign(String url) {

		Map<String, String> signdata = new HashMap<String, String>();
		String noncestr = UUID.randomUUID().toString().replace("-", "");
		String timestamp = System.currentTimeMillis() / 1000 + "";

		signdata.put("noncestr", noncestr);
		signdata.put("jsapi_ticket", RedisService.getRedisService().get("jsapiTicket")); //TODO 从redis取
		signdata.put("timestamp", timestamp);
		signdata.put("url", url);

		String dataStr = MapUtil.sortMap2String(signdata);

		log.debug("---------------------------- jsapi签名字符串：" + dataStr);

		String sign = null;
		try {
			byte[] shaBytes = CodeUtil.SHA.sha1(dataStr, "utf-8");
			sign = CodeUtil.MD5.byteArrayToHex(shaBytes);

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			log.debug("---------------------------- jsapi签名失败：" + dataStr);
			return null;
		}

		log.debug("--------------------  微信jsapi签名成功：" + sign);

		Map<String, String> result = new HashMap<String, String>();

		GwWechatPublic pub = wechatPublicService.getPubInfo(pubId);

		result.put("appid", pub.getAppId());
		result.put("signature", sign);
		result.put("timestamp", timestamp);
		result.put("noncestr", noncestr);

		return result;
	}
}
