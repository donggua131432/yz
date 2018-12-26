package com.yz.model.finance.receipt;

import java.util.ArrayList;
import java.util.List;

import com.yz.model.common.PubInfo;

public class BdReceipt extends PubInfo {

	private String reptId;
	private String stdId;
	private String stdName;
	private String learnId;
	private String serialNo;
	private String applyTime;
	private String reptStatus;
	private String sendTime;
	private String empId;
	private String sfMailno;
	private String destCode;
	private String address;
	private String payable;
	private String paidAmount;
	private String mobile;
	private String remark;
	private String custid;
	private String grade;
	private List<String> serialMarks;

	private String userName;

	private String hasInform;

	private String province;

	private String provinceCode;

	private String city;

	private String cityCode;

	private String district;

	private String districtCode;

	private String campusName;

	private String reptCampusName;

	private String reptType;

	private String paymentType;

	private String outSerialNo;

	private String expressAmount;

	private String campusId;

	private List<BdPayItem> items = new ArrayList<BdPayItem>();

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getHasInform() {
		return hasInform;
	}

	public void setHasInform(String hasInform) {
		this.hasInform = hasInform;
	}

	public String getExpressAmount() {
		return expressAmount;
	}

	public void setExpressAmount(String expressAmount) {
		this.expressAmount = expressAmount;
	}

	public String getReptType() {
		return reptType;
	}

	public void setReptType(String reptType) {
		this.reptType = reptType;
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

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getReptCampusName() {
		return reptCampusName;
	}

	public void setReptCampusName(String reptCampusName) {
		this.reptCampusName = reptCampusName;
	}

	public List<String> getSerialMarks() {
		return serialMarks;
	}

	public void setSerialMarks(List<String> serialMarks) {
		this.serialMarks = serialMarks;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<BdPayItem> getItems() {
		return items;
	}

	public void setItems(List<BdPayItem> items) {
		this.items = items;
	}

	public String getPayable() {
		return payable;
	}

	public void setPayable(String payable) {
		this.payable = payable;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDestCode() {
		return destCode;
	}

	public void setDestCode(String destCode) {
		this.destCode = destCode;
	}

	public String getReptId() {
		return reptId;
	}

	public void setReptId(String reptId) {
		this.reptId = reptId;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getReptStatus() {
		return reptStatus;
	}

	public void setReptStatus(String reptStatus) {
		this.reptStatus = reptStatus;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getSfMailno() {
		return sfMailno;
	}

	public void setSfMailno(String sfMailno) {
		this.sfMailno = sfMailno;
	}

}
