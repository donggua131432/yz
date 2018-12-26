package com.yz.job.model;

import java.io.Serializable;

/**
 * 招生老师的学员信息
 * @author lx
 * @date 2017年11月6日 下午5:12:19
 */
public class OaEmployeeLearnInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1905451423414096052L;
	
	private String recrodsNo;
	private String learnId;
	private String recruit;
	private String recruitCampusId;
	private String recruitDpId;
	private String modifyId;
	public String getRecrodsNo()
	{
		return recrodsNo;
	}
	public void setRecrodsNo(String recrodsNo)
	{
		this.recrodsNo = recrodsNo;
	}
	public String getLearnId()
	{
		return learnId;
	}
	public void setLearnId(String learnId)
	{
		this.learnId = learnId;
	}
	public String getRecruit()
	{
		return recruit;
	}
	public void setRecruit(String recruit)
	{
		this.recruit = recruit;
	}
	public String getRecruitCampusId()
	{
		return recruitCampusId;
	}
	public void setRecruitCampusId(String recruitCampusId)
	{
		this.recruitCampusId = recruitCampusId;
	}
	public String getRecruitDpId()
	{
		return recruitDpId;
	}
	public void setRecruitDpId(String recruitDpId)
	{
		this.recruitDpId = recruitDpId;
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
