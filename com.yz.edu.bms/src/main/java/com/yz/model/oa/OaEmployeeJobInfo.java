package com.yz.model.oa;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 员工部门
 * 
 * @author lx
 * @date 2017年7月6日 下午6:35:26
 */
public class OaEmployeeJobInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 248287371866198337L;

	private String empId; // 员工id
	private String yzId; // 远智id
	private String campusId; // 校区id
	private String dpId; // 部门id
	private String groupId; // 组id
	private String recruitCode; // 招生编码
	private String entryDate; // 入职日期
	private String leaveDate; // 离职日期
	private String isCheck; // 是否审核
	private String pactStartTime; // 合同开始日期
	private String pactEndTime; // 合同结束
	private String empStatus; // 状态
	private String turnRightTime; // 转正日期
	private String effectTime; // 修改生效时间
	private String modifyId;

	private String ifNeedRelate; // 是否需要关联

	private String[] jdIds = new String[] {}; // 员工职称
	private List<Map<String, String>> dpJdIdList; // 员工所属部门下的所有职称

	// 操作人
	private String userRealName;
	private String userId;

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmpId() {
		return empId;
	}

	public String getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(String effectTime) {
		this.effectTime = effectTime;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getYzId() {
		return yzId;
	}

	public void setYzId(String yzId) {
		this.yzId = yzId;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRecruitCode() {
		return recruitCode;
	}

	public void setRecruitCode(String recruitCode) {
		this.recruitCode = recruitCode;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getPactStartTime() {
		return pactStartTime;
	}

	public void setPactStartTime(String pactStartTime) {
		this.pactStartTime = pactStartTime;
	}

	public String getPactEndTime() {
		return pactEndTime;
	}

	public void setPactEndTime(String pactEndTime) {
		this.pactEndTime = pactEndTime;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}

	public String getIfNeedRelate() {
		return ifNeedRelate;
	}

	public void setIfNeedRelate(String ifNeedRelate) {
		this.ifNeedRelate = ifNeedRelate;
	}

	public String getTurnRightTime() {
		return turnRightTime;
	}

	public void setTurnRightTime(String turnRightTime) {
		this.turnRightTime = turnRightTime;
	}

	public String[] getJdIds() {
		return jdIds;
	}

	public void setJdIds(String[] jdIds) {
		this.jdIds = jdIds;
	}

	public List<Map<String, String>> getDpJdIdList() {
		return dpJdIdList;
	}

	public void setDpJdIdList(List<Map<String, String>> dpJdIdList) {
		this.dpJdIdList = dpJdIdList;
	}

}
