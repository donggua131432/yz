package com.yz.model.condition.recruit;

import com.yz.model.common.PageCondition;

public class StudentWhitelistQuery extends PageCondition {

	private String stdName;
	private String idCard;
	private String mobile;
	private String stdNo;
	
	public String getStdName() {
		return stdName;
	}
	public void setStdName(String stdName) {
		this.stdName = stdName == null ? null : stdName.trim();
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard == null ? null : idCard.trim();
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile == null ? null : mobile.trim();
	}
	public String getStdNo() {
		return stdNo;
	}
	public void setStdNo(String stdNo) {
		this.stdNo = stdNo == null ? null : stdNo.trim();
	}
}
