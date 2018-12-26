package com.yz.model;

import java.util.Date;

public class UsTaskCard {
    private String id;
    private String userId;
    private String taskId;
    private String taskCount;
    private String completeCount;
    private String completeStatus;
    private Date completeTime;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount == null ? null : taskCount.trim();
    }

    public String getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(String completeCount) {
        this.completeCount = completeCount == null ? null : completeCount.trim();
    }

    public String getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(String completeStatus) {
        this.completeStatus = completeStatus == null ? null : completeStatus.trim();
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}