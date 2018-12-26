package com.yz.model.oa;

import java.io.Serializable;

/**
 * 招生老师信息
 * @author lx
 * @date 2017年7月3日 下午12:02:14
 */
public class RecruiterInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5382974575489172834L;
	
	private String empId;               //员工id
	private String headShot;           //照片  
	private String empName;             //员工姓名
	private String dpId;                //部门id
	private String recruitCode;         //编码
	private String mobile;              //手机
	private String groupId;             //部门组
	private String empType;             //员工类型
	private String empStatus;           //状态
	private String personnelIsAffirm;   //人事是否确认
	private String isFormalEmp;         //是否转正
	private String titleName;           //职位
	private String campusName;          //校区
	
	private String campusId;            //校区id
	private String dpName;              //部门名称
	private String groupName;           //组名称
	
	private String[] jdIds = new String[]{};      //员工职称
	
	private String sexType;     //性别
	private String idCard;      //身份证
	private String yzId;         //远智编码
	
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
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId == null ? null : dpId.trim();
	}
	public String getRecruitCode() {
		return recruitCode;
	}
	public void setRecruitCode(String recruitCode) {
		this.recruitCode = recruitCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getEmpStatus() {
		return empStatus;
	}
	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
	public String getPersonnelIsAffirm() {
		return personnelIsAffirm;
	}
	public void setPersonnelIsAffirm(String personnelIsAffirm) {
		this.personnelIsAffirm = personnelIsAffirm;
	}
	public String getIsFormalEmp() {
		return isFormalEmp;
	}
	public void setIsFormalEmp(String isFormalEmp) {
		this.isFormalEmp = isFormalEmp;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getHeadShot() {
		return headShot;
	}
	public void setHeadShot(String headShot) {
		this.headShot = headShot;
	}
	public String getDpName() {
		return dpName;
	}
	public void setDpName(String dpName) {
		this.dpName = dpName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String[] getJdIds()
	{
		return jdIds;
	}
	public void setJdIds(String[] jdIds)
	{
		this.jdIds = jdIds;
	}
	public String getSexType()
	{
		return sexType;
	}
	public void setSexType(String sexType)
	{
		this.sexType = sexType;
	}
	public String getIdCard()
	{
		return idCard;
	}
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}
	public String getYzId() {
		return yzId;
	}
	public void setYzId(String yzId) {
		this.yzId = yzId;
	}
	

}
