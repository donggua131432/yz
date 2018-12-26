package com.yz.model.order;

public class BadPaymentInfo {
	private String payNo;//支付单号
	private String transNo;//第三方交易单号
	private String amount;//支付金额
	private String payType;//支付方式
	private String dealType;//处理类型
	private String dealAddr;//
	private String errorMsg;//错误信息
	private String id;//id号
	private String executeCount;//已执行次数
	private String lastExecuteDate;//最后执行时间
	
	public String getLastExecuteDate() {
		return lastExecuteDate;
	}
	public void setLastExecuteDate(String lastExecuteDate) {
		this.lastExecuteDate = lastExecuteDate;
	}
	public String getExecuteCount() {
		return executeCount;
	}
	public void setExecuteCount(String executeCount) {
		this.executeCount = executeCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getTransNo() {
		return transNo;
	}
	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
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
	public String getDealAddr() {
		return dealAddr;
	}
	public void setDealAddr(String dealAddr) {
		this.dealAddr = dealAddr;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
