package com.yz.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import com.yz.util.StringUtil;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import net.sf.json.JSONObject;

public class JWTUtil {
	/**
	 * jwt密匙
	 */
	public static final String JWT_SECRET = "@Syylfmlx0_29feE!";


	/**
	 * 解析jwt token
	 * @param token
	 * @return
	 */
	public static Claims parseJwt(String token) {
		final JWTVerifier verifier = new JWTVerifier(JWT_SECRET);
		if (!StringUtil.hasValue(token)) {
			return null;
		}
		Map<String, Object> claims = null;
		Claims tmp = null;
		try {
			claims = verifier.verify(token);
			tmp = (Claims) JSONObject.toBean(JSONObject.fromObject(claims), Claims.class);
		} catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException
				| IOException | JWTVerifyException e) {
			e.printStackTrace();
		}
		return tmp;

	}

	/**
	 * 创建JWTtoken
	 * @param userId 用户编号
	 * @param jwtToken JWTToken
	 * @param decodeKey 加密密钥
	 * @param timeout 超时时间 单位/秒
	 * @return
	 */
	public static String createJwt(String userId, String jwtToken, String decodeKey, long timeout) {
		final Claims claims = new Claims();
		claims.setTimeout(timeout);
		claims.setJwtToken(jwtToken);
		claims.setUserId(userId);
		claims.setKey(decodeKey);
		return createJwt(claims);
	}
	/**
	 * 创建JWTtoken 
	 * @param userId 用户编号
	 * @param jwtToken JWTToken
	 * @param timeout 超时时间 单位/秒
	 * @return
	 */
	public static String createJwt(String userId, String jwtToken, long timeout) {
		return createJwt(userId, jwtToken, StringUtil.randomString(8), timeout);
	}
	
	public static String createJwt(Claims claims) {
		final JWTSigner signer = new JWTSigner(JWT_SECRET);
		final String jwt = signer.sign(claims);
		return jwt;
	}
	
	/***
	 * JWT证书对象
	 * @author cyf
	 *
	 */
	public static class Claims extends HashMap<String, Object> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8344391046069252145L;
		
		public Claims() {
			put("iss", "远智教育");
		}
		
		public void setJwtToken(Object jwtToken) {
			put("jwtToken", jwtToken);
		}
		
		public void setTimeout(long timeout) {
			long exp = System.currentTimeMillis() + timeout * 1000;
			put("exp", exp);
		}
		
		/***
		 * 设置用户编号
		 * @param userId
		 */
		public void setUserId(String userId) {
			put("userId", userId);
		}
		
		/**
		 * 设置加密密钥
		 * @param decodeKey
		 */
		public void setKey(String decodeKey) {
			put("d", decodeKey);
		}
		
		/**
		 * 获取签发人
		 * @return
		 */
		public String getIss() {
			return (String)get("iss");
		}
		/**
		 * 获取用户编号
		 * @return
		 */
		public String getUserId() {
			return (String) get("userId");
		}
		/**
		 * 获取超时时间戳
		 * @return
		 */
		public Long getExp() {
			return (Long)get("exp");
		}
		/**
		 * 获取token
		 * @return
		 */
		public String getJwtToken() {
			return (String)get("jwtToken");
		}
		/**
		 * 获取密钥
		 * @return
		 */
		public String getKey() {
			return (String) get("d");
		}
	}

}
