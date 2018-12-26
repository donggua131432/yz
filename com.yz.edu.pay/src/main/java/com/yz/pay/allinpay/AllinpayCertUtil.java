package com.yz.pay.allinpay;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.yz.util.CodeUtil;

public class AllinpayCertUtil {

	/**
	 * 签名
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String sign(TreeMap<String, String> params, String appkey) {
		if (params.containsKey("sign"))// 签名明文组装不包含sign字段
			params.remove("sign");
		params.put("key", appkey);
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue() != null && entry.getValue().length() > 0) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		// String sign = md5(sb.toString());
		// 记得是md5编码的加签
		String sign = CodeUtil.MD5.encrypt(sb.toString());
		params.remove("key");
		return sign;
	}

	/**
	 * 拼接请求参数
	 * 
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String spliceParams(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue() != null && entry.getValue().length() > 0) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	public static boolean validSign(TreeMap<String, String> param, String appkey) {
		if (param != null && !param.isEmpty()) {
			if (!param.containsKey("sign"))
				return false;
			String sign = param.get("sign").toString();
			String mysign = sign(param, appkey);
			return sign.toLowerCase().equals(mysign.toLowerCase());
		}
		return false;
	}
	
}
