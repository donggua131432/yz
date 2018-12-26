package com.yz.model;

import java.io.Serializable;

/**
 * 往期竞拍记录
 * @author lx
 * @date 2017年7月25日 上午10:56:42
 */
public class GsPastAuctionRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8699647310729265469L;
	
	private String planCount;
	private String auctionTime;
	private String userName;
	private String mobile;
	private String auctionPrice;
	private String headImgUrl;
	
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}
	public String getAuctionTime() {
		return auctionTime;
	}
	public void setAuctionTime(String auctionTime) {
		this.auctionTime = auctionTime;
	}
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
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

}
