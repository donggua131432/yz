package com.yz.model.coupon;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BdCoupon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -601098488554676861L;
	private String couponId;
	private String couponName;
	private String amount;
	private String couponType;
	private String publishStartTime;
	private String publishExpireTime;
	private String availableStartTime;
	private String availableExpireTime;
	private String isAllow;
	private String triggerContent;
	private String cTriggerType;
	private String remark;
	private String grade;
	private String scholarship;
	private String isUse;
	private String[] itemCodes;
	private String[] pfsnIds;
	private String[] taIds;
	private String lowestScore;
	private String highestScore;
	private String scId;
	private List<BdCouponDetail> couponDetails;

	public List<BdCouponDetail> getCouponDetails() {
		return couponDetails;
	}

	public void setCouponDetails(List<BdCouponDetail> couponDetails) {
		this.couponDetails = couponDetails;
	}

	public String getScId() {
		return scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getLowestScore() {
		return lowestScore;
	}

	public void setLowestScore(String lowestScore) {
		this.lowestScore = lowestScore;
	}

	public String getHighestScore() {
		return highestScore;
	}

	public void setHighestScore(String highestScore) {
		this.highestScore = highestScore;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String[] getPfsnIds() {
		return pfsnIds;
	}

	public void setPfsnIds(String[] pfsnIds) {
		this.pfsnIds = pfsnIds;
	}

	public String[] getTaIds() {
		return taIds;
	}

	public void setTaIds(String[] taIds) {
		this.taIds = taIds;
	}

	public String[] getItemCodes() {
		return itemCodes;
	}

	public void setItemCodes(String[] itemCodes) {
		this.itemCodes = itemCodes;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getTriggerContent() {
		return triggerContent;
	}

	public void setTriggerContent(String triggerContent) {
		this.triggerContent = triggerContent;
	}

	public String getcTriggerType() {
		return cTriggerType;
	}

	public void setcTriggerType(String cTriggerType) {
		this.cTriggerType = cTriggerType;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getPublishStartTime() {
		return publishStartTime;
	}

	public void setPublishStartTime(String publishStartTime) {
		this.publishStartTime = publishStartTime;
	}

	public String getPublishExpireTime() {
		return publishExpireTime;
	}

	public void setPublishExpireTime(String publishExpireTime) {
		this.publishExpireTime = publishExpireTime;
	}

	public String getAvailableStartTime() {
		return availableStartTime;
	}

	public void setAvailableStartTime(String availableStartTime) {
		this.availableStartTime = availableStartTime;
	}

	public String getAvailableExpireTime() {
		return availableExpireTime;
	}

	public void setAvailableExpireTime(String availableExpireTime) {
		this.availableExpireTime = availableExpireTime;
	}

	public String getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}

}
