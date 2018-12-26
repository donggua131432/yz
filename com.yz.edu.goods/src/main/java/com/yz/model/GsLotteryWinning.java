package com.yz.model;

import java.io.Serializable;

public class GsLotteryWinning implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String winningId;

	private String lotteryId;

	private String userId;

	private String realName;

	private String mobile;

	public String getWinningId() {
		return winningId;
	}

	public void setWinningId(String winningId) {
		this.winningId = winningId;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
