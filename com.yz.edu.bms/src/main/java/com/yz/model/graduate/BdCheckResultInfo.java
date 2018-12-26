package com.yz.model.graduate;

import java.io.Serializable;

/**
 * 审核结果
 * @author lx
 * @date 2017年7月14日 下午7:24:41
 */
public class BdCheckResultInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8755113619216409224L;
	
	private String checkId;
	private String checkStatus;
	private String checkUserName;
	private String checkTime;
	private String remark;
	
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCheckUserName() {
		return checkUserName;
	}
	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}
	public String getCheckId() {
		return checkId;
	}
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
}	
