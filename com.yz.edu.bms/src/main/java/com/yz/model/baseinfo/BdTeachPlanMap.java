package com.yz.model.baseinfo;

import java.util.List;

import com.yz.model.educational.BdTextBook;


public class BdTeachPlanMap {
	private String recruitType;
	private String unvsName;
	private String pfsnCode;
	private String pfsnName;
	private String pfsnLevel;
	private String pfsnId;
	private String thpName;
	private String thpId;
	private String semester;
	private String totalHours;
	private String educatedHour;
	private String year;
	private String thpType;
	private String credit;
	private String remark;
	private String practiceHours;
	private String selfHours;
	private String grade;
	private String testSubject;//考试科目
	private List<BdTextBook> textbook;
	private List<BdCourse> course;



	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getPfsnCode() {
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
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

	public String getThpName() {
		return thpName;
	}

	public void setThpName(String thpName) {
		this.thpName = thpName;
	}

	public String getThpId() {
		return thpId;
	}

	public void setThpId(String thpId) {
		this.thpId = thpId;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}

	public String getEducatedHour() {
		return educatedHour;
	}

	public void setEducatedHour(String educatedHour) {
		this.educatedHour = educatedHour;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getThpType() {
		return thpType;
	}

	public void setThpType(String thpType) {
		this.thpType = thpType;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPracticeHours() {
		return practiceHours;
	}

	public void setPracticeHours(String practiceHours) {
		this.practiceHours = practiceHours;
	}

	public String getSelfHours() {
		return selfHours;
	}

	public void setSelfHours(String selfHours) {
		this.selfHours = selfHours;
	}

	public List<BdTextBook> getTextbook() {
		return textbook;
	}

	public void setTextbook(List<BdTextBook> textbook) {
		this.textbook = textbook;
	}

	public List<BdCourse> getCourse() {
		return course;
	}

	public void setCourse(List<BdCourse> course) {
		course = course;
	}

	public String getTestSubject() {
		return testSubject;
	}

	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
	}
	
	
}
