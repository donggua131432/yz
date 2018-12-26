package com.yz.model;

import java.io.Serializable;
import java.util.Date;

public class GsUserPrize implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String upId;

	private String winningId;

	private String prizeId;

	private Date winningTime;

	private String count;

	private String sendStatus;

	private String sendTime;

	private String empId;

	private String userName;

	private String mobile;

	private String province;

	private String provinceCode;

	private String city;

	private String cityCode;

	private String district;

	private String districtCode;

	private String address;
	
	private String remark;

	private String custid;

	private String destCode;

	private String mailno;

	private String updateTime;

	private String updateUser;

	private String updateUserId;
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUpId() {
		return upId;
	}

	public void setUpId(String upId) {
		this.upId = upId == null ? null : upId.trim();
	}

	public String getWinningId() {
		return winningId;
	}

	public void setWinningId(String winningId) {
		this.winningId = winningId == null ? null : winningId.trim();
	}

	public String getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(String prizeId) {
		this.prizeId = prizeId == null ? null : prizeId.trim();
	}

	public Date getWinningTime() {
		return winningTime;
	}

	public void setWinningTime(Date winningTime) {
		this.winningTime = winningTime;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count == null ? null : count.trim();
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus == null ? null : sendStatus.trim();
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId == null ? null : empId.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode == null ? null : provinceCode.trim();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city == null ? null : city.trim();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode == null ? null : cityCode.trim();
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district == null ? null : district.trim();
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode == null ? null : districtCode.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid == null ? null : custid.trim();
	}

	public String getDestCode() {
		return destCode;
	}

	public void setDestCode(String destCode) {
		this.destCode = destCode == null ? null : destCode.trim();
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno == null ? null : mailno.trim();
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
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