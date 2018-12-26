package com.yz.model.oa;

import java.io.Serializable;

/**
 * 员工变更记录
 * 
 * @author lx
 * @date 2017年11月6日 下午6:17:20
 */
public class EmpModifyListInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8698938805708652283L;

	private String modifyId;
	private String oldCampusName;
	private String oldDpName;
	private String oldGroupName;
	private String newCampusName;
	private String newDpName;
	private String newGroupName;
	private String oldTitle;
	private String newTitle;
	private String createUser;
	private String createTime;

	private String modifyText;

	private String empName;
	private String oldDpId;
	private String oldCampusId;
	private String newCampusId;
	private String newDpId;
	private String empStatus;
	private String status;
	private String effectTime;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(String effectTime) {
		this.effectTime = effectTime;
	}

	public String getModifyId() {
		return modifyId;
	}

	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}

	public String getOldCampusName() {
		return oldCampusName;
	}

	public void setOldCampusName(String oldCampusName) {
		this.oldCampusName = oldCampusName;
	}

	public String getOldDpName() {
		return oldDpName;
	}

	public void setOldDpName(String oldDpName) {
		this.oldDpName = oldDpName;
	}

	public String getOldGroupName() {
		return oldGroupName;
	}

	public void setOldGroupName(String oldGroupName) {
		this.oldGroupName = oldGroupName;
	}

	public String getNewCampusName() {
		return newCampusName;
	}

	public void setNewCampusName(String newCampusName) {
		this.newCampusName = newCampusName;
	}

	public String getNewDpName() {
		return newDpName;
	}

	public void setNewDpName(String newDpName) {
		this.newDpName = newDpName;
	}

	public String getNewGroupName() {
		return newGroupName;
	}

	public void setNewGroupName(String newGroupName) {
		this.newGroupName = newGroupName;
	}

	public String getOldTitle() {
		return oldTitle;
	}

	public void setOldTitle(String oldTitle) {
		this.oldTitle = oldTitle;
	}

	public String getNewTitle() {
		return newTitle;
	}

	public void setNewTitle(String newTitle) {
		this.newTitle = newTitle;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyText() {
		return modifyText;
	}

	public void setModifyText(String modifyText) {
		this.modifyText = modifyText;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getOldDpId() {
		return oldDpId;
	}

	public void setOldDpId(String oldDpId) {
		this.oldDpId = oldDpId;
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

	public String getOldCampusId() {
		return oldCampusId;
	}

	public void setOldCampusId(String oldCampusId) {
		this.oldCampusId = oldCampusId;
	}

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
}
