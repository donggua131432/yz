package com.yz.model.order;

import java.io.Serializable;
/**
 * 订单查询条件
 * @author lx
 * @date 2017年8月10日 上午10:01:32
 */
public class BsOrderQuery implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3354780814198658447L;
	
	private String goodsName;
	private String orderNo;
	private String mobile;
	private String transportNo;
	private String goodsType;
	private String orderStatus;
	private String sfOrderTimeBegin;
	private String sfOrderTimeEnd;
	private String orderTimeBegin;
	private String orderTimeEnd;
	private String salesType;
	private String jdGoodsType;
	public String getGoodsName()
	{
		return goodsName;
	}
	public void setGoodsName(String goodsName)
	{
		this.goodsName = goodsName;
	}
	public String getOrderNo()
	{
		return orderNo;
	}
	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getGoodsType()
	{
		return goodsType;
	}
	public void setGoodsType(String goodsType)
	{
		this.goodsType = goodsType;
	}
	public String getTransportNo()
	{
		return transportNo;
	}
	public void setTransportNo(String transportNo)
	{
		this.transportNo = transportNo;
	}
	public String getOrderStatus()
	{
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus)
	{
		this.orderStatus = orderStatus;
	}
	public String getSfOrderTimeBegin()
	{
		return sfOrderTimeBegin;
	}
	public void setSfOrderTimeBegin(String sfOrderTimeBegin)
	{
		this.sfOrderTimeBegin = sfOrderTimeBegin;
	}
	public String getSfOrderTimeEnd()
	{
		return sfOrderTimeEnd;
	}
	public void setSfOrderTimeEnd(String sfOrderTimeEnd)
	{
		this.sfOrderTimeEnd = sfOrderTimeEnd;
	}
	public String getOrderTimeBegin()
	{
		return orderTimeBegin;
	}
	public void setOrderTimeBegin(String orderTimeBegin)
	{
		this.orderTimeBegin = orderTimeBegin;
	}
	public String getOrderTimeEnd()
	{
		return orderTimeEnd;
	}
	public void setOrderTimeEnd(String orderTimeEnd)
	{
		this.orderTimeEnd = orderTimeEnd;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public String getJdGoodsType() {
		return jdGoodsType;
	}
	public void setJdGoodsType(String jdGoodsType) {
		this.jdGoodsType = jdGoodsType;
	}
	
}
