package com.yz.model.finance.fee;

import java.util.HashMap;
import java.util.List;

import com.yz.model.finance.stdfee.BdPayableDetailResponse;

public class BdFeeResponse extends BdFee {

	private List<BdPayableDetailResponse> payable;

	private List<PfsnInfoResponse> pfsnInfo;

	private List<HashMap<String, String>> testArea;

	private String scholarship;

	public List<HashMap<String, String>> getTestArea() {
		return testArea;
	}

	public void setTestArea(List<HashMap<String, String>> testArea) {
		this.testArea = testArea;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public List<PfsnInfoResponse> getPfsnInfo() {
		return pfsnInfo;
	}

	public void setPfsnInfo(List<PfsnInfoResponse> pfsnInfo) {
		this.pfsnInfo = pfsnInfo;
	}

	public List<BdPayableDetailResponse> getPayable() {
		return payable;
	}

	public void setPayable(List<BdPayableDetailResponse> payable) {
		this.payable = payable;
	}

}
