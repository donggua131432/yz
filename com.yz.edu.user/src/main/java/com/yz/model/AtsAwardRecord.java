package com.yz.model;

import java.util.Date;

public class AtsAwardRecord {
    private String awardId;

    private String userId;
    
    private String stdId;
    
    private String empId;

    private String ruleCode;

    private String awardDesc;

    private String zhimiCount;

    private String expCount;

    private String mappingId;

    private String triggerUserId;

    private Date updateTime;

    private String updateUserId;

    private String updateUser;
    
    private String ruleType;

    public String getAwardId() {
        return awardId;
    }

    public void setAwardId(String awardId) {
        this.awardId = awardId == null ? null : awardId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode == null ? null : ruleCode.trim();
    }

    public String getAwardDesc() {
        return awardDesc;
    }

    public void setAwardDesc(String awardDesc) {
        this.awardDesc = awardDesc == null ? null : awardDesc.trim();
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

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId == null ? null : mappingId.trim();
    }

    public String getTriggerUserId() {
        return triggerUserId;
    }

    public void setTriggerUserId(String triggerUserId) {
        this.triggerUserId = triggerUserId == null ? null : triggerUserId.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}
}