package com.yz.model.finance.stdfee;

import java.io.Serializable;

/**
 * 二维码支付
 * @author lx
 * @date 2017年10月10日 下午4:10:02
 */
public class BdQRCodePayableInfoResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4839814030053317939L;
	private String stdId;
	private String stdName;
	private String learnId;
	private String pfsnLevel;
	private String stdStage;
	private String recruitType;
	private String unvsName;
	private String grade;
	private String pfsnCode;
	private String pfsnName;
	private String orderStatus;
	private String mobile;
	private String idCard;

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

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
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
