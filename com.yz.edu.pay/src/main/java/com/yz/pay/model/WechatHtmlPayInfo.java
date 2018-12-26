package com.yz.pay.model;

public class WechatHtmlPayInfo extends RequestInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8168136508523676113L;

	@Override
	public void signBefore() {
	}

	/**
	 * appid
	 * 
	 * @param appid
	 */
	public void setAppId(String appid) {
		put("appId", appid);
	}

	/**
	 * timeStamp
	 * 
	 * @param appid
	 */
	public void setTimeStamp(String timeStamp) {
		put("timeStamp", timeStamp);
	}

	/**
	 * nonceStr
	 * 
	 * @param appid
	 */
	public void setNonceStr(String nonceStr) {
		put("nonceStr", nonceStr);
	}

	/**
	 * package
	 * 
	 * @param appid
	 */
	public void setPackage(String _package) {
		put("package", _package);
	}

	/**
	 * signType
	 * 
	 * @param appid
	 */
	public void setSignType(String signType) {
		put("signType", signType);
	}

}
