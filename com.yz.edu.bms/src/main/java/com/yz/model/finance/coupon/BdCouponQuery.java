package com.yz.model.finance.coupon;

public class BdCouponQuery extends BdCoupon {

	private String itemCode;
	private String availableStartTime;
	private String availableExpireTime;
	private String publishStartTime;
	private String publishExpireTime;
	private String grade;
	public String getItemCode()
	{
		return itemCode;
	}
	public void setItemCode(String itemCode)
	{
		this.itemCode = itemCode;
	}
	public String getAvailableStartTime()
	{
		return availableStartTime;
	}
	public void setAvailableStartTime(String availableStartTime)
	{
		this.availableStartTime = availableStartTime;
	}
	public String getAvailableExpireTime()
	{
		return availableExpireTime;
	}
	public void setAvailableExpireTime(String availableExpireTime)
	{
		this.availableExpireTime = availableExpireTime;
	}
	public String getPublishStartTime()
	{
		return publishStartTime;
	}
	public void setPublishStartTime(String publishStartTime)
	{
		this.publishStartTime = publishStartTime;
	}
	public String getPublishExpireTime()
	{
		return publishExpireTime;
	}
	public void setPublishExpireTime(String publishExpireTime)
	{
		this.publishExpireTime = publishExpireTime;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
}
