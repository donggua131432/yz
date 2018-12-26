package com.yz.model;

public class GwCertInfoKey {
    private String certId;

    private String transType;

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId == null ? null : certId.trim();
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType == null ? null : transType.trim();
    }
}