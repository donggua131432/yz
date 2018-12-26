package com.yz.model.educational;

import java.util.Date;

public class BdStudentSend {
	private String sendId;

	private String learnId;

	private String batchId;

	private String orderBookStatus;

	private String logisticsNo;

	private String logisticsName;

	private String logisticsContact;

	private String receiveStatus;

	private String semester;

	private String textbookType;

	private String address;
	
	private String mobile;
	
	private String userName;
	
	private String provinceCode;
	
	private String cityCode;
	
	private String districtCode;
	
	private String addressStatus;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;
	
	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getTextbookType() {
		return textbookType;
	}

	public void setTextbookType(String textbookType) {
		this.textbookType = textbookType;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId == null ? null : sendId.trim();
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId == null ? null : batchId.trim();
	}

	public String getOrderBookStatus() {
		return orderBookStatus;
	}

	public void setOrderBookStatus(String orderBookStatus) {
		this.orderBookStatus = orderBookStatus == null ? null : orderBookStatus.trim();
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo == null ? null : logisticsNo.trim();
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName == null ? null : logisticsName.trim();
	}

	public String getLogisticsContact() {
		return logisticsContact;
	}

	public void setLogisticsContact(String logisticsContact) {
		this.logisticsContact = logisticsContact == null ? null : logisticsContact.trim();
	}

	public String getReceiveStatus() {
		return receiveStatus;
	}

	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus == null ? null : receiveStatus.trim();
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester == null ? null : semester.trim();
	}

	public String getAddressStatus() {
		return addressStatus;
	}

	public void setAddressStatus(String addressStatus) {
		this.addressStatus = addressStatus == null ? null : addressStatus.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim();
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId == null ? null : updateUserId.trim();
	}
}