package com.yz.model.goods;

import com.yz.model.common.PageCondition;
/**
 * 采购订单的查询条件
 * @author lx
 * @date 2018年5月15日 上午11:17:15
 */
public class GsGoodsPurchasingQuery extends PageCondition{

	private String applyName;
	private String receiveName;
	private String receiveMobile;
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getReceiveMobile() {
		return receiveMobile;
	}
	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}
}
