package com.yz.model.operate.banner;

import com.yz.model.common.PubInfo;

/**
 * 首页轮播图 Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年8月7日.
 *
 */
public class BdBanner extends PubInfo {

	private String bannerId;
	private String bannerName;
	private String bannerDesc;
	private String bannerUrl;
	private String sort;
	private String redirectUrl;
	private String bannerType;
	private String isAllow;

	private String isPhotoChange;

	private Object bannerPic;

	public Object getBannerPic() {
		return bannerPic;
	}

	public void setBannerPic(Object bannerPic) {
		this.bannerPic = bannerPic;
	}

	public String getIsPhotoChange() {
		return isPhotoChange;
	}

	public void setIsPhotoChange(String isPhotoChange) {
		this.isPhotoChange = isPhotoChange;
	}

	public String getBannerId() {
		return bannerId;
	}

	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

	public String getBannerDesc() {
		return bannerDesc;
	}

	public void setBannerDesc(String bannerDesc) {
		this.bannerDesc = bannerDesc;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getBannerType() {
		return bannerType;
	}

	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}

	public String getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow;
	}

}
