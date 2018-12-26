package com.yz.model.finance.statistics;

import java.util.List;

import com.yz.model.common.PubInfo;

public class BdFeeStatisticsQuery extends PubInfo {

	private String stdName;
	private String pfsnId;
	private String grade;
	private String unvsId;
	private String idCard;
	private String pfsnLevel;
	private String scholarship;
	private String campusId;
	private String mobile;
	private String stdStage;
	private String startTime;
	private String endTime;
	private String dpId;
	private List<String> itemCodes;
	private List<String> stdStages;
	private List<String> homeCampusIds;

	public List<String> getStdStages() {
		return stdStages;
	}

	public void setStdStages(List<String> stdStages) {
		this.stdStages = stdStages;
	}

	public List<String> getHomeCampusIds() {
		return homeCampusIds;
	}

	public void setHomeCampusIds(List<String> homeCampusIds) {
		this.homeCampusIds = homeCampusIds;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

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

	public List<String> getItemCodes() {
		return itemCodes;
	}

	public void setItemCodes(List<String> itemCodes) {
		this.itemCodes = itemCodes;
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getPfsnId() {
		return pfsnId;
	}

	public void setPfsnId(String pfsnId) {
		this.pfsnId = pfsnId;
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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

}
