package com.yz.model.goods;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

public class GsSalesExchange extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7361058428085587408L;
	private String salesId;
	private String dailyCount;
	private String sumCount;
	private String onceCount;
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getDailyCount() {
		return dailyCount;
	}
	public void setDailyCount(String dailyCount) {
		this.dailyCount = dailyCount;
	}
	public String getSumCount() {
		return sumCount;
	}
	public void setSumCount(String sumCount) {
		this.sumCount = sumCount;
	}
	public String getOnceCount() {
		return onceCount;
	}
	public void setOnceCount(String onceCount) {
		this.onceCount = onceCount;
	}
}
