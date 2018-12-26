package com.yz.model.recruit;

import java.util.List;

/**
 * 我的学员列表信息
 * 
 * @author Administrator
 *
 */
public class BdRecruitStudentListInfo {

	private String learnId;
	private String stdId;
	private String stdName;
	private String stdStage;
	private String pfsnLevel;
	private String scholarship;
	private String unvsName;
	private String pfsnName;
	private String pfsnCode;
	private String grade;
	private String recruitType;
	private String stdType;
	/** 辅导费 */
	private String counsellingFees;
	/** 辅导费 缴费状态 */
	private String counsellingFeesStatus;
	/** 第一年学费*/
	private String tuition;
	/** 第一年学费 缴费状态*/
	private String tuitionStatus;
	/** 滞留账户余额 */
	private String demurrageAccount;
	private String myAnnexStatus;
	private String taName;
	private String isDataCompleted;
	/** 辅导书发放状态 */
	private String sendStatus;
	
	private List<BdLearnRemark> remarkList;
	
	private int annexCount;
	
	private String recruitName;
	
	private String empStatus;

	private String inclusionStatus;

	/**驳回备注**/
	private String ext1;

	/**分配标识**/
	private String assignFlag;

	private String userId;
	private String remark;

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
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

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
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

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getStdType() {
		return stdType;
	}

	public void setStdType(String stdType) {
		this.stdType = stdType;
	}

	public String getMyAnnexStatus() {
		return myAnnexStatus;
	}

	public void setMyAnnexStatus(String myAnnexStatus) {
		this.myAnnexStatus = myAnnexStatus;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}

	public String getIsDataCompleted() {
		return isDataCompleted;
	}

	public void setIsDataCompleted(String isDataCompleted) {
		this.isDataCompleted = isDataCompleted;
	}

	public String getCounsellingFees() {
		return counsellingFees;
	}

	public void setCounsellingFees(String counsellingFees) {
		this.counsellingFees = counsellingFees;
	}

	public String getTuition() {
		return tuition;
	}

	public void setTuition(String tuition) {
		this.tuition = tuition;
	}

	public String getDemurrageAccount() {
		return demurrageAccount;
	}

	public void setDemurrageAccount(String demurrageAccount) {
		this.demurrageAccount = demurrageAccount;
	}

	public List<BdLearnRemark> getRemarkList() {
		return remarkList;
	}

	public void setRemarkList(List<BdLearnRemark> remarkList) {
		this.remarkList = remarkList;
	}

	public String getCounsellingFeesStatus() {
		return counsellingFeesStatus;
	}

	public void setCounsellingFeesStatus(String counsellingFeesStatus) {
		this.counsellingFeesStatus = counsellingFeesStatus;
	}

	public String getTuitionStatus() {
		return tuitionStatus;
	}

	public void setTuitionStatus(String tuitionStatus) {
		this.tuitionStatus = tuitionStatus;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	/**
	 * @return the annexCount
	 */
	public int getAnnexCount() {
		return annexCount;
	}

	/**
	 * @param annexCount the annexCount to set
	 */
	public void setAnnexCount(int annexCount) {
		this.annexCount = annexCount;
	}

	/**
	 * @return the recruitName
	 */
	public String getRecruitName() {
		return recruitName;
	}

	/**
	 * @param recruitName the recruitName to set
	 */
	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName;
	}

	public String getEmpStatus()
	{
		return empStatus;
	}

	public void setEmpStatus(String empStatus)
	{
		this.empStatus = empStatus;
	}

	public String getInclusionStatus() {
		return inclusionStatus;
	}

	public void setInclusionStatus(String inclusionStatus) {
		this.inclusionStatus = inclusionStatus;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
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
