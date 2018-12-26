package com.yz.model.condition.zhimi;

import java.util.List;

import com.yz.model.common.PageCondition;

public class ZhimiProductRecordsQuery extends PageCondition {

	private String mobile;
	private String nickname;
	private String yzCode;
	private String productName;
	private String outSerialNo;
	private String paymentStatus;
	private String realName;
	
	private String startTime;
	private String endTime;
	
	private List<String> userIds;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname == null ? null : nickname.trim();
	}
	public String getYzCode() {
		return yzCode;
	}
	public void setYzCode(String yzCode) {
		this.yzCode = yzCode == null ? null : yzCode.trim();
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName == null ? null : productName.trim();
	}
	public String getOutSerialNo() {
		return outSerialNo;
	}
	public void setOutSerialNo(String outSerialNo) {
		this.outSerialNo = outSerialNo == null ? null : outSerialNo.trim();
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus == null ? null : paymentStatus.trim();
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime == null ? null : startTime.trim();
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime == null ? null : endTime.trim();
	}
	public List<String> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName == null ?  null : realName.trim();
	}

}
