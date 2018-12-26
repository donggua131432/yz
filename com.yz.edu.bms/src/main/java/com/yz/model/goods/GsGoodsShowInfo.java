package com.yz.model.goods;

import java.io.Serializable;
/**
 * 商品显示信息--管理平台
 * @author lx
 * @date 2017年7月27日 上午11:57:28
 */
public class GsGoodsShowInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2659860595647210295L;
	
	private String goodsId;
	private String annexUrl;
	private String goodsName;
	private String goodsCount;
	private String costPrice;
	private String originalPrice;
	private String goodsUse;
	private String isAllow;
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getAnnexUrl() {
		return annexUrl;
	}
	public void setAnnexUrl(String annexUrl) {
		this.annexUrl = annexUrl;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	public String getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getGoodsUse() {
		return goodsUse;
	}
	public void setGoodsUse(String goodsUse) {
		this.goodsUse = goodsUse;
	}
	public String getIsAllow() {
		return isAllow;
	}
	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}

}
