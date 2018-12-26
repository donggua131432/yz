package com.yz.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;

import com.yz.http.HttpUtil;
import com.yz.util.DateUtil; 
import com.yz.util.JsonUtil;

import net.sf.json.JSONObject;

/**
 * 京东实物对接util
 * @author lx
 * @date 2018年3月26日 上午11:22:53
 */
public class JdGoodsUtil
{

	private static String ACCESS_TOKEN_URL = "https://bizapi.jd.com/oauth2/access_token";
	private static String REFRESH_TOKEN_URL = "https://bizapi.jd.com/oauth2/refresh_token";
	
	/**
	 * 获取accessToken
	 * @param ifRefresh 是否是刷新获取,true:为刷新获取,false:直接获取
	 * @param refreshToken 
	 * @return
	 */
	public static String getAccessToken(boolean ifRefresh,String refreshToken) {
		if (!ifRefresh) {
			String data = null;
			try {
				String username = "远智教育VOP";
				String password = MD5("v2RTSvn6");
				System.out.println("加密后的密码:"+ password);
				String timestamp = DateUtil.getNowDateAndTime();
				String clientSecret = "cEZikN35TLLengz725TS";
				String clientId = "8L1cd7dDUmRqJfXKUN2W";
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
			return HttpUtil.sendPost(ACCESS_TOKEN_URL, data,null);
		}else{
			String data = null;
			try {
				String clientSecret = "cEZikN35TLLengz725TS";
				String clientId = "8L1cd7dDUmRqJfXKUN2W";
				data = "refresh_token="+refreshToken + "&client_id=" + clientId + "&client_secret="+clientSecret;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return HttpUtil.sendPost(REFRESH_TOKEN_URL, data,null);
		}
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
	public static void main(String[] args)
	{
		System.out.println(getAccessToken(false,null));
		String result = "{\"success\":true,\"resultMessage\":\"\",\"resultCode\":\"0000\",\"result\":{\"uid\":\"1898675129\",\"refresh_token_expires\":1537865844149,\"time\":1522141044149,\"expires_in\":86400,\"refresh_token\":\"HNL5mZ1n483dhjic8zbZd1YQKhRELSpVohJVZHlN\",\"access_token\":\"fOitYDGFxWVAwMBOBDMNmHptH\"}}";
		Map<String, Object> data = JsonUtil.str2Object(result,Map.class);
		JSONObject obj = JSONObject.fromObject(result);
		JSONObject obj2 = JSONObject.fromObject(obj.getString("result"));
		System.out.println(obj2.getString("access_token"));
	}
}
