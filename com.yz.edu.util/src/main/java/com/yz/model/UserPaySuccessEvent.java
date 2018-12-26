package com.yz.model;

/**
 * 
 * @author Administrator
 *
 */
public class UserPaySuccessEvent extends BaseEvent {

	//支付类型
	private String payType; // 支付方式

	private String dealType; // 处理类型

	// 订单号
	private String outOrderNo;
	
	// 交易流水号
	private String serialNo;

	//支付金额
	private String amount;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getOutOrderNo() {
		return outOrderNo;
	}

	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	} 
	
}
