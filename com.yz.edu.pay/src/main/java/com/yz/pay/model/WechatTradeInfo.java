package com.yz.pay.model;

import com.yz.pay.wechat.WeChatConstants;

/**
 * 微信支付信息
 * 
 * @author cyf
 *
 */
public class WechatTradeInfo extends RequestInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4214897355033628358L;

	public WechatTradeInfo() {
		this.put(WeChatConstants.param_package, WeChatConstants.PACKAGE);// 固定字段
	}

	/**
	 * appid
	 * 
	 * @param appid
	 */
	public void setAppid(String appid) {
		put("appId", appid);
	}

	/**
	 * 商户号
	 * 
	 * @param partnerid
	 */
	public void setPartnerid(String partnerid) {
		put(WeChatConstants.param_partnerid, partnerid);
	}

	/**
	 * 随机支付串
	 * 
	 * @param nonce_str
	 */
	public void setNonceStr(String nonce_str) {
		// put(WeChatConstants.param_nonce_str, nonce_str);
		put("nonceStr", nonce_str);
	}

	/**
	 * 预支付订单号
	 * 
	 * @param prepayid
	 */
	public void setPrepayid(String prepayid) {
		put(WeChatConstants.param_prepayid, prepayid);
	}

	/**
	 * 时间戳
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(String timestamp) {
		put("timeStamp", timestamp);
	}
	
	/**
	 * 时间戳
	 * 
	 * @param timestamp
	 */
	public void setOpenId(String openId) {
		put(WeChatConstants.param_open_id, openId);
	}
	
	/**
	 * 时间戳
	 * 
	 * @param timestamp
	 */
	public void setPackage(String packge) {
		put(WeChatConstants.param_package, packge);
	}
	
	public void setSignType(String signType) {
		put("signType", signType);
	}

	@Override
	public void signBefore() {
	}

}
