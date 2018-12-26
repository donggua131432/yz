package com.yz.model.baseinfo;

import java.util.Date;

public class BdTeachPlan {
	private String thpId;

	private String pfsnId;

	private String grade;

	private String thpName;

	private String semester;

	private String credit;

	private String thpType;

	private String totalHours;

	private String educatedHour;

	private String selfHours;

	private String practiceHours;

	private String remark;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private Date createTime;

	private String createUser;

	private String ext1;

	private String ext2;

	private String ext3;

	private String ext4;
	
	private String pfsnCode;
	
	private String testSubject;

	public String getThpId() {
		return thpId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public void setThpId(String thpId) {
		this.thpId = thpId == null ? null : thpId.trim();
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId == null ? null : pfsnId.trim();
	}

	public String getThpName() {
		return thpName;
	}

	public void setThpName(String thpName) {
		this.thpName = thpName == null ? null : thpName.trim();
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester == null ? null : semester.trim();
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit == null ? null : credit.trim();
	}

	public String getThpType() {
		return thpType;
	}

	public void setThpType(String thpType) {
		this.thpType = thpType == null ? null : thpType.trim();
	}

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours == null ? null : totalHours.trim();
	}

	public String getEducatedHour() {
		return educatedHour;
	}

	public void setEducatedHour(String educatedHour) {
		this.educatedHour = educatedHour == null ? null : educatedHour.trim();
	}

	public String getSelfHours() {
		return selfHours;
	}

	public void setSelfHours(String selfHours) {
		this.selfHours = selfHours == null ? null : selfHours.trim();
	}

	public String getPracticeHours() {
		return practiceHours;
	}

	public void setPracticeHours(String practiceHours) {
		this.practiceHours = practiceHours == null ? null : practiceHours.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
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

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1 == null ? null : ext1.trim();
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

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getPfsnCode()
	{
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode)
	{
		this.pfsnCode = pfsnCode;
	}

	public String getTestSubject() {
		return testSubject;
	}

	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
	}
	

}