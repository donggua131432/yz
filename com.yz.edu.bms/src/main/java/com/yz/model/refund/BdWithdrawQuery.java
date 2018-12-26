package com.yz.model.refund;

public class BdWithdrawQuery extends BdWithdrawResponse {

	private String beginTime;
	private String endTime;
	private String operBeginTime;
	private String operEndTime;
	public String getBeginTime()
	{
		return beginTime;
	}
	public void setBeginTime(String beginTime)
	{
		this.beginTime = beginTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public String getOperBeginTime()
	{
		return operBeginTime;
	}
	public void setOperBeginTime(String operBeginTime)
	{
		this.operBeginTime = operBeginTime;
	}
	public String getOperEndTime()
	{
		return operEndTime;
	}
	public void setOperEndTime(String operEndTime)
	{
		this.operEndTime = operEndTime;
	}
}
