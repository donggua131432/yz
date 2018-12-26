package com.yz.model.payment;

import java.io.Serializable;
import java.util.Date;

public class BdFeeItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7238471936898849813L;

	private String itemCode;

	private String pItemCode;

	private String itemName;

	private String itemType;

	private String itemYear;

	private String status;

	private String remark;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private String createTime;

	private String createUser;

	private String[] recruitTypes;

	public String getItemYear() {
		return itemYear;
	}

	public void setItemYear(String itemYear) {
		this.itemYear = itemYear;
	}

	public String[] getRecruitTypes() {
		return recruitTypes;
	}

	public void setRecruitTypes(String[] recruitTypes) {
		this.recruitTypes = recruitTypes;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode == null ? null : itemCode.trim();
	}

	public String getpItemCode() {
		return pItemCode;
	}

	public void setpItemCode(String pItemCode) {
		this.pItemCode = pItemCode == null ? null : pItemCode.trim();
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName == null ? null : itemName.trim();
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType == null ? null : itemType.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

}