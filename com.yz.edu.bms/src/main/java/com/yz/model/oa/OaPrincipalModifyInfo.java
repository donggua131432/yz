package com.yz.model.oa;

import com.yz.model.common.PubInfo;

/**
 * 校区,部门,组  负责人变化
 * @author lx
 * @date 2017年11月7日 上午9:14:56
 */
public class OaPrincipalModifyInfo extends PubInfo
{

	private String modifyId;
	private String oldEmpId;
	private String beginTime;
	private String endTime;
	private String newEmpId;
	private String principalType;
	private String businessId;
	public String getModifyId()
	{
		return modifyId;
	}
	public void setModifyId(String modifyId)
	{
		this.modifyId = modifyId;
	}
	public String getOldEmpId()
	{
		return oldEmpId;
	}
	public void setOldEmpId(String oldEmpId)
	{
		this.oldEmpId = oldEmpId;
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
	public String getNewEmpId()
	{
		return newEmpId;
	}
	public void setNewEmpId(String newEmpId)
	{
		this.newEmpId = newEmpId;
	}
	public String getPrincipalType()
	{
		return principalType;
	}
	public void setPrincipalType(String principalType)
	{
		this.principalType = principalType;
	}
	public String getBusinessId()
	{
		return businessId;
	}
	public void setBusinessId(String businessId)
	{
		this.businessId = businessId;
	}
}
