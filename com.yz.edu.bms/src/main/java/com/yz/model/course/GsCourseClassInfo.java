package com.yz.model.course;

import java.io.Serializable;

import com.yz.model.common.PubInfo;
/**
 * 课程开班
 * @author lx
 * @date 2017年8月15日 下午3:34:21
 */
public class GsCourseClassInfo extends PubInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -792438333703401520L;
	private String classId;
	private String courseName;
	private String className;
	private String classPlace;
	private String teacher;
	private String mobile;
	private String startTime;
	private String endTime;
	private String mins;
	private String isAllow;
	
	private String salesId;
	public String getClassId()
	{
		return classId;
	}
	public void setClassId(String classId)
	{
		this.classId = classId;
	}
	public String getCourseName()
	{
		return courseName;
	}
	public void setCourseName(String courseName)
	{
		this.courseName = courseName;
	}
	public String getClassName()
	{
		return className;
	}
	public void setClassName(String className)
	{
		this.className = className;
	}
	public String getClassPlace()
	{
		return classPlace;
	}
	public void setClassPlace(String classPlace)
	{
		this.classPlace = classPlace;
	}
	public String getTeacher()
	{
		return teacher;
	}
	public void setTeacher(String teacher)
	{
		this.teacher = teacher;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public String getMins()
	{
		return mins;
	}
	public void setMins(String mins)
	{
		this.mins = mins;
	}
	public String getIsAllow()
	{
		return isAllow;
	}
	public void setIsAllow(String isAllow)
	{
		this.isAllow = isAllow;
	}
	public String getSalesId()
	{
		return salesId;
	}
	public void setSalesId(String salesId)
	{
		this.salesId = salesId;
	}
}
