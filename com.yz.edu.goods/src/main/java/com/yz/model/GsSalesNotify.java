package com.yz.model;

import java.io.Serializable;

/**
 * 活动通知信息
 * @author lx
 * @date 2017年7月25日 下午6:04:27
 */
public class GsSalesNotify implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6678128364840142446L;
	
	private String notifyId;
	private String salesId;
	private String userId;
	private String notifyType;
	private String notifyTime;
	private String notifyContent;
	private String notifyTitle;
	private String planCount;
	private String salesType;
	private String nickName;
	private String mobile;
	private String openId;
	
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	public String getNotifyTime() {
		return notifyTime;
	}
	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}
	public String getNotifyContent() {
		return notifyContent;
	}
	public void setNotifyContent(String notifyContent) {
		this.notifyContent = notifyContent;
	}
	public String getNotifyTitle() {
		return notifyTitle;
	}
	public void setNotifyTitle(String notifyTitle) {
		this.notifyTitle = notifyTitle;
	}
	public String getNotifyId()
	{
		return notifyId;
	}
	public void setNotifyId(String notifyId)
	{
		this.notifyId = notifyId;
	}
	public String getPlanCount()
	{
		return planCount;
	}
	public void setPlanCount(String planCount)
	{
		this.planCount = planCount;
	}
	public String getSalesType()
	{
		return salesType;
	}
	public void setSalesType(String salesType)
	{
		this.salesType = salesType;
	}
	public String getOpenId()
	{
		return openId;
	}
	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
	public String getNickName()
	{
		return nickName;
	}
	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

}
