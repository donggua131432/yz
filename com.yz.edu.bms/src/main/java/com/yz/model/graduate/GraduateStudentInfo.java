package com.yz.model.graduate;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

/**
 * 毕业服务-毕业发起学员信息
 * @author lx
 * @date 2017年7月12日 下午12:09:47
 */
public class GraduateStudentInfo extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4640394888721590802L;
	
	private String graduateId;         //毕业记录id
	private String stdId;              //学员id
	private String grade;              //年级
	private String learnId;            //学业编号
	private String stdStage;           //学员状态
	private String pfsnName;           //专业名称
	private String pfsnCode;           //专业代码
	private String pfsnLevel;          //专业层次
	private String unvsName;           //院校名称
	
	public String getGraduateId() {
		return graduateId;
	}
	public void setGraduateId(String graduateId) {
		this.graduateId = graduateId;
	}
	
	public String getStdId() {
		return stdId;
	}
	public void setStdId(String stdId) {
		this.stdId = stdId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId;
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
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

}
