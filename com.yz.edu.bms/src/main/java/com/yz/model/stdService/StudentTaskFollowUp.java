package com.yz.model.stdService;

import java.io.Serializable;

/**
 * 学服任务跟进
 * @author lx
 * @date 2017年8月16日 下午3:58:41
 */
public class StudentTaskFollowUp implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2749154352268362566L;
	private String taskId;
	private String learnId;
	private String taskType;
	private String stdStage;
	private String taskTitle;
	private String stdNo;
	private String stdName;
	private String grade;
	private String recruitType;
	private String unvsName;
	private String pfsnLevel;
	private String pfsnCode;
	private String pfsnName;
	private String empName;
	private String taskStatus;
	public String getTaskId()
	{
		return taskId;
	}
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	public String getLearnId()
	{
		return learnId;
	}
	public void setLearnId(String learnId)
	{
		this.learnId = learnId;
	}
	public String getTaskType()
	{
		return taskType;
	}
	public void setTaskType(String taskType)
	{
		this.taskType = taskType;
	}
	public String getStdStage()
	{
		return stdStage;
	}
	public void setStdStage(String stdStage)
	{
		this.stdStage = stdStage;
	}
	public String getTaskTitle()
	{
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle)
	{
		this.taskTitle = taskTitle;
	}
	public String getStdNo()
	{
		return stdNo;
	}
	public void setStdNo(String stdNo)
	{
		this.stdNo = stdNo;
	}
	public String getStdName()
	{
		return stdName;
	}
	public void setStdName(String stdName)
	{
		this.stdName = stdName;
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
	public String getEmpName()
	{
		return empName;
	}
	public void setEmpName(String empName)
	{
		this.empName = empName;
	}
	public String getTaskStatus()
	{
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus)
	{
		this.taskStatus = taskStatus;
	}
}
