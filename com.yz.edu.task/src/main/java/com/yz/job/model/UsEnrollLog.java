package com.yz.job.model;

import java.util.Date;

/**
 * 
 * @author Administrator
 *
 */
public class UsEnrollLog implements java.io.Serializable{
	
    private String enrollId;

    private String userId;

    private String idCard;

    private String name;

    private String unvsId;

    private String grade;

    private String unvsName;

    private String pfsnLevel;

    private String pfsnId;

    private String pfsnName;

    private String taId;

    private String taName;

    private String scholarship;

    private Date enrollTime;
    
    private String enrollChannel;
    
    private String sg;

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId == null ? null : enrollId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUnvsId() {
        return unvsId;
    }

    public void setUnvsId(String unvsId) {
        this.unvsId = unvsId == null ? null : unvsId.trim();
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    public String getUnvsName() {
        return unvsName;
    }

    public void setUnvsName(String unvsName) {
        this.unvsName = unvsName == null ? null : unvsName.trim();
    }

    public String getPfsnLevel() {
        return pfsnLevel;
    }

    public void setPfsnLevel(String pfsnLevel) {
        this.pfsnLevel = pfsnLevel == null ? null : pfsnLevel.trim();
    }

    public String getPfsnId() {
        return pfsnId;
    }

    public void setPfsnId(String pfsnId) {
        this.pfsnId = pfsnId == null ? null : pfsnId.trim();
    }

    public String getPfsnName() {
        return pfsnName;
    }

    public void setPfsnName(String pfsnName) {
        this.pfsnName = pfsnName == null ? null : pfsnName.trim();
    }

    public String getTaId() {
        return taId;
    }

    public void setTaId(String taId) {
        this.taId = taId == null ? null : taId.trim();
    }

    public String getTaName() {
        return taName;
    }

    public void setTaName(String taName) {
        this.taName = taName == null ? null : taName.trim();
    }

    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        this.scholarship = scholarship == null ? null : scholarship.trim();
    }

    public Date getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(Date enrollTime) {
        this.enrollTime = enrollTime;
    }

	public String getEnrollChannel() {
		return enrollChannel;
	}

	public void setEnrollChannel(String enrollChannel) {
		this.enrollChannel = enrollChannel == null ? null : enrollChannel.trim();
	}

	public String getSg() {
		return sg;
	}

	public void setSg(String sg) {
		this.sg = sg;
	}
}