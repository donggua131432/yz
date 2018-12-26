package com.yz.model;

public class BccBwUser extends BccBwUserKey {
    private String isAllow;

    private String isCheckDevice;

    public String getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(String isAllow) {
        this.isAllow = isAllow;
    }

    public String getIsCheckDevice() {
        return isCheckDevice;
    }

    public void setIsCheckDevice(String isCheckDevice) {
        this.isCheckDevice = isCheckDevice;
    }
}