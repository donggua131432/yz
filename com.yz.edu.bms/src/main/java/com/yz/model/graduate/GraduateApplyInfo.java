package com.yz.model.graduate;

import java.io.Serializable;

/**
 * 毕业申请
 * @author lx
 * @date 2017年7月12日 下午3:10:02
 */
public class GraduateApplyInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4884880805889012626L;
	
	private String graduateId;     //毕业id 
	private String stdId;          //学员id
	private String learnId;        //学业id
	private String stdName;        //学员名字
	private String grade;          //年级
	private String pfsnName;       //专业名
	private String pfsnCode;       //专业代码
	private String pfsnLevel;      //专业层次
	private String unvsName;       //学院名称
	private String recruitType;    //招生类型
	private String stdStage;       //学员状态
	private String tutor;          //辅导员
	private String isRegister;     //是否注册(判断是否有学籍的依据)
	
	private String status;         //毕业发起的核准状态
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
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
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
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}
	public String getUnvsName() {
		return unvsName;
	}
	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}
	public String getIsRegister() {
		return isRegister;
	}
	public void setIsRegister(String isRegister) {
		this.isRegister = isRegister;
	}
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}
	public String getGraduateId() {
		return graduateId;
	}
	public void setGraduateId(String graduateId) {
		this.graduateId = graduateId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
