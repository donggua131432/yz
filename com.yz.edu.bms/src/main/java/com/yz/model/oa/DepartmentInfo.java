package com.yz.model.oa;

import java.io.Serializable;

/**
 * 部门信息
 * @author lx
 * @date 2017年6月29日 下午5:48:10
 */
public class DepartmentInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8287711031946113991L;
	
	private String dpId;               //部门id
	private String dpName;             //部门名称
	private String campusId;           //校区id
	private String empId;              //负责人id
	private String isRecruit;          //是否有招生职能
	private String isParticipate;      //是否参与招生计划
	private String isStop;             //是否停用   0-否，1-是
	private String recruitRules;       //招生权限
	
	private String campusName;         //校区名称
	private String empName;            //负责人
	private String[] recruitTypes = new String[]{};//招生类型
	
	private String updateUserId;
	private String updateUser;
	private String createUserId;
	private String createUser;
	
	private String[] jdIds = new String[]{}; //部门职称
	
	private String createTime;
	
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	public String getDpName() {
		return dpName;
	}
	public void setDpName(String dpName) {
		this.dpName = dpName;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getIsRecruit() {
		return isRecruit;
	}
	public void setIsRecruit(String isRecruit) {
		this.isRecruit = isRecruit;
	}
	public String getIsParticipate() {
		return isParticipate;
	}
	public void setIsParticipate(String isParticipate) {
		this.isParticipate = isParticipate;
	}
	public String getIsStop() {
		return isStop;
	}
	public void setIsStop(String isStop) {
		this.isStop = isStop;
	}
	public String getRecruitRules() {
		return recruitRules;
	}
	public void setRecruitRules(String recruitRules) {
		this.recruitRules = recruitRules;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
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
	public String[] getRecruitTypes() {
		return recruitTypes;
	}
	public String[] getJdIds()
	{
		return jdIds;
	}
	public void setJdIds(String[] jdIds)
	{
		this.jdIds = jdIds;
	}
	public void setRecruitTypes(String[] recruitTypes) {
		this.recruitTypes = recruitTypes;
	}
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

}
