package com.yz.model.statistics;

import java.io.Serializable;

public class PaperPrintStatInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6691627110032854037L;
	
	private String courseId;
	private String courseName;
	private String year;
	private int peopleNum;
	private String thpId;
	private String testSubject;
	private String testName;
	public String getTestName()
	{
		return testName;
	}
	public void setTestName(String testName)
	{
		this.testName = testName;
	}
	public String getCourseId()
	{
		return courseId;
	}
	public void setCourseId(String courseId)
	{
		this.courseId = courseId;
	}
	public String getCourseName()
	{
		return courseName;
	}
	public void setCourseName(String courseName)
	{
		this.courseName = courseName;
	}
	public String getYear()
	{
		return year;
	}
	public void setYear(String year)
	{
		this.year = year;
	}
	public int getPeopleNum()
	{
		return peopleNum;
	}
	public void setPeopleNum(int peopleNum)
	{
		this.peopleNum = peopleNum;
	}
	public String getThpId() {
		return thpId;
	}
	public void setThpId(String thpId) {
		this.thpId = thpId;
	}
	public String getTestSubject() {
		return testSubject;
	}
	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
	}
	
}
