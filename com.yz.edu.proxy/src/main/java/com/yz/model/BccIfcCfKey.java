package com.yz.model;

public class BccIfcCfKey {
    private String interfaceId;

    private String checkId;

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId == null ? null : interfaceId.trim();
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId == null ? null : checkId.trim();
    }
}