package com.yz.model.stdService;

public class StudentQingshuCourse {
    private String qcId;
    private String qingshuId;
    private String semester;
    private String courseName;
    private String genericScore;
    private String finalScore;
    private String summaryScore;

    public String getQcId() {
        return qcId;
    }

    public void setQcId(String qcId) {
        this.qcId = qcId;
    }

    public String getQingshuId() {
        return qingshuId;
    }

    public void setQingshuId(String qingshuId) {
        this.qingshuId = qingshuId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGenericScore() {
        return genericScore;
    }

    public void setGenericScore(String genericScore) {
        this.genericScore = genericScore;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getSummaryScore() {
        return summaryScore;
    }

    public void setSummaryScore(String summaryScore) {
        this.summaryScore = summaryScore;
    }
}
