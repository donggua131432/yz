package com.yz.model;

import java.io.Serializable;

/**
 * 商品附件信息
 * @author lx
 * @date 2017年7月24日 下午5:39:51
 */
public class GsGoodsAnnexInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6488981810908435659L;

	private String annexId;
	private String gsAnnexType;
	private String annexUrl;
	
	public String getAnnexId() {
		return annexId;
	}
	public void setAnnexId(String annexId) {
		this.annexId = annexId;
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
}
