package com.yz.model.payment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BdStudentSerial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2906569245325656205L;
	private String serialNo;
	private String amount;
	private String discount;
	private String deduction;
	private String payable;
	private String paymentType;
	private String orderNo;
	private String stdId;
	private String userId;
	private String scId;
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
	private String reptStatus;
	private String isAssembly;
	private String chargePlace;
	private String financeCode;
	private String campusName;

	private String pfsnName;
	private String pfsnLevel;
	private String pfsnCode;
	private String unvsName;
	private String chnAmount;
	private String grade;

	private List<BdStdPayInfoResponse> payInfos;
	private List<BdSubSerial> subSerials;

	private String learnId;
	private ArrayList<String> itemCodes;
	private String[] itemNames;
	private ArrayList<String> itemYears;

	private String itemCode;
	private String itemAmount;
	private String itemType;
	private String zmCouponNum;
	private String delayNum;
	private String serialMark;

	private String subOrderNo;
	
	private String payeeId;

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

	public String[] getItemNames() {
		return itemNames;
	}

	public void setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getPfsnCode() {
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getChnAmount() {
		return chnAmount;
	}

	public void setChnAmount(String chnAmount) {
		this.chnAmount = chnAmount;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
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

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public ArrayList<String> getItemCodes() {
		return itemCodes;
	}

	public void setItemCodes(ArrayList<String> itemCodes) {
		this.itemCodes = itemCodes;
	}

	public ArrayList<String> getItemYears() {
		return itemYears;
	}

	public void setItemYears(ArrayList<String> itemYears) {
		this.itemYears = itemYears;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsAssembly() {
		return isAssembly;
	}

	public void setIsAssembly(String isAssembly) {
		this.isAssembly = isAssembly;
	}

	public List<BdSubSerial> getSubSerials() {
		return subSerials;
	}

	public void setSubSerials(List<BdSubSerial> subSerials) {
		this.subSerials = subSerials;
	}

	public String getReptStatus() {
		return reptStatus;
	}

	public void setReptStatus(String reptStatus) {
		this.reptStatus = reptStatus;
	}

	public String getDeduction() {
		return deduction;
	}

	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}

	public String getScId() {
		return scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
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

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

}
