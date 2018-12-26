package com.yz.model;

import java.io.Serializable;


public class GsSalesAuction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6549156881410307199L;
	
	private String salesId;
	private String upsetPrice;
	private String raisePrice;
	private String auctionCount;
	private String curPrice;
	private String userId;
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getUpsetPrice() {
		return upsetPrice;
	}
	public void setUpsetPrice(String upsetPrice) {
		this.upsetPrice = upsetPrice;
	}
	public String getRaisePrice() {
		return raisePrice;
	}
	public void setRaisePrice(String raisePrice) {
		this.raisePrice = raisePrice;
	}
	public String getAuctionCount() {
		return auctionCount;
	}
	public void setAuctionCount(String auctionCount) {
		this.auctionCount = auctionCount;
	}
	public String getCurPrice() {
		return curPrice;
	}
	public void setCurPrice(String curPrice) {
		this.curPrice = curPrice;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

}
