package com.yz.model.educational;

import com.yz.model.common.PubInfo;

import java.util.Date;

public class BdExamSeats extends PubInfo {
    private String esId;
    private int esNum;
    private String placeId;
    private String learnId;
    private String pyId;
    private Date createTime;

    public String getEsId() {
        return esId;
    }

    public void setEsId(String esId) {
        this.esId = esId;
    }

    public int getEsNum() {
        return esNum;
    }

    public void setEsNum(int esNum) {
        this.esNum = esNum;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getLearnId() {
        return learnId;
    }

    public void setLearnId(String learnId) {
        this.learnId = learnId;
    }

    public String getPyId() {
        return pyId;
    }

    public void setPyId(String pyId) {
        this.pyId = pyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
