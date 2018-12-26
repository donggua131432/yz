package com.yz.model.educational;

/**
 * @author jyt
 * @version 1.0
 */
public class ExamRoomSeatsQuery {
    /**
     * 学员姓名
     */
    private String stdName;
    /**
     * 证件号码
     */
    private String idCard;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 院校
     */
    private String unvsId;
    /**
     * 专业名称
     */
    private String pfsnName;
    /**
     * 专业层次
     */
    private String pfsnLevel;
    /**
     * 年级
     */
    private String grade;
    /**
     * 考场名称
     */
    private String placeId;
    /**
     * 考试年度
     */
    private String eyId;
    /**
     * 考试开始时间
     */
    private String startTime;
    /**
     * 考试结束时间
     */
    private String endTime;
    /**
     * 招生类型
     */
    private String recruitType;
    
    private String pfsnId;

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

    public String getUnvsId() {
        return unvsId;
    }

    public void setUnvsId(String unvsId) {
        this.unvsId = unvsId;
    }

    public String getPfsnName() {
        return pfsnName;
    }

    public void setPfsnName(String pfsnName) {
        this.pfsnName = pfsnName;
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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getEyId() {
        return eyId;
    }

    public void setEyId(String eyId) {
        this.eyId = eyId;
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

    public String getRecruitType() {
        return recruitType;
    }

    public void setRecruitType(String recruitType) {
        this.recruitType = recruitType;
    }

	public String getPfsnId()
	{
		return pfsnId;
	}

	public void setPfsnId(String pfsnId)
	{
		this.pfsnId = pfsnId;
	}
}
