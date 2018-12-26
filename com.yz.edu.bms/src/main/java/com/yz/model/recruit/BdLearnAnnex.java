package com.yz.model.recruit;

import java.util.Date;

public class BdLearnAnnex {
    private String annexId;

    private String annexType;

    private String learnId;

    private String annexName;

    private String annexUrl;

    private String annexStatus;

    private String reason;

    private String isRequire;

    private Date updateTime;

    private String updateUser;

    private String updateUserId;

    private String uploadUser;

    private String uploadUserId;

    private Date uploadTime;
    
    private String uploadTimeStr;

    private String checkUser;

    private String checkUserId;

    private Date checkTime;
    
    private String checkTimeStr;
    
    private Object annexFile;

    public String getAnnexId() {
        return annexId;
    }

    public void setAnnexId(String annexId) {
        this.annexId = annexId == null ? null : annexId.trim();
    }

    public String getAnnexType() {
        return annexType;
    }

    public void setAnnexType(String annexType) {
        this.annexType = annexType == null ? null : annexType.trim();
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId == null ? null : learnId.trim();
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(String isRequire) {
        this.isRequire = isRequire == null ? null : isRequire.trim();
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

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser == null ? null : uploadUser.trim();
    }

    public String getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId == null ? null : uploadUserId.trim();
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser == null ? null : checkUser.trim();
    }

    public String getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId == null ? null : checkUserId.trim();
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

	public String getUploadTimeStr() {
		return uploadTimeStr;
	}

	public void setUploadTimeStr(String uploadTimeStr) {
		this.uploadTimeStr = uploadTimeStr;
	}

	public String getCheckTimeStr() {
		return checkTimeStr;
	}

	public void setCheckTimeStr(String checkTimeStr) {
		this.checkTimeStr = checkTimeStr;
	}

	public Object getAnnexFile() {
		return annexFile;
	}

	public void setAnnexFile(Object annexFile) {
		this.annexFile = annexFile;
	}
}