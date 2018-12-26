package com.yz.model.finance.fee;

import java.util.ArrayList;

import com.yz.model.common.PubInfo;

public class BdFeeEdit extends PubInfo{

	private String feeId;
	private String[] pfsns;
	private String[] testAreas;
	private String scholarship;
	private String feeName;
	private String status;
	private ArrayList<FeeItemForm> items;

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public ArrayList<FeeItemForm> getItems() {
		return items;
	}

	public void setItems(ArrayList<FeeItemForm> items) {
		this.items = items;
	}

	public String[] getPfsns() {
		return pfsns;
	}

	public void setPfsns(String[] pfsns) {
		this.pfsns = pfsns;
	}

	public String[] getTestAreas() {
		return testAreas;
	}

	public void setTestAreas(String[] testAreas) {
		this.testAreas = testAreas;
	}

	public String getScholarship() {
		return scholarship;
	}

	public void setScholarship(String scholarship) {
		this.scholarship = scholarship;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
