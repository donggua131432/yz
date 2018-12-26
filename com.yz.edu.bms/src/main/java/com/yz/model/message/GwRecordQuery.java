package com.yz.model.message;

public class GwRecordQuery extends GwSendRecords {
	private String msgChannel;
	private String startTime;
	private String endTime;
	private String tutorName;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMsgChannel() {
		return msgChannel;
	}

	public void setMsgChannel(String msgChannel) {
		this.msgChannel = msgChannel;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

}
