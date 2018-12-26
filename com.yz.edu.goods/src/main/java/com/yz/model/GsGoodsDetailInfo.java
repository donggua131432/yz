package com.yz.model;

import java.io.Serializable;

/**
 *  物品具体的信息
 * @author lx
 * @date 2017年7月27日 下午5:13:59
 */
public class GsGoodsDetailInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8346991292326502977L;
	private String goodsId;
	private String courseStartTime;
	private String courseEndTime;
	private String courseType;
	private String courseAddress;
	private String activityStartTime;
	private String activityEndTime;
	private String activityAddress;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getCourseStartTime() {
		return courseStartTime;
	}
	public void setCourseStartTime(String courseStartTime) {
		this.courseStartTime = courseStartTime;
	}
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getCourseAddress() {
		return courseAddress;
	}
	public void setCourseAddress(String courseAddress) {
		this.courseAddress = courseAddress;
	}
	public String getActivityStartTime() {
		return activityStartTime;
	}
	public void setActivityStartTime(String activityStartTime) {
		this.activityStartTime = activityStartTime;
	}
	public String getActivityEndTime() {
		return activityEndTime;
	}
	public void setActivityEndTime(String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}
	public String getActivityAddress() {
		return activityAddress;
	}
	public void setActivityAddress(String activityAddress) {
		this.activityAddress = activityAddress;
	}
}
