package com.yz.model.oa;


import com.yz.model.common.PubInfo;

/**
 *  员工基础信息
 * @author lx
 * @date 2017年7月5日 上午9:18:11
 */
public class OaEmployeeBaseInfo extends PubInfo {

	private String empId;                //员工id 
	private String empName;              //员工名称
	private String empType;             //员工类型
	private String empStatus;           //员工状态
	private String idType;               //证件类型
	private String idCard;               //证件号
	private String sex;                  //性别
	private String birthday;             //生日
	private String nation;               //名族
	private String politicalStatus;      //政治面貌
	private String rprType;              //户口类型
	private String rprProvinceCode;      //户口所在地(省)
	private String rprCityCode;          //户口所在地(市)
	private String rprDistrictCode;      //户口所在地(区)
	private String provinceCode;         //现居住地(省)
	private String cityCode;             //现居住地(市)
	private String districtCode;         //现居住地(区)
	private String address;              //地址
	private String mobile;               //手机
	private String emergencyContact;     //紧急联系人
	private String emergencyMobile;      //紧急联系人电话
	private String education;            //最高学历
	private String empSource;            //员工来源
	private String userId;				 //用户Id
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getPoliticalStatus() {
		return politicalStatus;
	}
	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}
	public String getRprType() {
		return rprType;
	}
	public void setRprType(String rprType) {
		this.rprType = rprType;
	}
	public String getRprProvinceCode() {
		return rprProvinceCode;
	}
	public void setRprProvinceCode(String rprProvinceCode) {
		this.rprProvinceCode = rprProvinceCode;
	}
	public String getRprCityCode() {
		return rprCityCode;
	}
	public void setRprCityCode(String rprCityCode) {
		this.rprCityCode = rprCityCode;
	}
	public String getRprDistrictCode() {
		return rprDistrictCode;
	}
	public void setRprDistrictCode(String rprDistrictCode) {
		this.rprDistrictCode = rprDistrictCode;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmergencyContact() {
		return emergencyContact;
	}
	
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	public String getEmergencyMobile() {
		return emergencyMobile;
	}
	public void setEmergencyMobile(String emergencyMobile) {
		this.emergencyMobile = emergencyMobile;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	/**
	 * @return the empStatus
	 */
	public String getEmpStatus() {
		return empStatus;
	}
	/**
	 * @param empStatus the empStatus to set
	 */
	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
	public String getEmpSource() {
		return empSource;
	}
	public void setEmpSource(String empSource) {
		this.empSource = empSource;
	}
	
	
}
