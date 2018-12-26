package com.yz.model;

import java.io.Serializable;

/**
 * 往期抽奖记录
 * @author lx
 * @date 2017年7月25日 下午2:22:16
 */
public class GsPastLotteryRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2977686579644420509L;
	private String planCount;
	private String runTime;
	private String userName;
	private String mobile;
	private String headImgUrl;
	
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
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
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
}
