package com.yz.model.condition.system;

import java.util.Date;
import java.util.List;

import com.yz.model.common.PageCondition;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

public class LogQueryInfo extends PageCondition {

	private String userId;
	private String userName;
	
	private String yzCode;
	private String mobile;
	private String realName;
	
	private String funcName;
	
	private String startTimeStr;
	private String endTimeStr;
	
	private Date startTime;
	
	private Date endTime;
	
	private String postData;
	
	private String errorMsg;
	
	private String ip;
	
	private String mac;
	
	private List<String> userIds;
	
	private String interfaceName;
	private String interfaceVersion;
	private String sysBelong;
	
	private String isSuccess;
	private String dpName;
	private String campusName;
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getYzCode() {
		return yzCode;
	}

	public void setYzCode(String yzCode) {
		this.yzCode = yzCode == null ? null : yzCode.trim();
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName == null ? null : realName.trim();
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName == null ? null : funcName.trim();
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr == null ? null : startTimeStr.trim();
		if(StringUtil.hasValue(startTimeStr)) {
			setStartTime(DateUtil.convertDateStrToDate(startTimeStr, DateUtil.YYYYMMDDHHMMSS_SPLIT));
		}
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr == null ? null : endTimeStr.trim();
		if(StringUtil.hasValue(endTimeStr)) {
			setEndTime(DateUtil.convertDateStrToDate(endTimeStr, DateUtil.YYYYMMDDHHMMSS_SPLIT));
		}
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPostData() {
		return postData;
	}

	public void setPostData(String postData) {
		this.postData = postData == null ? null : postData.trim();
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg == null ? null : errorMsg.trim();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac == null ? null : mac.trim();
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
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

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess == null ? null : isSuccess.trim();
	}

	public String getDpName() {
		return dpName;
	}

	public void setDpName(String dpName) {
		this.dpName = dpName == null ? null : dpName.trim();
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName == null ? null : campusName.trim();
	}

	public String getSysBelong() {
		return sysBelong;
	}

	public void setSysBelong(String sysBelong) {
		this.sysBelong = sysBelong;
	}
}
