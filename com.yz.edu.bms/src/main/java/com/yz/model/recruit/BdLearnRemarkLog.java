package com.yz.model.recruit;

import java.util.Date;

import com.yz.model.common.PubInfo;

public class BdLearnRemarkLog extends PubInfo {

	private String lrId;
	private String learnId;
	private String content;
	private Date createTime;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
