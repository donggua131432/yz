package com.yz.model.goods;

import java.io.Serializable;
import java.util.List;

import com.yz.model.common.PubInfo;
/**
 * 商品插入信息
 * @author lx
 * @date 2017年7月28日 下午5:36:33
 */
public class GsGoodsInsertInfo extends PubInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1278757510983922065L;
	
	private String goodsId;
	private String goodsName;
	private String goodsDesc;
	private String goodsContent;
	private String goodsType;
	private String goodsUse;
	private String unit;
	private String costPrice;
	private String originalPrice;
	private String goodsCount;
	private String isAllow;
	
	private String annexUrlPortrait;     //头像
	private String isPhotoChange;        //是否修改
	
	private Object annexUrl;

	private List<GsGoodsAnnex> annexList;
	private String[] goodsUses = new String[]{};
	
	private String startTime;
	private String endTime;
	private String location;
	private String takein;
	private String address;
	private String courseType;
	private String skuId;//京东商品编号
	private String jdGoodsType;//京东商品类型
	
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
	public String getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}
	public String getGoodsUse() {
		return goodsUse;
	}
	public void setGoodsUse(String goodsUse) {
		this.goodsUse = goodsUse;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
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
	public String getIsAllow()
	{
		return isAllow;
	}
	public void setIsAllow(String isAllow)
	{
		this.isAllow = isAllow;
	}
	public String getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(String goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getAnnexUrlPortrait() {
		return annexUrlPortrait;
	}
	public void setAnnexUrlPortrait(String annexUrlPortrait) {
		this.annexUrlPortrait = annexUrlPortrait;
	}
	public String getIsPhotoChange() {
		return isPhotoChange;
	}
	public void setIsPhotoChange(String isPhotoChange) {
		this.isPhotoChange = isPhotoChange;
	}
	public Object getAnnexUrl() {
		return annexUrl;
	}
	public List<GsGoodsAnnex> getAnnexList()
	{
		return annexList;
	}
	public void setAnnexList(List<GsGoodsAnnex> annexList)
	{
		this.annexList = annexList;
	}
	public void setAnnexUrl(Object annexUrl) {
		this.annexUrl = annexUrl;
	}
	public String[] getGoodsUses() {
		return goodsUses;
	}
	public void setGoodsUses(String[] goodsUses) {
		this.goodsUses = goodsUses;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTakein() {
		return takein;
	}
	public void setTakein(String takein) {
		this.takein = takein;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getJdGoodsType() {
		return jdGoodsType;
	}
	public void setJdGoodsType(String jdGoodsType) {
		this.jdGoodsType = jdGoodsType;
	}
	
}
