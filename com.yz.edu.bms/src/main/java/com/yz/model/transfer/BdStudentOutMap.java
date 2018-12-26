package com.yz.model.transfer;

import java.util.List;

public class BdStudentOutMap {

	private String stdName;
	private String phone;
	private String idCard;
	private String checkState;
	private String reason;
	private String grade;
	private String recruitType;
	private String unvsName;
	private String unvsId;
	private String pfsnName;
	private String pfsnLevel;
	private String scholarship;
	private String stdStage; // 学员状态
	private String createTime;
	private String outTd;
	private String learnId;
	private String jtId;
	private String createUser;
	private String recruitName; // 招生老师
	private String tutorName; // 班主任
	private String sg; // 优惠分组
	private String inclusionStatus;// 入围状态
	private String startTime;
	private String endTime;
	private String mobile;
	private List<BdCheckRecord> check;

	
	
	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getJtId() {
		return jtId;
	}

	public void setJtId(String jtId) {
		this.jtId = jtId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getOutTd() {
		return outTd;
	}

	public void setOutTd(String outTd) {
		this.outTd = outTd;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<BdCheckRecord> getCheck() {
		return check;
	}

	public void setCheck(List<BdCheckRecord> check) {
		this.check = check;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getRecruitName() {
		return recruitName;
	}

	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public String getSg() {
		return sg;
	}

	public void setSg(String sg) {
		this.sg = sg;
	}

	public String getInclusionStatus() {
		return inclusionStatus;
	}

	public void setInclusionStatus(String inclusionStatus) {
		this.inclusionStatus = inclusionStatus;
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}

}
