package com.yz.model;

import java.io.Serializable;

/**
 * 活动子订单信息表
 * @author lx
 * @date 2017年7月26日 下午12:35:21
 */
public class BsSalesOrderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8023942371540028765L;

	private String subOrderNo;
	private String orderNo;
	private String costPrice;
	private String originalPrice;
	private String unitPrice;
	private String salesId;
	private String salesType;
	private String salesName;
	private String goodsId;
	private String goodsCount;
	private String goodsName;
	private String accType;
	private String transAmount;
	private String subOrderTime;
	private String userId;
	private String subOrderStatus;
	private String subOrderDesc;
	private String unit;
	private String goodsType;
	private String goodsImg;
	
	private String skuId;      //商品编码
	private String num;        //数量
	private String category;   //类别
	private String name;       //名称
	private String tax;        //税种
	private String price;      //价格
	private String taxPrice;   //税额
	private String nakedPrice; //裸价
	private String type;       //类别
	private String oid;        //父商品id
	
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
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
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
	public String getGoodsImg()
	{
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg)
	{
		this.goodsImg = goodsImg;
	}
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
	public String getSkuId()
	{
		return skuId;
	}
	public void setSkuId(String skuId)
	{
		this.skuId = skuId;
	}
	public String getNum()
	{
		return num;
	}
	public void setNum(String num)
	{
		this.num = num;
	}
	public String getCategory()
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getTax()
	{
		return tax;
	}
	public void setTax(String tax)
	{
		this.tax = tax;
	}
	public String getPrice()
	{
		return price;
	}
	public void setPrice(String price)
	{
		this.price = price;
	}
	public String getTaxPrice()
	{
		return taxPrice;
	}
	public void setTaxPrice(String taxPrice)
	{
		this.taxPrice = taxPrice;
	}
	public String getNakedPrice()
	{
		return nakedPrice;
	}
	public void setNakedPrice(String nakedPrice)
	{
		this.nakedPrice = nakedPrice;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getOid()
	{
		return oid;
	}
	public void setOid(String oid)
	{
		this.oid = oid;
	}
	
	
	
}
