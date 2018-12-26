package com.yz.model.stdService;

/**
 * 学员联系信息
 * @author Administrator
 *
 */
public class StudyingContacts {

	private String stdId;
	private String learnId;
	private String mobile;
	private String emergencyContact;
	private String telephone;
	private String email;
	private String qq;
	private String wechat;
	private String workPlace;
	private Object headPic;
	private String isPhotoChange;
	private String headPortrait;
	
	public String getStdId() {
		return stdId;
	}
	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}
	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact == null ? null : emergencyContact.trim();
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
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace == null ? null : workPlace.trim();
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
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
}
