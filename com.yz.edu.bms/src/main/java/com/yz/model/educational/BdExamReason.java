package com.yz.model.educational;

public class BdExamReason {
    private String erId;

    private String eyId;

    private String reason;

    public String getErId() {
        return erId;
    }

    public void setErId(String erId) {
        this.erId = erId == null ? null : erId.trim();
    }

    public String getEyId() {
        return eyId;
    }

    public void setEyId(String eyId) {
        this.eyId = eyId == null ? null : eyId.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }
}