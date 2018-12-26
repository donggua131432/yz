package com.yz.model.goods;

import java.io.Serializable;
/**
 * 中拍记录
 * @author lx
 * @date 2017年8月23日 上午9:51:34
 */
public class GsAuctionPart implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8201254777816878411L;
	private String userId;
	private String auctionId;
	private String planCount;
	private String userName;
	private String mobile;
	private String auctionPrice;
	private String mineTime;
	private String openId;
	
	public String getAuctionId()
	{
		return auctionId;
	}
	public void setAuctionId(String auctionId)
	{
		this.auctionId = auctionId;
	}
	public String getPlanCount()
	{
		return planCount;
	}
	public void setPlanCount(String planCount)
	{
		this.planCount = planCount;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getAuctionPrice()
	{
		return auctionPrice;
	}
	public void setAuctionPrice(String auctionPrice)
	{
		this.auctionPrice = auctionPrice;
	}
	public String getMineTime()
	{
		return mineTime;
	}
	public void setMineTime(String mineTime)
	{
		this.mineTime = mineTime;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getOpenId()
	{
		return openId;
	}
	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
}
