package com.yz.model.goods;

import java.io.Serializable;
/**
 * 商品附件
 * @author lx
 * @date 2017年7月29日 下午12:56:15
 */

import com.yz.model.common.PubInfo;
public class GsGoodsAnnex extends PubInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8697183628032477446L;
	private String annexId;
	private String annexName;
	private String gsAnnexType;
	private String annexUrl;
	private String goodsId;
	public String getAnnexId() {
		return annexId;
	}
	public void setAnnexId(String annexId) {
		this.annexId = annexId;
	}
	public String getAnnexName() {
		return annexName;
	}
	public void setAnnexName(String annexName) {
		this.annexName = annexName;
	}
	public String getGsAnnexType() {
		return gsAnnexType;
	}
	public void setGsAnnexType(String gsAnnexType) {
		this.gsAnnexType = gsAnnexType;
	}
	public String getAnnexUrl() {
		return annexUrl;
	}
	public void setAnnexUrl(String annexUrl) {
		this.annexUrl = annexUrl;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
}
