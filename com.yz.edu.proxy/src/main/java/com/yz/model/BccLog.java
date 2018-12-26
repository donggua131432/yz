package com.yz.model;

import java.util.Date;

public class BccLog {
	
    private String logId;

    private String userId;
    
    private String iUserType;

    private String appType;
    
    private String appVersion;

    private String deviceMode;

    private String interfaceName;

    private String interfaceVersion;

    private Date updateTime;

    private String isSuccess;
    
    private String reason;
    
    private String postData;
    
    private String ipAddress;
    
    private String macAddress;
    
    private String sysBelong;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType == null ? null : appType.trim();
    }

    public String getDeviceMode() {
        return deviceMode;
    }

    public void setDeviceMode(String deviceMode) {
        this.deviceMode = deviceMode == null ? null : deviceMode.trim();
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName == null ? null : interfaceName.trim();
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion == null ? null : interfaceVersion.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPostData() {
		return postData;
	}

	public void setPostData(String postData) {
		this.postData = postData;
	}

	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * @return the iUserType
	 */
	public String getiUserType() {
		return iUserType;
	}

	/**
	 * @param iUserType the iUserType to set
	 */
	public void setiUserType(String iUserType) {
		this.iUserType = iUserType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getSysBelong() {
		return sysBelong;
	}

	public void setSysBelong(String sysBelong) {
		this.sysBelong = sysBelong;
	}
}