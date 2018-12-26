package com.yz.model;

/**
 * 智米中心--开始前提醒
 * @author lx
 * @date 2018年4月11日 下午6:14:39
 */
public class MsgRemindVo {
	
	private String salesId;       //商品的id
	private String salesName;     //商品名称
	private String startTime;     //开始时间
	private String salesType;     //类型
	private String planCount;     //排期
	private String ext1;          //预留字段1
	private String ext2;          //预留字段2
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	public String getPlanCount() {
		return planCount;
	}
	public void setPlanCount(String planCount) {
		this.planCount = planCount;
	}

}
