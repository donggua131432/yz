package com.yz.model.educational;

import java.util.List;

import com.yz.model.common.PubInfo;

public class BdExamYear extends PubInfo {
	private String eyId;

	private String examYear;

	private String status;

	private String updateTime;

	private String updateUser;

	private String updateUserId;

	private String createUserId;

	private String createTime;

	private String createUser;

	private String tips;

	private List<BdExamReason> reasons;

	private List<BdYearSubject> subjects;

	public List<BdYearSubject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<BdYearSubject> subjects) {
		this.subjects = subjects;
	}

	public List<BdExamReason> getReasons() {
		return reasons;
	}

	public void setReasons(List<BdExamReason> reasons) {
		this.reasons = reasons;
	}

	public String getEyId() {
		return eyId;
	}

	public void setEyId(String eyId) {
		this.eyId = eyId == null ? null : eyId.trim();
	}

	public String getExamYear() {
		return examYear;
	}

	public void setExamYear(String examYear) {
		this.examYear = examYear == null ? null : examYear.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
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

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips == null ? null : tips.trim();
	}
}