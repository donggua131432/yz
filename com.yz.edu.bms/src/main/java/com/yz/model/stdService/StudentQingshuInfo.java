package com.yz.model.stdService;

import java.io.Serializable;
import java.util.List;

/**
 * 新生学籍资料收集
 * @author jyt
 */
public class StudentQingshuInfo implements Serializable
{
	private String qingshuId;
	private String learnId;
	private String taskId;
	private String stdName;
	private String grade;
	private String unvsName;
	private String pfsnName;
	private String pfsnLevel;
	private String pfsnCode;
	private String schoolRoll;
	private String taskTitle;
	private String confirmStatus;
	private String tutor;
	private String remark;
	private String idCard;
	private String unvsId;

	private List<StudentQingshuCourse> firstCourse;

	private List<StudentQingshuCourse> secondCourse;

	private List<StudentQingshuCourse> thirdCourse;

	private List<StudentQingshuCourse> fourCourse;

	private List<StudentQingshuCourse> fiveCourse;

	private List<StudentQingshuCourse> sixCourse;

	public String getQingshuId() {
		return qingshuId;
	}

	public void setQingshuId(String qingshuId) {
		this.qingshuId = qingshuId;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getPfsnCode() {
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}

	public String getSchoolRoll() {
		return schoolRoll;
	}

	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public String getTutor() {
		return tutor;
	}

	public void setTutor(String tutor) {
		this.tutor = tutor;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}

	public List<StudentQingshuCourse> getFirstCourse() {
		return firstCourse;
	}

	public void setFirstCourse(List<StudentQingshuCourse> firstCourse) {
		this.firstCourse = firstCourse;
	}

	public List<StudentQingshuCourse> getSecondCourse() {
		return secondCourse;
	}

	public void setSecondCourse(List<StudentQingshuCourse> secondCourse) {
		this.secondCourse = secondCourse;
	}

	public List<StudentQingshuCourse> getThirdCourse() {
		return thirdCourse;
	}

	public void setThirdCourse(List<StudentQingshuCourse> thirdCourse) {
		this.thirdCourse = thirdCourse;
	}

	public List<StudentQingshuCourse> getFourCourse() {
		return fourCourse;
	}

	public void setFourCourse(List<StudentQingshuCourse> fourCourse) {
		this.fourCourse = fourCourse;
	}

	public List<StudentQingshuCourse> getFiveCourse() {
		return fiveCourse;
	}

	public void setFiveCourse(List<StudentQingshuCourse> fiveCourse) {
		this.fiveCourse = fiveCourse;
	}

	public List<StudentQingshuCourse> getSixCourse() {
		return sixCourse;
	}

	public void setSixCourse(List<StudentQingshuCourse> sixCourse) {
		this.sixCourse = sixCourse;
	}
}
