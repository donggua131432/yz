package com.yz.model.zhimi;

import com.yz.model.common.PubInfo;

public class ZhimiProductInfo extends PubInfo {

	private String productId;

    private String productName;

    private String productDesc;
    
    private String price;

    private String zhimi;

    private String isAllow;

    private Integer sort;
    
    private Integer oldSort;
    

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
	 * @return the productDesc
	 */
	public String getProductDesc() {
		return productDesc;
	}

	/**
	 * @param productDesc the productDesc to set
	 */
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getZhimi() {
        return zhimi;
    }

    public void setZhimi(String zhimi) {
        this.zhimi = zhimi == null ? null : zhimi.trim();
    }

    public String getIsAllow() {
        return isAllow;
    }

    public void setIsAllow(String isAllow) {
        this.isAllow = isAllow == null ? null : isAllow.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

	/**
	 * @return the oldSort
	 */
	public Integer getOldSort() {
		return oldSort;
	}

	/**
	 * @param oldSort the oldSort to set
	 */
	public void setOldSort(Integer oldSort) {
		this.oldSort = oldSort;
	}
}
