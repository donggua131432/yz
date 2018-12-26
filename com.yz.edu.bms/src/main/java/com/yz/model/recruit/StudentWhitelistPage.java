package com.yz.model.recruit;

import java.util.List;

public class StudentWhitelistPage {
	
	private String stdId;
	private String stdName;
	private String idType;
	private String idCard;
	private String mobile;
	private String createTime;
	private List<String> whitelist; 
	private List<StudentAllListInfo> learnList;
	
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public List<String> getWhitelist() {
		return whitelist;
	}
	public void setWhitelist(List<String> whitelist) {
		this.whitelist = whitelist;
	}
	public List<StudentAllListInfo> getLearnList() {
		return learnList;
	}
	public void setLearnList(List<StudentAllListInfo> learnList) {
		this.learnList = learnList;
	}
	/**
	 * @return the idType
	 */
	public String getIdType() {
		return idType;
	}
	/**
	 * @param idType the idType to set
	 */
	public void setIdType(String idType) {
		this.idType = idType;
	}
}
