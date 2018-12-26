package com.yz.model.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BdStudentInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9215166541347374476L;
	
	private String stdName;
	private String sex;
	private String mobile;
	private String idCard;
	private String headUrl;
	
	private List<BdLearnInfo> learnInfos = new ArrayList<BdLearnInfo>();
	
	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public List<BdLearnInfo> getLearnInfos() {
		return learnInfos;
	}

	public void setLearnInfos(List<BdLearnInfo> learnInfos) {
		this.learnInfos = learnInfos;
	}}
