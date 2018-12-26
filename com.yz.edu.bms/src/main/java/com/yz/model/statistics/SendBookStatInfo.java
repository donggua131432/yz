package com.yz.model.statistics;

import java.io.Serializable;
/**
 * 订书统计
 * @author lx
 * @date 2017年11月23日 下午5:42:44
 */
public class SendBookStatInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6110862002234821169L;
	private String unvsName;
	private String pfsnName;
	private String pfsnLevel;
	private String grade;
	private String semester;
	private String year;
	private String stdName;
	private String idCard;
	private String stdStage;
	private String alias;
	private String textbookName;
	private String price;
	private String orderNum;
	private String totalPrice;
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
	public String getPfsnLevel()
	{
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel)
	{
		this.pfsnLevel = pfsnLevel;
	}
	public String getGrade()
	{
		return grade;
	}
	public void setGrade(String grade)
	{
		this.grade = grade;
	}
	public String getSemester()
	{
		return semester;
	}
	public void setSemester(String semester)
	{
		this.semester = semester;
	}
	public String getYear()
	{
		return year;
	}
	public void setYear(String year)
	{
		this.year = year;
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
	public String getStdStage()
	{
		return stdStage;
	}
	public void setStdStage(String stdStage)
	{
		this.stdStage = stdStage;
	}
	public String getAlias()
	{
		return alias;
	}
	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	public String getTextbookName()
	{
		return textbookName;
	}
	public void setTextbookName(String textbookName)
	{
		this.textbookName = textbookName;
	}
	public String getPrice()
	{
		return price;
	}
	public void setPrice(String price)
	{
		this.price = price;
	}
	public String getOrderNum()
	{
		return orderNum;
	}
	public void setOrderNum(String orderNum)
	{
		this.orderNum = orderNum;
	}
	public String getTotalPrice()
	{
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice)
	{
		this.totalPrice = totalPrice;
	} 
}
