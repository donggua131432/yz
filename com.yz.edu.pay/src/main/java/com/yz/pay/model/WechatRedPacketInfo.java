package com.yz.pay.model;

import com.yz.pay.wechat.WeChatConstants;
import com.yz.util.Assert;

/**
 * 微信红包实体
 * Description: 
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年8月14日.
 *
 */
public class WechatRedPacketInfo extends RequestInfo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7181297411028480513L;

	public WechatRedPacketInfo() {
		this.put(WeChatConstants.param_device_info, WeChatConstants.WEB_SERVER);
	}

	/**
	 * 随机字符串 必填
	 * @param nonce_str
	 */
	public void setNonceStr(String nonce_str) {
		Assert.hasText(nonce_str, "随机字符串不能为空");
		this.put(WeChatConstants.param_nonce_str, nonce_str);
	}
	
	/**
	 * 签名 必填
	 * @param sign
	 */
	public void setSign(String sign) {
		Assert.hasText(sign, "签名不能为空");
		this.put(WeChatConstants.param_sign, sign);
	}
	
	/**
	 * 随机字符串 必填
	 * @param nonce_str
	 */
	public void setMchBillNo(String mch_billno) {
		Assert.hasText(mch_billno, "商户订单号不能为空");
		this.put(WeChatConstants.param_mch_billno, mch_billno);
	}
	
	/**
	 * mch_id 商户号 必填 
	 * @param mchId
	 */
	public void setMchId(String mchId) {
		Assert.hasText(mchId, "商户ID 不能为空");
		this.put(WeChatConstants.param_mch_id, mchId);
	}
	
	/**
	 * wxappid wxappid 必填
	 * @param wxappid
	 */
	public void setWxAppId(String wxappid) {
		Assert.hasText(wxappid, "wxappid 不能为空");
		this.put(WeChatConstants.param_wxappid, wxappid);
	}
	
	/**
	 * sendName 远智教育 必填
	 * @param sendName
	 */
	public void setSendName(String sendName) {
		Assert.hasText(sendName, "sendName 不能为空");
		this.put(WeChatConstants.param_send_name, sendName);
	}

	
	/**
	 * reOpenid 收款人openid 必填
	 * @param reOpenid
	 */
	public void setReOpenid(String reOpenid) {
		Assert.hasText(reOpenid, "reOpenid 不能为空");
		this.put(WeChatConstants.param_re_openid, reOpenid);
	}
	
	/**
	 * totalAmount 红包金额 必填
	 * @param totalAmount
	 */
	public void setTotalAmount(String totalAmount) {
		Assert.hasText(totalAmount, "total_amount 不能为空");
		this.put(WeChatConstants.param_total_amount, totalAmount);
	}
	
	/**
	 * totalNum 红包数量 必填
	 * @param totalNum
	 */
	public void setTotalNum(String totalNum) {
		Assert.hasText(totalNum, "total_num 不能为空");
		this.put(WeChatConstants.param_total_num, totalNum);
	}

	/**
	 * wishing 祝福语 必填
	 * @param wishing
	 */
	public void setWishing(String wishing) {
		Assert.hasText(wishing, "wishing 不能为空");
		this.put(WeChatConstants.param_wishing, wishing);
	}
	
	/**
	 * clientIp 本机IP 必填
	 * @param clientIp
	 */
	public void setClientIp(String clientIp) {
		Assert.hasText(clientIp, "client_ip 不能为空");
		this.put(WeChatConstants.param_client_ip, clientIp);
	}
	
	/**
	 * actName 活动名称 必填
	 * @param actName
	 */
	public void setActName(String actName) {
		Assert.hasText(actName, "act_name 不能为空");
		this.put(WeChatConstants.param_act_name, actName);
	}

	/**
	 * sceneId 活动类型 必填
	 * @param sceneId
	 */
	public void setSceneId(String sceneId) {
		Assert.hasText(sceneId, "scene_id 不能为空");
		this.put(WeChatConstants.param_scene_id, sceneId);
	}


	@Override
	public void signBefore() {
	}

}
