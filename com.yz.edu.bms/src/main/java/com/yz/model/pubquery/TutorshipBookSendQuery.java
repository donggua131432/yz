package com.yz.model.pubquery;

/**
 * @author jyt
 * @version 1.0
 */
public class TutorshipBookSendQuery {
    /**
     * 学员姓名
     */
    private String stdName;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 年级
     */
    private String grade;
    /**
     * 院校
     */
    private String unvsName;
    /**
     * 院校id
     */
    private String unvsId;
    /**
     * 层次
     */
    private String pfsnLevel;
    /**
     * 专业
     */
    private String pfsnName;
    /**
     * 优惠类型
     */
    private String scholarship;
    /**
     * 教材是否已发放
     */
    private String orderBookStatus;
    /**
     * 快递单号
     */
    private String logisticsNo;
    /**
     * 手机号
     */
    private String mobile;

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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

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

    public String getPfsnName() {
        return pfsnName;
    }

    public void setPfsnName(String pfsnName) {
        this.pfsnName = pfsnName;
    }

    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        this.scholarship = scholarship;
    }

    public String getOrderBookStatus() {
        return orderBookStatus;
    }

    public void setOrderBookStatus(String orderBookStatus) {
        this.orderBookStatus = orderBookStatus;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
