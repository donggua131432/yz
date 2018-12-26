package com.yz.model.enroll.stdenroll;

import com.yz.model.finance.stdfee.BdStdVolunteer;

public class BdStdEnrolled {

	private String stdName;
	private String stdId;
	private String learnId;
	private String stdStage;
	private String firstYearFee;
	private String firstOrderStatus;
	private String recruitType;

	private BdStdVolunteer admit; // 录取院校
	private BdStdVolunteer enroll; // 志愿

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
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

	public BdStdVolunteer getAdmit() {
		return admit;
	}

	public void setAdmit(BdStdVolunteer admit) {
		this.admit = admit;
	}

	public BdStdVolunteer getEnroll() {
		return enroll;
	}

	public void setEnroll(BdStdVolunteer enroll) {
		this.enroll = enroll;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String getFirstYearFee() {
		return firstYearFee;
	}

	public void setFirstYearFee(String firstYearFee) {
		this.firstYearFee = firstYearFee;
	}

	public String getFirstOrderStatus() {
		return firstOrderStatus;
	}

	public void setFirstOrderStatus(String firstOrderStatus) {
		this.firstOrderStatus = firstOrderStatus;
	}

}
