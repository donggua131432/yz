package com.yz.model.zhimi;


public class ZhimiAwardList {
	private String ruleCode;

    private String ruleType;

    private String ruleDesc;

    private String isAllow;

    private String zhimiCount;

    private String expCount;
    
    private String createTime;
    
    private String createUser;
    
    private String updateTime;
    
    private String updateUser;
    
    private String ruleGroup;
    
    private String startTime;
    
    private String endTime;
    
    private String isMutex;
    
    private String isRepeat;
    
    private String sort;
    
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getRuleGroup() {
		return ruleGroup;
	}

	public void setRuleGroup(String ruleGroup) {
		this.ruleGroup = ruleGroup;
	}
}
