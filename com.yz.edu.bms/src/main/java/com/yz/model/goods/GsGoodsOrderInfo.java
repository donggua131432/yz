package com.yz.model.goods;

import java.io.Serializable;
/**
 * 商品下单 封装信息表
 * @author lx
 * @date 2017年7月26日 下午3:54:53
 */
public class GsGoodsOrderInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5400357688550186943L;
	private String salesId;
	private String salesType;
	private String salesName;
	private String salesPrice;
	private String goodsId;
	private String originalPrice;
	private String costPrice;
	private String goodsName;
	private String unit;
	private String goodsType;
	private String goodsImg;
	
	private String planCount;
	private String planId;
	private String interval;
	private String startTime; //新的结束时间
	private String endTime;   //新的结束时间
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	public String getGoodsType()
	{
		return goodsType;
	}
	public void setGoodsType(String goodsType)
	{
		this.goodsType = goodsType;
	}
	public String getGoodsImg()
	{
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg)
	{
		this.goodsImg = goodsImg;
	}
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getPlanCount()
	{
		return planCount;
	}
	public void setPlanCount(String planCount)
	{
		this.planCount = planCount;
	}
	public String getPlanId()
	{
		return planId;
	}
	public void setPlanId(String planId)
	{
		this.planId = planId;
	}
	public String getInterval()
	{
		return interval;
	}
	public void setInterval(String interval)
	{
		this.interval = interval;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
}
