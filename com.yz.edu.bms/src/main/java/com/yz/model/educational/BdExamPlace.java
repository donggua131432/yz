package com.yz.model.educational;

import java.util.ArrayList;
import java.util.List;

import com.yz.model.common.PubInfo;

public class BdExamPlace extends PubInfo {
	private String epId;

	private String epCode;

	private String epName;

	private String testTime;

	private String courseId;

	private String remark;

	private String province;

	private String provinceCode;

	private String cityCode;

	private String districtCode;

	private String address;

	private String campusId;

	private String isLease;

	private String startTime2;

	private String endTime2;

	private String leaseFee;

	private String empId;

	private String empName;

	private String managerName;

	private String managerMobile;

	private String status;

	private List<BdPlaceInfo> places = new ArrayList<BdPlaceInfo>();

	private List<BdPlaceTime> times = new ArrayList<BdPlaceTime>();

	public String getEpCode() {
		return epCode;
	}

	public void setEpCode(String epCode) {
		this.epCode = epCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public List<BdPlaceInfo> getPlaces() {
		return places;
	}

	public void setPlaces(List<BdPlaceInfo> places) {
		this.places = places;
	}

	public List<BdPlaceTime> getTimes() {
		return times;
	}

	public void setTimes(List<BdPlaceTime> times) {
		this.times = times;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEpId() {
		return epId;
	}

	public void setEpId(String epId) {
		this.epId = epId == null ? null : epId.trim();
	}

	public String getEpName() {
		return epName;
	}

	public void setEpName(String epName) {
		this.epName = epName == null ? null : epName.trim();
	}

	public String getTestTime() {
		return testTime;
	}

	public void setTestTime(String testTime) {
		this.testTime = testTime == null ? null : testTime.trim();
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId == null ? null : courseId.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode == null ? null : provinceCode.trim();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode == null ? null : cityCode.trim();
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode == null ? null : districtCode.trim();
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId == null ? null : campusId.trim();
	}

	public String getIsLease() {
		return isLease;
	}

	public void setIsLease(String isLease) {
		this.isLease = isLease == null ? null : isLease.trim();
	}

	public String getStartTime2() {
		return startTime2;
	}

	public void setStartTime2(String startTime2) {
		this.startTime2 = startTime2;
	}

	public String getEndTime2() {
		return endTime2;
	}

	public void setEndTime2(String endTime2) {
		this.endTime2 = endTime2;
	}

	public String getLeaseFee() {
		return leaseFee;
	}

	public void setLeaseFee(String leaseFee) {
		this.leaseFee = leaseFee == null ? null : leaseFee.trim();
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId == null ? null : empId.trim();
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName == null ? null : empName.trim();
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName == null ? null : managerName.trim();
	}

	public String getManagerMobile() {
		return managerMobile;
	}

	public void setManagerMobile(String managerMobile) {
		this.managerMobile = managerMobile == null ? null : managerMobile.trim();
	}

}