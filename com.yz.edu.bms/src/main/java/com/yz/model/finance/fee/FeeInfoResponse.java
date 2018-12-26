package com.yz.model.finance.fee;

import java.util.List;

public class FeeInfoResponse {

	private String feeId;
	private String feeName;
	private String status;

	private List<PfsnInfoResponse> pfsnInfo;
	private List<FeeItemResponse> feeItem;
	private List<TestAreaResponse> taInfo;

	public List<TestAreaResponse> getTaInfo() {
		return taInfo;
	}

	public void setTaInfo(List<TestAreaResponse> taInfo) {
		this.taInfo = taInfo;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public List<PfsnInfoResponse> getPfsnInfo() {
		return pfsnInfo;
	}

	public void setPfsnInfo(List<PfsnInfoResponse> pfsnInfo) {
		this.pfsnInfo = pfsnInfo;
	}

	public List<FeeItemResponse> getFeeItem() {
		return feeItem;
	}

	public void setFeeItem(List<FeeItemResponse> feeItem) {
		this.feeItem = feeItem;
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
