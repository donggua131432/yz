package com.yz.model.transfer;

import java.util.List;

public class BdStudentOutExport {

	private String outId;

	private String stdName;

	private String idCard;

	private String amount;

	private String grade;

	private String recruitName;

	private List<String> checkUsers;

	private String checkUser;

	private String checkStatus;

	private String refundAmount;

	private String isComplete;

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRecruitName() {
		return recruitName;
	}

	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName;
	}

	public List<String> getCheckUsers() {
		return checkUsers;
	}

	public void setCheckUsers(List<String> checkUsers) {
		this.checkUsers = checkUsers;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

}
