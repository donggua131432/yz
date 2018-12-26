package com.yz.util;

public class TokenUtil {
	
	private static final String vector = "UWCEHDTVRK";
	
	private static final String key = "@fml_fdre83*43^kdi#)49<d";
	/**
	 * 创建token
	 * @param userId
	 * @param token
	 * @return
	 */
	public static String createToken(String userId, String token) {
		if(StringUtil.isEmpty(userId) || StringUtil.isEmpty(token)) {
			throw new IllegalArgumentException("parameter is missing");
		}
		
		String s = userId + "," + token;
		
		return createToken(s);
	}
	
	public static String createToken(String eStr) {
		try {
			return CodeUtil.DES3.encrypt(eStr, key, vector);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 解析token
	 * @param secureStr
	 * @return
	 */
	public static Secure convert(String secureStr) {
		Secure s = new Secure();
		
		try {
			String t = parse(secureStr);
			if(t == null)
				return s;

			String[] ss = t.split("[,]");
			
			if(ss == null || ss.length != 2) {
				throw new IllegalArgumentException("secureStr is uncompleted");
			}
			
			s.setUserId(ss[0]);
			s.setToken(ss[1]);
			
		} catch (Exception e) {
			
		}
		
		return s;
	}
	/**
	 * 解密token
	 * @param secureStr
	 * @return
	 */
	public static String parse(String secureStr) {
		try {
			return CodeUtil.DES3.decrypt(secureStr, key, vector);
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public static class Secure {
		private String userId;
		private String token;
		
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
	}
}
