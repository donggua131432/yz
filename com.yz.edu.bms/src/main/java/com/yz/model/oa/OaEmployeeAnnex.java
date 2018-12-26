package com.yz.model.oa;


import com.yz.model.common.PubInfo;

public class OaEmployeeAnnex extends PubInfo {

	private String annexId;
	private String empId;
	private String empAnnexType;
	private String annexName;
	private String annexUrl;
	private String annexStatus;
	private String isRequire;
	private String reason;
	private String updateTime;
	
	private Object annexFile;
	
	public String getAnnexId() {
		return annexId;
	}
	public void setAnnexId(String annexId) {
		this.annexId = annexId == null ? null : annexId.trim();
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId == null ? null : empId.trim();
	}
	public String getEmpAnnexType() {
		return empAnnexType;
	}
	public void setEmpAnnexType(String empAnnexType) {
		this.empAnnexType = empAnnexType == null ? null : empAnnexType.trim();
	}
	public String getAnnexName() {
		return annexName;
	}
	public void setAnnexName(String annexName) {
		this.annexName = annexName == null ? null : annexName.trim();
	}
	public String getAnnexUrl() {
		return annexUrl;
	}
	public void setAnnexUrl(String annexUrl) {
		this.annexUrl = annexUrl == null ? null : annexUrl.trim();
	}
	public String getAnnexStatus() {
		return annexStatus;
	}
	public void setAnnexStatus(String annexStatus) {
		this.annexStatus = annexStatus == null ? null : annexStatus.trim();
	}
	public String getIsRequire() {
		return isRequire;
	}
	public void setIsRequire(String isRequire) {
		this.isRequire = isRequire;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}
	public Object getAnnexFile() {
		return annexFile;
	}
	public void setAnnexFile(Object annexFile) {
		this.annexFile = annexFile;
	}
	
}
