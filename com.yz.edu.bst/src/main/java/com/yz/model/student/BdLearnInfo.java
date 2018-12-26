package com.yz.model.student;

import java.io.Serializable;

public class BdLearnInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7711332067289807240L;
	
	private String learnId;
	private String stdId;
	private String stdNo;
	private String stdType;
	
	private String pfsnName;
	private String unvsName;
	private String teachMethod;
	private String stdStage;
	private String grade;
	private String pfsnLevel;
	
	private String unvsId;
	private String pfsnId;
	
	private String taId;
	private String scholarship;
	private String feeId;
	private String offerId;
	
	private String learnStage;
	
	private String isCompleted;
	private String isDataCompleted;
	private String isDataCheck;
	private String financeCode;
	private String recruitType;
	private String mobile;
	private String stdName;
	private String examNum;
	
	

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getTeachMethod() {
		return teachMethod;
	}

	public void setTeachMethod(String teachMethod) {
		this.teachMethod = teachMethod;
	}

	public String getExamNum() {
		return examNum;
	}

	public void setExamNum(String examNum) {
		this.examNum = examNum == null ? null : examNum.trim();
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getStdNo() {
		return stdNo;
	}

	public void setStdNo(String stdNo) {
		this.stdNo = stdNo == null ? null : stdNo.trim();
	}

	public String getStdType() {
		return stdType;
	}

	public void setStdType(String stdType) {
		this.stdType = stdType == null ? null : stdType.trim();
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId == null ? null : unvsId.trim();
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId == null ? null : pfsnId.trim();
	}

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId == null ? null : taId.trim();
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship == null ? null : scholarship.trim();
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId == null ? null : feeId.trim();
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId == null ? null : offerId.trim();
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
	}

	public String getLearnStage() {
		return learnStage;
	}

	public void setLearnStage(String learnStage) {
		this.learnStage = learnStage == null ? null : learnStage.trim();
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage == null ? null : stdStage.trim();
	}

	public String getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted == null ? null : isCompleted.trim();
	}

	public String getFinanceCode() {
		return financeCode;
	}

	public void setFinanceCode(String financeCode) {
		this.financeCode = financeCode == null ? null : financeCode.trim();
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType == null ? null : recruitType.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName == null ? null : stdName.trim();
	}

	public String getIsDataCompleted() {
		return isDataCompleted;
	}

	public void setIsDataCompleted(String isDataCompleted) {
		this.isDataCompleted = isDataCompleted == null ? null : isDataCompleted.trim();
	}

	/**
	 * @return the isDataCheck
	 */
	public String getIsDataCheck() {
		return isDataCheck;
	}

	/**
	 * @param isDataCheck the isDataCheck to set
	 */
	public void setIsDataCheck(String isDataCheck) {
		this.isDataCheck = isDataCheck == null ? null : isDataCheck.trim();
	}
}
