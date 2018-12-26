package com.yz.model.enroll.stdenroll;

import java.util.List;

import com.yz.model.common.PubInfo;
import com.yz.model.educational.BdStudentEScore;
import com.yz.model.finance.stdfee.BdStdVolunteer;

public class BdStdEnroll extends PubInfo {

	private String stdId;
	private String learnId;
	private String stdName;
	private String recruitType;
	private String grade;
	private String recruit;
	private String stdStage;
	private String ticketNo;
	private String unvsName;
	private String pfsnId;
	private String unvsId;
	private String pfsnCode;
	private String pfsnName;
	private String pfsnLevel;
	private String campusName;
	private String scholarship;
	private String points;

	private String enrollPfsnId;
	private String enrollUnvsId;

	private List<BdStudentEScore> scores;

	private BdStdVolunteer firstAdmit;
	private BdStdVolunteer secondAdmit;
	
	
	private String idCard;
	
	private String homeCampusId;

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
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

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getEnrollPfsnId() {
		return enrollPfsnId;
	}

	public void setEnrollPfsnId(String enrollPfsnId) {
		this.enrollPfsnId = enrollPfsnId;
	}

	public String getEnrollUnvsId() {
		return enrollUnvsId;
	}

	public void setEnrollUnvsId(String enrollUnvsId) {
		this.enrollUnvsId = enrollUnvsId;
	}

	public BdStdVolunteer getSecondAdmit() {
		return secondAdmit;
	}

	public void setSecondAdmit(BdStdVolunteer secondAdmit) {
		this.secondAdmit = secondAdmit;
	}

	public BdStdVolunteer getFirstAdmit() {
		return firstAdmit;
	}

	public void setFirstAdmit(BdStdVolunteer firstAdmit) {
		this.firstAdmit = firstAdmit;
	}

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

	public List<BdStudentEScore> getScores() {
		return scores;
	}

	public void setScores(List<BdStudentEScore> scores) {
		this.scores = scores;
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRecruit() {
		return recruit;
	}

	public void setRecruit(String recruit) {
		this.recruit = recruit;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getIdCard()
	{
		return idCard;
	}

	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}

	public String getHomeCampusId()
	{
		return homeCampusId;
	}

	public void setHomeCampusId(String homeCampusId)
	{
		this.homeCampusId = homeCampusId;
	}

}
