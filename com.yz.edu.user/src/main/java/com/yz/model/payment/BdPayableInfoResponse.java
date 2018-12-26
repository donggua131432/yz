package com.yz.model.payment;

import java.io.Serializable;
import java.util.List;


public class BdPayableInfoResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8947354920326734069L;
	private String stdId;
	private String stdName;
	private String learnId;
	private String pfsnLevel;
	private String stdStage;
	private String accAmount;
	private String zmAmount;
	private String recruitType;
	private String unvsName;
	private String grade;
	private String pfsnCode;
	private String pfsnName;
	private String orderStatus;
	private String inclusionStatus;//入围状态
	private String payFeeTime; //缴费时间
	private String headUrl;//录取通知书附件
	private String scholarship;
	private List<BdStdPayInfoResponse> payInfos;
	private List<BdStdPayInfoResponse> tutorPayInfos;
	private List<BdStdPayInfoResponse> firstPayInfos;
	private List<BdStdPayInfoResponse> secondPayInfos;
	private List<BdStdPayInfoResponse> thirdPayInfos;
	private List<BdStdPayInfoResponse> fourPayInfos;
	private List<BdStdPayInfoResponse> otherPayInfos;

	public List<BdStdPayInfoResponse> getFourPayInfos() {
		return fourPayInfos;
	}

	public void setFourPayInfos(List<BdStdPayInfoResponse> fourPayInfos) {
		this.fourPayInfos = fourPayInfos;
	}

	public String getZmAmount() {
		return zmAmount;
	}

	public void setZmAmount(String zmAmount) {
		this.zmAmount = zmAmount;
	}

	public List<BdStdPayInfoResponse> getPayInfos() {
		return payInfos;
	}

	public void setPayInfos(List<BdStdPayInfoResponse> payInfos) {
		this.payInfos = payInfos;
	}

	public List<BdStdPayInfoResponse> getTutorPayInfos() {
		return tutorPayInfos;
	}

	public void setTutorPayInfos(List<BdStdPayInfoResponse> tutorPayInfos) {
		this.tutorPayInfos = tutorPayInfos;
	}

	public List<BdStdPayInfoResponse> getOtherPayInfos() {
		return otherPayInfos;
	}

	public void setOtherPayInfos(List<BdStdPayInfoResponse> otherPayInfos) {
		this.otherPayInfos = otherPayInfos;
	}

	public List<BdStdPayInfoResponse> getFirstPayInfos() {
		return firstPayInfos;
	}

	public void setFirstPayInfos(List<BdStdPayInfoResponse> firstPayInfos) {
		this.firstPayInfos = firstPayInfos;
	}

	public List<BdStdPayInfoResponse> getSecondPayInfos() {
		return secondPayInfos;
	}

	public void setSecondPayInfos(List<BdStdPayInfoResponse> secondPayInfos) {
		this.secondPayInfos = secondPayInfos;
	}

	public List<BdStdPayInfoResponse> getThirdPayInfos() {
		return thirdPayInfos;
	}

	public void setThirdPayInfos(List<BdStdPayInfoResponse> thirdPayInfos) {
		this.thirdPayInfos = thirdPayInfos;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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

	public String getAccAmount() {
		return accAmount;
	}

	public void setAccAmount(String accAmount) {
		this.accAmount = accAmount;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
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

	public String getInclusionStatus() {
		return inclusionStatus;
	}

	public void setInclusionStatus(String inclusionStatus) {
		this.inclusionStatus = inclusionStatus;
	}

	public String getPayFeeTime() {
		return payFeeTime;
	}

	public void setPayFeeTime(String payFeeTime) {
		this.payFeeTime = payFeeTime;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}
}
