package com.yz.job.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yz.http.HttpUtil;
import com.yz.util.DateUtil; 
import com.yz.util.JsonUtil;

import net.sf.json.JSONObject;

/**
 * 获取京东的token
 * @author Admin
 *
 */
@Service
public class JDAccessTokenService
{
	
	private static final Logger log = LoggerFactory.getLogger(JDAccessTokenService.class);
	
	private static String ACCESS_TOKEN_URL = "https://bizapi.jd.com/oauth2/access_token";
	
	@Value("${jd_goods.entity_client_id}")
	private String entityClientId; 
	@Value("${jd_goods.entity_client_secret}")
	private String entityClientSecret; 
	/**实物的信息-----远智教育VOP*/
	@Value("${jd_goods.entity_pwd}")
	private String entityPwd; 
	
	/**实体卡信息----远智教育实体卡VOP*/
	@Value("${jd_goods.entity_card_pwd}")
	private String entityCardPwd; 
	/**
	 * 获取accessToken(远智教育VOP,远智教育实体卡VOP)
	 * @return
	 */
	public String getAccessToken() {
			String data = null;
			try {
				String username = "远智教育VOP";
				String password = MD5(entityPwd);
				System.out.println("加密后的密码:"+ password);
				String timestamp = DateUtil.getNowDateAndTime();
				String clientSecret = entityClientSecret;
				String clientId = entityClientId;
				String sign = clientSecret + timestamp + clientId + username + password + "access_token" + clientSecret;
				System.out.println("sign=" + sign);
				sign = MD5(sign).toUpperCase();
				System.out.println("sign=" + sign);

				data = "grant_type=access_token" + "&client_id=" + clientId + "&username="
						+ URLEncoder.encode(username, "utf-8") + "&password=" + password + "&timestamp=" + timestamp
						+ "&sign=" + sign;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String result = HttpUtil.sendPost(ACCESS_TOKEN_URL , data,null);
			log.info("直接获取....result:{}",JsonUtil.object2String(result));
			JSONObject firstObj = JSONObject.fromObject(result);
			if(null != firstObj.getString("resultCode") && firstObj.getString("resultCode").equals("0000")){
				JSONObject secondObj = JSONObject.fromObject(firstObj.getString("result"));
				return secondObj.getString("access_token");
			}
			return null;
	}
	
	/**
	 * 获取京东实体卡的token(下单专用)
	 * @return
	 */
	public String getEntityCardAccessToken() {
		String data = null;
		try {
			String username = "远智教育实体卡VOP";
			String password = MD5(entityCardPwd);
			System.out.println("加密后的密码:" + password);
			String timestamp = DateUtil.getNowDateAndTime();
			String clientSecret = entityClientSecret;
			String clientId = entityClientId;
			String sign = clientSecret + timestamp + clientId + username + password + "access_token" + clientSecret;
			System.out.println("sign=" + sign);
			sign = MD5(sign).toUpperCase();
			System.out.println("sign=" + sign);

			data = "grant_type=access_token" + "&client_id=" + clientId + "&username="
					+ URLEncoder.encode(username, "utf-8") + "&password=" + password + "&timestamp=" + timestamp
					+ "&sign=" + sign;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String result = HttpUtil.sendPost(ACCESS_TOKEN_URL , data,null);
		log.info("直接获取....result:{}", JsonUtil.object2String(result));
		JSONObject firstObj = JSONObject.fromObject(result);
		if (null != firstObj.getString("resultCode") && firstObj.getString("resultCode").equals("0000")) {
			JSONObject secondObj = JSONObject.fromObject(firstObj.getString("result"));
			return secondObj.getString("access_token");
		}
		return null;
	}
	
	private static String MD5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	private static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
}
