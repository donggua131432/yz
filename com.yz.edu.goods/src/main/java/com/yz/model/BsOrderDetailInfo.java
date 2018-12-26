package com.yz.model;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详细
 * @author lx
 * @date 2017年7月27日 下午4:37:55
 */
public class BsOrderDetailInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7665669669943995231L;
	private String orderNo;
	private String orderDesc;
	private String goodsImg;
	private String orderTime;
	private String unitPrice;
	private String goodsCount;
	private String transAmount;
	private String orderStatus;
	private String userName;
	private String mobile;
	private String address;
	private String goodsId;
	private String subOrderNo;
	
	private List<BsActionMember> memberList;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public List<BsActionMember> getMemberList() {
		return memberList;
	}
	public String getSubOrderNo() {
		return subOrderNo;
	}
	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}
	public void setMemberList(List<BsActionMember> memberList) {
		this.memberList = memberList;
	}
}
