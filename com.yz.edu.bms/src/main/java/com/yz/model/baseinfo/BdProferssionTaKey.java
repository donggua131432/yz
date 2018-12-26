package com.yz.model.baseinfo;

public class BdProferssionTaKey {
    private String pfsnId;

    private String taId;

    public String getPfsnId() {
        return pfsnId;
    }

    public void setPfsnId(String pfsnId) {
        this.pfsnId = pfsnId == null ? null : pfsnId.trim();
    }

    public String getTaId() {
        return taId;
    }

    public void setTaId(String taId) {
        this.taId = taId == null ? null : taId.trim();
    }
}