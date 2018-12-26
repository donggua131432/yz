package com.yz.model.recruit;

import com.yz.model.common.PubInfo;

public class BdStudentOther extends PubInfo {

	private String stdId;
	private String headPortrait;
	private String isPhotoChange;
	private String telephone;
	private String email;
	private String qq;
	private String wechat;
	private String jobType;
	private String jobTitle;
	private String maritalStatus;
	private String workPlace;
	private String remark;
	
	private Object headPic;
	
	public String getStdId() {
		return stdId;
	}
	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait == null ? null : headPortrait.trim();
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone == null ? null : telephone.trim();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq == null ? null : qq.trim();
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat == null ? null : wechat.trim();
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType == null ? null : jobType.trim();
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle == null ? null : jobTitle.trim();
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus == null ? null : maritalStatus.trim();
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace == null ? null : workPlace.trim();
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}
	public Object getHeadPic() {
		return headPic;
	}
	public void setHeadPic(Object headPic) {
		this.headPic = headPic;
	}
	public String getIsPhotoChange() {
		return isPhotoChange;
	}
	public void setIsPhotoChange(String isPhotoChange) {
		this.isPhotoChange = isPhotoChange;
	}
	
}
