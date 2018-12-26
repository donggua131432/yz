package com.yz.model.condition.stdService;

import com.yz.model.common.PageCondition;

/**
 * 在读学员列表查询条件
 * 
 * @author Administrator
 *
 */
public class StudentStudyingQuery extends PageCondition {

	private String stdName;
	private String idCard;
	private String mobile;
	private String unvsName;
	private String pfsnName;
	private String grade;
	private String recruitType;

	private String sex;
	private String address;
	private String pfsnLevel;
	private String stdNo;
	private String schoolRoll;
	private String scholarship;
	private String empName;

	private String isArrears;//
	private String recruitName;
	private String stdType;
	private String statGroup;
	/**
	 * 优惠分组
	 */
	private String sg;
	/**
	 * 入围状态
	 */
	private String inclusionStatus;
	/**
	 * 考试区县
	 */
	private String taName;
	
	private String pfsnId;
	private String unvsId;
	private String workPlace; //工作单位
	private String campusId; //招生分校
	private String dpId;// 招生部门
	private String stdStage;//学员状态
	private String empStatus;//学服老师状态
	private String taId;	//考试县区
	private String homeCampusId;//校区归属
	private String isTutor;//是否分配辅导员
	private String isRemark;//是否填写业务备注
	private String startBirthday;
	private String endBirthday;
	
	private String yearArrears; //每个单独的学年是否欠费
	
	private String itemYear; //查询的缴费年度
	
	private String ifSemester; //是否按照学期
	
	private String yearArrearsValue;
	
	private String feeStandard;

	
	public String getSchoolRoll() {
		return schoolRoll;
	}

	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll =  schoolRoll == null ? null : schoolRoll.trim();
	}

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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType == null ? null : recruitType.trim();
	}
	public String getSex()
	{
		return sex;
	}
	public void setSex(String sex)
	{
		this.sex = sex;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address == null ? null : address.trim();
	}
	public String getPfsnLevel()
	{
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel)
	{
		this.pfsnLevel = pfsnLevel;
	}
	public String getStdNo()
	{
		return stdNo;
	}
	public void setStdNo(String stdNo)
	{
		this.stdNo =  stdNo == null ? null : stdNo.trim();
	}
	public String getScholarship()
	{
		return scholarship;
	}
	public void setScholarship(String scholarship)
	{
		this.scholarship = scholarship;
	}
	public String getEmpName()
	{
		return empName;
	}
	public void setEmpName(String empName)
	{
		this.empName = empName == null ? null : empName.trim();
	}


	public String getIsArrears() {
		return isArrears;
	}

	public void setIsArrears(String isArrears) {
		this.isArrears = isArrears;
	}

	public String getRecruitName() {
		return recruitName;
	}

	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName == null ? null : recruitName.trim();
	}

	public String getStdType() {
		return stdType;
	}

	public void setStdType(String stdType) {
		this.stdType = stdType;
	}

	public String getStatGroup() {
		return statGroup;
	}

	public void setStatGroup(String statGroup) {
		this.statGroup = statGroup;
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

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
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

	public String getWorkPlace() {
		return workPlace;
	}

	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace == null ? null : workPlace.trim();
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId;
	}

	public String getHomeCampusId() {
		return homeCampusId;
	}

	public void setHomeCampusId(String homeCampusId) {
		this.homeCampusId = homeCampusId;
	}

	public String getIsTutor() {
		return isTutor;
	}

	public void setIsTutor(String isTutor) {
		this.isTutor = isTutor;
	}

	public String getIsRemark() {
		return isRemark;
	}

	public void setIsRemark(String isRemark) {
		this.isRemark = isRemark;
	}

	public String getStartBirthday() {
		return startBirthday;
	}

	public void setStartBirthday(String startBirthday) {
		this.startBirthday = startBirthday;
	}

	public String getEndBirthday() {
		return endBirthday;
	}

	public void setEndBirthday(String endBirthday) {
		this.endBirthday = endBirthday;
	}

	public String getYearArrears()
	{
		return yearArrears;
	}

	public void setYearArrears(String yearArrears)
	{
		this.yearArrears = yearArrears;
	}

	public String getItemYear()
	{
		return itemYear;
	}

	public void setItemYear(String itemYear)
	{
		this.itemYear = itemYear;
	}

	public String getIfSemester()
	{
		return ifSemester;
	}

	public void setIfSemester(String ifSemester)
	{
		this.ifSemester = ifSemester;
	}

	public String getYearArrearsValue()
	{
		return yearArrearsValue;
	}

	public void setYearArrearsValue(String yearArrearsValue)
	{
		this.yearArrearsValue = yearArrearsValue;
	}

	public String getFeeStandard()
	{
		return feeStandard;
	}

	public void setFeeStandard(String feeStandard)
	{
		this.feeStandard = feeStandard;
	}
	
	

}
