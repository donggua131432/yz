package com.yz.model.common;

import com.yz.model.common.PageCondition;

/**
 * 教学计划选择列表 查询实体
 * @author Administrator
 *
 */
public class ThpQueryInfo extends PageCondition {

	private String unvsName;
	private String pfsnName;
	private String unvsId;
	private String pfsnId;
	private String semester;
	private String recruitType;
	private String pfsnLevel;
	private String grade;
	
	private String[] selectedThpId;
	private String courseId;
	
	public String getUnvsName() {
		return unvsName;
	}
	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}
	public String getPfsnName() {
		return pfsnName;
	}
	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}
	public String[] getSelectedThpId() {
		return selectedThpId;
	}
	public void setSelectedThpId(String[] selectedThpId) {
		this.selectedThpId = selectedThpId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getUnvsId() {
		return unvsId;
	}
	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}
	public String getPfsnId() {
		return pfsnId;
	}
	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	
}
