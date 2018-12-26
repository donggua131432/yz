package com.yz.model.admin;

import java.util.ArrayList;
import java.util.List;

public class BmsUserResponse extends BmsUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4477561889250418766L;

	private String[] roleArray;

	private List<BmsRole> roles = new ArrayList<BmsRole>();
	
	private String campusId;
	private String dpId;

	public String[] getRoleArray() {
		return roleArray;
	}

	public void setRoleArray(String[] roleArray) {
		this.roleArray = roleArray;
	}

	public List<BmsRole> getRoles() {
		return roles;
	}

	public void setRoles(List<BmsRole> roles) {
		this.roles = roles;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

}
