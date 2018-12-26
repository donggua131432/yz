package com.yz.model.educational;

import java.util.ArrayList;

import com.yz.model.common.PubInfo;

public class BdStudentTScoreReceive extends PubInfo {

	private String learnId;

	private String stdId;
	
	private String semester;
	
	private String grade;

	private ArrayList<BdStudentTScore> scores;

	
	
	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public ArrayList<BdStudentTScore> getScores() {
		return scores;
	}

	public void setScores(ArrayList<BdStudentTScore> scores) {
		this.scores = scores;
	}

}
