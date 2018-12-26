package com.yz.model.condition.recruit;

import com.yz.model.common.PageCondition;

public class JudgeQueryInfo extends PageCondition {

	private String stdName;
	private String idCard;
	private String mobile;
	private String unvsName;
	private String pfsnName;

	private String grade;
	private String scholarship;
	private String scholarshipStatus;

	private String[] scholarships;

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
	public String getScholarship() {
		return scholarship;
	}
	public void setScholarship(String scholarship) {
		this.scholarship = scholarship == null ? null : scholarship.trim();
	}
	public String getScholarshipStatus() {
		return scholarshipStatus;
	}
	public void setScholarshipStatus(String scholarshipStatus) {
		this.scholarshipStatus = scholarshipStatus == null ? null : scholarshipStatus.trim();
	}
	
	public String[] getScholarships() {
		return scholarships;
	}
	public void setScholarships(String[] scholarships) {
		this.scholarships = scholarships;
	}
	
}
