package com.yz.model.finance.stdfee;

public class BdStdPayInfoResponse {

	private String itemCode;
	private String itemName;
	private String itemYear;
	private String payable;
	private String subOrderStatus;
	private String refundAmount;
	private String subOrderNo;
	private String xjAmount;
	private String zmAmount;
	private String zlAmount;
	private String yhqAmount;
	
	
	
	public String getSubOrderNo() {
		return subOrderNo;
	}

	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}

	public String getXjAmount() {
		return xjAmount;
	}

	public void setXjAmount(String xjAmount) {
		this.xjAmount = xjAmount;
	}

	public String getZmAmount() {
		return zmAmount;
	}

	public void setZmAmount(String zmAmount) {
		this.zmAmount = zmAmount;
	}

	public String getZlAmount() {
		return zlAmount;
	}

	public void setZlAmount(String zlAmount) {
		this.zlAmount = zlAmount;
	}

	public String getYhqAmount() {
		return yhqAmount;
	}

	public void setYhqAmount(String yhqAmount) {
		this.yhqAmount = yhqAmount;
	}

	public String getItemYear() {
		return itemYear;
	}

	public void setItemYear(String itemYear) {
		this.itemYear = itemYear;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
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

}
