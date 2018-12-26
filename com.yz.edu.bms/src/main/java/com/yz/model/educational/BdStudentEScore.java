package com.yz.model.educational;

import java.util.Date;

import com.yz.model.common.PubInfo;

public class BdStudentEScore extends PubInfo {

	private String learnId;

	private String escoreId;

	private String stdId;

	private String courseName;

	private String courseId;

	private String score;

	private String addScore;

	private Date updateTime;

	private String updateUser;

	private String updateUserId;

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getAddScore() {
		return addScore;
	}

	public void setAddScore(String addScore) {
		this.addScore = addScore;
	}

	public String getEscoreId() {
		return escoreId;
	}

	public void setEscoreId(String escoreId) {
		this.escoreId = escoreId == null ? null : escoreId.trim();
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName == null ? null : courseName.trim();
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId == null ? null : courseId.trim();
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score == null ? null : score.trim();
	}

}