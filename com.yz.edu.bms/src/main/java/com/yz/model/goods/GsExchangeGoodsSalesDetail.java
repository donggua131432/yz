package com.yz.model.goods;

/**
 * 兑换的编辑
 * @author lx
 * @date 2017年8月1日 下午3:47:53
 */
public class GsExchangeGoodsSalesDetail extends GsExchangeGoodsSales {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7530186955991068140L;
	private String goodsType;
	private String goodsId;
	private String skuId;
	private String goodsName;
	private String goodsDesc;
	private String goodsContent;
	private String onceCount;
	private String showAfterOver;
	
	private String interval;
	private Object coverUrl;
	private String coverUrlPortrait;     //头像
	private String isPhotoChange;        //是否修改
	
	private String showSeq;
	
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public String getGoodsContent() {
		return goodsContent;
	}
	public void setGoodsContent(String goodsContent) {
		this.goodsContent = goodsContent;
	}
	public String getOnceCount() {
		return onceCount;
	}
	public void setOnceCount(String onceCount) {
		this.onceCount = onceCount;
	}
	public String getShowAfterOver() {
		return showAfterOver;
	}
	public void setShowAfterOver(String showAfterOver) {
		this.showAfterOver = showAfterOver;
	}
	public Object getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(Object coverUrl) {
		this.coverUrl = coverUrl;
	}
	public String getCoverUrlPortrait() {
		return coverUrlPortrait;
	}
	public void setCoverUrlPortrait(String coverUrlPortrait) {
		this.coverUrlPortrait = coverUrlPortrait;
	}
	public String getIsPhotoChange() {
		return isPhotoChange;
	}
	public void setIsPhotoChange(String isPhotoChange) {
		this.isPhotoChange = isPhotoChange;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getShowSeq()
	{
		return showSeq;
	}
	public void setShowSeq(String showSeq)
	{
		this.showSeq = showSeq;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
}
