package com.yz.model.goods;

import com.yz.model.common.PageCondition;

public class GsGoodsSalesQuery extends PageCondition{
	
	private String salesName;
	private String salesStatus;
	private String salesType;
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getSalesStatus() {
		return salesStatus;
	}
	public void setSalesStatus(String salesStatus) {
		this.salesStatus = salesStatus;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

}
