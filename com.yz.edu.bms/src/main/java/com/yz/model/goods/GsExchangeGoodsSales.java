package com.yz.model.goods;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

/**
 * 兑换管理
 * @author lx
 * @date 2017年8月1日 上午11:39:34
 */
public class GsExchangeGoodsSales extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String salesId;
	private String skuId;//京东商品ID
	private String jdGoodsType;//京东商品类型
	private String goodsId;
	private String annexUrl;
	private String salesName;
	private String goodsCount;
	private String costPrice;
	private String originalPrice;
	private String salesPrice;
	private String startTime;
	private String endTime;
	private String salesStatus;
	private String activityStatus;
	
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getAnnexUrl() {
		return annexUrl;
	}
	public void setAnnexUrl(String annexUrl) {
		this.annexUrl = annexUrl;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
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
	public String getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
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
	public String getActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getJdGoodsType() {
		return jdGoodsType;
	}
	public void setJdGoodsType(String jdGoodsType) {
		this.jdGoodsType = jdGoodsType;
	}
	
	
	

}
