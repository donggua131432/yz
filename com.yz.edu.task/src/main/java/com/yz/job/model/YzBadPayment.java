package com.yz.job.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author Administrator
 *
 */
public class YzBadPayment implements java.io.Serializable{
	
    private Long id;

    private String payNo;

    private String transNo;

    private BigDecimal amount;

    private String payType;

    private String dealType;

    private String errorMsg;

    private Date createDate;
    
    private String dealAddr; 
    
    public void setDealAddr(String dealAddr) {
		this.dealAddr = dealAddr;
	}
    
    public String getDealAddr() {
		return dealAddr;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo == null ? null : payNo.trim();
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo == null ? null : transNo.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType == null ? null : dealType.trim();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}