package com.yz.model;

import java.io.Serializable;

/**
 * 课程/活动的基本信息
 * @author lx
 * @date 2017年8月8日 下午7:58:32
 */
public class GsCourseGoods implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2480929088367069322L;
	private String startTime;
	private String endTime;
	private String courseType;
	private String address;
	private String takein;
	private String location;
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public String getCourseType()
	{
		return courseType;
	}
	public void setCourseType(String courseType)
	{
		this.courseType = courseType;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public String getTakein()
	{
		return takein;
	}
	public void setTakein(String takein)
	{
		this.takein = takein;
	}
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location = location;
	}
}
