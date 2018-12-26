package com.yz.model.educational;

/**
 * @author jyt
 * @version 1.0
 */
public class ExamRoomConfig {
    /**
     * 考场安排ID
     */
    private String pyId;
    /**
     * 考场年度ID
     */
    private String eyId;
    /**
     * 考场ID
     */
    private String placeId;
    /**
     * 日期
     */
    private String date;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 考场容量
     */
    private String seats;
    /**
     * 备注
     */
    private String remark;

    public String getPyId() {
        return pyId;
    }

    public void setPyId(String pyId) {
        this.pyId = pyId;
    }

    public String getEyId() {
        return eyId;
    }

    public void setEyId(String eyId) {
        this.eyId = eyId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
