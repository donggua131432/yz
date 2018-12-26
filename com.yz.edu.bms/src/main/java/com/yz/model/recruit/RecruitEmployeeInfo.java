package com.yz.model.recruit;

/**
 * 报读招生信息
 * @author Administrator
 *
 */
public class RecruitEmployeeInfo {

	private String stdName;
	private String stdNo;
	private String schollRoll;
	private String stdStage;

	private String recruitDpName;
	private String recruitCampusName;
	private String recruitName;
	private String recruitStatus;
	
	private String tutorCampusName;
	private String tutorDpName;
	private String tutorName;
	private String tutorStatus;
	private String createTime;

	private String assignFlag;

	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String getStdNo() {
		return stdNo;
	}
	public void setStdNo(String stdNo) {
		this.stdNo = stdNo;
	}
	public String getSchollRoll() {
		return schollRoll;
	}
	public void setSchollRoll(String schollRoll) {
		this.schollRoll = schollRoll;
	}
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}
	public String getRecruitDpName() {
		return recruitDpName;
	}
	public void setRecruitDpName(String recruitDpName) {
		this.recruitDpName = recruitDpName;
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
	public String getRecruitStatus() {
		return recruitStatus;
	}
	public void setRecruitStatus(String recruitStatus) {
		this.recruitStatus = recruitStatus;
	}
	public String getTutorStatus() {
		return tutorStatus;
	}
	public void setTutorStatus(String tutorStatus) {
		this.tutorStatus = tutorStatus;
	}
	public String getRecruitCampusName() {
		return recruitCampusName;
	}
	public void setRecruitCampusName(String recruitCampusName) {
		this.recruitCampusName = recruitCampusName;
	}
	public String getTutorCampusName() {
		return tutorCampusName;
	}
	public void setTutorCampusName(String tutorCampusName) {
		this.tutorCampusName = tutorCampusName;
	}
	public String getTutorDpName() {
		return tutorDpName;
	}
	public void setTutorDpName(String tutorDpName) {
		this.tutorDpName = tutorDpName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAssignFlag() {
		return assignFlag;
	}

	public void setAssignFlag(String assignFlag) {
		this.assignFlag = assignFlag;
	}
}
