package com.yz.model.common;

import java.io.Serializable;

public class ItemSelectInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String itemCode;
	private String itemName;

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

}
