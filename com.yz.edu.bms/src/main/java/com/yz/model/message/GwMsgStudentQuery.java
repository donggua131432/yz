package com.yz.model.message;

import java.util.List;

public class GwMsgStudentQuery {
	private String recruitType;
	private String stdName;
	private String idCard;
	private String mobile;
	private String isChecked;
	private String mtpId;
	private String tutorName;
	private String unvsId;
	private String pfsnId;
	private String grade;
	private String taId;
	private String pfsnLevel;
	private String hasScore;
	private String recruitName;
	private String scholarship;
	private String[] stdStages;
	private String[] learnIds;

	private String isArrearage;
	private String recruitCampus;
	private String recruitDepartment;
	private String stdType;

	public String[] getLearnIds() {
		return learnIds;
	}

	public void setLearnIds(String[] learnIds) {
		this.learnIds = learnIds;
	}

	public String getStdType() {
		return stdType;
	}

	public void setStdType(String stdType) {
		this.stdType = stdType == null ? null : stdType.trim();
	}

	public String getRecruitCampus() {
		return recruitCampus;
	}

	public void setRecruitCampus(String recruitCampus) {
		this.recruitCampus = recruitCampus == null ? null : recruitCampus.trim();
	}

	public String getRecruitDepartment() {
		return recruitDepartment;
	}

	public void setRecruitDepartment(String recruitDepartment) {
		this.recruitDepartment = recruitDepartment == null ? null : recruitDepartment.trim();
	}

	public String getIsArrearage() {
		return isArrearage;
	}

	public void setIsArrearage(String isArrearage) {
		this.isArrearage = isArrearage == null ? null : isArrearage.trim();
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName == null ? null : tutorName.trim();
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

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel == null ? null : pfsnLevel.trim();
	}

	public String getHasScore() {
		return hasScore;
	}

	public void setHasScore(String hasScore) {
		this.hasScore = hasScore == null ? null : hasScore.trim();
	}

	public String getRecruitName() {
		return recruitName;
	}

	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName == null ? null : recruitName.trim();
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship == null ? null : scholarship.trim();
	}

	public String[] getStdStages() {
		return stdStages;
	}

	public void setStdStages(String[] stdStages) {
		this.stdStages = stdStages;
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId == null ? null : unvsId.trim();
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType == null ? null : recruitType.trim();
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
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

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked == null ? null : isChecked.trim();
	}

	public String getMtpId() {
		return mtpId;
	}

	public void setMtpId(String mtpId) {
		this.mtpId = mtpId == null ? null : mtpId.trim();
	}

}
