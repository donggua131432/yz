package com.yz.model.statistics;

import java.io.Serializable;

/**
 * 订书统计 查询条件
 * @author lx
 * @date 2017年11月23日 下午7:18:34
 */
public class SendBookStatQuery implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8434608482430517962L;
	private String stdName;
	private String mobile;
	private String idCard;
	private String unvsId;
	private String pfsnLevel;
	private String pfsnName;
	private String grade;
	private String semester;
	private String stdStageQ;
	private String zempName;
	private String empStatus;
	private String campusId;
	private String dpId;
	
	private String stdStage;
    private String[] stdStageArray;
    private String statGroup;
    private String pfsnId;
    
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
	public String getIdCard()
	{
		return idCard;
	}
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}
	public String getUnvsId()
	{
		return unvsId;
	}
	public void setUnvsId(String unvsId)
	{
		this.unvsId = unvsId;
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
	public String getStdStage()
	{
		return stdStage;
	}
	public void setStdStage(String stdStage)
	{
		this.stdStage = stdStage;
	}
	public String getZempName()
	{
		return zempName;
	}
	public void setZempName(String zempName)
	{
		this.zempName = zempName;
	}
	public String getEmpStatus()
	{
		return empStatus;
	}
	public void setEmpStatus(String empStatus)
	{
		this.empStatus = empStatus;
	}
	public String getCampusId()
	{
		return campusId;
	}
	public void setCampusId(String campusId)
	{
		this.campusId = campusId;
	}
	public String getDpId()
	{
		return dpId;
	}
	public void setDpId(String dpId)
	{
		this.dpId = dpId;
	}
	public String[] getStdStageArray()
	{
		return stdStageArray;
	}
	public void setStdStageArray(String[] stdStageArray)
	{
		this.stdStageArray = stdStageArray;
	}
	public String getStatGroup()
	{
		return statGroup;
	}
	public void setStatGroup(String statGroup)
	{
		this.statGroup = statGroup;
	}
	public String getStdStageQ()
	{
		return stdStageQ;
	}
	public void setStdStageQ(String stdStageQ)
	{
		this.stdStageQ = stdStageQ;
	}
	public String getPfsnId()
	{
		return pfsnId;
	}
	public void setPfsnId(String pfsnId)
	{
		this.pfsnId = pfsnId;
	}
}
