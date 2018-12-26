package com.yz.model.condition.recruit;

import com.yz.model.common.PageCondition;
/**
 * 学员考前资料核查-查询条件
 * @author Administrator
 *
 */
public class StudentAnnexCheckQuery extends PageCondition {

	private String stdName;
	private String idCard;
	private String mobile;
	private String myAnnexStatus;
	private String recruitType;
	private String scholarship;
	private String stdStage;
	private String isDataCompleted;
	private String recruitName;
	private String grade;
	private String sg;
	private String unvsId;
	private String pfsnLevel;
	private String pfsnId;
	private String inclusionStatus;
	private String campusId;
	private String dpId;
	private String taId;
	private String taName;

	private String ext1;
	
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
	public String getIsDataCompleted() {
		return isDataCompleted;
	}
	public void setIsDataCompleted(String isDataCompleted) {
		this.isDataCompleted = isDataCompleted == null ? null : isDataCompleted.trim();
	}
	public String getRecruitName() {
		return recruitName;
	}
	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName == null ? null : recruitName.trim();
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
	}
	public String getSg() {
		return sg;
	}
	public void setSg(String sg) {
		this.sg = sg;
	}
	public String getUnvsId() {
		return unvsId;
	}
	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}
	public String getPfsnId() {
		return pfsnId;
	}
	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
	}
	public String getInclusionStatus()
	{
		return inclusionStatus;
	}
	public void setInclusionStatus(String inclusionStatus)
	{
		this.inclusionStatus = inclusionStatus;
	}
	public String getExt1()
	{
		return ext1;
	}
	public void setExt1(String ext1)
	{
		this.ext1 = ext1;
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

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}


	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId;
	}
}
