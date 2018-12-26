package com.yz.model.oa;

import java.io.Serializable;

/**
 * 校区信息
 * @author lx
 * @date 2017年6月28日 上午11:15:12
 */
public class OaCampusInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3589683910989709991L;
	
	private String campusId;            //主键id
	private String campusName;          //校区名称  
	private String financeNo;           //财务代码
	private String empName;            //分管领导
	private String telephone;          //座机
	private String address;            //地址 
	private String isForeign;          //是否外校区
	private String empId;              //分管领导id
	private String isStop;             //是否停用
	
	private String updateUserId;
	private String updateUser;
	private String createUserId;
	private String createUser;
	
    private String provinceId;     //省code
    private String cityId;         //市code
    private String areaId;         //区code
    
    private String createTime;
	
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
	public String getFinanceNo() {
		return financeNo;
	}
	public void setFinanceNo(String financeNo) {
		this.financeNo = financeNo;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIsForeign() {
		return isForeign;
	}
	public void setIsForeign(String isForeign) {
		this.isForeign = isForeign;
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
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getAreaId() {
		return areaId;
	}
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getIsStop() {
		return isStop;
	}
	public void setIsStop(String isStop) {
		this.isStop = isStop;
	}


}
