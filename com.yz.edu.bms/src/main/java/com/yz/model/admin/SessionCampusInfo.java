package com.yz.model.admin;

import java.io.Serializable;

public class SessionCampusInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6367408589660199119L;
	private String campusName;
	private String campusId;
	private String financeNo;
	
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getFinanceNo() {
		return financeNo;
	}
	public void setFinanceNo(String financeNo) {
		this.financeNo = financeNo;
	}
}
