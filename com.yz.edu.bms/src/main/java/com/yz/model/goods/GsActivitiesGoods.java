package com.yz.model.goods;

import java.io.Serializable;

/**
 * 线下活动附属信息
 * @author lx
 * @date 2017年7月31日 上午9:32:52
 */
public class GsActivitiesGoods implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4478912957366809559L;
	
	private String goodsId;
	private String startTime;
	private String endTime;
	private String location;
	private String takein;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTakein() {
		return takein;
	}
	public void setTakein(String takein) {
		this.takein = takein;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
