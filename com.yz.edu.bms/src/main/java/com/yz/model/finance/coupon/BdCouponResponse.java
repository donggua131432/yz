package com.yz.model.finance.coupon;

import java.util.List;

import com.yz.model.finance.fee.FeeItemResponse;
import com.yz.model.finance.fee.PfsnInfoResponse;
import com.yz.model.finance.fee.TestAreaResponse;

public class BdCouponResponse extends BdCoupon {

	private List<TestAreaResponse> taInfo;

	private List<PfsnInfoResponse> pfsnInfo;

	private List<FeeItemResponse> items;

	public List<FeeItemResponse> getItems() {
		return items;
	}

	public void setItems(List<FeeItemResponse> items) {
		this.items = items;
	}

	public List<TestAreaResponse> getTaInfo() {
		return taInfo;
	}

	public void setTaInfo(List<TestAreaResponse> taInfo) {
		this.taInfo = taInfo;
	}

	public List<PfsnInfoResponse> getPfsnInfo() {
		return pfsnInfo;
	}

	public void setPfsnInfo(List<PfsnInfoResponse> pfsnInfo) {
		this.pfsnInfo = pfsnInfo;
	}

}
