package com.yz.model.condition.recruit;

import com.yz.model.common.PageCondition;
/**
 * 全部学员查询条件
 * @author Administrator
 *
 */
public class AllStudentQuery extends PageCondition {

	private String stdName;
	private String idCard;
	private String mobile;
	private String stdNo;
	private String recruitName;
	private String tutorName;
	private String recruitType;
	private String scholarship;
	private String stdStage;
	private String grade;
	private String inclusionStatus;
	
	
	private String unvsId;
	private String sg;
	private String pfsnId;
	private String pfsnLevel;
	private String stdType;

	private String remark;
	
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
	public String getStdNo() {
		return stdNo;
	}
	public void setStdNo(String stdNo) {
		this.stdNo = stdNo == null ? null : stdNo.trim();
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
	public String getRecruitName() {
		return recruitName;
	}
	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName == null ? null : recruitName.trim();
	}
	public String getTutorName() {
		return tutorName;
	}
	public void setTutorName(String tutorName) {
		this.tutorName = tutorName == null ? null : tutorName.trim();
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
	}
	public String getInclusionStatus() {
		return inclusionStatus;
	}
	public void setInclusionStatus(String inclusionStatus) {
		this.inclusionStatus = inclusionStatus;
	}
	public String getUnvsId()
	{
		return unvsId;
	}
	public void setUnvsId(String unvsId)
	{
		this.unvsId = unvsId;
	}
	public String getSg()
	{
		return sg;
	}
	public void setSg(String sg)
	{
		this.sg = sg;
	}
	public String getPfsnId()
	{
		return pfsnId;
	}
	public void setPfsnId(String pfsnId)
	{
		this.pfsnId = pfsnId;
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
}
