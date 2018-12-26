package com.yz.model.transfer;

import java.util.Date;
import java.util.Map;

public class BdStudentChange {
	private String changeId;

	private String newLearnId;

	private String learnId;

	private String stdId;

	private String newUnvsId;

	private String newTaId;

	private String newPfsnId;

	private String newScholarship;

	private String reason;

	private String checkOrder;

	private String checkType;

	private String stdStage;

	private String isComplete;

	private String updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private String createTime;

	private String createUser;

	private String ext1;

	private String ext2;

	private String ext3;

	private Map<String, String> old;

	private Map<String, String> prevent;

	private String stdName;

	public Map<String, String> getPrevent() {
		return prevent;
	}

	public void setPrevent(Map<String, String> prevent) {
		this.prevent = prevent;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public Map<String, String> getOld() {
		return old;
	}

	public void setOld(Map<String, String> old) {
		this.old = old;
	}

	public String getChangeId() {
		return changeId;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId == null ? null : changeId.trim();
	}

	public String getNewLearnId() {
		return newLearnId;
	}

	public void setNewLearnId(String newLearnId) {
		this.newLearnId = newLearnId == null ? null : newLearnId.trim();
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getNewUnvsId() {
		return newUnvsId;
	}

	public void setNewUnvsId(String newUnvsId) {
		this.newUnvsId = newUnvsId == null ? null : newUnvsId.trim();
	}

	public String getNewTaId() {
		return newTaId;
	}

	public void setNewTaId(String newTaId) {
		this.newTaId = newTaId == null ? null : newTaId.trim();
	}

	public String getNewPfsnId() {
		return newPfsnId;
	}

	public void setNewPfsnId(String newPfsnId) {
		this.newPfsnId = newPfsnId == null ? null : newPfsnId.trim();
	}

	public String getNewScholarship() {
		return newScholarship;
	}

	public void setNewScholarship(String newScholarship) {
		this.newScholarship = newScholarship == null ? null : newScholarship.trim();
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}

	public String getCheckOrder() {
		return checkOrder;
	}

	public void setCheckOrder(String checkOrder) {
		this.checkOrder = checkOrder == null ? null : checkOrder.trim();
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType == null ? null : checkType.trim();
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete == null ? null : isComplete.trim();
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

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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