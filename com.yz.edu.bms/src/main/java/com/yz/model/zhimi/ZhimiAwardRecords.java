package com.yz.model.zhimi;

import java.util.Map;

public class ZhimiAwardRecords {

	private String awardId;

    private String userId;

    private String ruleCode;

    private String awardDesc;

    private String zhimiCount;

    private String expCount;

    private String mappingId;

    private String triggerUserId;



    private String updateTime;
    
    private String nickname;
    private String mobile;
    private String realName;
    private String idCard;
    private String yzCode;

    private String triggerNickname;
    private String triggerMobile;
    private String triggerRealName;
    private String triggerIdCard;
    private String triggerYzCode;



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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getYzCode() {
		return yzCode;
	}

	public void setYzCode(String yzCode) {
		this.yzCode = yzCode;
	}

    public String getTriggerNickname() {
        return triggerNickname;
    }

    public void setTriggerNickname(String triggerNickname) {
        this.triggerNickname = triggerNickname;
    }

    public String getTriggerMobile() {
        return triggerMobile;
    }

    public void setTriggerMobile(String triggerMobile) {
        this.triggerMobile = triggerMobile;
    }

    public String getTriggerRealName() {
        return triggerRealName;
    }

    public void setTriggerRealName(String triggerRealName) {
        this.triggerRealName = triggerRealName;
    }

    public String getTriggerIdCard() {
        return triggerIdCard;
    }

    public void setTriggerIdCard(String triggerIdCard) {
        this.triggerIdCard = triggerIdCard;
    }

    public String getTriggerYzCode() {
        return triggerYzCode;
    }

    public void setTriggerYzCode(String triggerYzCode) {
        this.triggerYzCode = triggerYzCode;
    }
}
