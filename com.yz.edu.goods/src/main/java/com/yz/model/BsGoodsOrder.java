package com.yz.model;

import java.io.Serializable;

/**
 * 商品订单
 * @author lx
 * @date 2017年7月27日 下午6:29:10
 */
public class BsGoodsOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1285569075620602558L;
	private String subOrderNo;
	private String orderNo;
	private String costPrice;
	private String originalPrice;
	private String unitPrice;
	private String goodsId;
	private String goodsCount;
	private String goodsName;
	private String goodsImg;
	private String unit;
	private String transAmount;
	private String subOrderTime;
	private String userId;
	private String subOrderStatus;
	private String subOrderDesc;
	public String getSubOrderNo() {
		return subOrderNo;
	}
	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getSubOrderTime() {
		return subOrderTime;
	}
	public void setSubOrderTime(String subOrderTime) {
		this.subOrderTime = subOrderTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSubOrderStatus() {
		return subOrderStatus;
	}
	public void setSubOrderStatus(String subOrderStatus) {
		this.subOrderStatus = subOrderStatus;
	}
	public String getSubOrderDesc() {
		return subOrderDesc;
	}
	public void setSubOrderDesc(String subOrderDesc) {
		this.subOrderDesc = subOrderDesc;
	}
}
