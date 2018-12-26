package com.yz.model.condition.stdService;

import com.yz.model.common.PageCondition;
/**
 * 辅导员列表分配查询条件
 * @author Administrator
 *
 */
public class StudentAssignQuery extends PageCondition {

	private String stdName;
	private String idCard;
	private String mobile;
	private String unvsId;
	private String pfsnName;
	private String pfsnLevel;
	private String enrollUnvsId;
	private String enrollPfsnName;
	private String grade;
	private String recruitType;
	private String scholarship;
	private String sg;
	private String inclusionStatus;
	private String taName;
	private String stdType;
	private String stdStage;
	private String tutorName;
	
	private String pfsnId;
	
	private String isAssign;//是否分配辅导员
	
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
	
	public String getPfsnName() {
		return pfsnName;
	}
	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName == null ? null : pfsnName.trim();
	}
	public String getUnvsId()
	{
		return unvsId;
	}
	public void setUnvsId(String unvsId)
	{
		this.unvsId = unvsId;
	}
	public String getPfsnLevel()
	{
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel)
	{
		this.pfsnLevel = pfsnLevel;
	}
	public String getEnrollUnvsId()
	{
		return enrollUnvsId;
	}
	public void setEnrollUnvsId(String enrollUnvsId)
	{
		this.enrollUnvsId = enrollUnvsId;
	}
	public String getEnrollPfsnName()
	{
		return enrollPfsnName;
	}
	public void setEnrollPfsnName(String enrollPfsnName)
	{
		this.enrollPfsnName = enrollPfsnName;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getRecruitType()
	{
		return recruitType;
	}
	public void setRecruitType(String recruitType)
	{
		this.recruitType = recruitType;
	}
	public String getScholarship()
	{
		return scholarship;
	}
	public void setScholarship(String scholarship)
	{
		this.scholarship = scholarship;
	}
	
	public String getTaName()
	{
		return taName;
	}
	public void setTaName(String taName)
	{
		this.taName = taName;
	}

	public String getStdType()
	{
		return stdType;
	}
	public void setStdType(String stdType)
	{
		this.stdType = stdType;
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
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}
	public String getTutorName() {
		return tutorName;
	}
	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}
	public String getPfsnId()
	{
		return pfsnId;
	}
	public void setPfsnId(String pfsnId)
	{
		this.pfsnId = pfsnId;
	}
	public String getIsAssign() {
		return isAssign;
	}
	public void setIsAssign(String isAssign) {
		this.isAssign = isAssign;
	}
	
}
