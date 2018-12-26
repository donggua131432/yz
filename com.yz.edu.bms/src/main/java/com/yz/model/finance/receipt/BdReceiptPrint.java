package com.yz.model.finance.receipt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BdReceiptPrint {

	private String stdName;
	private String unvsName;
	private String pfsnName;
	private String pfsnLevel;
	private String grade;
	private String payTime;
	private String reptId;
	private String amount;
	private String payee;
	private String paySite;
	private String paymentType;
	private String cnAmount;
	private String serialNo;
	private String serialMark;
	private String deduction;
	private List<Map<String, String>> zmDeduction;

	private List<BdPayItem> items = new ArrayList<BdPayItem>();

	public List<Map<String, String>> getZmDeduction() {
		return zmDeduction;
	}

	public void setZmDeduction(List<Map<String, String>> zmDeduction) {
		this.zmDeduction = zmDeduction;
	}

	public String getDeduction() {
		return deduction;
	}

	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}

	public String getSerialMark() {
		return serialMark;
	}

	public void setSerialMark(String serialMark) {
		this.serialMark = serialMark;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getCnAmount() {
		return cnAmount;
	}

	public void setCnAmount(String cnAmount) {
		this.cnAmount = cnAmount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getReptId() {
		return reptId;
	}

	public void setReptId(String reptId) {
		this.reptId = reptId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getPaySite() {
		return paySite;
	}

	public void setPaySite(String paySite) {
		this.paySite = paySite;
	}

	public List<BdPayItem> getItems() {
		return items;
	}

	public void setItems(List<BdPayItem> items) {
		this.items = items;
	}

}
