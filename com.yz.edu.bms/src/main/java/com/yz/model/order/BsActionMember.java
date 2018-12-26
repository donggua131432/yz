package com.yz.model.order;

import java.io.Serializable;

/**
 * 线下活动订单-队员信息
 * @author lx
 * @date 2017年8月8日 下午3:45:26
 */
public class BsActionMember implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2929909782130851793L;

	private String name;
	private String mobile;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
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
