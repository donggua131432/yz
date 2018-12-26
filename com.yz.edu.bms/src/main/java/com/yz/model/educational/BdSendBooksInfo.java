package com.yz.model.educational;

import java.io.Serializable;
/**
 * 外包发书 
 * @author lx
 * @date 2017年9月19日 下午2:34:51
 */
public class BdSendBooksInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7274781476801279735L;
	private String sendId;
	private String userName;
	private String mobile;
	private String provinceCode;
	private String cityCode;
	private String districtCode;
	private String address;
	private String semester;
	private String batchId;
	private String logisticsNo;
	private String logisticsName;
	private String textbookType;
	private String grade;
	private String recruitType;
	private String unvsName;
	private String pfsnLevel;
	private String pfsnCode;
	private String pfsnName;
	
	private String orderBookStatus;
	private String remark;
	private String custid;
	
	private String sendDate;
	
	private String destCode;
	
	private String recruitName;
	private String tutorName;
	private String recruitStatus;
	private String tutorStatus;
	
	private String learnId;
	private String[] semesters; 
	
	private String stdName;
	private String stdType;
	
	private String sendPeople;  //下单人
	
	public String getSendId()
	{
		return sendId;
	}
	public void setSendId(String sendId)
	{
		this.sendId = sendId;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getProvinceCode()
	{
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode)
	{
		this.provinceCode = provinceCode;
	}
	public String getCityCode()
	{
		return cityCode;
	}
	public void setCityCode(String cityCode)
	{
		this.cityCode = cityCode;
	}
	public String getDistrictCode()
	{
		return districtCode;
	}
	public void setDistrictCode(String districtCode)
	{
		this.districtCode = districtCode;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getSemester()
	{
		return semester;
	}
	public void setSemester(String semester)
	{
		this.semester = semester;
	}
	public String getBatchId()
	{
		return batchId;
	}
	public void setBatchId(String batchId)
	{
		this.batchId = batchId;
	}
	public String getLogisticsNo()
	{
		return logisticsNo;
	}
	public void setLogisticsNo(String logisticsNo)
	{
		this.logisticsNo = logisticsNo;
	}
	public String getTextbookType()
	{
		return textbookType;
	}
	public void setTextbookType(String textbookType)
	{
		this.textbookType = textbookType;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getRecruitType()
	{
		return recruitType;
	}
	public void setRecruitType(String recruitType)
	{
		this.recruitType = recruitType;
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
	public String getPfsnCode()
	{
		return pfsnCode;
	}
	public void setPfsnCode(String pfsnCode)
	{
		this.pfsnCode = pfsnCode;
	}
	public String getPfsnName()
	{
		return pfsnName;
	}
	public void setPfsnName(String pfsnName)
	{
		this.pfsnName = pfsnName;
	}
	public String getOrderBookStatus()
	{
		return orderBookStatus;
	}
	public void setOrderBookStatus(String orderBookStatus)
	{
		this.orderBookStatus = orderBookStatus;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getCustid()
	{
		return custid;
	}
	public void setCustid(String custid)
	{
		this.custid = custid;
	}
	public String getSendDate()
	{
		return sendDate;
	}
	public void setSendDate(String sendDate)
	{
		this.sendDate = sendDate;
	}
	public String getDestCode()
	{
		return destCode;
	}
	public void setDestCode(String destCode)
	{
		this.destCode = destCode;
	}
	public String getRecruitName()
	{
		return recruitName;
	}
	public void setRecruitName(String recruitName)
	{
		this.recruitName = recruitName;
	}
	public String getTutorName()
	{
		return tutorName;
	}
	public void setTutorName(String tutorName)
	{
		this.tutorName = tutorName;
	}
	public String getRecruitStatus()
	{
		return recruitStatus;
	}
	public void setRecruitStatus(String recruitStatus)
	{
		this.recruitStatus = recruitStatus;
	}
	public String getTutorStatus()
	{
		return tutorStatus;
	}
	public void setTutorStatus(String tutorStatus)
	{
		this.tutorStatus = tutorStatus;
	}
	public String getLearnId()
	{
		return learnId;
	}
	public void setLearnId(String learnId)
	{
		this.learnId = learnId;
	}
	public String[] getSemesters()
	{
		return semesters;
	}
	public void setSemesters(String[] semesters)
	{
		this.semesters = semesters;
	}
	public String getLogisticsName() {
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}
	public String getStdName()
	{
		return stdName;
	}
	public void setStdName(String stdName)
	{
		this.stdName = stdName;
	}
	public String getStdType()
	{
		return stdType;
	}
	public void setStdType(String stdType)
	{
		this.stdType = stdType;
	}
	public String getSendPeople() {
		return sendPeople;
	}
	public void setSendPeople(String sendPeople) {
		this.sendPeople = sendPeople;
	}
	
}
