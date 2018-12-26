package com.yz.model;

import java.io.Serializable;

/**
 * 商品的基础信息
 * @author lx
 * @date 2017年7月25日 上午9:27:14
 */
public class GsGoodsBaseInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4898931245167347414L;
	
	private String goodsId;            //商品id
	private String salesId;            //活动商品id
	private String salesName;          //活动名称
	private String originalPrice;      //市场价
	private String salesPrice;         //兑换,竞拍,抽奖 智米数量
	private String goodsCount;         //库存
	private String startTime;          //开始时间
	private String endTime;            //结束时间
	private String salesStatus;        //状态 (3:进行中2:即将开始1:已结束)
	private String goodsDesc;          //商品介绍
	private String goodsContent;       //内容详细
	private String logisticsType;      //配送方式
	private String goodsType;          //商品类型
	private String ifAddNotify = "N";  //是否添加了开始前提醒
	
	private String salesType;          //活动类型

	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	public String getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
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
	public String getLogisticsType() {
		return logisticsType;
	}
	public void setLogisticsType(String logisticsType) {
		this.logisticsType = logisticsType;
	}
	public String getGoodsType()
	{
		return goodsType;
	}
	public void setGoodsType(String goodsType)
	{
		this.goodsType = goodsType;
	}
	public String getIfAddNotify()
	{
		return ifAddNotify;
	}
	public void setIfAddNotify(String ifAddNotify)
	{
		this.ifAddNotify = ifAddNotify;
	}
	public String getSalesType()
	{
		return salesType;
	}
	public void setSalesType(String salesType)
	{
		this.salesType = salesType;
	}
}
