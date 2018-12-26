package com.yz.model.goods;

import com.yz.model.common.PageCondition;

public class GsGoodsQueryInfo extends PageCondition {

	private String goodsName;
	private String isAllow;
	private String goodsUse;
	private String goodsType;
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsUse() {
		return goodsUse;
	}
	public void setGoodsUse(String goodsUse) {
		this.goodsUse = goodsUse;
	}
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getIsAllow() {
		return isAllow;
	}
	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}
}
