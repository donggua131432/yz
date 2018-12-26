package com.yz.model.finance.review;

public class BdFeeReviewTuition {

	private String payable;
	private String serialStatus;
	private String offerAmount;
	private String payTime;
	private String subOrderStatus;

	public String getSubOrderStatus() {
		return subOrderStatus;
	}

	public void setSubOrderStatus(String subOrderStatus) {
		this.subOrderStatus = subOrderStatus;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getOfferAmount() {
		return offerAmount;
	}

	public void setOfferAmount(String offerAmount) {
		this.offerAmount = offerAmount;
	}

	public String getPayable() {
		return payable;
	}

	public void setPayable(String payable) {
		this.payable = payable;
	}

	public String getSerialStatus() {
		return serialStatus;
	}

	public void setSerialStatus(String serialStatus) {
		this.serialStatus = serialStatus;
	}

}
