package com.yz.model.admin;

import java.io.Serializable;

public class SessionEmpInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 415938572168232880L;
	private String empId;
	private String empName;
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
}
