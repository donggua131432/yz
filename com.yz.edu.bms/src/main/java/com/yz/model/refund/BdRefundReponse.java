package com.yz.model.refund;

import java.util.List;

import com.yz.model.transfer.BdCheckRecord;

public class BdRefundReponse {

	private String learnId;
	private String stdName;
	private String phone;
	private String idCard;
	private String checkState;
	private String reason;
	private String grade;
	private String recruitType;
	private String unvsName;
	private String pfsnName;
	private String pfsnLevel;
	private String scholarship;
	private String stdStage;
	private String demurrageAmount;
	private String cashAmount;
	private String refundAmount;
	private String createTime;
	private String refundId;
	private String createUser;
	private String remark;
	private String checkStatus;
	private String stdId;
	private String isComplete;

	private List<BdCheckRecord> checks;

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
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

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
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

	public String getDemurrageAmount() {
		return demurrageAmount;
	}

	public void setDemurrageAmount(String demurrageAmount) {
		this.demurrageAmount = demurrageAmount;
	}

	public String getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(String cashAmount) {
		this.cashAmount = cashAmount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<BdCheckRecord> getChecks() {
		return checks;
	}

	public void setChecks(List<BdCheckRecord> checks) {
		this.checks = checks;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

}
