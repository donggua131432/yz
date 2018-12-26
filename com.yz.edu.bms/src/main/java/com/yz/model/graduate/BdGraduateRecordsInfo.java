package com.yz.model.graduate;

import java.io.Serializable;

/**
 * 毕业审核记录
 * @author lx
 * @date 2017年7月13日 上午9:33:03
 */
public class BdGraduateRecordsInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1757621921681456619L;
	
	private String checkId;
	private String graduateId;
	private String checkStatus;
	private String gCheckType;
	private String checkTime;
	private String checkUserName;
	private String checkUserId;
	private String remark;
	public String getCheckId() {
		return checkId;
	}
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	public String getGraduateId() {
		return graduateId;
	}
	public void setGraduateId(String graduateId) {
		this.graduateId = graduateId;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getgCheckType() {
		return gCheckType;
	}
	public void setgCheckType(String gCheckType) {
		this.gCheckType = gCheckType;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckUserName() {
		return checkUserName;
	}
	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}
	public String getCheckUserId() {
		return checkUserId;
	}
	public void setCheckUserId(String checkUserId) {
		this.checkUserId = checkUserId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
