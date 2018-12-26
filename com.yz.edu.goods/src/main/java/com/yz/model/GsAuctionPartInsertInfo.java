package com.yz.model;
/**
 * 竞拍插入信息
 * @author lx
 * @date 2017年7月25日 下午7:52:31
 */
public class GsAuctionPartInsertInfo extends GsAuctionPart {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5264170230189215761L;
	private String salesId;
	private String headImgUrl;
	private String planCount;
	private String openId;
	
	public String getSalesId() {
		return salesId;
	}
	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getPlanCount()
	{
		return planCount;
	}
	public void setPlanCount(String planCount)
	{
		this.planCount = planCount;
	}
	public String getOpenId()
	{
		return openId;
	}
	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
	
}
