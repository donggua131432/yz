package com.yz.model.finance.stdfee;

import java.util.List;

import com.yz.model.common.PubInfo;

public class BdStudentSerial extends PubInfo {

	private String serialNo;
	private String amount;
	private String discount;
	private String deduction;
	private String payable;
	private String paymentType;
	private String orderNo;
	private String stdId;
	private String userId;
	private String stdName;
	private String payTime;
	private String empId;
	private String empName;
	private String mobile;
	private String outSerialNo;
	private String serialStatus;
	private String checkUser;
	private String checkUserId;
	private String checkTime;
	private String isAssembly;
	private String chargePlace;
	private String financeCode;
	private String campusName;
	private String demurrageBefore;
	private String demurrageAfter;
	private String ext1;
	private String remark;

	private List<BdStdPayInfoResponse> payInfos;
	private List<BdSubSerial> subSerials;

	private String itemCode;
	private String itemAmount;
	private String itemType;
	private String zmCouponNum;
	private String delayNum;
	private String serialMark;

	private String subOrderNo;
	private String reptStatus;

	private String payeeId;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReptStatus() {
		return reptStatus;
	}

	public void setReptStatus(String reptStatus) {
		this.reptStatus = reptStatus;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(String itemAmount) {
		this.itemAmount = itemAmount;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getZmCouponNum() {
		return zmCouponNum;
	}

	public void setZmCouponNum(String zmCouponNum) {
		this.zmCouponNum = zmCouponNum;
	}

	public String getDelayNum() {
		return delayNum;
	}

	public void setDelayNum(String delayNum) {
		this.delayNum = delayNum;
	}

	public String getSerialMark() {
		return serialMark;
	}

	public void setSerialMark(String serialMark) {
		this.serialMark = serialMark;
	}

	public String getSubOrderNo() {
		return subOrderNo;
	}

	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}

	public String getDemurrageBefore() {
		return demurrageBefore;
	}

	public void setDemurrageBefore(String demurrageBefore) {
		this.demurrageBefore = demurrageBefore;
	}

	public String getDemurrageAfter() {
		return demurrageAfter;
	}

	public void setDemurrageAfter(String demurrageAfter) {
		this.demurrageAfter = demurrageAfter;
	}

	public List<BdSubSerial> getSubSerials() {
		return subSerials;
	}

	public void setSubSerials(List<BdSubSerial> subSerials) {
		this.subSerials = subSerials;
	}

	public String getChargePlace() {
		return chargePlace;
	}

	public void setChargePlace(String chargePlace) {
		this.chargePlace = chargePlace;
	}

	public String getFinanceCode() {
		return financeCode;
	}

	public void setFinanceCode(String financeCode) {
		this.financeCode = financeCode;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getIsAssembly() {
		return isAssembly;
	}

	public void setIsAssembly(String isAssembly) {
		this.isAssembly = isAssembly;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeduction() {
		return deduction;
	}

	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}

	public List<BdStdPayInfoResponse> getPayInfos() {
		return payInfos;
	}

	public void setPayInfos(List<BdStdPayInfoResponse> payInfos) {
		this.payInfos = payInfos;
	}

	public String getPayable() {
		return payable;
	}

	public void setPayable(String payable) {
		this.payable = payable;
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

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOutSerialNo() {
		return outSerialNo;
	}

	public void setOutSerialNo(String outSerialNo) {
		this.outSerialNo = outSerialNo;
	}

	public String getSerialStatus() {
		return serialStatus;
	}

	public void setSerialStatus(String serialStatus) {
		this.serialStatus = serialStatus;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getCheckUserId() {
		return checkUserId;
	}

	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

}
