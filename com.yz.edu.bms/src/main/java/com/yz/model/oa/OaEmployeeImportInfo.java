package com.yz.model.oa;

import java.io.Serializable;

import com.yz.util.ExcelUtil;

/**
 * 导入信息
 * @author lx
 * @date 2017年7月18日 下午6:20:42
 */
public class OaEmployeeImportInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1381605700540217763L;
	private String empId;
	private String empName;              //员工名称
	private String empType;             //员工类型
	private String idType;               //证件类型
	private String idCard;               //证件号
	private String sex;                  //性别
	private String age;                  //年龄
	private String nation;				 //民族
	private String birthday;			 //出生年月日
	private String address;				//联系地址
	private String hourFee;              //每课时费用
	private String otherFee;             //其他费用
	private String mobile;               //手机号
	private String finishSchool;		 //毕业院校
	private String finishMajor;			 //毕业专业
	private String finishTime;			 //毕业时间
	private String jobTitle;			 //职称
	private String degree;				 //学位
	private String professionalTime;	 //职称评定时间
	private String campusId;			 //所属校区
	private String campusName;				
	private String teach;				 //任教学课
	private String bankCard;			 //银行账号
	private String email;				 //邮箱
	private String teachCertType;		 //教师资格证种类
	private String teachCertNo;			 //教师资格证书号
	private String workPlace;			 //工作单位
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHourFee() {
		return hourFee;
	}
	public void setHourFee(String hourFee) {
		this.hourFee = hourFee;
	}
	public String getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFinishSchool() {
		return finishSchool;
	}
	public void setFinishSchool(String finishSchool) {
		this.finishSchool = finishSchool;
	}
	public String getFinishMajor() {
		return finishMajor;
	}
	public void setFinishMajor(String finishMajor) {
		this.finishMajor = finishMajor;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getProfessionalTime() {
		return professionalTime;
	}
	public void setProfessionalTime(String professionalTime) {
		this.professionalTime = professionalTime;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getTeach() {
		return teach;
	}
	public void setTeach(String teach) {
		this.teach = teach;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTeachCertType() {
		return teachCertType;
	}
	public void setTeachCertType(String teachCertType) {
		this.teachCertType = teachCertType;
	}
	public String getTeachCertNo() {
		return teachCertNo;
	}
	public void setTeachCertNo(String teachCertNo) {
		this.teachCertNo = teachCertNo;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
	

}
