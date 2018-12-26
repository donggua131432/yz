package com.yz.model.gk;

import java.io.Serializable;
import java.util.List;

import com.yz.model.finance.stdfee.BdStdPayInfoResponse;

/**
 * 国开本科统考跟进
 * @author juliet
 * @date 2018-06-06 上午10:44:55
 */
public class StudentGraduateExamGKInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8689493393651009781L;
	
	private String followId;
	private String taskId;
    private String notification; //通知情况
    private String remark;
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
    private String taskTitle;  
    private List<StudentGraduateExamGKInfoSub> subinfos;
    
	public String getFollowId() {
		return followId;
	}
	public void setFollowId(String followId) {
		this.followId = followId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public List<StudentGraduateExamGKInfoSub> getSubinfos() {
		return subinfos;
	}
	public void setSubinfos(List<StudentGraduateExamGKInfoSub> subinfos) {
		this.subinfos = subinfos;
	}
	
	
    
    
}
