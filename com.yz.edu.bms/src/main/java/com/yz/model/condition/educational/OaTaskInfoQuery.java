package com.yz.model.condition.educational;

import com.yz.model.common.PageCondition;

/**
 * 教务任务查询条件
 * @author lx
 * @date 2017年7月19日 下午7:56:23
 */
public class OaTaskInfoQuery extends PageCondition  {
	
	private String taskTitle;
	private String creater;
	private String isAllow;
	private String isNeedCheck;
	private String startTimeStart;
	private String startTimeEnd;
	private String endTimeStart;
	private String endTimeEnd;
	
	private String issuer;
	
	private String taskId;
	
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getIsAllow() {
		return isAllow;
	}
	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}
	public String getIsNeedCheck() {
		return isNeedCheck;
	}
	public void setIsNeedCheck(String isNeedCheck) {
		this.isNeedCheck = isNeedCheck;
	}
	public String getStartTimeStart() {
		return startTimeStart;
	}
	public void setStartTimeStart(String startTimeStart) {
		this.startTimeStart = startTimeStart;
	}
	public String getStartTimeEnd() {
		return startTimeEnd;
	}
	public void setStartTimeEnd(String startTimeEnd) {
		this.startTimeEnd = startTimeEnd;
	}
	public String getEndTimeStart() {
		return endTimeStart;
	}
	public void setEndTimeStart(String endTimeStart) {
		this.endTimeStart = endTimeStart;
	}
	public String getEndTimeEnd() {
		return endTimeEnd;
	}
	public void setEndTimeEnd(String endTimeEnd) {
		this.endTimeEnd = endTimeEnd;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getTaskId()
	{
		return taskId;
	}
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

}
