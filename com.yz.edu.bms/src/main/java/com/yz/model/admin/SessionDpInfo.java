package com.yz.model.admin;

import java.io.Serializable;

public class SessionDpInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4247441272074887004L;
	private String dpName;
	private String dpId;
	
	public String getDpName() {
		return dpName;
	}
	public void setDpName(String dpName) {
		this.dpName = dpName;
	}
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
}
