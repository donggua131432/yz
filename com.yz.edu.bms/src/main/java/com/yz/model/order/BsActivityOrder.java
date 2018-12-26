package com.yz.model.order;

import java.util.List;

public class BsActivityOrder extends BsOrder
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 282750564047666565L;

	private List<BsActionMember> memberList;
	private String goodsId;
	
	private String startTime;
	private String address;
	private String takeIn;
	private String endTime;
	public String getGoodsId()
	{
		return goodsId;
	}

	public void setGoodsId(String goodsId)
	{
		this.goodsId = goodsId;
	}

	public List<BsActionMember> getMemberList()
	{
		return memberList;
	}

	public void setMemberList(List<BsActionMember> memberList)
	{
		this.memberList = memberList;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getTakeIn()
	{
		return takeIn;
	}

	public void setTakeIn(String takeIn)
	{
		this.takeIn = takeIn;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
}
