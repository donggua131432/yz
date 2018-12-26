package com.yz.model.recruit;

import java.util.List;

public class StudentFeeListInfo {

	private String feeAmount;
	private String offerAmount;
	private String payable;
	private String subOrderStatus;
	private String itemCode;
	private String itemName;
	
	private List<StudentSerialInfo> serialInfo;
	
	public String getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
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
	public String getSubOrderStatus() {
		return subOrderStatus;
	}
	public void setSubOrderStatus(String subOrderStatus) {
		this.subOrderStatus = subOrderStatus;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public List<StudentSerialInfo> getSerialInfo() {
		return serialInfo;
	}
	public void setSerialInfo(List<StudentSerialInfo> serialInfo) {
		this.serialInfo = serialInfo;
	}
	
}
