package com.yz.model.recruit;
/**
 * 全部学员列表数据
 * @author Administrator
 *
 */
public class StudentAllListInfo {

	private String learnId;

	private String stdId;

	private String stdName;

	private String grade;

	private String recruitType;

	private String unvsName;

	private String pfsnName;

	private String pfsnCode;

	private String pfsnLevel;

	private String stdStage;

	private String scholarship;

	private String recruitName;

	private String tutorName;

	private String dpName;
	
	private String isCompleted;
	
	private String empStatus;
	
	private String stdType;

	private String inclusionStatus;

	private String assignFlag;

	private String userId;

	private String remark;

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

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStatus(String stdStage) {
		this.stdStage = stdStage;
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

	public String getPfsnCode() {
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getDpName() {
		return dpName;
	}

	public void setDpName(String dpName) {
		this.dpName = dpName;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	/**
	 * @return the isCompleted
	 */
	public String getIsCompleted() {
		return isCompleted;
	}

	/**
	 * @param isCompleted the isCompleted to set
	 */
	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getEmpStatus()
	{
		return empStatus;
	}

	public void setEmpStatus(String empStatus)
	{
		this.empStatus = empStatus;
	}

	public String getStdType()
	{
		return stdType;
	}

	public void setStdType(String stdType)
	{
		this.stdType = stdType;
	}

	public String getInclusionStatus() {
		return inclusionStatus;
	}

	public void setInclusionStatus(String inclusionStatus) {
		this.inclusionStatus = inclusionStatus;
	}


	public String getAssignFlag() {
		return assignFlag;
	}

	public void setAssignFlag(String assignFlag) {
		this.assignFlag = assignFlag;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
