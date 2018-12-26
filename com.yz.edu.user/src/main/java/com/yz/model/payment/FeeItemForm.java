package com.yz.model.payment;

import java.io.Serializable;

public class FeeItemForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1349061453393979509L;
	private String itemCode;
	private String amount;
	private String itemType;
	private String zmCouponNum;
	private String delayNum;

	public String getZmCouponNum() {
		return zmCouponNum;
	}

	public void setZmCouponNum(String zmCouponNum) {
		this.zmCouponNum = zmCouponNum;
	}

	public String getDelayNum() {
		return delayNum;
	}

	public void setDelayNum(String delayNum) {
		this.delayNum = delayNum;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
