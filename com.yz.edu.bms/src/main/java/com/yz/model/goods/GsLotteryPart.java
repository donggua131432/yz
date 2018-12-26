package com.yz.model.goods;

import java.io.Serializable;
/**
 * 参与抽奖的记录
 * @author lx
 * @date 2017年7月25日 下午2:25:04
 */
public class GsLotteryPart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2823339457259086336L;
	private String userId;
	private String userName;
	private String mobile;
	private String winTime;
	private String headImgUrl;
	private String planCount;
	private String openId;
	
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

	public String getHeadImgUrl()
	{
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl)
	{
		this.headImgUrl = headImgUrl;
	}

	public String getPlanCount()
	{
		return planCount;
	}
	public void setPlanCount(String planCount)
	{
		this.planCount = planCount;
	}

	public String getWinTime()
	{
		return winTime;
	}
	public void setWinTime(String winTime)
	{
		this.winTime = winTime;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getOpenId()
	{
		return openId;
	}
	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
}
