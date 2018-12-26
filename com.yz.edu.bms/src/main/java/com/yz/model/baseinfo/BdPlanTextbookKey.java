package com.yz.model.baseinfo;

public class BdPlanTextbookKey {
    private String thpId;

    private String textbookId;

    public String getThpId() {
        return thpId;
    }

    public void setThpId(String thpId) {
        this.thpId = thpId == null ? null : thpId.trim();
    }

    public String getTextbookId() {
        return textbookId;
    }

    public void setTextbookId(String textbookId) {
        this.textbookId = textbookId == null ? null : textbookId.trim();
    }
}