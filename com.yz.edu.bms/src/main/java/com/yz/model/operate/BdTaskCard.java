package com.yz.model.operate;

public class BdTaskCard {
	private String taskId;

	private String taskType;

	private String taskName;

	private String startTime;

	private String endTime;

	private String taskTarget;

	private String taskReward;

	private String taskStatus;

	private String isOverlap;

	private String createTime;

	private String getCount;

	private String finishCount;

	private String nowDate;

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public String getGetCount() {
		return getCount;
	}

	public void setGetCount(String getCount) {
		this.getCount = getCount;
	}

	public String getFinishCount() {
		return finishCount;
	}

	public void setFinishCount(String finishCount) {
		this.finishCount = finishCount;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType == null ? null : taskType.trim();
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName == null ? null : taskName.trim();
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

	public String getTaskTarget() {
		return taskTarget;
	}

	public void setTaskTarget(String taskTarget) {
		this.taskTarget = taskTarget == null ? null : taskTarget.trim();
	}

	public String getTaskReward() {
		return taskReward;
	}

	public void setTaskReward(String taskReward) {
		this.taskReward = taskReward == null ? null : taskReward.trim();
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus == null ? null : taskStatus.trim();
	}

	public String getIsOverlap() {
		return isOverlap;
	}

	public void setIsOverlap(String isOverlap) {
		this.isOverlap = isOverlap == null ? null : isOverlap.trim();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}