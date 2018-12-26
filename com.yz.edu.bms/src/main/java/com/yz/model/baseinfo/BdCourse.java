package com.yz.model.baseinfo;

import java.util.Date;

/**
 * Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年12月11日.
 *
 */
public class BdCourse {
	private String courseId;

	private String courseName;

	private String year;

	private String grade;

	private String courseType;

	private String pfnsType;

	private String isAllow;

	private String testSubject;

	private String pfsnId;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private Date createTime;

	private String createUser;

	private String ext2;

	private String ext3;

	private String thpName;

	private String textbookName;

	private String semester;
	
	private String pfsnLevel;
	
	private String isResource;
	public String getPfsnId() {
		return pfsnId;
	}

	public String getTestSubject() {
		return testSubject;
	}

	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
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

	public String getThpName() {
		return thpName;
	}

	public void setThpName(String thpName) {
		this.thpName = thpName;
	}

	public String getTextbookName() {
		return textbookName;
	}

	public void setTextbookName(String textbookName) {
		this.textbookName = textbookName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId == null ? null : courseId.trim();
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName == null ? null : courseName.trim();
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year == null ? null : year.trim();
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType == null ? null : courseType.trim();
	}

	public String getPfnsType() {
		return pfnsType;
	}

	public void setPfnsType(String pfnsType) {
		this.pfnsType = pfnsType == null ? null : pfnsType.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim();
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId == null ? null : updateUserId.trim();
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId == null ? null : createUserId.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2 == null ? null : ext2.trim();
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3 == null ? null : ext3.trim();
	}

	public String getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getIsResource() {
		return isResource;
	}

	public void setIsResource(String isResource) {
		this.isResource = isResource;
	}
	
	
}