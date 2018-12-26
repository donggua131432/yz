package com.yz.model.goods;

import java.io.Serializable;

/**
 * 活动排期
 * @author lx
 * @date 2017年8月2日 上午11:32:55
 */
public class GsSalesPlan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4123427510292567801L;
	private String planId;
	private String totalCount;
	private String curCount;
	private String lessCount;
	private String planStatus;
	private String updateUser;
	private String updateUserId;
	private String updateTime;
	private String reason;
	private String startTime;
	private String endTime;
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getCurCount() {
		return curCount;
	}
	public void setCurCount(String curCount) {
		this.curCount = curCount;
	}
	public String getLessCount() {
		return lessCount;
	}
	public void setLessCount(String lessCount) {
		this.lessCount = lessCount;
	}
	public String getPlanStatus() {
		return planStatus;
	}
	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
}
