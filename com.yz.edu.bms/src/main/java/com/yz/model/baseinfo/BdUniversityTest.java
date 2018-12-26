package com.yz.model.baseinfo;

public class BdUniversityTest {
    private String unvsId;

    private String taId;

    public String getUnvsId() {
        return unvsId;
    }

    public void setUnvsId(String unvsId) {
        this.unvsId = unvsId == null ? null : unvsId.trim();
    }

    public String getTaId() {
        return taId;
    }

    public void setTaId(String taId) {
        this.taId = taId == null ? null : taId.trim();
    }
}