package com.yz.model.payment;

import java.io.Serializable;

/**
 * 微信二维码缴费详细信息
 * @author lx
 * @date 2017年10月10日 下午8:21:03
 */
public class BdPayInfoDetail implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6627700649688256161L;
	
	private String amount;
	private String stdName;
	private String mobile;
	private String idCard;
	private String grade;
	private String pfsnLevel;
	private String unvsName;
	private String pfsnName;
	private String taName;
	private String payable;
	public String getAmount()
	{
		return amount;
	}
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	public String getStdName()
	{
		return stdName;
	}
	public void setStdName(String stdName)
	{
		this.stdName = stdName;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getIdCard()
	{
		return idCard;
	}
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getPfsnLevel()
	{
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel)
	{
		this.pfsnLevel = pfsnLevel;
	}
	public String getUnvsName()
	{
		return unvsName;
	}
	public void setUnvsName(String unvsName)
	{
		this.unvsName = unvsName;
	}
	public String getPfsnName()
	{
		return pfsnName;
	}
	public void setPfsnName(String pfsnName)
	{
		this.pfsnName = pfsnName;
	}
	public String getTaName()
	{
		return taName;
	}
	public void setTaName(String taName)
	{
		this.taName = taName;
	}
	public String getPayable()
	{
		return payable;
	}
	public void setPayable(String payable)
	{
		this.payable = payable;
	}
}
