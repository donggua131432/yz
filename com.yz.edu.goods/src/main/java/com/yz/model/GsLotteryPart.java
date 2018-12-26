package com.yz.model;

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
	private String partId;
	private String salesId;
	private String userName;
	private String mobile;
	private String joinTime;
	private String headImgUrl;
	private String userId;
	private String salesPrice;
	private String openId;
	
	private String planCount;
	public String getSalesId()
	{
		return salesId;
	}
	public void setSalesId(String salesId)
	{
		this.salesId = salesId;
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
	public String getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}
	public String getHeadImgUrl()
	{
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl)
	{
		this.headImgUrl = headImgUrl;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getPlanCount()
	{
		return planCount;
	}
	public void setPlanCount(String planCount)
	{
		this.planCount = planCount;
	}
	public String getSalesPrice()
	{
		return salesPrice;
	}
	public void setSalesPrice(String salesPrice)
	{
		this.salesPrice = salesPrice;
	}
	public String getPartId()
	{
		return partId;
	}
	public void setPartId(String partId)
	{
		this.partId = partId;
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
