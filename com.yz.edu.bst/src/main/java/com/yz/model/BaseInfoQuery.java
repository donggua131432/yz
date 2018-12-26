package com.yz.model;

public class BaseInfoQuery {
	private String unvsName;
	private String unvsId;
	private String pfsnLevel;
	private String grade;
	private String pfsnName; 
	private String pfsnId;
	private String taName;
	
	private String type;
	
	private int pageSize;
	private int pageNum;
	
	public String getUnvsName() {
		return unvsName;
	}
	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}
	public String getUnvsId() {
		return unvsId;
	}
	public void setUnvsId(String unvsId) {
		this.unvsId = unvsId;
	}
	public String getPfsnLevel() {
		return pfsnLevel;
	}
	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
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
	public String getTaName() {
		return taName;
	}
	public void setTaName(String taName) {
		this.taName = taName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	} 
}
