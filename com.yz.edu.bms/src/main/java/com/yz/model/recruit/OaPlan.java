package com.yz.model.recruit;

import java.util.Date;

public class OaPlan {
	private String planId;

	private String dpId;

	private String timeSlot;

	private String targetCount;

	private String minCount;

	private String realCount;

	private String status;

	private String planType;

	private String todayIncr;

	private String isDel;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private Date createTime;

	private String createUser;

	private String ext1;

	private String ext2;

	private String ext3;

	public String getTodayIncr() {
		return todayIncr;
	}

	public void setTodayIncr(String todayIncr) {
		this.todayIncr = todayIncr;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId == null ? null : planId.trim();
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId == null ? null : dpId.trim();
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot == null ? null : timeSlot.trim();
	}

	public String getTargetCount() {
		return targetCount;
	}

	public void setTargetCount(String targetCount) {
		this.targetCount = targetCount == null ? null : targetCount.trim();
	}

	public String getMinCount() {
		return minCount;
	}

	public void setMinCount(String minCount) {
		this.minCount = minCount == null ? null : minCount.trim();
	}

	public String getRealCount() {
		return realCount;
	}

	public void setRealCount(String realCount) {
		this.realCount = realCount == null ? null : realCount.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType == null ? null : planType.trim();
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel == null ? null : isDel.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim();
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId == null ? null : updateUserId.trim();
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId == null ? null : createUserId.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1 == null ? null : ext1.trim();
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2 == null ? null : ext2.trim();
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3 == null ? null : ext3.trim();
	}
}