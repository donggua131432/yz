package com.yz.model.system;

import java.io.Serializable;

/**
 * 京东地址
 * @author lx
 * @date 2018年3月28日 下午4:22:47
 */
public class JDAddressVo implements Comparable<JDAddressVo>,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2661710523514376579L;
	private String name;
	private int code;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getCode()
	{
		return code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
	@Override
	public int compareTo(JDAddressVo o)
	{
		if(this.code >= o.getCode()){
			return 1;
		}
		return -1;
	}

}
