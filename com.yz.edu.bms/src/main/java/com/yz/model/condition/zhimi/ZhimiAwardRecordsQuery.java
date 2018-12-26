package com.yz.model.condition.zhimi;

import java.util.List;

import com.yz.model.common.PageCondition;

public class ZhimiAwardRecordsQuery extends PageCondition{
	
	private String nickname;
	private String yzCode;
	private String mobile;
	private String ruleCode;
	private String ruleType;
	private String startTime;
	private String endTime;
	private String realName;
	
	private List<String> userIds;
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname == null ?  null : nickname.trim();
	}
	public String getYzCode() {
		return yzCode;
	}
	public void setYzCode(String yzCode) {
		this.yzCode = yzCode == null ?  null : yzCode.trim();
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode == null ?  null : ruleCode.trim();
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime == null ?  null : startTime.trim();
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime == null ?  null : endTime.trim();
	}
	public List<String> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<String> userIds) {
		this.userIds = userIds;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile == null ?  null : mobile.trim();
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType == null ?  null : ruleType.trim();
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName == null ?  null : realName.trim();
	}
}
