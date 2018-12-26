package com.yz.model.educational;

import java.util.List;

import com.yz.model.recruit.BdLearnRemark;

public class JStudentStudyingListInfo {

	private String learnId;
	private String stdId;
	private String stdName;
	private String stdNo;
	private String stdStage;
	private String unvsName;
	private String pfsnName;
	private String unvsId;
	private String pfsnId;
	private String pfsnCode;
	private String pfsnLevel;
	private String recruitType;
	private String grade;
	private String tutorName;
	private String tutorEmpId;
	
	private String recruitName;
	private String campusName;
	private String dpName;
	
	/**
	 * 已付
	 */
	private String paid;
	/**
	 * 欠款
	 */
	private String debt;
	
	/**
	 * 应缴
	 */
	private String amount;
	
	/**
	 * 备注标签
	 */
	private List<BdLearnRemark> remarkList;
	
	private String stdType;
	
	private String schoolRoll;
	
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
	public String getStdNo() {
		return stdNo;
	}
	public void setStdNo(String stdNo) {
		this.stdNo = stdNo;
	}
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
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
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}
	public String getPfsnCode() {
		return pfsnCode;
	}
	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getPaid() {
		return paid;
	}
	public void setPaid(String paid) {
		this.paid = paid;
	}
	public String getDebt() {
		return debt;
	}
	public void setDebt(String debt) {
		this.debt = debt;
	}
	public List<BdLearnRemark> getRemarkList() {
		return remarkList;
	}
	public void setRemarkList(List<BdLearnRemark> remarkList) {
		this.remarkList = remarkList;
	}
	public String getTutorName() {
		return tutorName;
	}
	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}
	public String getTutorEmpId() {
		return tutorEmpId;
	}
	public void setTutorEmpId(String tutorEmpId) {
		this.tutorEmpId = tutorEmpId;
	}
	public String getRecruitName() {
		return recruitName;
	}
	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getDpName() {
		return dpName;
	}
	public void setDpName(String dpName) {
		this.dpName = dpName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStdType()
	{
		return stdType;
	}
	public void setStdType(String stdType)
	{
		this.stdType = stdType;
	}
	public String getSchoolRoll()
	{
		return schoolRoll;
	}
	public void setSchoolRoll(String schoolRoll)
	{
		this.schoolRoll = schoolRoll;
	}
	public String getUnvsId() {
		return unvsId;
	}
	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}
	public String getPfsnId() {
		return pfsnId;
	}
	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
	}
	
}
