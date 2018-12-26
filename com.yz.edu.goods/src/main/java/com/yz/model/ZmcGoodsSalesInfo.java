package com.yz.model;

/**
 * 活动商品信息
 * @author lx
 * @date 2017年7月24日 上午11:48:10
 */
public class ZmcGoodsSalesInfo extends GsGoodsBaseInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2962496447415959258L;
	
	private String annexUrl;           //封面图
	
	private String curPrice;           //竞拍现价
	private String upsetPrice;         //起拍价
	private String auctionCount;       //竞拍次数
	private String mineUser;           //中拍人
	private String autionPrice;        //竞拍价
	private String planCount;          //期数 
	
	private String runCount;           //多少人开奖
	private String runTime;            //开奖时间
	private String winUser;            //中奖人
	private String joinCount;          //已经参加人数
	
	public String getAnnexUrl() {
		return annexUrl;
	}
	public void setAnnexUrl(String annexUrl) {
		this.annexUrl = annexUrl;
	}
	
	public String getCurPrice() {
		return curPrice;
	}
	public void setCurPrice(String curPrice) {
		this.curPrice = curPrice;
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
	public String getMineUser() {
		return mineUser;
	}
	public void setMineUser(String mineUser) {
		this.mineUser = mineUser;
	}
	public String getAutionPrice() {
		return autionPrice;
	}
	public void setAutionPrice(String autionPrice) {
		this.autionPrice = autionPrice;
	}
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}
	public String getRunCount() {
		return runCount;
	}
	public void setRunCount(String runCount) {
		this.runCount = runCount;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getWinUser() {
		return winUser;
	}
	public void setWinUser(String winUser) {
		this.winUser = winUser;
	}
	public String getJoinCount() {
		return joinCount;
	}
	public void setJoinCount(String joinCount) {
		this.joinCount = joinCount;
	}
}
