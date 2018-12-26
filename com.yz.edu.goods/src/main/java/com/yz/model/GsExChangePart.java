package com.yz.model;

import java.io.Serializable;

/**
 * 商品兑换记录
 * @author lx
 * @date 2017年7月24日 下午5:53:16
 */
public class GsExChangePart implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1138601262367384237L;

	private String exchangeId;
	private String userName;
	private String mobile;
	private String exchangeCount;
	private String exchangeTime;
	
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
	public String getExchangeCount() {
		return exchangeCount;
	}
	public void setExchangeCount(String exchangeCount) {
		this.exchangeCount = exchangeCount;
	}
	public String getExchangeTime() {
		return exchangeTime;
	}
	public void setExchangeTime(String exchangeTime) {
		this.exchangeTime = exchangeTime;
	}
	public String getExchangeId()
	{
		return exchangeId;
	}
	public void setExchangeId(String exchangeId)
	{
		this.exchangeId = exchangeId;
	}
}
