package com.yz.model.recruit;

import com.yz.model.common.PubInfo;

/**
 * 学员信息编辑实体
 * 
 * @author Administrator
 *
 */
public class BdStudentBaseInfo extends PubInfo {

	private String stdId;
	private String stdName;
	private String idType;
	private String idCard;
	private String s_idCard;
	private String sex;
	private String birthday;
	private String nation;
	private String politicalStatus;
	private String rprType;
	private String rprProvinceCode;
	private String rprCityCode;
	private String rprDistrictCode;
	private String zipCode;
	private String nowProvinceCode;
	private String nowCityCode;
	private String nowDistrictCode;
	private String address;
	private String mobile;
	private String s_mobile;
	private String emergencyContact;
	private String edcsType;
	private String jobType;
	private String jobStatus;
	private String wpProvinceCode;
	private String wpCityCode;
	private String wpDistrictCode;
	private String wpAddress;
	private String wpTelephone;
	private String wpTime;

	private String learnId;

	private boolean isNew;
	
	private String myAnnexStatus;
	
	private String stdSource; //学员来源 : D:钉钉引用 X学员系统 W:微信 A:app

	private String maritalStatus;
	
	private String userId;  //关联的用户id
	
	private String addressEditTime;  //辅导教材修改的时间
	private String grade;            //年级

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName == null ? null : stdName.trim();
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType == null ? null : idType.trim();
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard == null ? null : idCard.trim();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday == null ? null : birthday.trim();
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation == null ? null : nation.trim();
	}

	public String getPoliticalStatus() {
		return politicalStatus;
	}

	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus == null ? null : politicalStatus.trim();
	}

	public String getRprType() {
		return rprType;
	}

	public void setRprType(String rprType) {
		this.rprType = rprType == null ? null : rprType.trim();
	}

	public String getRprProvinceCode() {
		return rprProvinceCode;
	}

	public void setRprProvinceCode(String rprProvinceCode) {
		this.rprProvinceCode = rprProvinceCode == null ? null : rprProvinceCode.trim();
	}

	public String getRprCityCode() {
		return rprCityCode;
	}

	public void setRprCityCode(String rprCityCode) {
		this.rprCityCode = rprCityCode == null ? null : rprCityCode.trim();
	}

	public String getRprDistrictCode() {
		return rprDistrictCode;
	}

	public void setRprDistrictCode(String rprDistrictCode) {
		this.rprDistrictCode = rprDistrictCode == null ? null : rprDistrictCode.trim();
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode == null ? null : zipCode.trim();
	}

	public String getNowProvinceCode() {
		return nowProvinceCode;
	}

	public void setNowProvinceCode(String nowProvinceCode) {
		this.nowProvinceCode = nowProvinceCode == null ? null : nowProvinceCode.trim();
	}

	public String getNowCityCode() {
		return nowCityCode;
	}

	public void setNowCityCode(String nowCityCode) {
		this.nowCityCode = nowCityCode == null ? null : nowCityCode.trim();
	}

	public String getNowDistrictCode() {
		return nowDistrictCode;
	}

	public void setNowDistrictCode(String nowDistrictCode) {
		this.nowDistrictCode = nowDistrictCode == null ? null : nowDistrictCode.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
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

	public String getEdcsType() {
		return edcsType;
	}

	public void setEdcsType(String edcsType) {
		this.edcsType = edcsType == null ? null : edcsType.trim();
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType == null ? null : jobType.trim();
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getWpProvinceCode() {
		return wpProvinceCode;
	}

	public void setWpProvinceCode(String wpProvinceCode) {
		this.wpProvinceCode = wpProvinceCode == null ? null : wpProvinceCode.trim();
	}

	public String getWpCityCode() {
		return wpCityCode;
	}

	public void setWpCityCode(String wpCityCode) {
		this.wpCityCode = wpCityCode == null ? null : wpCityCode.trim();
	}

	public String getWpDistrictCode() {
		return wpDistrictCode;
	}

	public void setWpDistrictCode(String wpDistrictCode) {
		this.wpDistrictCode = wpDistrictCode == null ? null : wpDistrictCode.trim();
	}

	public String getWpAddress() {
		return wpAddress;
	}

	public void setWpAddress(String wpAddress) {
		this.wpAddress = wpAddress == null ? null : wpAddress.trim();
	}

	public String getWpTelephone() {
		return wpTelephone;
	}

	public void setWpTelephone(String wpTelephone) {
		this.wpTelephone = wpTelephone == null ? null : wpTelephone.trim();
	}

	public String getWpTime() {
		return wpTime;
	}

	public void setWpTime(String wpTime) {
		this.wpTime = wpTime == null ? null : wpTime.trim();
	}

	/**
	 * @return the myAnnexStatus
	 */
	public String getMyAnnexStatus() {
		return myAnnexStatus;
	}

	/**
	 * @param myAnnexStatus the myAnnexStatus to set
	 */
	public void setMyAnnexStatus(String myAnnexStatus) {
		this.myAnnexStatus = myAnnexStatus;
	}

	public String getS_idCard() {
		return s_idCard;
	}

	public void setS_idCard(String s_idCard) {
		this.s_idCard = s_idCard;
	}

	public String getS_mobile() {
		return s_mobile;
	}

	public void setS_mobile(String s_mobile) {
		this.s_mobile = s_mobile;
	}

	public String getStdSource()
	{
		return stdSource;
	}

	public void setStdSource(String stdSource)
	{
		this.stdSource = stdSource;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAddressEditTime() {
		return addressEditTime;
	}

	public void setAddressEditTime(String addressEditTime) {
		this.addressEditTime = addressEditTime;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
}
