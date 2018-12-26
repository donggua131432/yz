package com.yz.model.condition.zhimi;

import com.yz.model.common.PageCondition;

public class ZhimiAwardQueryInfo extends PageCondition{

	private String ruleCode;
	private String ruleDesc;
	private String isAllow;
	private String zhimiCount;
	
	private String ruleGroup;
	private String startTime;
	private String endTime;
	private String isMutex;
	private String isRepeat; 
	
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getRuleDesc() {
		return ruleDesc;
	}
	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
	public String getIsAllow() {
		return isAllow;
	}
	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}
	public String getZhimiCount() {
		return zhimiCount;
	}
	public void setZhimiCount(String zhimiCount) {
		this.zhimiCount = zhimiCount;
	}
	public String getRuleGroup() {
		return ruleGroup;
	}
	public void setRuleGroup(String ruleGroup) {
		this.ruleGroup = ruleGroup;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getIsMutex() {
		return isMutex;
	}
	public void setIsMutex(String isMutex) {
		this.isMutex = isMutex;
	}
	public String getIsRepeat() {
		return isRepeat;
	}
	public void setIsRepeat(String isRepeat) {
		this.isRepeat = isRepeat;
	}
}
