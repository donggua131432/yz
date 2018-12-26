package com.yz.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @desc 奖励规则
 * @author Administrator
 *
 */
public class AtsAwardRule implements java.io.Serializable, Comparable<AtsAwardRule> {

	private String ruleGroup; // 规则分组

	private Date startTime; // 开始时间

	private Date endTime; // 结束时间

	private int isMutex; // 是否互斥，叠加使用

	private int isRepeat; // 是否可以重复使用 0 是 1 否

	private int sort; // 启用排序

	private String ruleCode;

	private String ruleType;

	private String ruleDesc;

	private String isAllow;

	private String zhimiCount;

	private String expCount;

	private Map<String, String> customizeAttr;// 自定义规则字段

	
	
	public Map<String, String> getCustomizeAttr() {
		return customizeAttr;
	}

	public void setCustomizeAttr(Map<String, String> customizeAttr) {
		this.customizeAttr = customizeAttr;
	}

	public AtsAwardRule() {
		this.customizeAttr = new HashMap<>();
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addCustomizeAttr(String key, String value) {
		this.customizeAttr.put(key, value);
	}

	/**
	 * 
	 * @param customizeAttrs
	 */
	public void addCustomizeAttrs(Map<String, String> customizeAttrs) {
		this.customizeAttr.putAll(customizeAttrs);
	}

	public int getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(int isRepeat) {
		this.isRepeat = isRepeat;
	}

	public String getRuleGroup() {
		return ruleGroup;
	}

	public void setRuleGroup(String ruleGroup) {
		this.ruleGroup = ruleGroup;
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

	public int getIsMutex() {
		return isMutex;
	}

	public void setIsMutex(int isMutex) {
		this.isMutex = isMutex;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode == null ? null : ruleCode.trim();
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType == null ? null : ruleType.trim();
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc == null ? null : ruleDesc.trim();
	}

	public String getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow == null ? null : isAllow.trim();
	}

	public String getZhimiCount() {
		return zhimiCount;
	}

	public void setZhimiCount(String zhimiCount) {
		this.zhimiCount = zhimiCount == null ? null : zhimiCount.trim();
	}

	public String getExpCount() {
		return expCount;
	}

	public void setExpCount(String expCount) {
		this.expCount = expCount == null ? null : expCount.trim();
	}

	@Override
	public int compareTo(AtsAwardRule rule) {
		return this.getSort() >= rule.getSort() ? 1 : -1;
	}

}