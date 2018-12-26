package com.yz.model.educational;

import java.io.Serializable;
/**
 * 教师管理-列表显示
 * @author lx
 * @date 2017年7月10日 下午7:28:39
 */
public class TeacherShowInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7264650213222139857L;
	
	private String empId;
	private String empName;
	private String age;
	private String campusName;
	private String finishMajor;
	private String teach;
	private String mobile;
	private String hourFee;
	private String address;
	private String otherFee;
	private String workPlace;
	
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getFinishMajor() {
		return finishMajor;
	}
	public void setFinishMajor(String finishMajor) {
		this.finishMajor = finishMajor;
	}
	public String getTeach() {
		return teach;
	}
	public void setTeach(String teach) {
		this.teach = teach;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getHourFee() {
		return hourFee;
	}
	public void setHourFee(String hourFee) {
		this.hourFee = hourFee;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}

}
