package com.yz.model.invite;

import com.yz.model.common.PubInfo;

public class InviteAssignInfo extends PubInfo {

	private String[] userIds;
	private String empId;
	private String dpId;
	private String campusId;
	private String campusManager;
	private String empStatus;
	private String assignFlag;
	private String AssignTime;
	private String dpManager;
	
	private String userId;
	
	public String[] getUserIds() {
		return userIds;
	}
	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getCampusManager() {
		return campusManager;
	}
	public void setCampusManager(String campusManager) {
		this.campusManager = campusManager;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmpStatus() {
		return empStatus;
	}
	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}


	public String getAssignFlag() {
		return assignFlag;
	}

	public void setAssignFlag(String assignFlag) {
		this.assignFlag = assignFlag;
	}

	public String getAssignTime() {
		return AssignTime;
	}

	public void setAssignTime(String assignTime) {
		AssignTime = assignTime;
	}

	public String getDpManager() {
		return dpManager;
	}

	public void setDpManager(String dpManager) {
		this.dpManager = dpManager;
	}
}
