package com.yz.model.recruit;

import com.yz.model.common.PubInfo;

public class StudentWhitelistEdit extends PubInfo {

	private String[] stdIds;
	private String[] scholarships;
	
	public String[] getStdIds() {
		return stdIds;
	}
	public void setStdIds(String[] stdIds) {
		this.stdIds = stdIds;
	}
	public String[] getScholarships() {
		return scholarships;
	}
	public void setScholarships(String[] scholarships) {
		this.scholarships = scholarships;
	}
	
}
