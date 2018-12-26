package com.yz.model.recruit;

public class StudentJudgeInfo {

	private String[] learnIds;
	private String scholarshipStatus;
	private String reason;
	
	public String[] getLearnIds() {
		return learnIds;
	}
	public void setLearnIds(String[] learnIds) {
		this.learnIds = learnIds;
	}
	public String getScholarshipStatus() {
		return scholarshipStatus;
	}
	public void setScholarshipStatus(String scholarshipStatus) {
		this.scholarshipStatus = scholarshipStatus;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	private String learnId;
	
	private BdStudentBaseInfo baseInfo;
	private BdStudentEnroll enroll;
	private BdStudentHistory history;

	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}
	public BdStudentBaseInfo getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(BdStudentBaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}
	public BdStudentEnroll getEnroll() {
		return enroll;
	}
	public void setEnroll(BdStudentEnroll enroll) {
		this.enroll = enroll;
	}
	public BdStudentHistory getHistory() {
		return history;
	}
	public void setHistory(BdStudentHistory history) {
		this.history = history;
	}
}
