package com.yz.model.message;

import java.util.Date;

public class GwSendRecords {
    private String srId;

    private String mtpId;

    private String receiver;

    private String receiverId;

    private String sendStatus;

    private String operator;

    private String operatorId;

    private Date sendTime;

    public String getSrId() {
        return srId;
    }

    public void setSrId(String srId) {
        this.srId = srId == null ? null : srId.trim();
    }

    public String getMtpId() {
        return mtpId;
    }

    public void setMtpId(String mtpId) {
        this.mtpId = mtpId == null ? null : mtpId.trim();
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? null : receiver.trim();
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId == null ? null : receiverId.trim();
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus == null ? null : sendStatus.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}