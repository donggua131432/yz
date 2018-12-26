package com.yz.model;


import java.util.List;

/**
 * 活动兑换商品详信息
 * @author lx
 * @date 2017年7月24日 下午5:37:11
 */
public class ZmcGoodsExChangeInfo extends GsGoodsBaseInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2656906960687704985L;
	
	private String onceCount;
	private List<GsExChangePart> changeParts;
    private List<GsGoodsAnnexInfo> annexInfos;
	private List<GsGoodsComment> comments;
	
	private String ifExchange;         //是否兑换
	private String exchangeCount;      //兑换个数
	
	private String activityStartTime;
	private String activityEndTime;
	private String courseType;
	private String address;
	private String takein;
	private String location;
	
	private String skuId;         //JD对接商品id
	private String showAfterOver; //结束后是否可见
	
	private String jdGoodsType;  //京东的商品类型  0-实物，1-实体卡
	
	public String getOnceCount() {
		return onceCount;
	}
	public void setOnceCount(String onceCount) {
		this.onceCount = onceCount;
	}

	public String getIfExchange() {
		return ifExchange;
	}
	public void setIfExchange(String ifExchange) {
		this.ifExchange = ifExchange;
	}
	public List<GsGoodsAnnexInfo> getAnnexInfos() {
		return annexInfos;
	}
	public void setAnnexInfos(List<GsGoodsAnnexInfo> annexInfos) {
		this.annexInfos = annexInfos;
	}
	public List<GsGoodsComment> getComments() {
		return comments;
	}
	public void setComments(List<GsGoodsComment> comments) {
		this.comments = comments;
	}
	public List<GsExChangePart> getChangeParts() {
		return changeParts;
	}
	public void setChangeParts(List<GsExChangePart> changeParts) {
		this.changeParts = changeParts;
	}
	public String getCourseType()
	{
		return courseType;
	}
	public void setCourseType(String courseType)
	{
		this.courseType = courseType;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getTakein()
	{
		return takein;
	}
	public void setTakein(String takein)
	{
		this.takein = takein;
	}
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location = location;
	}
	public String getActivityStartTime()
	{
		return activityStartTime;
	}
	public void setActivityStartTime(String activityStartTime)
	{
		this.activityStartTime = activityStartTime;
	}
	public String getActivityEndTime()
	{
		return activityEndTime;
	}
	public void setActivityEndTime(String activityEndTime)
	{
		this.activityEndTime = activityEndTime;
	}
	public String getExchangeCount()
	{
		return exchangeCount;
	}
	public void setExchangeCount(String exchangeCount)
	{
		this.exchangeCount = exchangeCount;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getShowAfterOver() {
		return showAfterOver;
	}
	public void setShowAfterOver(String showAfterOver) {
		this.showAfterOver = showAfterOver;
	}
	public String getJdGoodsType() {
		return jdGoodsType;
	}
	public void setJdGoodsType(String jdGoodsType) {
		this.jdGoodsType = jdGoodsType;
	}

}
