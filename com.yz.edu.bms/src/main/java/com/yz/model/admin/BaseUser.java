package com.yz.model.admin;

import java.io.Serializable;
import java.util.List;

public class BaseUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7808369757449516436L;
	/** 用户Id */
	private String userId;
	/** 用户名 */
	private String username;
	/** 真实姓名 */
	private String realName;

	private String password;
	/** 员工编号 */
	private String empId;

	/** 所属部门ID */
	private String departmentId;
	/** 所属部门名称 */
	private String departmentName;
	/** 所属校区Id */
	private String campusId;
	/** 所属校区名称 */
	private String campusName;
	/** 我的权限 */
	private List<BmsFunc> funcs;
	/** 用户级别  1-超级管理员 2-校区管理者 3-部门管理者 4-员工、校监*/
	private String userLevel;
	/** 我负责的校区*/
	private List<SessionCampusInfo> myCampusList;
	/** 我负责的部门和下属部门*/
	private List<SessionDpInfo> myDpList;
	/** 我的下属职员 */
	private List<SessionEmpInfo> myEmplyeeList;
	/** 是否停用 */
	private String isBlock;
	
	/** 我的职称 */
	private List<String> jtList;
	
	/**我的角色代码*/
	private List<String> roleCodeList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public List<BmsFunc> getFuncs() {
		return funcs;
	}

	public void setFuncs(List<BmsFunc> funcs) {
		this.funcs = funcs;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public List<SessionCampusInfo> getMyCampusList() {
		return myCampusList;
	}

	public void setMyCampusList(List<SessionCampusInfo> myCampusList) {
		this.myCampusList = myCampusList;
	}

	public List<SessionDpInfo> getMyDpList() {
		return myDpList;
	}

	public void setMyDpList(List<SessionDpInfo> myDpList) {
		this.myDpList = myDpList;
	}

	public List<SessionEmpInfo> getMyEmplyeeList() {
		return myEmplyeeList;
	}

	public void setMyEmplyeeList(List<SessionEmpInfo> myEmplyeeList) {
		this.myEmplyeeList = myEmplyeeList;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getIsBlock() {
		return isBlock;
	}

	public void setIsBlock(String isBlock) {
		this.isBlock = isBlock;
	}

	public List<String> getJtList() {
		return jtList;
	}

	public void setJtList(List<String> jtList) {
		this.jtList = jtList;
	}

	public List<String> getRoleCodeList()
	{
		return roleCodeList;
	}

	public void setRoleCodeList(List<String> roleCodeList)
	{
		this.roleCodeList = roleCodeList;
	}

}
