package com.yz.model.finance.fee;

public class FeeItemForm {

	private String itemCode;
	private String amount;
	private String itemType;
	private String zmCouponNum;
	private String delayNum;
	private String discountType;

	private String fdId;
	private String odId;

	public String getOdId() {
		return odId;
	}

	public void setOdId(String odId) {
		this.odId = odId;
	}

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

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
