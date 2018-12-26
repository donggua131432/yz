package com.yz.model.graduate;

import java.io.Serializable;

/**
 * 毕业服务-教务任务
 * @author lx
 * @date 2017年7月13日 下午4:52:41
 */
public class EducationalTaskInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4003241166688626135L;
	
	private String taskTitle;
	private String taskContent;
	private String pubName;
	private String taskStatus;
	private String startTime;
	private String endTime;
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getTaskContent() {
		return taskContent;
	}
	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}
	public String getPubName() {
		return pubName;
	}
	public void setPubName(String pubName) {
		this.pubName = pubName;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
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

}
