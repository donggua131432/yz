package com.yz.model.condition.educational;

import com.yz.model.common.PageCondition;

public class JStudentStudyingQuery extends PageCondition {

	private String stdName;
	private String idCard;
	private String mobile;
	private String unvsName;
	private String pfsnName;
	private String grade;
	private String recruitType;
	
	private String campusName;
	private String dpName;
	private String recruitName;
	private String tutorName;
	
	private String stdType;
	private String stdNo;
	private String schoolRoll;
	private String pfsnLevel;
	
	private String unvsId;
	private String pfsnId;
	private String dpId;
	private String campusId;
	private String sg;
	private String scholarship;
	private String inclusionStatus;
	private String homeCampusId;
	private String taId;
	private String workPlace;
	private String address;
	private String stdStage;
	private String empStatus;
	private String isTutor;
	private String startBirthday;
	private String endBirthday;
	
	
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
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName == null ? null : campusName.trim();
	}
	public String getDpName() {
		return dpName;
	}
	public void setDpName(String dpName) {
		this.dpName = dpName == null ? null : dpName.trim();
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
	public String getStdType() {
		return stdType;
	}
	public void setStdType(String stdType) {
		this.stdType = stdType == null ? null : stdType.trim();
	}
	public String getStdNo() {
		return stdNo;
	}
	public void setStdNo(String stdNo) {
		this.stdNo = stdNo == null ? null : stdNo.trim();
	}
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel == null ? null : pfsnLevel.trim();
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
	public String getDpId()
	{
		return dpId;
	}
	public void setDpId(String dpId)
	{
		this.dpId = dpId;
	}
	public String getCampusId()
	{
		return campusId;
	}
	public void setCampusId(String campusId)
	{
		this.campusId = campusId;
	}
	public String getSchoolRoll() {
		return schoolRoll;
	}
	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll == null ? null : schoolRoll.trim();
	}
	public String getSg() {
		return sg;
	}
	public void setSg(String sg) {
		this.sg = sg == null ? null : sg.trim();
	}
	public String getScholarship() {
		return scholarship;
	}
	public void setScholarship(String scholarship) {
		this.scholarship = scholarship == null ? null : scholarship.trim();
	}
	public String getInclusionStatus() {
		return inclusionStatus;
	}
	public void setInclusionStatus(String inclusionStatus) {
		this.inclusionStatus = inclusionStatus == null ? null : inclusionStatus.trim();
	}
	public String getHomeCampusId() {
		return homeCampusId;
	}
	public void setHomeCampusId(String homeCampusId) {
		this.homeCampusId = homeCampusId == null ? null : homeCampusId.trim();
	}
	public String getTaId() {
		return taId;
	}
	public void setTaId(String taId) {
		this.taId = taId == null ? null : taId.trim();
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace == null ? null : workPlace.trim();
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address =  address == null ? null : address.trim();
	}
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage == null ? null : stdStage.trim();
	}
	public String getEmpStatus() {
		return empStatus;
	}
	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus == null ? null : empStatus.trim();
	}
	public String getIsTutor() {
		return isTutor;
	}
	public void setIsTutor(String isTutor) {
		this.isTutor = isTutor == null ? null : isTutor.trim();
	}
	public String getStartBirthday() {
		return startBirthday;
	}
	public void setStartBirthday(String startBirthday) {
		this.startBirthday = startBirthday == null ? null : startBirthday.trim();
	}
	public String getEndBirthday() {
		return endBirthday;
	}
	public void setEndBirthday(String endBirthday) {
		this.endBirthday = endBirthday == null ? null : endBirthday.trim();
	}
	
	
}
