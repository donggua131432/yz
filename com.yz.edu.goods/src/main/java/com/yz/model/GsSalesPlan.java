package com.yz.model;

import java.io.Serializable;

/**
 * 活动期数信息
 * @author lx
 * @date 2017年8月21日 下午7:47:50
 */
public class GsSalesPlan implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8733942417115933547L;

	private String planId;
	private String totalCount;
	private String curCount;
	private String lessCount;
	private String reason;
	private String planStatus;
	private String endTime;
	private String startTime;
	public String getPlanId()
	{
		return planId;
	}
	public void setPlanId(String planId)
	{
		this.planId = planId;
	}
	public String getTotalCount()
	{
		return totalCount;
	}
	public void setTotalCount(String totalCount)
	{
		this.totalCount = totalCount;
	}
	public String getCurCount()
	{
		return curCount;
	}
	public void setCurCount(String curCount)
	{
		this.curCount = curCount;
	}
	public String getLessCount()
	{
		return lessCount;
	}
	public void setLessCount(String lessCount)
	{
		this.lessCount = lessCount;
	}
	public String getReason()
	{
		return reason;
	}
	public void setReason(String reason)
	{
		this.reason = reason;
	}
	public String getPlanStatus()
	{
		return planStatus;
	}
	public void setPlanStatus(String planStatus)
	{
		this.planStatus = planStatus;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
}
