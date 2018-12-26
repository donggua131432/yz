package com.yz.model;
/**
 * 兑换记录插入
 * @author lx
 * @date 2017年7月27日 上午10:30:04
 */
public class GsExchangePartInsertInfo extends GsExChangePart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1873850397012687696L;
	
	private String salesId;
	private String userId;
	private String headImgUrl;
	
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

}
