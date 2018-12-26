package com.yz.model.payment;

import java.io.Serializable;
import java.util.Date;

public class BdSubOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3276915079275537581L;

	private String subOrderNo;

	private String orderNo;

	private String itemCode;

	private String feeAmount;

	private String offerAmount;

	private String payable;

	private String fdId;

	private String odId;

	private String subOrderStatus;

	private String stdId;

	private String qrCode;

	private Date createTime;

	private Date expireTime;

	private String mobile;

	private String stdName;

	public String getSubOrderNo() {
		return subOrderNo;
	}

	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo == null ? null : subOrderNo.trim();
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode == null ? null : itemCode.trim();
	}

	public String getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount == null ? null : feeAmount.trim();
	}

	public String getOfferAmount() {
		return offerAmount;
	}

	public void setOfferAmount(String offerAmount) {
		this.offerAmount = offerAmount == null ? null : offerAmount.trim();
	}

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId == null ? null : fdId.trim();
	}

	public String getOdId() {
		return odId;
	}

	public void setOdId(String odId) {
		this.odId = odId == null ? null : odId.trim();
	}

	public String getSubOrderStatus() {
		return subOrderStatus;
	}

	public void setSubOrderStatus(String subOrderStatus) {
		this.subOrderStatus = subOrderStatus == null ? null : subOrderStatus.trim();
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode == null ? null : qrCode.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName == null ? null : stdName.trim();
	}

	public String getPayable() {
		return payable;
	}

	public void setPayable(String payable) {
		this.payable = payable == null ? null : payable.trim();
	}

}