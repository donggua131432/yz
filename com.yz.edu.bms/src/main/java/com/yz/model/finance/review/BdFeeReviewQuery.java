package com.yz.model.finance.review;

public class BdFeeReviewQuery {

	private String orderNo;
	private String serialNo;
	private String outSerialNo;
	private String stdName;
	private String idCard;
	private String mobile;
	private String paymentType;
	private String serialStatus;
	private String startTime;
	private String endTime;
	
	private String recruitType;
	private String grade;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getOutSerialNo() {
		return outSerialNo;
	}

	public void setOutSerialNo(String outSerialNo) {
		this.outSerialNo = outSerialNo;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getSerialStatus() {
		return serialStatus;
	}

	public void setSerialStatus(String serialStatus) {
		this.serialStatus = serialStatus;
	}

	public String getRecruitType()
	{
		return recruitType;
	}

	public void setRecruitType(String recruitType)
	{
		this.recruitType = recruitType;
	}

	public String getGrade()
	{
		return grade;
	}

	public void setGrade(String grade)
	{
		this.grade = grade;
	}

}
