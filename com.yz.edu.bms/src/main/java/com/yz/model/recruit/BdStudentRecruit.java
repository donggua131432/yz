package com.yz.model.recruit;

import com.yz.model.common.PubInfo;

/**
 * 学员报读编辑信息
 * 
 * @author Administrator
 *
 */
public class BdStudentRecruit extends PubInfo {

	private String stdId;
	private String learnId;
	private String pfsnLevel;
	private String enrollType;
	private String grade;
	private String unvsId;
	private String pfsnId;
	private String taId;
	private String secUnvsId;
	private String secPfsnId;
	private String secTaId;
	private String bpType;
	private String points;
	private String sg;
	private String scholarship;
	private String recruitType;

	private String canDistribution;

	private String recruitEmpId;
	private String recruitDpId;
	private String recruitCampusId;
	private String recruitCampusManager;
	private String tutorEmpId;
	private String tutorCampusId;
	private String tutorDpId;
	private String tutorCampusManager;

	private String feeId;
	private String offerId;

	private String empId;
	private String examNum;
	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel == null ? null : pfsnLevel.trim();
	}

	public String getEnrollType() {
		return enrollType;
	}

	public void setEnrollType(String enrollType) {
		this.enrollType = enrollType == null ? null : enrollType.trim();
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId == null ? null : unvsId.trim();
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId == null ? null : pfsnId.trim();
	}

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId == null ? null : taId.trim();
	}

	public String getSecUnvsId() {
		return secUnvsId;
	}

	public void setSecUnvsId(String secUnvsId) {
		this.secUnvsId = secUnvsId == null ? null : secUnvsId.trim();
	}

	public String getSecPfsnId() {
		return secPfsnId;
	}

	public void setSecPfsnId(String secPfsnId) {
		this.secPfsnId = secPfsnId == null ? null : secPfsnId.trim();
	}

	public String getSecTaId() {
		return secTaId;
	}

	public void setSecTaId(String secTaId) {
		this.secTaId = secTaId == null ? null : secTaId.trim();
	}

	public String getBpType() {
		return bpType;
	}

	public void setBpType(String bpType) {
		this.bpType = bpType == null ? null : bpType.trim();
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points == null ? null : points.trim();
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship == null ? null : scholarship.trim();
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getRecruitEmpId() {
		return recruitEmpId;
	}

	public void setRecruitEmpId(String recruitEmpId) {
		this.recruitEmpId = recruitEmpId;
	}

	public String getRecruitDpId() {
		return recruitDpId;
	}

	public void setRecruitDpId(String recruitDpId) {
		this.recruitDpId = recruitDpId;
	}

	public String getRecruitCampusId() {
		return recruitCampusId;
	}

	public void setRecruitCampusId(String recruitCampusId) {
		this.recruitCampusId = recruitCampusId;
	}

	public String getTutorEmpId() {
		return tutorEmpId;
	}

	public void setTutorEmpId(String tutorEmpId) {
		this.tutorEmpId = tutorEmpId;
	}

	public String getTutorCampusId() {
		return tutorCampusId;
	}

	public void setTutorCampusId(String tutorCampusId) {
		this.tutorCampusId = tutorCampusId;
	}

	public String getTutorDpId() {
		return tutorDpId;
	}

	public void setTutorDpId(String tutorDpId) {
		this.tutorDpId = tutorDpId;
	}

	public String getCanDistribution() {
		return canDistribution;
	}

	public void setCanDistribution(String canDistribution) {
		this.canDistribution = canDistribution;
	}

	public String getRecruitCampusManager() {
		return recruitCampusManager;
	}

	public void setRecruitCampusManager(String recruitCampusManager) {
		this.recruitCampusManager = recruitCampusManager;
	}

	public String getTutorCampusManager() {
		return tutorCampusManager;
	}

	public void setTutorCampusManager(String tutorCampusManager) {
		this.tutorCampusManager = tutorCampusManager;
	}

	public String getSg() {
		return sg;
	}

	public void setSg(String sg) {
		this.sg = sg;
	}

	public String getExamNum() {
		return examNum;
	}

	public void setExamNum(String examNum) {
		this.examNum = examNum;
	}
	
}
