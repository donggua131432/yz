package com.yz.model.baseinfo;

import java.util.Date;

public class BdUnvsProfession {
    private String pfsnId;

    private String year;

    private String grade;

    private String unvsId;
    
    private String unvsName;
    
    private String recruitType;

    private String pfsnName;

    private String pfsnCode;

    private String pfsnCata;

    private String pfsnLevel;

    private String teachMethod;

    private String passScore;

    private String testSubject;

    private String groupId;
    
    private String groupName;

    private String isTeach;

    private String isAllow;

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
    
    private String showTestSubject;
    
    private String textbookPfsncode;

    public String getPfsnId() {
        return pfsnId;
    }

    public void setPfsnId(String pfsnId) {
        this.pfsnId = pfsnId == null ? null : pfsnId.trim();
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
        this.grade = grade == null ? null : grade.trim();
    }

    public String getUnvsId() {
        return unvsId;
    }

    public void setUnvsId(String unvsId) {
        this.unvsId = unvsId == null ? null : unvsId.trim();
    }

    public String getPfsnName() {
        return pfsnName;
    }

    public void setPfsnName(String pfsnName) {
        this.pfsnName = pfsnName == null ? null : pfsnName.trim();
    }

    public String getPfsnCode() {
        return pfsnCode;
    }

    public void setPfsnCode(String pfsnCode) {
        this.pfsnCode = pfsnCode == null ? null : pfsnCode.trim();
    }

    public String getPfsnCata() {
        return pfsnCata;
    }

    public void setPfsnCata(String pfsnCata) {
        this.pfsnCata = pfsnCata == null ? null : pfsnCata.trim();
    }

    public String getPfsnLevel() {
        return pfsnLevel;
    }

    public void setPfsnLevel(String pfsnLevel) {
        this.pfsnLevel = pfsnLevel == null ? null : pfsnLevel.trim();
    }

    public String getTeachMethod() {
        return teachMethod;
    }

    public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public void setTeachMethod(String teachMethod) {
        this.teachMethod = teachMethod == null ? null : teachMethod.trim();
    }

    public String getPassScore() {
        return passScore;
    }

    public void setPassScore(String passScore) {
        this.passScore = passScore == null ? null : passScore.trim();
    }

    public String getTestSubject() {
        return testSubject;
    }

    public void setTestSubject(String testSubject) {
        this.testSubject = testSubject == null ? null : testSubject.trim();
    }

    public String getIsTeach() {
        return isTeach;
    }

    public void setIsTeach(String isTeach) {
        this.isTeach = isTeach == null ? null : isTeach.trim();
    }

    public String getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(String isAllow) {
        this.isAllow = isAllow == null ? null : isAllow.trim();
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

	public String getShowTestSubject()
	{
		return showTestSubject;
	}

	public void setShowTestSubject(String showTestSubject)
	{
		this.showTestSubject = showTestSubject;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTextbookPfsncode() {
		return textbookPfsncode;
	}

	public void setTextbookPfsncode(String textbookPfsncode) {
		this.textbookPfsncode = textbookPfsncode;
	}
	
}