package com.yz.model.admin;

import java.io.Serializable;

public class SessionGroupInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7055145502990120632L;
	private String groupId;
	private String groupName;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
