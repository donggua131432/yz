package com.yz.model.stdService;

import java.io.Serializable;
/**
 * 学员服务---毕业资料提交
 * @author lx
 * @date 2018年1月26日 上午11:48:16
 */
public class StudentGraduateDataInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5263905230970445873L;
	
	private String id;
	private String taskId;
	private String learnId;
	private String stdId;
	
	
	
	/** 扩展显示字段*/
	private String stdName;
	private String grade;
	private String unvsName;
	private String pfsnCode;
	private String pfsnName;
	private String pfsnLevel;
	private String stdStage;
	private String tutorName;
	private String idCard;
	private String taskTitle;
	private String schoolRoll;
	private String stdNo;
	
	private String ifSubmit;
	private String ifPass;
	private String noPassReason;
	private String ifMail;
	private String userName;
	private String mobile;
	private String provinceCode;
	private String cityCode;
	private String districtCode;
	private String expressNo;
	private String remark;
	private String address;
	
	private String operUserId;
	private String operUser;
	
	private String exportAddress;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
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
	public String getStdId()
	{
		return stdId;
	}
	public void setStdId(String stdId)
	{
		this.stdId = stdId;
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
	public String getTutorName()
	{
		return tutorName;
	}
	public void setTutorName(String tutorName)
	{
		this.tutorName = tutorName;
	}
	public String getIdCard()
	{
		return idCard;
	}
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}
	public String getTaskTitle()
	{
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle)
	{
		this.taskTitle = taskTitle;
	}
	public String getSchoolRoll()
	{
		return schoolRoll;
	}
	public void setSchoolRoll(String schoolRoll)
	{
		this.schoolRoll = schoolRoll;
	}
	public String getStdNo()
	{
		return stdNo;
	}
	public void setStdNo(String stdNo)
	{
		this.stdNo = stdNo;
	}
	public String getIfSubmit()
	{
		return ifSubmit;
	}
	public void setIfSubmit(String ifSubmit)
	{
		this.ifSubmit = ifSubmit;
	}
	public String getIfPass()
	{
		return ifPass;
	}
	public void setIfPass(String ifPass)
	{
		this.ifPass = ifPass;
	}
	public String getNoPassReason()
	{
		return noPassReason;
	}
	public void setNoPassReason(String noPassReason)
	{
		this.noPassReason = noPassReason;
	}
	public String getIfMail()
	{
		return ifMail;
	}
	public void setIfMail(String ifMail)
	{
		this.ifMail = ifMail;
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
	public String getExpressNo()
	{
		return expressNo;
	}
	public void setExpressNo(String expressNo)
	{
		this.expressNo = expressNo;
	}
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getOperUserId()
	{
		return operUserId;
	}
	public void setOperUserId(String operUserId)
	{
		this.operUserId = operUserId;
	}
	public String getOperUser()
	{
		return operUser;
	}
	public void setOperUser(String operUser)
	{
		this.operUser = operUser;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getExportAddress()
	{
		return exportAddress;
	}
	public void setExportAddress(String exportAddress)
	{
		this.exportAddress = exportAddress;
	}
	

}
