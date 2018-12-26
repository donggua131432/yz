package com.yz.model;

import java.io.Serializable;

/**
 * 订单信息
 * @author lx
 * @date 2017年7月26日 上午11:53:46
 */
public class BsOrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3033038004995026358L;
	private String orderNo;
	private String accType;
	private String unit;
	private String mobile;
	private String orderType;
	private String transAmount;
	private String userName;
	private String userId;
	private String orderTime;
	private String orderStatus;
	private String orderDesc;
	
	private String jdOrderId;            //京东订单号
	private String freight;                 //运费
	private String orderPrice;           //订单总价格		
	private String orderNakedPrice; //订单裸价		
	private String orderTaxPrice;     //订单税额
	
	private String salesType;
	private String skuId;//京东商品编号
	private String jdPrice;//京东单价
	private String goodsName;
	private String goodsCount;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public String getAccType()
	{
		return accType;
	}
	public void setAccType(String accType)
	{
		this.accType = accType;
	}
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
	public String getJdOrderId()
	{
		return jdOrderId;
	}
	public void setJdOrderId(String jdOrderId)
	{
		this.jdOrderId = jdOrderId;
	}
	public String getFreight()
	{
		return freight;
	}
	public void setFreight(String freight)
	{
		this.freight = freight;
	}
	public String getOrderPrice()
	{
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice)
	{
		this.orderPrice = orderPrice;
	}
	public String getOrderNakedPrice()
	{
		return orderNakedPrice;
	}
	public void setOrderNakedPrice(String orderNakedPrice)
	{
		this.orderNakedPrice = orderNakedPrice;
	}
	public String getOrderTaxPrice()
	{
		return orderTaxPrice;
	}
	public void setOrderTaxPrice(String orderTaxPrice)
	{
		this.orderTaxPrice = orderTaxPrice;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getJdPrice() {
		return jdPrice;
	}
	public void setJdPrice(String jdPrice) {
		this.jdPrice = jdPrice;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	
	
}
