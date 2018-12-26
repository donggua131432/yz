package com.yz.model.student;

import java.util.Date;

public class BdCheckRecord {
	private String crId;

	private String mappingId;

	private String empId;

	private String checkStatus;

	private String reason;

	private String checkOrder;

	private String checkType;

	private String crStatus;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	private String ext1;

	private String ext2;

	private String ext3;

	private String titleName;

	private String empName;

	private String jtId;

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getCrId() {
		return crId;
	}

	public void setCrId(String crId) {
		this.crId = crId == null ? null : crId.trim();
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId == null ? null : mappingId.trim();
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId == null ? null : empId.trim();
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus == null ? null : checkStatus.trim();
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}

	public String getCheckOrder() {
		return checkOrder;
	}

	public void setCheckOrder(String checkOrder) {
		this.checkOrder = checkOrder == null ? null : checkOrder.trim();
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType == null ? null : checkType.trim();
	}

	public String getCrStatus() {
		return crStatus;
	}

	public void setCrStatus(String crStatus) {
		this.crStatus = crStatus == null ? null : crStatus.trim();
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

	public String getJtId() {
		return jtId;
	}

	public void setJtId(String jtId) {
		this.jtId = jtId;
	}

}