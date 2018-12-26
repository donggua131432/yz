package com.yz.model.stdService;

public class StudentCertificateCheckRecord {
    private String reason;

    private String bcrUpdateTime;

    private String empName;

    private String crId;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getBcrUpdateTime() {
        return bcrUpdateTime;
    }

    public void setBcrUpdateTime(String bcrUpdateTime) {
        this.bcrUpdateTime = bcrUpdateTime;
    }

    public String getCrId() {
        return crId;
    }

    public void setCrId(String crId) {
        this.crId = crId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }
}
