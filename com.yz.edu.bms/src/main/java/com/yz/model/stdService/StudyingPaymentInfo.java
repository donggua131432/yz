package com.yz.model.stdService;

/**
 * 在读学员缴费明细
 * @author Administrator
 *
 */
public class StudyingPaymentInfo {

	private String itemCode;
	private String itemName;
	private String feeAmount;
	private String offerAmount;
	private String payable;
	private String subOrderStatus;
	
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
	
	public String getSubOrderStatus() {
		return subOrderStatus;
	}
	public void setSubOrderStatus(String subOrderStatus) {
		this.subOrderStatus = subOrderStatus;
	}
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
}
