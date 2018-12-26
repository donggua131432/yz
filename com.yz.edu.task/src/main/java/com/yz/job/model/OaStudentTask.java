package com.yz.job.model;

/**
 * 学服任务-未完成的学员信息
 * @author lx
 * @date 2018年4月11日 下午4:21:58
 */
public class OaStudentTask {

	private String taskId;
	private String taskTitle;
	private String learnId;
	private String tutorId;
	private String taskStatus;
	private String isNotify;
	private String openId;
	private String endTime;
	private String taskContent;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}
	public String getTutorId() {
		return tutorId;
	}
	public void setTutorId(String tutorId) {
		this.tutorId = tutorId;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getIsNotify() {
		return isNotify;
	}
	public void setIsNotify(String isNotify) {
		this.isNotify = isNotify;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTaskContent() {
		return taskContent;
	}
	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}
}
