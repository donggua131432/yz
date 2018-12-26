package com.yz.model;

import java.io.Serializable;

/**
 * 竞拍记录
 * @author lx
 * @date 2017年7月25日 上午10:51:17
 */
public class GsAuctionPart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5225152825752053988L;
	private String auctionId;
	private String userName;
	private String mobile;
	private String auctionPrice;
	private String auctionTime;
	private String userId;
	private String openId;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAuctionPrice() {
		return auctionPrice;
	}
	public void setAuctionPrice(String auctionPrice) {
		this.auctionPrice = auctionPrice;
	}
	public String getAuctionTime() {
		return auctionTime;
	}
	public void setAuctionTime(String auctionTime) {
		this.auctionTime = auctionTime;
	}
	public String getAuctionId()
	{
		return auctionId;
	}
	public void setAuctionId(String auctionId)
	{
		this.auctionId = auctionId;
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
