package com.yz.model.goods;

public class GsLotteryGoodsSalesDetail extends GsLotteryGoodsSales {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5049649392182183872L;
	private String goodsType;
	private String goodsId;
	private String goodsName;
	private String goodsDesc;
	private String goodsContent;
	private String showAfterOver;
	private String goodsCount;
	private String salesRules;
	private String costPrice;
	private String originalPrice;
	private String planId;
	private String interval;
	private String planCount;
	
	private Object coverUrl;
	private String coverUrlPortrait;     //头像
	private String isPhotoChange;        //是否修改
	
	private String showSeq;;
	
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public String getGoodsContent() {
		return goodsContent;
	}
	public void setGoodsContent(String goodsContent) {
		this.goodsContent = goodsContent;
	}

	public String getShowAfterOver() {
		return showAfterOver;
	}
	public void setShowAfterOver(String showAfterOver) {
		this.showAfterOver = showAfterOver;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

	public Object getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(Object coverUrl) {
		this.coverUrl = coverUrl;
	}
	public String getCoverUrlPortrait() {
		return coverUrlPortrait;
	}
	public void setCoverUrlPortrait(String coverUrlPortrait) {
		this.coverUrlPortrait = coverUrlPortrait;
	}
	public String getIsPhotoChange() {
		return isPhotoChange;
	}
	public void setIsPhotoChange(String isPhotoChange) {
		this.isPhotoChange = isPhotoChange;
	}
	public String getSalesRules() {
		return salesRules;
	}
	public void setSalesRules(String salesRules) {
		this.salesRules = salesRules;
	}
	public String getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getPlanCount()
	{
		return planCount;
	}
	public void setPlanCount(String planCount)
	{
		this.planCount = planCount;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getShowSeq()
	{
		return showSeq;
	}
	public void setShowSeq(String showSeq)
	{
		this.showSeq = showSeq;
	}
}
