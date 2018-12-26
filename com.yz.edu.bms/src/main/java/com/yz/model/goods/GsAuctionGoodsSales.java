package com.yz.model.goods;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

public class GsAuctionGoodsSales extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7525260953692044L;
	private String salesId;
	private String annexUrl;
	private String salesName;
	private String totalCount;
	private String curCount;
	private String startTime;
	private String endTime;
	private String upsetPrice;
	private String salesStatus;
	private String planCount;
	private String activityStatus;
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getAnnexUrl() {
		return annexUrl;
	}
	public void setAnnexUrl(String annexUrl) {
		this.annexUrl = annexUrl;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getCurCount() {
		return curCount;
	}
	public void setCurCount(String curCount) {
		this.curCount = curCount;
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
	public String getUpsetPrice() {
		return upsetPrice;
	}
	public void setUpsetPrice(String upsetPrice) {
		this.upsetPrice = upsetPrice;
	}
	public String getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}
	public String getActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
}
