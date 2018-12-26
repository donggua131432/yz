package com.yz.model.oa;

import com.yz.model.common.PubInfo;

/**
 * 员工部门信息变更记录
 * 
 * @author lx
 * @date 2017年11月6日 下午2:21:31
 */
public class OaEmployeeModifyInfo extends PubInfo {

	private String modifyId;
	private String empId;
	private String oldCampusId;
	private String oldDpId;
	private String oldGroupId;
	private String oldRecruitCode;
	private String oldTitle;
	private String newCampusId;
	private String newDpId;
	private String newGroupId;
	private String newRecruitCode;
	private String newTitle;
	private String beginTime;
	private String endTime;
	private String effectTime;
	private String status;
	private String isDelete;
	private String jobInfo;

	public String getJobInfo() {
		return jobInfo;
	}

	public void setJobInfo(String jobInfo) {
		this.jobInfo = jobInfo;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(String effectTime) {
		this.effectTime = effectTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getOldCampusId() {
		return oldCampusId;
	}

	public void setOldCampusId(String oldCampusId) {
		this.oldCampusId = oldCampusId;
	}

	public String getOldDpId() {
		return oldDpId;
	}

	public void setOldDpId(String oldDpId) {
		this.oldDpId = oldDpId;
	}

	public String getOldGroupId() {
		return oldGroupId;
	}

	public void setOldGroupId(String oldGroupId) {
		this.oldGroupId = oldGroupId;
	}

	public String getOldRecruitCode() {
		return oldRecruitCode;
	}

	public void setOldRecruitCode(String oldRecruitCode) {
		this.oldRecruitCode = oldRecruitCode;
	}

	public String getOldTitle() {
		return oldTitle;
	}

	public void setOldTitle(String oldTitle) {
		this.oldTitle = oldTitle;
	}

	public String getNewCampusId() {
		return newCampusId;
	}

	public void setNewCampusId(String newCampusId) {
		this.newCampusId = newCampusId;
	}

	public String getNewDpId() {
		return newDpId;
	}

	public void setNewDpId(String newDpId) {
		this.newDpId = newDpId;
	}

	public String getNewGroupId() {
		return newGroupId;
	}

	public void setNewGroupId(String newGroupId) {
		this.newGroupId = newGroupId;
	}

	public String getNewRecruitCode() {
		return newRecruitCode;
	}

	public void setNewRecruitCode(String newRecruitCode) {
		this.newRecruitCode = newRecruitCode;
	}

	public String getNewTitle() {
		return newTitle;
	}

	public void setNewTitle(String newTitle) {
		this.newTitle = newTitle;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

}
