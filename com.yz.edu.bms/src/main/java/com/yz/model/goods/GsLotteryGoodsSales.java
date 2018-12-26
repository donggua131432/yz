package com.yz.model.goods;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

public class GsLotteryGoodsSales extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7432210057645169896L;
    private String salesId;
    private String annexUrl;
    private String salesName;
    private String salesPrice;
    private String startTime;
    private String endTime;
    private String curCount;
    private String totalCount;
    private String runCount;
    private String winnerCount;
    private String salesStatus;
    private String runTime;
    private String activityStatus;
    private String planStatus;
	  
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
	public String getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
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
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getRunCount() {
		return runCount;
	}
	public void setRunCount(String runCount) {
		this.runCount = runCount;
	}
	public String getWinnerCount() {
		return winnerCount;
	}
	public void setWinnerCount(String winnerCount) {
		this.winnerCount = winnerCount;
	}
	public String getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getActivityStatus() {
		return activityStatus;
	}
	public String getCurCount()
	{
		return curCount;
	}
	public void setCurCount(String curCount)
	{
		this.curCount = curCount;
	}
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	public String getPlanStatus()
	{
		return planStatus;
	}
	public void setPlanStatus(String planStatus)
	{
		this.planStatus = planStatus;
	}
}
