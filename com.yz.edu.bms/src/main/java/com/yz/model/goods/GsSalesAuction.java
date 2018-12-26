package com.yz.model.goods;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

public class GsSalesAuction extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6549156881410307199L;
	
	private String salesId;
	private String upsetPrice;
	private String raisePrice;
	private String auctionCount;
	private String curPrice;
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

}
