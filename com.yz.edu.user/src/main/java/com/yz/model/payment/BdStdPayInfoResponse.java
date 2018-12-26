package com.yz.model.payment;

import java.io.Serializable;

public class BdStdPayInfoResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2661551693305491308L;
	private String itemCode;
	private String itemName;
	private String itemYear;
	private String payable;
	private String subOrderStatus;
	private String refundAmount;

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
