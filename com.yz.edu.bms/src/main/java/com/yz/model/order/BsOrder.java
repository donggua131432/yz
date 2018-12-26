package com.yz.model.order;

import java.io.Serializable;
/**
 * 订单
 * @author lx
 * @date 2017年8月8日 下午2:05:33
 */
public class BsOrder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5731187574687929781L;
	private String orderNo;
	private String salesType;
	private String goodsName;
	private String unitPrice;
	private String goodsCount;
	private String transAmount;
	private String userName;
	private String orderTime;
	private String mobile;
	private String orderStatus;
	private String address;
	private String takeUserName;
	private String takeMobile;
	private String transportNo;
	private String subOrderNo;
	private String logisticsId;
	private String logisticsName;
	
	private String checkStatus;         //审核状态
	private String checkTime;          //审核时间
	private String checkUser;          //审核人
	
	private String province;
	private String city;
	private String district;
	private String provinceName;
	private String cityName;
	private String districtName;
	
	private String printStatus;        //打印状态
	private String sfOrderTime;       //顺丰下单时间
	
	private String jdOrderId;//京东订单编号
	private String skuId;//京东商品编号
	private String freight;//运费
	private String orderPrice; //京东订单总价格
	private String jdPrice;//京东单价
	private String jdOrderCompleteTime;//京东订单完成时间
	
	public String getOrderNo()
	{
		return orderNo;
	}
	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}
	public String getSalesType()
	{
		return salesType;
	}
	public void setSalesType(String salesType)
	{
		this.salesType = salesType;
	}
	public String getGoodsName()
	{
		return goodsName;
	}
	public void setGoodsName(String goodsName)
	{
		this.goodsName = goodsName;
	}
	public String getUnitPrice()
	{
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice)
	{
		this.unitPrice = unitPrice;
	}
	public String getGoodsCount()
	{
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount)
	{
		this.goodsCount = goodsCount;
	}
	public String getTransAmount()
	{
		return transAmount;
	}
	public void setTransAmount(String transAmount)
	{
		this.transAmount = transAmount;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getOrderTime()
	{
		return orderTime;
	}
	public void setOrderTime(String orderTime)
	{
		this.orderTime = orderTime;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getOrderStatus()
	{
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus)
	{
		this.orderStatus = orderStatus;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getTakeUserName()
	{
		return takeUserName;
	}
	public void setTakeUserName(String takeUserName)
	{
		this.takeUserName = takeUserName;
	}
	public String getTakeMobile()
	{
		return takeMobile;
	}
	public void setTakeMobile(String takeMobile)
	{
		this.takeMobile = takeMobile;
	}
	public String getTransportNo()
	{
		return transportNo;
	}
	public void setTransportNo(String transportNo)
	{
		this.transportNo = transportNo;
	}
	public String getSubOrderNo()
	{
		return subOrderNo;
	}
	
	public String getPrintStatus()
	{
		return printStatus;
	}
	public void setPrintStatus(String printStatus)
	{
		this.printStatus = printStatus;
	}
	public String getLogisticsName()
	{
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName)
	{
		this.logisticsName = logisticsName;
	}
	public String getLogisticsId()
	{
		return logisticsId;
	}
	public void setLogisticsId(String logisticsId)
	{
		this.logisticsId = logisticsId;
	}
	public void setSubOrderNo(String subOrderNo)
	{
		this.subOrderNo = subOrderNo;
	}
	public String getProvince()
	{
		return province;
	}
	public void setProvince(String province)
	{
		this.province = province;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getDistrict()
	{
		return district;
	}
	public void setDistrict(String district)
	{
		this.district = district;
	}
	public String getCheckStatus()
	{
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus)
	{
		this.checkStatus = checkStatus;
	}
	public String getCheckTime()
	{
		return checkTime;
	}
	public void setCheckTime(String checkTime)
	{
		this.checkTime = checkTime;
	}
	public String getCheckUser()
	{
		return checkUser;
	}
	public void setCheckUser(String checkUser)
	{
		this.checkUser = checkUser;
	}
	public String getSfOrderTime()
	{
		return sfOrderTime;
	}
	public void setSfOrderTime(String sfOrderTime)
	{
		this.sfOrderTime = sfOrderTime;
	}
	public String getJdOrderId() {
		return jdOrderId;
	}
	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}
	
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	
	public String getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getJdPrice() {
		return jdPrice;
	}
	public void setJdPrice(String jdPrice) {
		this.jdPrice = jdPrice;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getJdOrderCompleteTime() {
		return jdOrderCompleteTime;
	}
	public void setJdOrderCompleteTime(String jdOrderCompleteTime) {
		this.jdOrderCompleteTime = jdOrderCompleteTime;
	}
	
	
	
}
