package com.yz.model.order;

/**
 * 课程订单信息
 * @author lx
 * @date 2017年8月8日 下午3:56:38
 */
public class BsCourseOrder extends BsOrder
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -680827539338727081L;
	
	private String courseType;
	private String startTime;
	private String address;
	private String goodsId;
	private String endTime;
	
	public String getCourseType()
	{
		return courseType;
	}
	public void setCourseType(String courseType)
	{
		this.courseType = courseType;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getGoodsId()
	{
		return goodsId;
	}
	public void setGoodsId(String goodsId)
	{
		this.goodsId = goodsId;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
}
