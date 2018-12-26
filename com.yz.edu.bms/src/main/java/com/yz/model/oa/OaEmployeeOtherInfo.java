package com.yz.model.oa;

import com.yz.model.common.PubInfo;

/**
 *  员工附属信息表
 * @author lx
 * @date 2017年7月5日 上午9:18:11
 */
public class OaEmployeeOtherInfo extends PubInfo {

	private String empId;                //员工id 
	private String telephone;            //员工手机号
	private String email;                //email
	private String qq;                   //qq
	private String wechat;               //微信
	private String finishSchool;         //毕业院校
	private String finishMajor;          //毕业专业
	private String finishTime;           //毕业时间
	private String maritalStatus;        //婚姻状况
	private String nssfTime;             //购买社保时间
	private String englishLevel;         //英语水平
	private String computerLevel;        //计算机水平
	private String remark;               //备注
	private String finishCode;           //毕业证编码
	
	private String headPortrait;         //头像
	private String isPhotoChange;        //是否修改
	
	private Object headPic;
	
	//针对教师
	private String age;                  //年龄
	private String degree;               //学位
	private String workPlace;            //工作地点
	private String hourFee;              //每课时费用
	private String otherFee;             //其他费用
	private String jobTitle;            //职称
	private String teachCertType;      //教师资格证
	private String bankCard;            //银行卡账号
	private String teach;                //任教学科
	private String teachIdea;           //教学点意见
	private String teachEote;           //继续教育学院意见
	
	private String campusId;            //分配校区
	private String teachEducation;     //学历
	private String professionalTime;   //职称评定时间
	private String teachCertNo;      //教师资格证书号
	
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
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
		this.finishTime = finishTime == null ? null : finishTime.trim();
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getNssfTime() {
		return nssfTime;
	}
	public void setNssfTime(String nssfTime) {
		this.nssfTime = nssfTime;
	}
	public String getEnglishLevel() {
		return englishLevel;
	}
	public void setEnglishLevel(String englishLevel) {
		this.englishLevel = englishLevel;
	}
	public String getComputerLevel() {
		return computerLevel;
	}
	public void setComputerLevel(String computerLevel) {
		this.computerLevel = computerLevel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	public String getIsPhotoChange() {
		return isPhotoChange;
	}
	public void setIsPhotoChange(String isPhotoChange) {
		this.isPhotoChange = isPhotoChange;
	}
	public Object getHeadPic() {
		return headPic;
	}
	public void setHeadPic(Object headPic) {
		this.headPic = headPic;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
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
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getTeachCertType() {
		return teachCertType;
	}
	public void setTeachCertType(String teachCertType) {
		this.teachCertType = teachCertType;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getTeach() {
		return teach;
	}
	public void setTeach(String teach) {
		this.teach = teach;
	}
	public String getTeachIdea() {
		return teachIdea;
	}
	public void setTeachIdea(String teachIdea) {
		this.teachIdea = teachIdea;
	}
	public String getTeachEote() {
		return teachEote;
	}
	public void setTeachEote(String teachEote) {
		this.teachEote = teachEote;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getFinishCode()
	{
		return finishCode;
	}
	public void setFinishCode(String finishCode)
	{
		this.finishCode = finishCode;
	}

	public String getTeachEducation() {
		return teachEducation;
	}

	public void setTeachEducation(String teachEducation) {
		this.teachEducation = teachEducation;
	}

	public String getProfessionalTime() {
		return professionalTime;
	}

	public void setProfessionalTime(String professionalTime) {
		this.professionalTime = professionalTime;
	}
	public String getTeachCertNo() {
		return teachCertNo;
	}
	public void setTeachCertNo(String teachCertNo) {
		this.teachCertNo = teachCertNo;
	}
	
}
