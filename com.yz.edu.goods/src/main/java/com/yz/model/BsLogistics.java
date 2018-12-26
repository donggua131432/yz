package com.yz.model;

import java.io.Serializable;


public class BsLogistics implements Serializable
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
	
	private String userName;
	private String mobile;
	private String province;
	private String city;
	private String district;
	private String address;
	private String street;
	
	private String provinceName;
	private String cityName;
	private String districtName;
	private String streetName;
	
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
	public String getProvince()
	{
		return province;
	}
	public void setProvince(String province)
	{
		this.province = province;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getDistrict()
	{
		return district;
	}
	public void setDistrict(String district)
	{
		this.district = district;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getStreet()
	{
		return street;
	}
	public void setStreet(String street)
	{
		this.street = street;
	}
	public String getProvinceName()
	{
		return provinceName;
	}
	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}
	public String getCityName()
	{
		return cityName;
	}
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}
	public String getDistrictName()
	{
		return districtName;
	}
	public void setDistrictName(String districtName)
	{
		this.districtName = districtName;
	}
	public String getStreetName()
	{
		return streetName;
	}
	public void setStreetName(String streetName)
	{
		this.streetName = streetName;
	}
}
