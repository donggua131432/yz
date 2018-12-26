package com.yz.model.statistics;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 考试课程统计
 * @author lx
 * @date 2017年11月16日 下午4:31:33
 */
public class BdExamCourseStatisticsInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2892011156296638087L;
	
	private String grade;
	private String year;
	private String unvsName;
	private String semester;
	private String pfsnLevel;
	private String pfsnName;
	private String pfsnId;
	private String thpId;
	private int peopleNum;
	private List<Map<String, String>> courseMap;
	private List<Map<String, String>> testSubjectMap;
	
	private String courseText;
	private String testSubjectText;
	
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getYear()
	{
		return year;
	}
	public void setYear(String year)
	{
		this.year = year;
	}
	public String getUnvsName()
	{
		return unvsName;
	}
	public void setUnvsName(String unvsName)
	{
		this.unvsName = unvsName;
	}
	public String getSemester()
	{
		return semester;
	}
	public void setSemester(String semester)
	{
		this.semester = semester;
	}
	public String getPfsnLevel()
	{
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel)
	{
		this.pfsnLevel = pfsnLevel;
	}
	public String getPfsnName()
	{
		return pfsnName;
	}
	public void setPfsnName(String pfsnName)
	{
		this.pfsnName = pfsnName;
	}
	public String getPfsnId()
	{
		return pfsnId;
	}
	public void setPfsnId(String pfsnId)
	{
		this.pfsnId = pfsnId;
	}
	public String getThpId()
	{
		return thpId;
	}
	public void setThpId(String thpId)
	{
		this.thpId = thpId;
	}
	public int getPeopleNum()
	{
		return peopleNum;
	}
	public void setPeopleNum(int peopleNum)
	{
		this.peopleNum = peopleNum;
	}
	public List<Map<String, String>> getCourseMap()
	{
		return courseMap;
	}
	public void setCourseMap(List<Map<String, String>> courseMap)
	{
		this.courseMap = courseMap;
	}
	public String getCourseText()
	{
		return courseText;
	}
	public void setCourseText(String courseText)
	{
		this.courseText = courseText;
	}
	public List<Map<String, String>> getTestSubjectMap() {
		return testSubjectMap;
	}
	public void setTestSubjectMap(List<Map<String, String>> testSubjectMap) {
		this.testSubjectMap = testSubjectMap;
	}
	public String getTestSubjectText() {
		return testSubjectText;
	}
	public void setTestSubjectText(String testSubjectText) {
		this.testSubjectText = testSubjectText;
	}
	

}
