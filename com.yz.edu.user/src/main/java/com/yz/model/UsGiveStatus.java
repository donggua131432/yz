package com.yz.model;

import java.util.Date;

public class UsGiveStatus {
    private String giveId;

    private String fieldName;

    private String fieldDesc;

    private String isGive;

    private Date giveTime;

    private String userId;

    private String amount;

    public String getGiveId() {
        return giveId;
    }

    public void setGiveId(String giveId) {
        this.giveId = giveId == null ? null : giveId.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc == null ? null : fieldDesc.trim();
    }

    public String getIsGive() {
        return isGive;
    }

    public void setIsGive(String isGive) {
        this.isGive = isGive == null ? null : isGive.trim();
    }

    public Date getGiveTime() {
        return giveTime;
    }

    public void setGiveTime(Date giveTime) {
        this.giveTime = giveTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }
}