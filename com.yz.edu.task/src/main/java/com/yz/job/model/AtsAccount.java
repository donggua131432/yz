package com.yz.job.model;

/**
 * 
 * @author Administrator
 *
 */
public class AtsAccount implements java.io.Serializable {

	private String accId;

    private String userId;

    private String accType;

    private String accAmount;

    private String accStatus;

    private String thawTime;

    private String frezTime;

    private String canDeposit;

    private String isVisiable;

    private String stdId;

    private String empId;
    
    private String unit;

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId == null ? null : accId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType == null ? null : accType.trim();
    }

    public String getAccAmount() {
        return accAmount;
    }

    public void setAccAmount(String accAmount) {
        this.accAmount = accAmount == null ? null : accAmount.trim();
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus == null ? null : accStatus.trim();
    }

    public String getThawTime() {
        return thawTime;
    }

    public void setThawTime(String thawTime) {
        this.thawTime = thawTime;
    }

    public String getFrezTime() {
        return frezTime;
    }

    public void setFrezTime(String frezTime) {
        this.frezTime = frezTime;
    }

    public String getCanDeposit() {
        return canDeposit;
    }

    public void setCanDeposit(String canDeposit) {
        this.canDeposit = canDeposit == null ? null : canDeposit.trim();
    }

    public String getIsVisiable() {
        return isVisiable;
    }

    public void setIsVisiable(String isVisiable) {
        this.isVisiable = isVisiable == null ? null : isVisiable.trim();
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId == null ? null : stdId.trim();
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId == null ? null : empId.trim();
    }

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
