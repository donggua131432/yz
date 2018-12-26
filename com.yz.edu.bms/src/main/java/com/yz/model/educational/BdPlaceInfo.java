package com.yz.model.educational;

import java.util.Date;

public class BdPlaceInfo {
	private String placeId;

	private String placeName;

	private String orderNum;

	private String maxCount;

	private String minCount;

	private String epId;

	private String pyId;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private String createTime;

	private String createUser;

	private String ext1;

	private String ext2;

	private String ext3;

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getPyId() {
		return pyId;
	}

	public void setPyId(String pyId) {
		this.pyId = pyId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId == null ? null : placeId.trim();
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName == null ? null : placeName.trim();
	}

	public String getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(String maxCount) {
		this.maxCount = maxCount == null ? null : maxCount.trim();
	}

	public String getMinCount() {
		return minCount;
	}

	public void setMinCount(String minCount) {
		this.minCount = minCount == null ? null : minCount.trim();
	}

	public String getEpId() {
		return epId;
	}

	public void setEpId(String epId) {
		this.epId = epId == null ? null : epId.trim();
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