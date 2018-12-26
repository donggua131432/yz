package com.yz.model.finance;

import com.yz.model.common.PubInfo;

public class BdAccountSerial extends PubInfo {
    private String accSerialNo;

    private String accId;
    // 流水号
    private String mappingId;
    // 学员编号
    private String stdId;
    // 账户类型
    private String accType;
    // 1-进账 2-出账
    private String action;
    // 金额
    private String amount;
    
    private String accSerialStatus;

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

    public String getMappingId() {
        return mappingId;
    }

    public void setMappingId(String mappingId) {
        this.mappingId = mappingId == null ? null : mappingId.trim();
    }

    public String getStdId() {
        return stdId;
    }

    public void setStdId(String stdId) {
        this.stdId = stdId == null ? null : stdId.trim();
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

	public String getAccSerialStatus() {
		return accSerialStatus;
	}

	public void setAccSerialStatus(String accSerialStatus) {
		this.accSerialStatus = accSerialStatus == null ? null : accSerialStatus.trim();
	}

}