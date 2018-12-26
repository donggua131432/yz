package com.yz.model;

import java.util.Date;

public class AtsRechargeRecords {
    private String recordsNo;

    private String accId;

    private String stdId;

    private String empId;

    private String userId;

    private String paymentStatus;

    private String amount;

    private String zhimi;

    private String paymentType;

    private String remark;

    private String outSerialNo;

    private String reason;
    
    private String wechatOpenId;
    
    private String wechatUnionId;

    private Date createTime;

    private Date completeTime;
    
    private String productId;

    public String getRecordsNo() {
        return recordsNo;
    }

    public void setRecordsNo(String recordsNo) {
        this.recordsNo = recordsNo == null ? null : recordsNo.trim();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus == null ? null : paymentStatus.trim();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    public String getZhimi() {
        return zhimi;
    }

    public void setZhimi(String zhimi) {
        this.zhimi = zhimi == null ? null : zhimi.trim();
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType == null ? null : paymentType.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOutSerialNo() {
        return outSerialNo;
    }

    public void setOutSerialNo(String outSerialNo) {
        this.outSerialNo = outSerialNo == null ? null : outSerialNo.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

	public String getWechatOpenId() {
		return wechatOpenId;
	}

	public void setWechatOpenId(String wechatOpenId) {
		this.wechatOpenId = wechatOpenId;
	}

	public String getWechatUnionId() {
		return wechatUnionId;
	}

	public void setWechatUnionId(String wechatUnionId) {
		this.wechatUnionId = wechatUnionId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}