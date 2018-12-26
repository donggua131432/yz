package com.yz.model.order;

import java.io.Serializable;

/**
 * 商品/活动流水信息表
 * @author lx
 * @date 2017年7月26日 下午12:10:48
 */
public class BsSerialInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3909152223890037099L;
	private String serialNo;
	private String orderNo;
	private String transAmount;
	private String userName;
	private String userId;
	private String mobile;
	private String accType;
	private String serialStatus;
	private String createTime;
	private String outSerialNo;
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getSerialStatus() {
		return serialStatus;
	}
	public void setSerialStatus(String serialStatus) {
		this.serialStatus = serialStatus;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOutSerialNo() {
		return outSerialNo;
	}
	public void setOutSerialNo(String outSerialNo) {
		this.outSerialNo = outSerialNo;
	}
}
