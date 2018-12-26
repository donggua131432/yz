package com.yz.model.salarykpi;

public class StudentDetailQuery {
    private String queryType;
    private String recruit;
    private String scholarship;
    private String inclusionStatus;
    private String[] scholarshipItems;
    private String[] inclusionStatusItems;

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getRecruit() {
        return recruit;
    }

    public void setRecruit(String recruit) {
        this.recruit = recruit;
    }

    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        this.scholarship = scholarship;
    }

    public String getInclusionStatus() {
        return inclusionStatus;
    }

    public void setInclusionStatus(String inclusionStatus) {
        this.inclusionStatus = inclusionStatus;
    }

    public String[] getScholarshipItems() {
        return scholarshipItems;
    }

    public void setScholarshipItems(String[] scholarshipItems) {
        this.scholarshipItems = scholarshipItems;
    }

    public String[] getInclusionStatusItems() {
        return inclusionStatusItems;
    }

    public void setInclusionStatusItems(String[] inclusionStatusItems) {
        this.inclusionStatusItems = inclusionStatusItems;
    }
}
