package com.yz.model;

import java.util.List;

/**
 * 商品竞拍详细信息
 * @author lx
 * @date 2017年7月25日 上午10:46:21
 */
public class ZmcGoodsAuctionInfo extends GsGoodsBaseInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1737520795010019571L;
	
	private String planId;
	private String planCount;
	private String salesRules;
	private String upsetPrice;
	private String auctionCount;
	private String raisePrice;
	private String curPrice;
	private String mineUser;
	private String auctionPrice;
	private List<GsGoodsComment> comments;
	private List<GsAuctionPart> auctionParts;
	private List<GsPastAuctionRecord> auctionRecords;
	private List<GsGoodsAnnexInfo> annexInfos;
	
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}
	public String getSalesRules() {
		return salesRules;
	}
	public void setSalesRules(String salesRules) {
		this.salesRules = salesRules;
	}
	public List<GsGoodsComment> getComments() {
		return comments;
	}
	public void setComments(List<GsGoodsComment> comments) {
		this.comments = comments;
	}
	public List<GsAuctionPart> getAuctionParts() {
		return auctionParts;
	}
	public void setAuctionParts(List<GsAuctionPart> auctionParts) {
		this.auctionParts = auctionParts;
	}
	public List<GsPastAuctionRecord> getAuctionRecords() {
		return auctionRecords;
	}
	public void setAuctionRecords(List<GsPastAuctionRecord> auctionRecords) {
		this.auctionRecords = auctionRecords;
	}
	public String getUpsetPrice() {
		return upsetPrice;
	}
	public void setUpsetPrice(String upsetPrice) {
		this.upsetPrice = upsetPrice;
	}
	public String getAuctionCount() {
		return auctionCount;
	}
	public void setAuctionCount(String auctionCount) {
		this.auctionCount = auctionCount;
	}
	public String getRaisePrice() {
		return raisePrice;
	}
	public void setRaisePrice(String raisePrice) {
		this.raisePrice = raisePrice;
	}
	public String getCurPrice() {
		return curPrice;
	}
	public void setCurPrice(String curPrice) {
		this.curPrice = curPrice;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getMineUser() {
		return mineUser;
	}
	public void setMineUser(String mineUser) {
		this.mineUser = mineUser;
	}
	public String getAuctionPrice() {
		return auctionPrice;
	}
	public void setAuctionPrice(String auctionPrice) {
		this.auctionPrice = auctionPrice;
	}
	public List<GsGoodsAnnexInfo> getAnnexInfos() {
		return annexInfos;
	}
	public void setAnnexInfos(List<GsGoodsAnnexInfo> annexInfos) {
		this.annexInfos = annexInfos;
	}
	
	

}
