package com.yz.model;

import java.util.Date;

public class UsSign {
    private String signId;

    private String userId;

    private Date signTime;

    private String signGet;

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId == null ? null : signId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getSignGet() {
        return signGet;
    }

    public void setSignGet(String signGet) {
        this.signGet = signGet == null ? null : signGet.trim();
    }
}