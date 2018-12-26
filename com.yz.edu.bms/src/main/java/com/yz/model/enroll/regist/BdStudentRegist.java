package com.yz.model.enroll.regist;

import java.util.List;

import com.yz.model.educational.BdStudentEScore;
import com.yz.model.enroll.stdenroll.BdStdEnroll;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;

public class BdStudentRegist {

	private String learnId;
	private String stdId;
	private String stdNo;
	private String stdName;
	private String stdStage;
	private String grade;
	private String recruitType;
	private String unvsName;
	private String pfsnCode;
	private String pfsnName;
	private String pfsnLevel;
	private String schoolRoll;
	private String recruit;
	private String campusName;
	private String empName;
	private String points;
	private String feeName;
	private String scholarship;

	private List<BdStudentEScore> score;

	private BdStdEnroll application;

	private List<BdStdPayInfoResponse> payInfos;
	
	private String idCard;

	public List<BdStdPayInfoResponse> getPayInfos() {
		return payInfos;
	}

	public void setPayInfos(List<BdStdPayInfoResponse> payInfos) {
		this.payInfos = payInfos;
	}

	public void setApplication(BdStdEnroll application) {
		this.application = application;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getRecruit() {
		return recruit;
	}

	public void setRecruit(String recruit) {
		this.recruit = recruit;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public List<BdStudentEScore> getScore() {
		return score;
	}

	public void setScore(List<BdStudentEScore> score) {
		this.score = score;
	}

	public BdStdEnroll getApplication() {
		return application;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
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

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
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

	public String getSchoolRoll() {
		return schoolRoll;
	}

	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll;
	}

	public String getIdCard()
	{
		return idCard;
	}

	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}

}
