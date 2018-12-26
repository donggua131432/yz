package com.yz.model.transfer;

import com.yz.model.common.PubInfo;

public class BdStudentChangeMap extends PubInfo {
	private String changeId;
	private String pfsnLevel;
	private String grade;
	private String unvsId;
	private String pfsnId;
	private String taId;
	private String scholarship;
	private String reason;
	private String learnId;
	private String oldLearnId;
	private String stdId;
	private String oldStdStage;
	private String recruitType;
	private String isComplete;

	public String getChangeId() {
		return changeId;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId;
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getOldStdStage() {
		return oldStdStage;
	}

	public void setOldStdStage(String oldStdStage) {
		this.oldStdStage = oldStdStage;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getOldLearnId() {
		return oldLearnId;
	}

	public void setOldLearnId(String oldLearnId) {
		this.oldLearnId = oldLearnId;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
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

	public String getTaId() {
		return taId;
	}

	public void setTaId(String taId) {
		this.taId = taId;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
