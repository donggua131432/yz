package com.yz.model.stdService;

import java.io.Serializable;

/**
 * 学员开课通知
 * @author lx
 * @date 2018年3月12日 上午10:44:55
 */
public class StudentLectureNoticeInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8689493393651009781L;
	
	private String lectureId;
	private String taskId;
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
    private String recruitType;
    private String isReceiveBook; //是否收到教材
    private String isKnowTimetables;  //是否知道课程表查看方式 
    private String isKnowCourseType;  //是否知道上课方式
    private String updateTime;
    private String tutor;
    private String logisticsNo;//快递单号
    private String isView;
    private String viewTime;
    private String isReset; 
    private String remark;
    private String submitTime;
    private String taskTitle;
    private String isRemark;
    private String unvsId;
    private String pfsnId;
    
	public String getLectureId() {
		return lectureId;
	}
	public void setLectureId(String lectureId) {
		this.lectureId = lectureId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
	public String getIsReceiveBook() {
		return isReceiveBook;
	}
	public void setIsReceiveBook(String isReceiveBook) {
		this.isReceiveBook = isReceiveBook;
	}
	public String getIsKnowTimetables() {
		return isKnowTimetables;
	}
	public void setIsKnowTimetables(String isKnowTimetables) {
		this.isKnowTimetables = isKnowTimetables;
	}
	public String getIsKnowCourseType() {
		return isKnowCourseType;
	}
	public void setIsKnowCourseType(String isKnowCourseType) {
		this.isKnowCourseType = isKnowCourseType;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
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
	public String getIsReset() {
		return isReset;
	}
	public void setIsReset(String isReset) {
		this.isReset = isReset;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}
	public String getLogisticsNo() {
		return logisticsNo;
	}
	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}
	public String getRecruitType() {
		return recruitType;
	}
	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}
	
	public String getIsRemark() {
		return isRemark;
	}
	public void setIsRemark(String isRemark) {
		this.isRemark = isRemark;
	}
	public String getUnvsId() {
		return unvsId;
	}
	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}
	public String getPfsnId() {
		return pfsnId;
	}
	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
    
    
    
	
}
