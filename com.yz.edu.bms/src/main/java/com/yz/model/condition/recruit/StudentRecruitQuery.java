package com.yz.model.condition.recruit;

import com.yz.model.common.PageCondition;
/**
 * 我的学员查询条件
 * @author Administrator
 *
 */
public class StudentRecruitQuery extends PageCondition {

	private String stdName;
	private String idCard;
	private String mobile;
	private String unvsName;
	private String pfsnName;
	private String myAnnexStatus;
	private String recruitType;
	private String scholarship;
	private String stdStage;
	private String remarkType;
	private String rtCompleted;
	private String isDataCompleted;
	
	private String grade;
	private String recruitName;
	private String taName;
	private String recruitStatus;
	
	private String unvsId;
	private String pfsnId;
	private String sg;
	private String inclusionStatus;
	private String pfsnLevel;
	private String stdType;

	private String remark;
	private String assignFlag;
	
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName == null ? null : stdName.trim();
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard == null ? null : idCard.trim();
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}
	public String getUnvsName() {
		return unvsName;
	}
	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName == null ? null : unvsName.trim();
	}
	public String getPfsnName() {
		return pfsnName;
	}
	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName == null ? null : pfsnName.trim();
	}
	public String getMyAnnexStatus() {
		return myAnnexStatus;
	}
	public void setMyAnnexStatus(String myAnnexStatus) {
		this.myAnnexStatus = myAnnexStatus == null ? null : myAnnexStatus.trim();
	}
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType == null ? null : recruitType.trim();
	}
	public String getScholarship() {
		return scholarship;
	}
	public void setScholarship(String scholarship) {
		this.scholarship = scholarship == null ? null : scholarship.trim();
	}
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage == null ? null : stdStage.trim();
	}
	public String getRemarkType() {
		return remarkType;
	}
	public void setRemarkType(String remarkType) {
		this.remarkType = remarkType == null ? null : remarkType.trim();
	}
	public String getRtCompleted() {
		return rtCompleted;
	}
	public void setRtCompleted(String rtCompleted) {
		this.rtCompleted = rtCompleted == null ? null : rtCompleted.trim();
	}
	public String getIsDataCompleted() {
		return isDataCompleted;
	}
	public void setIsDataCompleted(String isDataCompleted) {
		this.isDataCompleted = isDataCompleted == null ? null : isDataCompleted.trim();
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
	}
	public String getRecruitName() {
		return recruitName;
	}
	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName == null ? null : recruitName.trim();
	}
	public String getTaName() {
		return taName;
	}
	public void setTaName(String taName) {
		this.taName = taName;
	}
	public String getRecruitStatus() {
		return recruitStatus;
	}
	public void setRecruitStatus(String recruitStatus) {
		this.recruitStatus = recruitStatus;
	}
	public String getUnvsId()
	{
		return unvsId;
	}
	public void setUnvsId(String unvsId)
	{
		this.unvsId = unvsId;
	}
	public String getPfsnId()
	{
		return pfsnId;
	}
	public void setPfsnId(String pfsnId)
	{
		this.pfsnId = pfsnId;
	}
	public String getSg()
	{
		return sg;
	}
	public void setSg(String sg)
	{
		this.sg = sg;
	}
	public String getInclusionStatus()
	{
		return inclusionStatus;
	}
	public void setInclusionStatus(String inclusionStatus)
	{
		this.inclusionStatus = inclusionStatus;
	}
	public String getPfsnLevel()
	{
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel)
	{
		this.pfsnLevel = pfsnLevel;
	}
	public String getStdType()
	{
		return stdType;
	}
	public void setStdType(String stdType)
	{
		this.stdType = stdType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAssignFlag() {
		return assignFlag;
	}

	public void setAssignFlag(String assignFlag) {
		this.assignFlag = assignFlag;
	}
}
