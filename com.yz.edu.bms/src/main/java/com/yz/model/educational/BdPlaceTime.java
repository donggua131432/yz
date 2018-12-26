package com.yz.model.educational;

public class BdPlaceTime {
	private String timeId;

	private String date;

	private String startTime;

	private String endTime;

	private String placeId;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTimeId() {
		return timeId;
	}

	public void setTimeId(String timeId) {
		this.timeId = timeId == null ? null : timeId.trim();
	}

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

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId == null ? null : placeId.trim();
	}
}