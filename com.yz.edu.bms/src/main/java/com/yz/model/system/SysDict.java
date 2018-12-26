package com.yz.model.system;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class SysDict {

	@NotNull(message = "dictId不能为空")
	private String dictId;

	private String pId;

	@NotNull(message = "dictName不能为空")
	private String dictName;

	private String dictValue;

	private String orderNum;

	private Date updateTime;

	private String updateUser;

	private String description;

	private String updateUserId;

	private String ext1;

	private String ext2;

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId == null ? null : dictId.trim();
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId == null ? null : pId.trim();
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName == null ? null : dictName.trim();
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue == null ? null : dictValue.trim();
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum == null ? null : orderNum.trim();
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId == null ? null : updateUserId.trim();
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

	@Override
	public String toString() {
		return "SysDict [dictId=" + dictId + ", pId=" + pId + ", dictName=" + dictName + ", dictValue=" + dictValue
				+ ", orderNum=" + orderNum + ", updateTime=" + updateTime + ", updateUser=" + updateUser
				+ ", description=" + description + ", updateUserId=" + updateUserId + "]";
	}

}