package com.yz.model.oa;

import java.util.List;

public class OaCommission {

	private String expenseId;
	private String empId;
	private String empName;
	private String year;
	private String empType;
	private String empStatus;
	private String dpName;
	private String campusName;
	private String count;
	private String amount;
	private String outCount;

	private List<String> jtIds;

	public String getOutCount() {
		return outCount;
	}

	public void setOutCount(String outCount) {
		this.outCount = outCount;
	}

	public List<String> getJtIds() {
		return jtIds;
	}

	public void setJtIds(List<String> jtIds) {
		this.jtIds = jtIds;
	}

	public String getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
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

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
