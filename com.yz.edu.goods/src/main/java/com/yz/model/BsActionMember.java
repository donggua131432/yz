package com.yz.model;

import java.io.Serializable;

/**
 * 线下活动 队友信息
 * @author lx
 * @date 2017年7月27日 下午5:34:31
 */
public class BsActionMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1907750734059156873L;
	private String name;
	private String mobile;
	private String subOrderNo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSubOrderNo() {
		return subOrderNo;
	}
	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}
}
