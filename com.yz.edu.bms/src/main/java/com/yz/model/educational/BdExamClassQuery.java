package com.yz.model.educational;

public class BdExamClassQuery {

	/**
	 * 考场编号
	 */
	private String pyCode;
	/**
	 * 考场名称
	 */
	private String epName;
	/**
	 * 省代码
	 */
	private String provinceCode;
	/**
	 * 市代码
	 */
	private String cityCode;
	/**
	 * 区代码
	 */
	private String districtCode;

	/**
	 * 划分状态
	 */
	private String divideStatus;

	/**
	 * 考试开始时间
	 */
	private String startTime;
	/**
	 * 考试结束时间
	 */
	private String endTime;
	
	/**
	 * 考试年度
	 */
	private String eyId;

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getEpName() {
		return epName;
	}

	public void setEpName(String epName) {
		this.epName = epName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getDivideStatus() {
		return divideStatus;
	}

	public void setDivideStatus(String divideStatus) {
		this.divideStatus = divideStatus;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEyId() {
		return eyId;
	}

	public void setEyId(String eyId) {
		this.eyId = eyId;
	}
	

}
