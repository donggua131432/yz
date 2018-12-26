package com.yz.model.message;

import com.yz.model.common.PubInfo;

public class GwMsgTemplate extends PubInfo {
	private String mtpId;

	private String mtpType;

	private String sendType;

	private String msgChannel;

	private String msgTitle;

	private String msgName;

	private String msgCode;

	private String msgContent;

	private String msgOther;

	private String sendTime;

	private String mtpStatus;

	private String checkUser;

	private String checkTime;

	private String checkUserId;

	private String cronStr;

	private String otpId;

	private String sender;

	private String scheduleId;

	private String skipUrl;

	private String remark;

	private String createTime;

	private String updateTime;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getSkipUrl() {
		return skipUrl;
	}

	public void setSkipUrl(String skipUrl) {
		this.skipUrl = skipUrl;
	}

	public String getMsgName() {
		return msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getMtpStatus() {
		return mtpStatus;
	}

	public void setMtpStatus(String mtpStatus) {
		this.mtpStatus = mtpStatus;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getMtpId() {
		return mtpId;
	}

	public void setMtpId(String mtpId) {
		this.mtpId = mtpId == null ? null : mtpId.trim();
	}

	public String getMtpType() {
		return mtpType;
	}

	public void setMtpType(String mtpType) {
		this.mtpType = mtpType == null ? null : mtpType.trim();
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType == null ? null : sendType.trim();
	}

	public String getMsgChannel() {
		return msgChannel;
	}

	public void setMsgChannel(String msgChannel) {
		this.msgChannel = msgChannel == null ? null : msgChannel.trim();
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle == null ? null : msgTitle.trim();
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent == null ? null : msgContent.trim();
	}

	public String getMsgOther() {
		return msgOther;
	}

	public void setMsgOther(String msgOther) {
		this.msgOther = msgOther == null ? null : msgOther.trim();
	}

	public String getCheckUserId() {
		return checkUserId;
	}

	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId == null ? null : checkUserId.trim();
	}

	public String getCronStr() {
		return cronStr;
	}

	public void setCronStr(String cronStr) {
		this.cronStr = cronStr == null ? null : cronStr.trim();
	}

	public String getOtpId() {
		return otpId;
	}

	public void setOtpId(String otpId) {
		this.otpId = otpId == null ? null : otpId.trim();
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender == null ? null : sender.trim();
	}
}