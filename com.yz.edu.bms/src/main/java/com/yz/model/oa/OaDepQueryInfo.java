package com.yz.model.oa;

import com.yz.model.common.PageCondition;

/**
 * 部门查询条件
 * @author lx
 * @date 2017年9月27日 下午3:50:44
 */
public class OaDepQueryInfo extends PageCondition
{

	private String dpName;
	private String campusId;
	public String getDpName()
	{
		return dpName;
	}
	public void setDpName(String dpName)
	{
		this.dpName = dpName;
	}
	public String getCampusId()
	{
		return campusId;
	}
	public void setCampusId(String campusId)
	{
		this.campusId = campusId;
	}
}
