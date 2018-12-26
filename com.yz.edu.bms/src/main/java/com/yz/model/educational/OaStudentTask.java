package com.yz.model.educational;

import java.io.Serializable;

public class OaStudentTask implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2617926423181885356L;
	
	private String taskId;
	private String taskTitle;
	private String learnId;
	private String tutorId;
	private String taskStatus;
	private String isNotify;
	private String openId;
	private String endTime;
	private String taskContent;
	private String stuName;//姓名
	private String cardId;//身份证号
	private String universitiy;//院校
	private String  grad;//层次
	private String  specialty;//专业
	private String  follow;//跟进人

	
	
	public String getName() {
		return stuName;
	}
	public void setName(String stuName) {
		this.stuName = stuName;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getUniversitiy() {
		return universitiy;
	}
	public void setUniversitiy(String universitiy) {
		this.universitiy = universitiy;
	}
	public String getGrad() {
		return grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getTaskTitle()
	{
		return taskTitle;
	}
	public void setTaskTitle(String taskTitle)
	{
		this.taskTitle = taskTitle;
	}
	public String getLearnId()
	{
		return learnId;
	}
	public void setLearnId(String learnId)
	{
		this.learnId = learnId;
	}
	public String getTutorId()
	{
		return tutorId;
	}
	public void setTutorId(String tutorId)
	{
		this.tutorId = tutorId;
	}
	public String getTaskStatus()
	{
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus)
	{
		this.taskStatus = taskStatus;
	}
	public String getIsNotify()
	{
		return isNotify;
	}
	public void setIsNotify(String isNotify)
	{
		this.isNotify = isNotify;
	}
	public String getOpenId()
	{
		return openId;
	}
	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public String getTaskId()
	{
		return taskId;
	}
	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	public String getTaskContent()
	{
		return taskContent;
	}
	public void setTaskContent(String taskContent)
	{
		this.taskContent = taskContent;
	}
}
