package com.yz.model.educational;

public class BdPlanCourseKey {
    private String thpId;

    private String courseId;

    public String getThpId() {
        return thpId;
    }

    public void setThpId(String thpId) {
        this.thpId = thpId == null ? null : thpId.trim();
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId == null ? null : courseId.trim();
    }
}