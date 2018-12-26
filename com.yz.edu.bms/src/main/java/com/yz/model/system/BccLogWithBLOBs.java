package com.yz.model.system;

public class BccLogWithBLOBs extends BccLog {
    private String reason;

    private String postData;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData == null ? null : postData.trim();
    }
}