package com.yz.model;

import java.util.Date;

public class AtsAccountChange {
    private String changeId;

    private String accId;

    private String oldUserId;

    private String userId;

    private String accType;

    private String reason;

    private String oldStdId;

    private String stdId;

    private Date createTime;

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId == null ? null : changeId.trim();
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId == null ? null : accId.trim();
    }

    public String getOldUserId() {
        return oldUserId;
    }

    public void setOldUserId(String oldUserId) {
        this.oldUserId = oldUserId == null ? null : oldUserId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType == null ? null : accType.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getOldStdId() {
        return oldStdId;
    }

    public void setOldStdId(String oldStdId) {
        this.oldStdId = oldStdId == null ? null : oldStdId.trim();
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId == null ? null : stdId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}