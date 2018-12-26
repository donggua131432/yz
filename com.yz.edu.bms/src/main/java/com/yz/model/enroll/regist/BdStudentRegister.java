package com.yz.model.enroll.regist;

public class BdStudentRegister {

	private String registerId;

	private String stdId;

	private String stdNo;

	private String schoolRoll;

	private String pfsnName;

	private String pfsnId;

	private String unvsName;

	private String unvsId;

	public String getRegisterId() {
		return registerId;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getStdNo() {
		return stdNo;
	}

	public void setStdNo(String stdNo) {
		this.stdNo = stdNo == null ? null : stdNo.trim();
	}

	public String getSchoolRoll() {
		return schoolRoll;
	}

	public void setSchoolRoll(String schoolRoll) {
		this.schoolRoll = schoolRoll == null ? null : schoolRoll.trim();
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName == null ? null : unvsName.trim();
	}

	public String getUnvsId() {
		return unvsId;
	}

	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId == null ? null : unvsId.trim();
	}
}