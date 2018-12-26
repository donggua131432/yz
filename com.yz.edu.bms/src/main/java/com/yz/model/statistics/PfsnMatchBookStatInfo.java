package com.yz.model.statistics;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PfsnMatchBookStatInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3193579062136634784L;
	
	private String grade;
	private String unvsName;
	private String pfsnLevel;
	private String pfsnName;
	private String matchCode;
	private String semester;
	private int totalNum;
	private int sendNum;
	private String bookNum;
	private String pfsnId;
	private List<Map<String, String>> bookInfo;
	private String bookText;
	private String semesterName;
	private String textbookPfsncode;//教材专业编码
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getUnvsName()
	{
		return unvsName;
	}
	public void setUnvsName(String unvsName)
	{
		this.unvsName = unvsName;
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
	public String getMatchCode()
	{
		return matchCode;
	}
	public void setMatchCode(String matchCode)
	{
		this.matchCode = matchCode;
	}
	public String getSemester()
	{
		return semester;
	}
	public void setSemester(String semester)
	{
		this.semester = semester;
	}
	public int getTotalNum()
	{
		return totalNum;
	}
	public void setTotalNum(int totalNum)
	{
		this.totalNum = totalNum;
	}
	public int getSendNum()
	{
		return sendNum;
	}
	public void setSendNum(int sendNum)
	{
		this.sendNum = sendNum;
	}
	public String getBookNum()
	{
		return bookNum;
	}
	public void setBookNum(String bookNum)
	{
		this.bookNum = bookNum;
	}
	public String getPfsnId()
	{
		return pfsnId;
	}
	public void setPfsnId(String pfsnId)
	{
		this.pfsnId = pfsnId;
	}
	public List<Map<String, String>> getBookInfo()
	{
		return bookInfo;
	}
	public void setBookInfo(List<Map<String, String>> bookInfo)
	{
		this.bookInfo = bookInfo;
	}
	public String getBookText()
	{
		return bookText;
	}
	public void setBookText(String bookText)
	{
		this.bookText = bookText;
	}
	public String getSemesterName()
	{
		return semesterName;
	}
	public void setSemesterName(String semesterName)
	{
		this.semesterName = semesterName;
	}
	public String getTextbookPfsncode() {
		return textbookPfsncode;
	}
	public void setTextbookPfsncode(String textbookPfsncode) {
		this.textbookPfsncode = textbookPfsncode;
	}
	
}
