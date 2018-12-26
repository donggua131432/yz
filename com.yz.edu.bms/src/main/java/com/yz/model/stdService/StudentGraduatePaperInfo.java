package com.yz.model.stdService;

import java.io.Serializable;
import java.util.List;

/**
 * 毕业论文及报告
 * @author lx
 * @date 2018年3月12日 上午10:48:25
 */
public class StudentGraduatePaperInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2149290816184211280L;
	
	private String gpId;
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
	private String paperNo;
	private String guideTeacher;
	private String guideTeacherEmail;
	private String guideTeacherPhone;
	private String paperTitle;
	private String tutor;
	private String isUpload;
	private String checkStatus;
	private String remark;
	private String idCard;
	private String unvsId;
	private List<StudentAttachment> attachments;
	private String paperDataStatus;
	private String isView;
	private String schoolDepartment;

	public String getGpId()
	{
		return gpId;
	}
	public void setGpId(String gpId)
	{
		this.gpId = gpId;
	}
	public String getLearnId()
	{
		return learnId;
	}
	public void setLearnId(String learnId)
	{
		this.learnId = learnId;
	}
	public String getTaskId()
	{
		return taskId;
	}
	public void setTaskId(String taskId)
	{
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

	public String getPaperNo() {
		return paperNo;
	}

	public void setPaperNo(String paperNo) {
		this.paperNo = paperNo;
	}

	public String getGuideTeacher() {
		return guideTeacher;
	}

	public void setGuideTeacher(String guideTeacher) {
		this.guideTeacher = guideTeacher;
	}

	public String getPaperTitle() {
		return paperTitle;
	}

	public void setPaperTitle(String paperTitle) {
		this.paperTitle = paperTitle;
	}

	public String getTutor() {
		return tutor;
	}

	public void setTutor(String tutor) {
		this.tutor = tutor;
	}

	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
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

	public List<StudentAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<StudentAttachment> attachments) {
		this.attachments = attachments;
	}

	public String getPaperDataStatus() {
		return paperDataStatus;
	}

	public void setPaperDataStatus(String paperDataStatus) {
		this.paperDataStatus = paperDataStatus;
	}

	public String getGuideTeacherEmail() {
		return guideTeacherEmail;
	}

	public void setGuideTeacherEmail(String guideTeacherEmail) {
		this.guideTeacherEmail = guideTeacherEmail;
	}

	public String getGuideTeacherPhone() {
		return guideTeacherPhone;
	}

	public void setGuideTeacherPhone(String guideTeacherPhone) {
		this.guideTeacherPhone = guideTeacherPhone;
	}

	public String getIsView() {
		return isView;
	}

	public void setIsView(String isView) {
		this.isView = isView;
	}

	public String getSchoolDepartment() {
		return schoolDepartment;
	}

	public void setSchoolDepartment(String schoolDepartment) {
		this.schoolDepartment = schoolDepartment;
	}
}
