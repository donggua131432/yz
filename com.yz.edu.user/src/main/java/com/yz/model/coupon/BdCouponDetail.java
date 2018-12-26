package com.yz.model.coupon;

import java.io.Serializable;

public class BdCouponDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String itemCode;
	private String price;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
