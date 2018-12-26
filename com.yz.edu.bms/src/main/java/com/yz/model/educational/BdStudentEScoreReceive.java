package com.yz.model.educational;

import java.util.ArrayList;

import com.yz.model.common.PubInfo;

public class BdStudentEScoreReceive extends PubInfo {

	private String learnId;

	private String stdId;

	private ArrayList<BdStudentEScore> scores;

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

	public ArrayList<BdStudentEScore> getScores() {
		return scores;
	}

	public void setScores(ArrayList<BdStudentEScore> scores) {
		this.scores = scores;
	}

}
