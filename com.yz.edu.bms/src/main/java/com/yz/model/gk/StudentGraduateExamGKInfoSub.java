package com.yz.model.gk;

import java.io.Serializable;

/**
 * 国开本科统考科目跟进
 * @author juliet
 * @date 2018-06-06 上午10:44:55
 */
public class StudentGraduateExamGKInfoSub implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8689493393651009781L;
	private String id;
	private String followId;
    private String testArea;//考试县区
    private String isPayreg;//是否缴费报名
    private String enrollSubject;//报名科目
    private String testTime;//考试时间
    private String testAddress;//考试地点
    private String isTest;//是否参考
    private String isPass;//是否合格
    private String isCcaa;//是否考前辅导
    
    
	public String getFollowId() {
		return followId;
	}
	public void setFollowId(String followId) {
		this.followId = followId;
	}	
	public String getTestArea() {
		return testArea;
	}
	public void setTestArea(String testArea) {
		this.testArea = testArea == null ? null : testArea.trim();
	}
	public String getIsPayreg() {
		return isPayreg;
	}
	public void setIsPayreg(String isPayreg) {
		this.isPayreg = isPayreg == null ? null : isPayreg.trim();
	}
	public String getEnrollSubject() {
		return enrollSubject;
	}
	public void setEnrollSubject(String enrollSubject) {
		this.enrollSubject = enrollSubject == null ? null : enrollSubject.trim();
	}
	public String getTestTime() {
		return testTime;
	}
	public void setTestTime(String testTime) {
		this.testTime = testTime == null ? null : testTime.trim();
	}
	public String getTestAddress() {
		return testAddress;
	}
	public void setTestAddress(String testAddress) {
		this.testAddress = testAddress == null ? null : testAddress.trim();
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    
	
    
    
}
