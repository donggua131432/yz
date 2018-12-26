package com.yz.model.educational;

/**
 * @author jyt
 * @version 1.0
 */
public class ExamRoomAssignQuery {
    /**
     * 考场编号
     */
    private String pyCode;
    /**
     * 考场名称
     */
    private String  placeId;
    /**
     * 地址
     */
    private String address;
    /**
     * 省代码
     */
    private String provinceCode;
    /**
     * 市代码
     */
    private String cityCode;
    /**
     * 区代码
     */
    private String districtCode;
    /**
     * 年度
     */
    private String eyId;
    /**
     * 考场容量
     */
    private String status;
    /**
     * 考场容量
     */
    private String seats;
    /**
     * 剩余座位
     */
    private String restSeats;
    /**
     * 考试开始时间
     */
    private String startTime;
    /**
     * 考试结束时间
     */
    private String endTime;

    public String getPyCode() {
        return pyCode;
    }

    public void setPyCode(String pyCode) {
        this.pyCode = pyCode;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String epName) {
        this.placeId = epName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getEyId() {
        return eyId;
    }

    public void setEyId(String examYear) {
        this.eyId = examYear;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getRestSeats() {
        return restSeats;
    }

    public void setRestSeats(String restSeats) {
        this.restSeats = restSeats;
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
}
