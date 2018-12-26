package com.yz.model.payment;

import java.io.Serializable;

public class BdSubSerial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5547118096959719468L;
	private String subSerialNo;
	private String serialNo;

	private String amount;
	private String unit;
	private String qrCode;
	private String paymentType;
	private String subSerialStatus;
	private String outSerialNo;
	private String outSerialStatus;
	private String couponId;
	private String accType;
	private String scId;
	private String couponName;
	private String remark;
	private String zmScale;

	public String getZmScale() {
		return zmScale;
	}

	public void setZmScale(String zmScale) {
		this.zmScale = zmScale;
	}

	public String getSubSerialStatus() {
		return subSerialStatus;
	}

	public void setSubSerialStatus(String subSerialStatus) {
		this.subSerialStatus = subSerialStatus;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSubSerialNo() {
		return subSerialNo;
	}

	public void setSubSerialNo(String subSerialNo) {
		this.subSerialNo = subSerialNo;
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

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getOutSerialNo() {
		return outSerialNo;
	}

	public void setOutSerialNo(String outSerialNo) {
		this.outSerialNo = outSerialNo;
	}

	public String getOutSerialStatus() {
		return outSerialStatus;
	}

	public void setOutSerialStatus(String outSerialStatus) {
		this.outSerialStatus = outSerialStatus;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getScId() {
		return scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

}
