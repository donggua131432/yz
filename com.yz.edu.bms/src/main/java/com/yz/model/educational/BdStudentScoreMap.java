package com.yz.model.educational;

import java.util.List;

public class BdStudentScoreMap {
	private String stdId;
	private String stdName;
	private String recruitType;
	private String unvsName;
	private String grade;
	private String pfsnName;
	private String pfsnLevel;
	private String empName;
	private String ticketNo;
	private String points;
	private String telephone;
	private String lowScore;
	private String hightScore;
	private String isAddScore;
	private String isScore;
	private String idCard;
	private String countNumber;
	private String learnId;
	private String stdStage;

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	private List<BdStudentEScore> studentScores;

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
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

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public List<BdStudentEScore> getStudentScores() {
		return studentScores;
	}

	public void setStudentScores(List<BdStudentEScore> studentScores) {
		this.studentScores = studentScores;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getLowScore() {
		return lowScore;
	}

	public void setLowScore(String lowScore) {
		this.lowScore = lowScore;
	}

	public String getHightScore() {
		return hightScore;
	}

	public void setHightScore(String hightScore) {
		this.hightScore = hightScore;
	}

	public String getIsAddScore() {
		return isAddScore;
	}

	public void setIsAddScore(String isAddScore) {
		this.isAddScore = isAddScore;
	}

	public String getIsScore() {
		return isScore;
	}

	public void setIsScore(String isScore) {
		this.isScore = isScore;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getCountNumber() {
		return countNumber;
	}

	public void setCountNumber(String countNumber) {
		this.countNumber = countNumber;
	}

}
