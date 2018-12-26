package com.yz.job.model;

import com.yz.model.AtsAwardRule;

public class RechargeAwardRule extends AtsAwardRule {
	
	private String userId; 
	
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}

}
