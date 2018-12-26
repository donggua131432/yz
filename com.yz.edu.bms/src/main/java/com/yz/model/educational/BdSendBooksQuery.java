package com.yz.model.educational;

import java.io.Serializable;

public class BdSendBooksQuery implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1055815765155041807L;

	private String stdName;
	private String mobile;
	private String unvsName;
	private String pfsnName;
	private String batchId;
	private String grade;
	private String semester;
	private String orderBookStatus;
	
	private String pfsnLevel;
	private String textbookType;
	
	private String beginTime;
	private String endTime;
	private String unvsId;
	private String logisticsNo;
	private String logisticsName;
	private String checkTimeBegin;
	private String checkTimeEnd;
	
	private String pfsnId;
	private String ifOrder;
	
	private String tutorName;
	private String recruitName;
	
	private String userName;
	
	private String ifNewRepeatBook;
	private String remark;
	
	public String getTextbookType()
	{
		return textbookType;
	}
	public void setTextbookType(String textbookType)
	{
		this.textbookType = textbookType;
	}
	public String getStdName()
	{
		return stdName;
	}
	public void setStdName(String stdName)
	{
		this.stdName = stdName;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getUnvsName()
	{
		return unvsName;
	}
	public void setUnvsName(String unvsName)
	{
		this.unvsName = unvsName;
	}
	public String getPfsnName()
	{
		return pfsnName;
	}
	public void setPfsnName(String pfsnName)
	{
		this.pfsnName = pfsnName;
	}
	public String getBatchId()
	{
		return batchId;
	}
	public void setBatchId(String batchId)
	{
		this.batchId = batchId;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getSemester()
	{
		return semester;
	}
	public void setSemester(String semester)
	{
		this.semester = semester;
	}
	public String getOrderBookStatus()
	{
		return orderBookStatus;
	}
	public void setOrderBookStatus(String orderBookStatus)
	{
		this.orderBookStatus = orderBookStatus;
	}
	public String getPfsnLevel()
	{
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel)
	{
		this.pfsnLevel = pfsnLevel;
	}
	public String getBeginTime()
	{
		return beginTime;
	}
	public void setBeginTime(String beginTime)
	{
		this.beginTime = beginTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public String getUnvsId()
	{
		return unvsId;
	}
	public void setUnvsId(String unvsId)
	{
		this.unvsId = unvsId;
	}
	public String getLogisticsNo()
	{
		return logisticsNo;
	}
	public void setLogisticsNo(String logisticsNo)
	{
		this.logisticsNo = logisticsNo;
	}
	public String getCheckTimeBegin()
	{
		return checkTimeBegin;
	}
	public void setCheckTimeBegin(String checkTimeBegin)
	{
		this.checkTimeBegin = checkTimeBegin;
	}
	public String getCheckTimeEnd()
	{
		return checkTimeEnd;
	}
	public void setCheckTimeEnd(String checkTimeEnd)
	{
		this.checkTimeEnd = checkTimeEnd;
	}
	public String getPfsnId()
	{
		return pfsnId;
	}
	public void setPfsnId(String pfsnId)
	{
		this.pfsnId = pfsnId;
	}
	public String getIfOrder()
	{
		return ifOrder;
	}
	public void setIfOrder(String ifOrder)
	{
		this.ifOrder = ifOrder;
	}
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	public String getTutorName()
	{
		return tutorName;
	}
	public void setTutorName(String tutorName)
	{
		this.tutorName = tutorName;
	}
	public String getRecruitName()
	{
		return recruitName;
	}
	public void setRecruitName(String recruitName)
	{
		this.recruitName = recruitName;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getIfNewRepeatBook() {
		return ifNewRepeatBook;
	}
	public void setIfNewRepeatBook(String ifNewRepeatBook) {
		this.ifNewRepeatBook = ifNewRepeatBook;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
