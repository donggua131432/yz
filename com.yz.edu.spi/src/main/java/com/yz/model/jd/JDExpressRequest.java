package com.yz.model.jd;

import java.io.Serializable;

public class JDExpressRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5935352183156106121L;
	
	/** 系统订单号 */
	private String orderid;
	
	/**配送业务类型（ 1:普通，3:填仓，4:特配，5:鲜活，6:控温，7:冷藏，8:冷冻，9:深冷）默认是1*/
	private String goodsType;
	/**运单号*/
	private String deliveryId;
	/**寄件人姓名*/
	private String senderName;
	/**寄件人地址*/
	private String senderAddress;
	/**寄件人电话*/
	private String senderTel;
	/**寄件人手机*/
	private String senderMobile;
	/**	收件人名称*/
	private String receiveName;
	/**收件人地址*/
	private String receiveAddress;
	private String receiveTel;
	private String receiveMobile;
	/**包裹数*/
	private Integer packageCount;
	/**重量(单位：kg，保留小数点后两位，默认 为0)*/
	private Double weight;
	/**体积(单位：cm3，保留小数点后两位，默认 为0)*/
	private Double vloumn;
	
	
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	
	public Integer getPackageCount() {
		return packageCount;
	}
	public void setPackageCount(Integer packageCount) {
		this.packageCount = packageCount;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getVloumn() {
		return vloumn;
	}
	public void setVloumn(Double vloumn) {
		this.vloumn = vloumn;
	}
	public String getSenderTel() {
		return senderTel;
	}
	public void setSenderTel(String senderTel) {
		this.senderTel = senderTel;
	}
	public String getSenderMobile() {
		return senderMobile;
	}
	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}
	public String getReceiveTel() {
		return receiveTel;
	}
	public void setReceiveTel(String receiveTel) {
		this.receiveTel = receiveTel;
	}
	public String getReceiveMobile() {
		return receiveMobile;
	}
	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}
	
	
}
