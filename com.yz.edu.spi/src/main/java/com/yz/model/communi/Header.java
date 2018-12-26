package com.yz.model.communi;

import java.io.Serializable;

public class Header implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4784586731839998759L;

	/** APP类型 商户 用户 */
	private String appType;
	/** 用户编号*/
	private String userId;
	/** 员工编号 */
	private String empId;
	/** 学员编号 */
	private String stdId;
	/** 学业编号 */
	private String learnId;
	
	/** 微信ID */
	private String openId;

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getLearnId() {
		return learnId;
	}
	
	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Override
	public String toString() {
		return Header.class.getName() 
				+ ":{appType:" + appType 
				+ ",userId:" + userId 
				+ ",empId:" + empId 
				+ ",stdId:" + stdId 
				+ ",learnId:" + learnId 
				+ ",openId:" + openId 
				+ "}";
	}

}
