package com.yz.model.oa;

import java.util.List;

import com.yz.model.common.PubInfo;

public class OaMonthExpense extends PubInfo {

	private String eiId;
	private String expenseId;
	private String empId;
	private String empName;
	private String year;
	private String expenseYear;
	private String month;
	private String amount;
	private String remark;
	private String empType;
	private String empStatus;
	private String dpName;
	private String campusName;

	private String estimate;
	private String rendered;
	private String balance;

	private List<String> jtIds;

	public String getEiId() {
		return eiId;
	}

	public void setEiId(String eiId) {
		this.eiId = eiId;
	}

	public String getExpenseYear() {
		return expenseYear;
	}

	public void setExpenseYear(String expenseYear) {
		this.expenseYear = expenseYear;
	}

	public String getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEstimate() {
		return estimate;
	}

	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}

	public String getRendered() {
		return rendered;
	}

	public void setRendered(String rendered) {
		this.rendered = rendered;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}

	public String getDpName() {
		return dpName;
	}

	public void setDpName(String dpName) {
		this.dpName = dpName;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public List<String> getJtIds() {
		return jtIds;
	}

	public void setJtIds(List<String> jtIds) {
		this.jtIds = jtIds;
	}

}
