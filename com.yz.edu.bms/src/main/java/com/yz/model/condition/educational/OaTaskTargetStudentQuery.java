package com.yz.model.condition.educational;

import com.yz.model.common.PageCondition;

/**
 * 教务任务-目标学员的查询条件
 * @author lx
 * @date 2017年7月20日 上午11:29:31
 */
public class OaTaskTargetStudentQuery extends PageCondition {
	
	private String unvsId;
	private String recruitType;
	private String grade;
	private String pfsnName;
	private String stdName;
	private String idCard;
	private String mobile;
	private String isChecked;
	private String taskId;
	
	private String examDataCheck;
	private String graduateDataCheck;
	private String graduateFinanceCheck;
	private String tutorName;
	private String pfsnLevel;
	private String taName;
	private String address;
	private String sg;
	private String inclusionStatus;
	private String scholarship;
	private String stdType;
	
	private String stdStage;
    private String[] stdStageArray;
	private String pfsnId;  
	public String getUnvsId() {
		return unvsId;
	}
	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getPfsnName() {
		return pfsnName;
	}
	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getExamDataCheck()
	{
		return examDataCheck;
	}
	public void setExamDataCheck(String examDataCheck)
	{
		this.examDataCheck = examDataCheck;
	}
	public String getGraduateDataCheck()
	{
		return graduateDataCheck;
	}
	public void setGraduateDataCheck(String graduateDataCheck)
	{
		this.graduateDataCheck = graduateDataCheck;
	}
	public String getGraduateFinanceCheck()
	{
		return graduateFinanceCheck;
	}
	public void setGraduateFinanceCheck(String graduateFinanceCheck)
	{
		this.graduateFinanceCheck = graduateFinanceCheck;
	}
	public String getTutorName()
	{
		return tutorName;
	}
	public void setTutorName(String tutorName)
	{
		this.tutorName = tutorName;
	}
	public String getPfsnLevel()
	{
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel)
	{
		this.pfsnLevel = pfsnLevel;
	}
	public String getTaName()
	{
		return taName;
	}
	public void setTaName(String taName)
	{
		this.taName = taName;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getScholarship()
	{
		return scholarship;
	}
	public void setScholarship(String scholarship)
	{
		this.scholarship = scholarship;
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
	public String getStdStage()
	{
		return stdStage;
	}
	public void setStdStage(String stdStage)
	{
		this.stdStage = stdStage;
	}
	public String[] getStdStageArray()
	{
		return stdStageArray;
	}
	public void setStdStageArray(String[] stdStageArray)
	{
		this.stdStageArray = stdStageArray;
	}
	public String getPfsnId()
	{
		return pfsnId;
	}
	public void setPfsnId(String pfsnId)
	{
		this.pfsnId = pfsnId;
	}

}
