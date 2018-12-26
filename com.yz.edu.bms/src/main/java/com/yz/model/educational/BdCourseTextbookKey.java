package com.yz.model.educational;

public class BdCourseTextbookKey {
    private String courseId;

    private String textbookId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId == null ? null : courseId.trim();
    }

    public String getTextbookId() {
        return textbookId;
    }

    public void setTextbookId(String textbookId) {
        this.textbookId = textbookId == null ? null : textbookId.trim();
    }
}