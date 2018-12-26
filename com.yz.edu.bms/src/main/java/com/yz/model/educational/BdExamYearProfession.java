package com.yz.model.educational;

import com.yz.model.common.PubInfo;

import java.util.Date;

public class BdExamYearProfession extends PubInfo {
    private String eypId;
    private String eypCode;
    private String eyId;
    private String grade;
    private String unvsId;
    private String pfsnLevel;
    private String pfsnId;
    private Date updateTime;
    private Date createTime;

    public String getEypId() {
        return eypId;
    }

    public void setEypId(String eypId) {
        this.eypId = eypId;
    }

    public String getEypCode() {
        return eypCode;
    }

    public void setEypCode(String eypCode) {
        this.eypCode = eypCode;
    }

    public String getEyId() {
        return eyId;
    }

    public void setEyId(String eyId) {
        this.eyId = eyId;
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

    public String getPfsnLevel() {
        return pfsnLevel;
    }

    public void setPfsnLevel(String pfsnLevel) {
        this.pfsnLevel = pfsnLevel;
    }

    public String getPfsnId() {
        return pfsnId;
    }

    public void setPfsnId(String pfsnId) {
        this.pfsnId = pfsnId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
