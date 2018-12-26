package com.yz.model.graduate;

import java.io.Serializable;

/**
 * 毕业审核-基础显示信息
 * @author lx
 * @date 2017年7月13日 下午2:27:23
 */
public class CheckDataBaseInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3565316910054555747L;
	
	private String checkId;
	private String stdId;
	private String grade;
	private String learnId;
	private String stdName;
	private String remark;
	private String checkStatus;
	private String recruitType;
	private String unvsName;
	private String pfsnLevel;
	private String pfsnName;
	private String pfsnCode;
	private String tutor;
	
	private String ifAffirmOrPass;  //图像的确认或者论文的是否通过
	
	private String status;         //是否被核准,核准后不可再审核
	
	public String getCheckId() {
		return checkId;
	}
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	public String getStdId() {
		return stdId;
	}
	public void setStdId(String stdId) {
		this.stdId = stdId;
	}
	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}
	public String getUnvsName() {
		return unvsName;
	}
	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}
	public String getPfsnName() {
		return pfsnName;
	}
	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}
	public String getPfsnCode() {
		return pfsnCode;
	}
	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}
	public String getIfAffirmOrPass() {
		return ifAffirmOrPass;
	}
	public void setIfAffirmOrPass(String ifAffirmOrPass) {
		this.ifAffirmOrPass = ifAffirmOrPass;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
