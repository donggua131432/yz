package com.yz.model.common;

/**
 * 员工查询
 * @author Administrator
 *
 */
public class EmpQueryInfo extends PageCondition {

	private String campus;
	private String department;
	private String group;
	private String employee;

	private String[] jtIds;
	
	private String mobile;

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String[] getJtIds() {
		return jtIds;
	}

	public void setJtIds(String[] jtIds) {
		this.jtIds = jtIds;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
