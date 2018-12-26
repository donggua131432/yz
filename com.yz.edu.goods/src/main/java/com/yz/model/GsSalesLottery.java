package com.yz.model;

import java.io.Serializable;

public class GsSalesLottery implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9032931586019717304L;

	private String salesId;
	private String runCount;
	private String winnerCount;
	private String runTime;
	public String getSalesId()
	{
		return salesId;
	}
	public void setSalesId(String salesId)
	{
		this.salesId = salesId;
	}
	public String getRunCount()
	{
		return runCount;
	}
	public void setRunCount(String runCount)
	{
		this.runCount = runCount;
	}
	public String getWinnerCount()
	{
		return winnerCount;
	}
	public void setWinnerCount(String winnerCount)
	{
		this.winnerCount = winnerCount;
	}
	public String getRunTime()
	{
		return runTime;
	}
	public void setRunTime(String runTime)
	{
		this.runTime = runTime;
	}
}
