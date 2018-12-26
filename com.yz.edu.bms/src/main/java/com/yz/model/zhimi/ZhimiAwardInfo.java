package com.yz.model.zhimi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yz.model.common.PubInfo;

public class ZhimiAwardInfo extends PubInfo {

	private String ruleCode;

    private String ruleType;

    private String ruleDesc;

    private String isAllow;

    private String zhimiCount;

    private String expCount;
    
    private String ruleGroup;
    
    private String startTime;
    
    private String endTime;
    
    private String isMutex;
    
    private String isRepeat;
    
    private String sort;
    
    private List<Map<String, String>> attrList;
    
    private ArrayList<ZhimiCustomizeAttr> items;
    
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public List<Map<String, String>> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<Map<String, String>> attrList) {
		this.attrList = attrList;
	}

	public ArrayList<ZhimiCustomizeAttr> getItems() {
		return items;
	}

	public void setItems(ArrayList<ZhimiCustomizeAttr> items) {
		this.items = items;
	}
}
