package com.yz.model.finance.stdfee;

public class BdPayableDetailResponse {

	private String fdId;
	private String defineAmount;
	private String turnoverAmount;
	private String itemCode;
	private String itemName;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getDefineAmount() {
		return defineAmount;
	}

	public void setDefineAmount(String defineAmount) {
		this.defineAmount = defineAmount;
	}

	public String getTurnoverAmount() {
		return turnoverAmount;
	}

	public void setTurnoverAmount(String turnoverAmount) {
		this.turnoverAmount = turnoverAmount;
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

}
