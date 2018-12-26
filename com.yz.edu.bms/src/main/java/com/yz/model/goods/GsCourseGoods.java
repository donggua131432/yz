package com.yz.model.goods;

import java.io.Serializable;

/**
 * 课程附属信息
 * @author lx
 * @date 2017年7月31日 上午9:27:13
 */
public class GsCourseGoods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2109180737109578480L;

	private String goodsId;
	private String startTime;
	private String endTime;
	private String courseType;
	private String location;
	private String address;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
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
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
