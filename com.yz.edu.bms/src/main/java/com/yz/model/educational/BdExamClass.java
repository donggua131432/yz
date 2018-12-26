package com.yz.model.educational;

import java.util.List;

import com.yz.model.common.PubInfo;

public class BdExamClass extends PubInfo {

	/**
	 * 考场安排ID
	 */
	private String pyId;
	/**
	 * 考场年度ID
	 */
	private String eyId;
	/**
	 * 考场编码
	 */
	private String epId;
	/**
	 * 考场安排编码
	 */
	private String pyCode;
	/**
	 * 考场年度
	 */
	private String examYear;
	/**
	 * 考场名称
	 */
	private String epName;
	/**
	 * 考场ID
	 */
	private String placeId;
	/**
	 * 日期
	 */
	private String date;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 省份
	 */
	private String provinceName;
	/**
	 * 城市
	 */
	private String cityName;
	/**
	 * 地区名称
	 */
	private String districtName;

	/**
	 * 考场容量
	 */
	private String seats;
	/**
	 * 已选座位
	 */
	private String restSeats;
	/**
	 * 课室划分备注
	 */
	private String divideRemark;
	/**
	 * 课室安排
	 */
	private List<BdPlaceInfo> places;

	public String getEpId() {
		return epId;
	}

	public void setEpId(String epId) {
		this.epId = epId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getDivideRemark() {
		return divideRemark;
	}

	public void setDivideRemark(String divideRemark) {
		this.divideRemark = divideRemark;
	}

	public List<BdPlaceInfo> getPlaces() {
		return places;
	}

	public void setPlaces(List<BdPlaceInfo> places) {
		this.places = places;
	}

	public String getPyId() {
		return pyId;
	}

	public void setPyId(String pyId) {
		this.pyId = pyId;
	}

	public String getEyId() {
		return eyId;
	}

	public void setEyId(String eyId) {
		this.eyId = eyId;
	}

	public String getExamYear() {
		return examYear;
	}

	public void setExamYear(String examYear) {
		this.examYear = examYear;
	}

	public String getEpName() {
		return epName;
	}

	public void setEpName(String epName) {
		this.epName = epName;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getSeats() {
		return seats;
	}

	public void setSeats(String seats) {
		this.seats = seats;
	}

	public String getRestSeats() {
		return restSeats;
	}

	public void setRestSeats(String restSeats) {
		this.restSeats = restSeats;
	}

}
