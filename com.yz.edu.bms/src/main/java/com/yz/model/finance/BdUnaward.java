package com.yz.model.finance;

import java.util.Date;

public class BdUnaward {
    private String recordsNo;

    private String userId;

    private String triggerUserId;

    private String accType;

    private String action;

    private String excDesc;

    private String amount;

    private String ruleType;

    private String ruleCode;

    private Date createTime;

    private String isDone;

    public String getRecordsNo() {
        return recordsNo;
    }

    public void setRecordsNo(String recordsNo) {
        this.recordsNo = recordsNo == null ? null : recordsNo.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getTriggerUserId() {
        return triggerUserId;
    }

    public void setTriggerUserId(String triggerUserId) {
        this.triggerUserId = triggerUserId == null ? null : triggerUserId.trim();
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType == null ? null : accType.trim();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public String getExcDesc() {
        return excDesc;
    }

    public void setExcDesc(String excDesc) {
        this.excDesc = excDesc == null ? null : excDesc.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType == null ? null : ruleType.trim();
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode == null ? null : ruleCode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone == null ? null : isDone.trim();
    }
}