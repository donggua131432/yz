package com.yz.model.stdService;

import java.io.Serializable;
/**
 * 学服任务跟进 查询条件
 * @author lx
 * @date 2017年8月16日 下午8:18:37
 */
public class StudentTaskFollowUpQuery implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2152869625918573413L;
    private String unvsName;
    private String recruitType;
    private String grade;
    private String pfsnName;
    private String stdName;
    private String idCard;
    private String mobile;
    private String tutorId;
	public String getTutorId()
	{
		return tutorId;
	}
	public void setTutorId(String tutorId)
	{
		this.tutorId = tutorId;
	}
	public String getUnvsName()
	{
		return unvsName;
	}
	public void setUnvsName(String unvsName)
	{
		this.unvsName = unvsName;
	}
	public String getRecruitType()
	{
		return recruitType;
	}
	public void setRecruitType(String recruitType)
	{
		this.recruitType = recruitType;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getPfsnName()
	{
		return pfsnName;
	}
	public void setPfsnName(String pfsnName)
	{
		this.pfsnName = pfsnName;
	}
	public String getStdName()
	{
		return stdName;
	}
	public void setStdName(String stdName)
	{
		this.stdName = stdName;
	}
	public String getIdCard()
	{
		return idCard;
	}
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
}
