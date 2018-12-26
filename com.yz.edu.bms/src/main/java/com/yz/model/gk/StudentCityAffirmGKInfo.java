package com.yz.model.gk;

import java.io.Serializable;

/**
 * 国开考场城市确认
 * @author juliet
 * @date 2018-05-02 上午10:44:55
 */
public class StudentCityAffirmGKInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8689493393651009781L;
	
	private String affirmId;
	private String taskId;
    private String eyId;
    private String examYear;//考场年度名称
    private String ecId;
    private String ecName;//考场城市名称
    private String learnId;
    private String schoolRoll;
    private String stdNo;
    private String stdName;
    private String idCard;
    private String grade;
    private String unvsName;
    private String pfsnCode;
    private String pfsnName;
    private String pfsnLevel;
    private String stdStage;
    private String isView;
    private String viewTime;
    private String tutor;
    private String isReset;
    private String isExam;
    private String isAffirm;
    private String reason;
    private String taskTitle;
    private String number; //人数
	public String getAffirmId() {
		return affirmId;
	}
	public void setAffirmId(String affirmId) {
		this.affirmId = affirmId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getEyId() {
		return eyId;
	}
	public void setEyId(String eyId) {
		this.eyId = eyId;
	}
	public String getEcId() {
		return ecId;
	}
	public void setEcId(String ecId) {
		this.ecId = ecId;
	}
	public String getIsExam() {
		return isExam;
	}
	public void setIsExam(String isExam) {
		this.isExam = isExam;
	}
	public String getLearnId() {
		return learnId;
	}
	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}
	public String getSchoolRoll() {
		return schoolRoll;
	}
	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll;
	}
	public String getStdNo() {
		return stdNo;
	}
	public void setStdNo(String stdNo) {
		this.stdNo = stdNo;
	}
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
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
	public String getPfsnCode() {
		return pfsnCode;
	}
	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
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
	public String getStdStage() {
		return stdStage;
	}
	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}
	public String getIsView() {
		return isView;
	}
	public void setIsView(String isView) {
		this.isView = isView;
	}
	public String getViewTime() {
		return viewTime;
	}
	public void setViewTime(String viewTime) {
		this.viewTime = viewTime;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}
	public String getIsReset() {
		return isReset;
	}
	public void setIsReset(String isReset) {
		this.isReset = isReset;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	
	public String getIsAffirm() {
		return isAffirm;
	}
	public void setIsAffirm(String isAffirm) {
		this.isAffirm = isAffirm;
	}
	public String getExamYear() {
		return examYear;
	}
	public void setExamYear(String examYear) {
		this.examYear = examYear;
	}
	public String getEcName() {
		return ecName;
	}
	public void setEcName(String ecName) {
		this.ecName = ecName;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
    
    
}
