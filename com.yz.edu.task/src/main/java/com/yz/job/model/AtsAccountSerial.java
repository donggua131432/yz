package com.yz.job.model;

/**
 * 
 * @author Administrator
 *
 */
public class AtsAccountSerial implements java.io.Serializable {
    private String accSerialNo;
    /** 账户ID*/
    private String accId;
    /** 学员ID */
    private String stdId;
    /** 员工ID */
    private String empId;
    /** 员工ID */
    private String mappingId;
    /** 用户给ID */
    private String userId;
    /** 账户类型 */
    private String accType;
    /** 进出账类型 1-进账 2-出账 */
    private String action;
    /** 流水状态 1-处理中 2-成功 3-失败 */
    private String accSerialStatus;
    /** 流水类型*/
    private String accSerialType;
    /** 动账金额 */
    private String amount;
    /** 动账描述 */
    private String excDesc;
    /** 动账前账户余额 */
    private String beforeAmount;
    /** 动账后账户余额 */
    private String afterAmount;
    /** 操作人姓名 */
    private String updateUser;
    /** 操作人ID */
    private String updateUserId;
    /** 失败原因 */
    private String reason;
    /** 操作时间 */
    private String updateTime;
    /** 是否可见 */
    private String isVisiable;

    public String getAccSerialNo() {
        return accSerialNo;
    }

    public void setAccSerialNo(String accSerialNo) {
        this.accSerialNo = accSerialNo == null ? null : accSerialNo.trim();
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId == null ? null : accId.trim();
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

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId == null ? null : mappingId.trim();
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public String getAccSerialStatus() {
        return accSerialStatus;
    }

    public void setAccSerialStatus(String accSerialStatus) {
        this.accSerialStatus = accSerialStatus == null ? null : accSerialStatus.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    public String getExcDesc() {
        return excDesc;
    }

    public void setExcDesc(String excDesc) {
        this.excDesc = excDesc == null ? null : excDesc.trim();
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

	public String getBeforeAmount() {
		return beforeAmount;
	}

	public void setBeforeAmount(String beforeAmount) {
		this.beforeAmount = beforeAmount == null ? null : beforeAmount.trim();
	}

	public String getAfterAmount() {
		return afterAmount;
	}

	public void setAfterAmount(String afterAmount) {
		this.afterAmount = afterAmount == null ? null : afterAmount.trim();
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsVisiable() {
		return isVisiable;
	}

	public void setIsVisiable(String isVisiable) {
		this.isVisiable = isVisiable;
	}

	public String getAccSerialType() {
		return accSerialType;
	}

	public void setAccSerialType(String accSerialType) {
		this.accSerialType = accSerialType;
	}

}