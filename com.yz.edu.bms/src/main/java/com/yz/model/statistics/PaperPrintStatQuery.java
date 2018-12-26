package com.yz.model.statistics;

/**
 * 试卷印刷统计查询条件
 * @author lx
 * @date 2017年11月17日 下午12:10:05
 */
public class PaperPrintStatQuery
{

	private String courseName;
	private String examName;
	private String examYear;
	private String testSubject;
	public String getCourseName()
	{
		return courseName;
	}
	public void setCourseName(String courseName)
	{
		this.courseName = courseName;
	}
	public String getExamName()
	{
		return examName;
	}
	public void setExamName(String examName)
	{
		this.examName = examName;
	}
	public String getExamYear()
	{
		return examYear;
	}
	public void setExamYear(String examYear)
	{
		this.examYear = examYear;
	}
	public String getTestSubject() {
		return testSubject;
	}
	public void setTestSubject(String testSubject) {
		this.testSubject = testSubject;
	}
	
}
