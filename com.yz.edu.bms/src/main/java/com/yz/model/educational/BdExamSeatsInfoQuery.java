package com.yz.model.educational;

import com.yz.util.StringUtil;

import java.util.Date;

public class BdExamSeatsInfoQuery {
    /**
     * 年度
     */
    private String eyId;
    /**
     * 考场名称
     */
    private String epId;
    /**
     * 考试开始时间
     */
    private String startTime;
    /**
     * 考试结束时间
     */
    private String endTime;
    /**
     * 课室名称
     */
    private String placeId;

    /**
     * 考试时间
     */
    private String examTime;

    private String eyName;
    private String epName;
    private String placeName;
    private String examTimeStr;

    public String getEyId() {
        return eyId;
    }

    public void setEyId(String eyId) {
        this.eyId = eyId;
    }

    public String getEpId() {
        return epId;
    }

    public void setEpId(String epId) {
        this.epId = epId;
    }

    public String getStartTime() {
        if (StringUtil.hasValue(this.examTime)) {
            return examTime.split("=")[0];
        } else {
            return this.startTime;
        }
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        if (StringUtil.hasValue(this.examTime)) {
            return examTime.split("=")[1];
        } else {
            return this.endTime;
        }
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getEyName() {
        return eyName;
    }

    public void setEyName(String eyName) {
        this.eyName = eyName;
    }

    public String getEpName() {
        return epName;
    }

    public void setEpName(String epName) {
        this.epName = epName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getExamTimeStr() {
        return examTimeStr;
    }

    public void setExamTimeStr(String examTimeStr) {
        this.examTimeStr = examTimeStr;
    }
}
