package com.yz.model.finance;

import java.util.Date;

public class BdSubOrder {
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
	
	private String ext1;

	private String itemYear;
	private String itemType;
	
	private String payeeId;

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

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getItemYear() {
		return itemYear;
	}

	public void setItemYear(String itemYear) {
		this.itemYear = itemYear;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}
}