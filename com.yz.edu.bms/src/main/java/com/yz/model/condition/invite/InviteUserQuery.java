package com.yz.model.condition.invite;

import java.util.List;

import com.yz.model.common.PageCondition;

public class InviteUserQuery extends PageCondition {
	/** 粉丝姓名 */
	private String name;
	/** 远智编号 */
	private String mobile;
	/** 远智编号 */
	private String yzCode;
	/** 身份证号 */
	private String idCard;
	/** 招生老师姓名 */
	private String recruitName;
	private String empId;

	/** 来源 */
	private String sScholarship;
	
	/** 去向 */
	private String dScholarship;
	
	/** 院校名称 */
	private String unvsName;
	/** 专业名称 */
	private String pfsnName;
	/** 年级 */
	private String grade;
	
	/** 是否有跟进人 */
	private String hasFollow;

	private String empStatus;
	
	/** 人员类型 */
	private String inviteType;

	private List<String> empIds;


	/** 校监 */
	private List<String> dpIds;
	private String dpManager;
	private String dpManagerName;

	/** 上线姓名 */
	private String pName;
	/** 上线手机号 */
	private String pMobile;
	/** 上线远智编号 */
	private String pYzCode;
	/** 注册时间 */
	private String regTime;
	private String regTime1;

	/** 是否学校分配 **/
	private String assignFlag;

	/** 分配时间 */
	private String assignTime;
	private String assignTime1;

	/** 手机归属地 */
	private String mobileLocation;

	/** 是否已分配 **/
	private String isAssign;

	/** 是否已处理 **/
	private String isHandle;

	private String intentionType;
	private String remark;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getYzCode() {
		return yzCode;
	}

	public void setYzCode(String yzCode) {
		this.yzCode = yzCode == null ? null : yzCode.trim();
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard == null ? null : idCard.trim();
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getRecruitName() {
		return recruitName;
	}

	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName == null ? null : recruitName.trim();
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

	public String getsScholarship() {
		return sScholarship;
	}

	public void setsScholarship(String sScholarship) {
		this.sScholarship = sScholarship == null ? null : sScholarship.trim();
	}

	public String getdScholarship() {
		return dScholarship;
	}

	public void setdScholarship(String dScholarship) {
		this.dScholarship = dScholarship == null ? null : dScholarship.trim();
	}

	public String getHasFollow() {
		return hasFollow;
	}

	public void setHasFollow(String hasFollow) {
		this.hasFollow = hasFollow == null ? null : hasFollow.trim();
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}

	public String getInviteType() {
		return inviteType;
	}

	public void setInviteType(String inviteType) {
		this.inviteType = inviteType == null ? null : inviteType.trim();
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the empIds
	 */
	public List<String> getEmpIds() {
		return empIds;
	}

	/**
	 * @param empIds the empIds to set
	 */
	public void setEmpIds(List<String> empIds) {
		this.empIds = empIds;
	}

	public List<String> getDpIds() {
		return dpIds;
	}

	public void setDpIds(List<String> dpIds) {
		this.dpIds = dpIds;
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

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getpMobile() {
		return pMobile;
	}

	public void setpMobile(String pMobile) {
		this.pMobile = pMobile;
	}

	public String getpYzCode() {
		return pYzCode;
	}

	public void setpYzCode(String pYzCode) {
		this.pYzCode = pYzCode;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getRegTime1() {
		return regTime1;
	}

	public void setRegTime1(String regTime1) {
		this.regTime1 = regTime1;
	}

	public String getMobileLocation() {
		return mobileLocation;
	}

	public void setMobileLocation(String mobileLocation) {
		this.mobileLocation = mobileLocation;
	}

	public String getIsAssign() {
		return isAssign;
	}

	public void setIsAssign(String isAssign) {
		this.isAssign = isAssign;
	}

	public String getIsHandle() {
		return isHandle;
	}

	public void setIsHandle(String isHandle) {
		this.isHandle = isHandle;
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

	public String getAssignFlag() {
		return assignFlag;
	}

	public void setAssignFlag(String assignFlag) {
		this.assignFlag = assignFlag;
	}

	public String getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}

	public String getAssignTime1() {
		return assignTime1;
	}

	public void setAssignTime1(String assignTime1) {
		this.assignTime1 = assignTime1;
	}
}
