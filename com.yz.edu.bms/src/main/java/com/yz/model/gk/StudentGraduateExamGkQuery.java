package com.yz.model.gk;

import com.yz.model.common.PageCondition;

public class StudentGraduateExamGkQuery  extends PageCondition {
	private String followId;
	private String stdName;
	private String idCard;
	private String schoolRoll;
	private String taskId;
	private String grade;
	private String unvsName;
	private String pfsnLevel;
	private String pfsnName;
	private String tutor;
	private String unvsId;
	private String pfsnId;
	private String notification; //通知情况
    private String testArea;//考试县区
    private String testAddress;//考试地址
    private String isPayreg;//是否缴费报名
    private String enrollSubject;//报名科目
    private String testTime;//考试时间
    private String isTest;//是否参考
    private String isPass;//是否合格
    private String isCcaa;//是否考前辅导
    private String isRemark;//是否备注
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getSchoolRoll() {
		return schoolRoll;
	}
	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getUnvsName() {
		return unvsName;
	}
	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}
	public String getPfsnName() {
		return pfsnName;
	}
	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
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
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	public String getTestArea() {
		return testArea;
	}
	public void setTestArea(String testArea) {
		this.testArea = testArea;
	}
	public String getIsPayreg() {
		return isPayreg;
	}
	public void setIsPayreg(String isPayreg) {
		this.isPayreg = isPayreg;
	}
	public String getEnrollSubject() {
		return enrollSubject;
	}
	public void setEnrollSubject(String enrollSubject) {
		this.enrollSubject = enrollSubject;
	}
	public String getTestTime() {
		return testTime;
	}
	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}
	public String getIsTest() {
		return isTest;
	}
	public void setIsTest(String isTest) {
		this.isTest = isTest;
	}
	public String getIsPass() {
		return isPass;
	}
	public void setIsPass(String isPass) {
		this.isPass = isPass;
	}
	public String getIsCcaa() {
		return isCcaa;
	}
	public void setIsCcaa(String isCcaa) {
		this.isCcaa = isCcaa;
	}
	public String getIsRemark() {
		return isRemark;
	}
	public void setIsRemark(String isRemark) {
		this.isRemark = isRemark;
	}
	public String getFollowId() {
		return followId;
	}
	public void setFollowId(String followId) {
		this.followId = followId;
	}
	public String getTestAddress() {
		return testAddress;
	}
	public void setTestAddress(String testAddress) {
		this.testAddress = testAddress;
	}
	
}
