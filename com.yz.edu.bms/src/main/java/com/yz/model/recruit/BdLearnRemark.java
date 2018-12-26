package com.yz.model.recruit;

import com.yz.model.common.PubInfo;

public class BdLearnRemark extends PubInfo {

	private String lrId;
	private String learnId;
	private String remarkType;
	private String isCompleted;
	
	public String getLrId() {
		return lrId;
	}
	public void setLrId(String lrId) {
		this.lrId = lrId;
	}
	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}
	public String getRemarkType() {
		return remarkType;
	}
	public void setRemarkType(String remarkType) {
		this.remarkType = remarkType;
	}
	public String getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}
	
}
