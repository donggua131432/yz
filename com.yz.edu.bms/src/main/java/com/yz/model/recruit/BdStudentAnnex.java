package com.yz.model.recruit;

import java.util.Date;

import com.yz.model.common.PubInfo;

public class BdStudentAnnex extends PubInfo {

	private String annexId;
	private String stdId;
	private String annexType;
	private String annexName;
	private String annexUrl;
	private String annexStatus;
	private String isRequire;
	private String reason;
	
	private String uploadUser;
	private Date uploadTime;
	private String uploadTimeStr;
	private String uploadUserId;
	
	private String checkUser;
	private Date checkTime;
	private String checkTimeStr;
	private String checkUserId;
	
	private String learnId;
	
	private Object annexFile;
	
	public String getAnnexId() {
		return annexId;
	}
	public void setAnnexId(String annexId) {
		this.annexId = annexId == null ? null : annexId.trim();
	}
	public String getStdId() {
		return stdId;
	}
	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}
	public String getAnnexType() {
		return annexType;
	}
	public void setAnnexType(String annexType) {
		this.annexType = annexType == null ? null : annexType.trim();
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
	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}
	public String getUploadUser() {
		return uploadUser;
	}
	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getUploadUserId() {
		return uploadUserId;
	}
	public void setUploadUserId(String uploadUserId) {
		this.uploadUserId = uploadUserId;
	}
	public String getUploadTimeStr() {
		return uploadTimeStr;
	}
	public void setUploadTimeStr(String uploadTimeStr) {
		this.uploadTimeStr = uploadTimeStr;
	}
	public String getCheckUser() {
		return checkUser;
	}
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckTimeStr() {
		return checkTimeStr;
	}
	public void setCheckTimeStr(String checkTimeStr) {
		this.checkTimeStr = checkTimeStr;
	}
	public String getCheckUserId() {
		return checkUserId;
	}
	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}
	
}
