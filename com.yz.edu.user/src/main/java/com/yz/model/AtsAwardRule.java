package com.yz.model;

public class AtsAwardRule {
    private String ruleCode;

    private String ruleType;

    private String ruleDesc;

    private String isAllow;

    private String zhimiCount;

    private String expCount;

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

}