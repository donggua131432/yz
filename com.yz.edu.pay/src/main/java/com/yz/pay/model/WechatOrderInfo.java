package com.yz.pay.model;

 
import com.yz.pay.wechat.WeChatConstants;
import com.yz.util.Assert;
/**
 * 微信订单信息
 * @author cyf
 *
 */
public class WechatOrderInfo extends RequestInfo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7749435209505474548L;
	
	public WechatOrderInfo() {
		this.put(WeChatConstants.param_device_info, WeChatConstants.WEB_SERVER);
	}
	
	public void setPrepayId(String prepayId){
		Assert.hasText(prepayId, "prepayId 不能为空");
		this.put("prepayid", prepayId);
	}
	
	/**
	 * appid APPID 必填
	 * @param appId
	 */
	public void setAppId(String appId) {
		Assert.hasText(appId, "appid 不能为空");
		this.put(WeChatConstants.param_appid, appId);
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
	 * 商户ID
	 * @param partnerid
	 */
	public void setPartnerid(String partnerid) {
		Assert.hasText(partnerid, "商户ID 不能为空");
		this.put("partnerid", partnerid);
	}
	
	
	/**
	 * device_info 设备信息
	 * @param deviceInfo
	 */
	public void setDeviceInfo(String deviceInfo) {
		this.put(WeChatConstants.param_device_info, deviceInfo);
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
	 * 商品描述 必填
	 * @param body
	 */
	public void setBody(String body) {
		Assert.hasText(body, "商品描述不能为空");
		this.put(WeChatConstants.param_body, body);
	}
	
	/**
	 * 商品详情
	 * @param detail
	 */
	public void setDetail(String detail) {
		this.put(WeChatConstants.param_detail, detail);
	}
	
	/**
	 * 附加数据
	 * @param attach
	 */
	public void setAttach(String attach) {
		this.put(WeChatConstants.param_attach, attach);
	}
	
	/**
	 * 商户订单号 必填
	 * @param out_trade_no
	 */
	public void setOutTradeNo(String out_trade_no) {
		Assert.hasText(out_trade_no, "商户订单号不能为空");
		this.put(WeChatConstants.param_out_trade_no, out_trade_no);
	}
	
	/**
	 * 货币类型 必填
	 * @param body
	 */
	public void setFeeType(String fee_type) {
		this.put(WeChatConstants.param_fee_type, fee_type);
	}
	
	/**
	 * 总金额
	 * @param totalFee
	 */
	public void setTotalFee(String totalFee) {
		Assert.hasText(totalFee, "总金额不能为空");
		this.put(WeChatConstants.param_total_fee, totalFee);
	}
	
	/**
	 * 终端IP
	 * @param spbill_create_ip
	 */
	public void setSpbillCreateIp(String spbill_create_ip) {
		Assert.hasText(spbill_create_ip, "终端IP不能为空");
		this.put(WeChatConstants.param_spbill_create_ip, spbill_create_ip);
	}
	
	/**
	 * 交易起始时间
	 * @param timeStart
	 */
	public void setTimeStart(String time_start) {
		this.put(WeChatConstants.param_time_start, time_start);
	}
	
	/**
	 * 交易结束时间
	 * @param timeStart
	 */
	public void setTimeExpire(String time_expire) {
		this.put(WeChatConstants.param_time_expire, time_expire);
	}
	
	/**
	 * 商品标记
	 * @param goods_tag
	 */
	public void setGoodsTag(String goods_tag) {
		this.put(WeChatConstants.param_goods_tag, goods_tag);
	}
	
	/**
	 * 通知地址 必填
	 * @param notify_url
	 */
	public void setNotifyUrl(String notify_url) {
		Assert.hasText(notify_url, "通知地址 不能为空");
		this.put(WeChatConstants.param_notify_url, notify_url);
	}
	
	/**
	 * 交易类型
	 * @param trade_type
	 */
	public void setTradeType(String trade_type) {
		Assert.hasText(trade_type, "交易类型不能为空");
		this.put(WeChatConstants.param_trade_type, trade_type);
	}
	
	/**
	 * 指定支付方式
	 * @param limit_pay
	 */
	public void limit_pay(String limit_pay) {
		this.put(WeChatConstants.param_limit_pay, limit_pay);
	}
	
	/**
	 * 用户标识
	 * @param open_id
	 */
	public void setOpenId(String open_id) {
		this.put(WeChatConstants.param_open_id, open_id);
	}

	@Override
	public void signBefore() {}

	/**
	 * 商品id
	 * @param productId
	 */
	public void setProductId(String productId) {
		Assert.hasText(productId, "商品id不能为空");
		this.put(WeChatConstants.param_product_id, productId);
	}
}
