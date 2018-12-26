package com.yz.model.condition.recruit;

import com.yz.model.common.PageCondition;

public class StudentInfoQuery extends PageCondition {

	public StudentInfoQuery() {
		super();
	}

	public StudentInfoQuery(String grade) {
		super();
		this.grade = grade;
	}

	private String stdName;

	private String stdNo;

	private String grade;

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getStdNo() {
		return stdNo;
	}

	public void setStdNo(String stdNo) {
		this.stdNo = stdNo;
	}

}
