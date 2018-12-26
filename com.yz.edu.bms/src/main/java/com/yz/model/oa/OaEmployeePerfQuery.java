package com.yz.model.oa;

import com.yz.model.common.PageCondition;

/**
 * 招生老师分配学员查询条件
 * @author lx
 * @date 2017年11月8日 上午10:52:33
 */
public class OaEmployeePerfQuery extends PageCondition
{

	private String empId;
	private String dpId;
	private String stdName;
	private String stdPhone;
	private String stdIdCard; 
	private String recruitType;
	private String stdUnvsName; 
	private String stdPfsnName; 
	private String stdStage;
	private String scholarship; 
	private String stdGrade; 
	private String beginTime;
	private String endTime;
	private String operStatus;
	private String modifyId;
	public String getEmpId()
	{
		return empId;
	}
	public void setEmpId(String empId)
	{
		this.empId = empId;
	}
	public String getDpId()
	{
		return dpId;
	}
	public void setDpId(String dpId)
	{
		this.dpId = dpId;
	}
	public String getStdName()
	{
		return stdName;
	}
	public void setStdName(String stdName)
	{
		this.stdName = stdName;
	}
	public String getStdPhone()
	{
		return stdPhone;
	}
	public void setStdPhone(String stdPhone)
	{
		this.stdPhone = stdPhone;
	}
	public String getStdIdCard()
	{
		return stdIdCard;
	}
	public void setStdIdCard(String stdIdCard)
	{
		this.stdIdCard = stdIdCard;
	}
	public String getRecruitType()
	{
		return recruitType;
	}
	public void setRecruitType(String recruitType)
	{
		this.recruitType = recruitType;
	}
	public String getStdUnvsName()
	{
		return stdUnvsName;
	}
	public void setStdUnvsName(String stdUnvsName)
	{
		this.stdUnvsName = stdUnvsName;
	}
	public String getStdPfsnName()
	{
		return stdPfsnName;
	}
	public void setStdPfsnName(String stdPfsnName)
	{
		this.stdPfsnName = stdPfsnName;
	}
	public String getStdStage()
	{
		return stdStage;
	}
	public void setStdStage(String stdStage)
	{
		this.stdStage = stdStage;
	}
	public String getScholarship()
	{
		return scholarship;
	}
	public void setScholarship(String scholarship)
	{
		this.scholarship = scholarship;
	}
	public String getStdGrade()
	{
		return stdGrade;
	}
	public void setStdGrade(String stdGrade)
	{
		this.stdGrade = stdGrade;
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
	public String getOperStatus()
	{
		return operStatus;
	}
	public void setOperStatus(String operStatus)
	{
		this.operStatus = operStatus;
	}
	public String getModifyId()
	{
		return modifyId;
	}
	public void setModifyId(String modifyId)
	{
		this.modifyId = modifyId;
	}
}
