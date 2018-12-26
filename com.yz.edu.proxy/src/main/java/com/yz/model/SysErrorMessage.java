package com.yz.model;

import java.util.Date;

public class SysErrorMessage {
	
    private String errorCode;
    private String sysMsg;
    private String appMsg;

    private String sysBelong;

    private String description;

    private Date updateTime;

    private String updateUser;

    private String updateUserId;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode == null ? null : errorCode.trim();
    }

    public String getSysMsg() {
        return sysMsg;
    }

    public void setSysMsg(String sysMsg) {
        this.sysMsg = sysMsg == null ? null : sysMsg.trim();
    }

    public String getAppMsg() {
        return appMsg;
    }

    public void setAppMsg(String appMsg) {
        this.appMsg = appMsg == null ? null : appMsg.trim();
    }

    public String getSysBelong() {
        return sysBelong;
    }

    public void setSysBelong(String sysBelong) {
        this.sysBelong = sysBelong == null ? null : sysBelong.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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

	@Override
	public String toString() {
		return "SysErrorMessage [errorCode=" + errorCode + ", sysMsg=" + sysMsg + ", appMsg=" + appMsg + ", sysBelong="
				+ sysBelong + ", description=" + description + ", updateTime=" + updateTime 
				+ ", updateUser=" + updateUser + ", updateUserId=" + updateUserId + "]";
	}
    
    
}