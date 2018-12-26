package com.yz.model.finance;

import java.util.ArrayList;

public class BdOrder {

	private String orderNo;
	private String amount;
	private String payable;
	private String discount;
	private String learnId;
	private String stdId;
	private String stdName;
	private String mobile;
	private String unvsId;
	private String unvsName;
	private String taId;
	private String taName;
	private String pfsnId;
	private String pfsnName;
	private String createTime;
	private String expireTime;
	private String orderStatus;
	private String payeeId;

	private ArrayList<BdSubOrder> subOrders;

	public ArrayList<BdSubOrder> getSubOrders() {
		return subOrders;
	}

	public void setSubOrders(ArrayList<BdSubOrder> subOrders) {
		this.subOrders = subOrders;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPayable() {
		return payable;
	}

	public void setPayable(String payable) {
		this.payable = payable;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * @return the payeeId
	 */
	public String getPayeeId() {
		return payeeId;
	}

	/**
	 * @param payeeId the payeeId to set
	 */
	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

}
