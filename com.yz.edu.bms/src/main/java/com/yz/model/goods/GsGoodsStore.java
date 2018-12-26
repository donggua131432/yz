package com.yz.model.goods;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

public class GsGoodsStore extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2071741176554371097L;
	
	private String goodsId;
	private String goodsCount;
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}

}
