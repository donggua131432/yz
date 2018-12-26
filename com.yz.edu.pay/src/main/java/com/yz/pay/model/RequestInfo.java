package com.yz.pay.model;

import java.util.LinkedHashMap;

/**
 * 支付请求信息类
 * @author cyf
 *
 */
public abstract class RequestInfo extends LinkedHashMap<String, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6963142782383050282L;

	public abstract void signBefore();
}
