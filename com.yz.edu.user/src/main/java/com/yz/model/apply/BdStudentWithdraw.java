package com.yz.model.apply;

import java.io.Serializable;
import java.util.Date;

public class BdStudentWithdraw implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 969094933071512826L;

	private String withdrawId;

	private String stdId;

	private String amount;

	private Date applyTime;

	private String checkStatus;

	private String empId;

	private String bankCard;

	private String bankType;

	private String remark;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	private String bankOpen;
	private String provinceCode;
	private String cityCode;
	
	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getWithdrawId() {
		return withdrawId;
	}

	public void setWithdrawId(String withdrawId) {
		this.withdrawId = withdrawId == null ? null : withdrawId.trim();
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount == null ? null : amount.trim();
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus == null ? null : checkStatus.trim();
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId == null ? null : empId.trim();
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

	public String getBankOpen()
	{
		return bankOpen;
	}

	public void setBankOpen(String bankOpen)
	{
		this.bankOpen = bankOpen;
	}

	public String getProvinceCode()
	{
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode)
	{
		this.provinceCode = provinceCode;
	}

	public String getCityCode()
	{
		return cityCode;
	}

	public void setCityCode(String cityCode)
	{
		this.cityCode = cityCode;
	}
}