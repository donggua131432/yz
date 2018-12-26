package com.yz.model.payment;

import java.io.Serializable;

public class BdStudentOrder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4371360478293882974L;

	private String subOrderNo;

	private String financeCode;

	private String orderNo;

	private String amount;

	private String payable;

	private String discount;

	private String learnId;

	private String stdId;

	private String stdName;

	private String mobile;

	private String pfsnName;

	private String pfsnId;

	private String unvsId;

	private String unvsName;

	private String taId;

	private String taName;

	private String createTime;

	private String expireTime;

	private String orderStatus;

	public String getSubOrderNo() {
		return subOrderNo;
	}

	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount == null ? null : amount.trim();
	}

	public String getPayable() {
		return payable;
	}

	public void setPayable(String payable) {
		this.payable = payable == null ? null : payable.trim();
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount == null ? null : discount.trim();
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName == null ? null : stdName.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName == null ? null : pfsnName.trim();
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId == null ? null : pfsnId.trim();
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId == null ? null : unvsId.trim();
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName == null ? null : unvsName.trim();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus == null ? null : orderStatus.trim();
	}

	public String getFinanceCode() {
		return financeCode;
	}

	public void setFinanceCode(String financeCode) {
		this.financeCode = financeCode == null ? null : financeCode.trim();
	}

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId == null ? null : taId.trim();
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName == null ? null : taName.trim();
	}

}
