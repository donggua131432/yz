package com.yz.model.condition.zhimi;

import com.yz.model.common.PageCondition;

public class ZhimiProductQuery extends PageCondition {

	private String productId;
	private String productName;
	private String isAllow;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getIsAllow() {
		return isAllow;
	}
	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}
}   
