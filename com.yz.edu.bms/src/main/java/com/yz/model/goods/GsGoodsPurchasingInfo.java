package com.yz.model.goods;

import java.io.Serializable;

/**
 * 采购列表信息
 * @author lx
 * @date 2018年5月15日 上午10:57:04
 */
public class GsGoodsPurchasingInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6824395136374315131L;

	private String id;
	private String applyName;
	private String applyReason;
	private String annexPath;
	private String goodsSkuId;
	private String goodsName;
	private String goodsNum;
	private String goodsPrice;
	private String totalPrice;
	private String receiveName;
	private String receiveMobile;
	private String province;
	private String city;
	private String district;
	private String street;
	private String provinceName;
	private String cityName;
	private String districtName;
	private String streetName;
	private String address;
	private String operUserName;
	private String operUserId;
	private String operTime;
	private String email;
	private String jdGoodsType;
	private String remark;
	private String ifSuccess;
	private String jdOrderNo;
	
	private String isPhotoChange;        //是否修改
	
	private Object annexUrl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public String getAnnexPath() {
		return annexPath;
	}
	public void setAnnexPath(String annexPath) {
		this.annexPath = annexPath;
	}
	public String getGoodsSkuId() {
		return goodsSkuId;
	}
	public void setGoodsSkuId(String goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
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
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOperUserName() {
		return operUserName;
	}
	public void setOperUserName(String operUserName) {
		this.operUserName = operUserName;
	}
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
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
	public void setAnnexUrl(Object annexUrl) {
		this.annexUrl = annexUrl;
	}
	public String getOperUserId() {
		return operUserId;
	}
	public void setOperUserId(String operUserId) {
		this.operUserId = operUserId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getJdGoodsType() {
		return jdGoodsType;
	}
	public void setJdGoodsType(String jdGoodsType) {
		this.jdGoodsType = jdGoodsType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIfSuccess() {
		return ifSuccess;
	}
	public void setIfSuccess(String ifSuccess) {
		this.ifSuccess = ifSuccess;
	}
	public String getJdOrderNo() {
		return jdOrderNo;
	}
	public void setJdOrderNo(String jdOrderNo) {
		this.jdOrderNo = jdOrderNo;
	}
}
