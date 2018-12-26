package com.yz.model;

public class StudentHistory {

	private String unvsName;
	private String profession;
	private String graduateTime;
	private String diploma;
	private String edcsType;
	
	private String learnId;

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName == null ? null : unvsName.trim();
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession == null ? null : profession.trim();
	}

	public String getGraduateTime() {
		return graduateTime;
	}

	public void setGraduateTime(String graduateTime) {
		this.graduateTime = graduateTime == null ? null : graduateTime.trim();
	}

	public String getDiploma() {
		return diploma;
	}

	public void setDiploma(String diploma) {
		this.diploma = diploma == null ? null : diploma.trim();
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getEdcsType() {
		return edcsType;
	}

	public void setEdcsType(String edcsType) {
		this.edcsType = edcsType == null ? null : edcsType.trim();
	}
	
	
}
