package com.yz.model.condition.invite;

import java.util.List;

import com.yz.model.common.PageCondition;

public class InviteFansQuery extends PageCondition{
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

	/** 来源 */
	private String sScholarship;
	
	/** 来源 */
	private String dScholarship;
	
	/** 院校名称 */
	private String unvsName;
	/** 专业名称 */
	private String pfsnName;
	/** 年级 */
	private String grade;
	

	/** 人员类型 */
	private String inviteType;

	private List<String> empIds;

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

	public String getRecruitName() {
		return recruitName;
	}

	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName == null ? null : recruitName.trim();
	}

	public String getsScholarship() {
		return sScholarship;
	}

	public void setsScholarship(String sScholarship) {
		this.sScholarship = sScholarship == null ? null : sScholarship.trim();
	}

	public String getInviteType() {
		return inviteType;
	}

	public void setInviteType(String inviteType) {
		this.inviteType = inviteType == null ? null : inviteType.trim();
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

	public String getdScholarship() {
		return dScholarship;
	}

	public void setdScholarship(String dScholarship) {
		this.dScholarship = dScholarship;
	}

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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
}
