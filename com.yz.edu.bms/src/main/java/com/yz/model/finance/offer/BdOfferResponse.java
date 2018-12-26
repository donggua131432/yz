package com.yz.model.finance.offer;

import java.util.HashMap;
import java.util.List;

import com.yz.model.finance.fee.PfsnInfoResponse;

public class BdOfferResponse extends BdOffer {

	private List<HashMap<String, String>> testArea;

	private List<PfsnInfoResponse> pfsnInfos;

	private String pfsnLevel;

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public List<PfsnInfoResponse> getPfsnInfos() {
		return pfsnInfos;
	}

	public void setPfsnInfos(List<PfsnInfoResponse> pfsnInfos) {
		this.pfsnInfos = pfsnInfos;
	}

	public List<HashMap<String, String>> getTestArea() {
		return testArea;
	}

	public void setTestArea(List<HashMap<String, String>> testArea) {
		this.testArea = testArea;
	}

}
