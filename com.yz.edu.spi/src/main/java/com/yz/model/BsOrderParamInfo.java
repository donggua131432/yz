package com.yz.model;

import java.io.Serializable;
/**
 * 下单参数
 * @author lx
 * @date 2017年7月26日 下午5:20:34
 */
public class BsOrderParamInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 441299308350098087L;
	private String salesId;
	private String salesType;
	private String salesName;
	private String salesPrice;
	private String goodsId;
	private String originalPrice;
	private String costPrice;
	private String goodsName;
	private String count;
	private String unit;
	private String mobile;
	private String userId;
	private String userName;
	private String goodsType;
	private String goodsImg;
	private String saId;  //收货地址id
	
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
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
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
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
	public String getSaId()
	{
		return saId;
	}
	public void setSaId(String saId)
	{
		this.saId = saId;
	}
}
