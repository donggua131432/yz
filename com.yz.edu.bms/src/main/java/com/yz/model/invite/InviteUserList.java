package com.yz.model.invite;

import java.util.List;
import java.util.Map;

public class InviteUserList {

	private String userId;
    private String nickname;
    private String yzCode;
    private String name;
    private String idCard;
    private String mobile;
    private String mobileLocation;
    
    private String pId;
    private String pNickname;
    private String pYzCode;
    private String pName;
    private String pIdCard;
    private String pMobile;
    private String pMobileLocation;
    
    private String sScholarship;
    private String regTime;
    
    private String dScholarship;
    private String enrollTime;
    private String pfsnLevel;
    private String pfsnName;
    private String unvsName;
    private String grade;
    
    private String empId;
    private String empName;
    private String empMobile;
    private String empStatus;

    private String dpManager;
	private String dpManagerName;

	private String allotCount;
	private String assignFlag;
    
    private List<Map<String, String>> accList;

    private String intentionType;
    private String remark;
    private String stdId;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getYzCode() {
		return yzCode;
	}
	public void setYzCode(String yzCode) {
		this.yzCode = yzCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getpNickname() {
		return pNickname;
	}
	public void setpNickname(String pNickname) {
		this.pNickname = pNickname;
	}
	public String getpYzCode() {
		return pYzCode;
	}
	public void setpYzCode(String pYzCode) {
		this.pYzCode = pYzCode;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpIdCard() {
		return pIdCard;
	}
	public void setpIdCard(String pIdCard) {
		this.pIdCard = pIdCard;
	}
	public String getpMobile() {
		return pMobile;
	}
	public void setpMobile(String pMobile) {
		this.pMobile = pMobile;
	}
	public String getsScholarship() {
		return sScholarship;
	}
	public void setsScholarship(String sScholarship) {
		this.sScholarship = sScholarship;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getdScholarship() {
		return dScholarship;
	}
	public void setdScholarship(String dScholarship) {
		this.dScholarship = dScholarship;
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
	public String getUnvsName() {
		return unvsName;
	}
	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getMobileLocation() {
		return mobileLocation;
	}
	public void setMobileLocation(String mobileLocation) {
		this.mobileLocation = mobileLocation;
	}
	public String getpMobileLocation() {
		return pMobileLocation;
	}
	public void setpMobileLocation(String pMobileLocation) {
		this.pMobileLocation = pMobileLocation;
	}
	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	/**
	 * @return the empName
	 */
	public String getEmpName() {
		return empName;
	}
	/**
	 * @param empName the empName to set
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
	public String getEmpMobile() {
		return empMobile;
	}
	public void setEmpMobile(String empMobile) {
		this.empMobile = empMobile;
	}
	
	/**
	 * @return the empStatus
	 */
	public String getEmpStatus() {
		return empStatus;
	}
	/**
	 * @param empStatus the empStatus to set
	 */
	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
	/**
	 * @return the enrollTime
	 */
	public String getEnrollTime() {
		return enrollTime;
	}
	/**
	 * @param enrollTime the enrollTime to set
	 */
	public void setEnrollTime(String enrollTime) {
		this.enrollTime = enrollTime;
	}
	/**
	 * @return the idCard
	 */
	public String getIdCard() {
		return idCard;
	}
	/**
	 * @param idCard the idCard to set
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public List<Map<String, String>> getAccList() {
		return accList;
	}
	public void setAccList(List<Map<String, String>> accList) {
		this.accList = accList;
	}

	public String getDpManager() {
		return dpManager;
	}

	public void setDpManager(String dpManager) {
		this.dpManager = dpManager;
	}

	public String getDpManagerName() {
		return dpManagerName;
	}

	public void setDpManagerName(String dpManagerName) {
		this.dpManagerName = dpManagerName;
	}

	public String getAllotCount() {
		return allotCount;
	}

	public void setAllotCount(String allotCount) {
		this.allotCount = allotCount;
	}

	public String getAssignFlag() {
		return assignFlag;
	}

	public void setAssignFlag(String assignFlag) {
		this.assignFlag = assignFlag;
	}

	public String getIntentionType() {
		return intentionType;
	}

	public void setIntentionType(String intentionType) {
		this.intentionType = intentionType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}
}
