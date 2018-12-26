package com.yz.model.stdService;

import java.io.Serializable;
/**
 * 学员服务-考场确认跟进
 * @author lx
 * @date 2017年12月7日 下午12:23:31
 */
public class StudentExamAffirmInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3498852776396331465L;
	private String affirmId;          
	private String taskId;            
    private String eyId;    
	private String learnId;           
	private String pyId; 
	private String erId;
	private String affirmTime;        
	private String signStatus;        
	private String signTime;          
	private String resetStatus;       
	private String resetTime;         
	private String resetUserId;
	
	private String testYear;  
	private String unconfirmedReason; 
	private String unconfirmedTime;   
	private String unconfirmedUserId;
	
	private String stdName;
	private String grade;
	private String recruitType;
	private String unvsName;
	private String pfsnCode;
	private String pfsnName;
	private String pfsnLevel;
	private String stdStage;
	private String tutorName;
	private String idCard;
	
	private String epName;
	private String startTime;
	private String endTime;
	private String schoolRoll;
	
	public String getAffirmId()
	{
		return affirmId;
	}
	public void setAffirmId(String affirmId)
	{
		this.affirmId = affirmId;
	}
	public String getTaskId()
	{
		return taskId;
	}
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	public String getTestYear()
	{
		return testYear;
	}
	public void setTestYear(String testYear)
	{
		this.testYear = testYear;
	}
	public String getLearnId()
	{
		return learnId;
	}
	public void setLearnId(String learnId)
	{
		this.learnId = learnId;
	}
	
	public String getAffirmTime()
	{
		return affirmTime;
	}
	public void setAffirmTime(String affirmTime)
	{
		this.affirmTime = affirmTime;
	}
	public String getUnconfirmedReason()
	{
		return unconfirmedReason;
	}
	public void setUnconfirmedReason(String unconfirmedReason)
	{
		this.unconfirmedReason = unconfirmedReason;
	}
	public String getUnconfirmedTime()
	{
		return unconfirmedTime;
	}
	public void setUnconfirmedTime(String unconfirmedTime)
	{
		this.unconfirmedTime = unconfirmedTime;
	}
	public String getUnconfirmedUserId()
	{
		return unconfirmedUserId;
	}
	public void setUnconfirmedUserId(String unconfirmedUserId)
	{
		this.unconfirmedUserId = unconfirmedUserId;
	}
	public String getSignStatus()
	{
		return signStatus;
	}
	public void setSignStatus(String signStatus)
	{
		this.signStatus = signStatus;
	}
	public String getSignTime()
	{
		return signTime;
	}
	public void setSignTime(String signTime)
	{
		this.signTime = signTime;
	}
	public String getResetStatus()
	{
		return resetStatus;
	}
	public void setResetStatus(String resetStatus)
	{
		this.resetStatus = resetStatus;
	}
	public String getResetTime()
	{
		return resetTime;
	}
	public void setResetTime(String resetTime)
	{
		this.resetTime = resetTime;
	}
	public String getResetUserId()
	{
		return resetUserId;
	}
	public void setResetUserId(String resetUserId)
	{
		this.resetUserId = resetUserId;
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
	public String getPfsnLevel()
	{
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel)
	{
		this.pfsnLevel = pfsnLevel;
	}
	public String getStdStage()
	{
		return stdStage;
	}
	public void setStdStage(String stdStage)
	{
		this.stdStage = stdStage;
	}
	public String getIdCard()
	{
		return idCard;
	}
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}
	public String getTutorName()
	{
		return tutorName;
	}
	public void setTutorName(String tutorName)
	{
		this.tutorName = tutorName;
	}
	public String getPyId()
	{
		return pyId;
	}
	public void setPyId(String pyId)
	{
		this.pyId = pyId;
	}
	public String getEpName()
	{
		return epName;
	}
	public void setEpName(String epName)
	{
		this.epName = epName;
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
	public String getEyId()
	{
		return eyId;
	}
	public void setEyId(String eyId)
	{
		this.eyId = eyId;
	}
	public String getErId()
	{
		return erId;
	}
	public void setErId(String erId)
	{
		this.erId = erId;
	}

	public String getSchoolRoll() {
		return schoolRoll;
	}

	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll;
	}
}
