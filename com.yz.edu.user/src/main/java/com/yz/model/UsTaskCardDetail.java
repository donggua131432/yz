package com.yz.model;

import java.util.Date;

public class UsTaskCardDetail {
    private String id;

    private String userId;

    private String taskId;

    private String triggerUserId;

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

    public String getTriggerUserId() {
        return triggerUserId;
    }

    public void setTriggerUserId(String triggerUserId) {
        this.triggerUserId = triggerUserId == null ? null : triggerUserId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}