package com.yz.model.order;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

public class BsLogistics extends PubInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9082388313229371548L;
	
	private String logisticsId;
	private String transportNo;
	private String logisticsName;
	private String orderNo;
	private String zipCode;
	private String logisticsStatus;
	private String remark;
	
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	public String getLogisticsStatus()
	{
		return logisticsStatus;
	}
	public void setLogisticsStatus(String logisticsStatus)
	{
		this.logisticsStatus = logisticsStatus;
	}
	public String getZipCode()
	{
		return zipCode;
	}
	public void setZipCode(String zipCode)
	{
		this.zipCode = zipCode;
	}
	public String getOrderNo()
	{
		return orderNo;
	}
	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}
	public String getLogisticsId()
	{
		return logisticsId;
	}
	public void setLogisticsId(String logisticsId)
	{
		this.logisticsId = logisticsId;
	}
	public String getTransportNo()
	{
		return transportNo;
	}
	public void setTransportNo(String transportNo)
	{
		this.transportNo = transportNo;
	}
	public String getLogisticsName()
	{
		return logisticsName;
	}
	public void setLogisticsName(String logisticsName)
	{
		this.logisticsName = logisticsName;
	}
}
