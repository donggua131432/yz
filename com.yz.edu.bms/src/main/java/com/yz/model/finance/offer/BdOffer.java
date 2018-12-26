package com.yz.model.finance.offer;

import java.util.ArrayList;
import java.util.Date;

import com.yz.model.common.PubInfo;
import com.yz.model.finance.fee.FeeItemForm;

public class BdOffer extends PubInfo {

	private String offerId;
	private String offerName;
	private String startTime;
	private String expireTime;
	private String status;
	private String scholarship;
	private String inclusionStatus;

	private String[] pfsnIds;
	private String[] taIds;
	private ArrayList<FeeItemForm> items;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private Date createTime;

	private String createUser;

	public String getInclusionStatus() {
		return inclusionStatus;
	}

	public void setInclusionStatus(String inclusionStatus) {
		this.inclusionStatus = inclusionStatus;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<FeeItemForm> getItems() {
		return items;
	}

	public void setItems(ArrayList<FeeItemForm> items) {
		this.items = items;
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
		this.updateUser = updateUser;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
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
		this.createUser = createUser;
	}

	public String[] getPfsnIds() {
		return pfsnIds;
	}

	public void setPfsnIds(String[] pfsnIds) {
		this.pfsnIds = pfsnIds;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String[] getTaIds() {
		return taIds;
	}

	public void setTaIds(String[] taIds) {
		this.taIds = taIds;
	}

}
