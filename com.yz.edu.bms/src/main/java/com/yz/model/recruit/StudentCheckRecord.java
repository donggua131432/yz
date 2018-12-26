package com.yz.model.recruit;

import com.yz.model.common.PubInfo;

/**
 * 学员审核记录
 * @author Administrator
 *
 */
public class StudentCheckRecord extends PubInfo {

	private String crId;
	private String mappingId;
	private String empId;
	private String jtId;
	private String titleName;
	private String checkStatus;
	private String reason;
	private String checkOrder;
	private String checkType;
	private String crStatus;
	private String isDataCheck;
	private String isDataCompleted;
	private String recruitType;
	private String learnId;
	private String updateTime;

	private String ext1;
	
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
	public String getJtId() {
		return jtId;
	}
	public void setJtId(String jtId) {
		this.jtId = jtId == null ? null : jtId.trim();
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
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName == null ? null : titleName.trim();
	}
	public String getIsDataCheck() {
		return isDataCheck;
	}
	public void setIsDataCheck(String isDataCheck) {
		this.isDataCheck = isDataCheck == null ? null : isDataCheck.trim();
	}
	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getIsDataCompleted() {
		return isDataCompleted;
	}

	public void setIsDataCompleted(String isDataCompleted) {
		this.isDataCompleted = isDataCompleted;
	}
}
