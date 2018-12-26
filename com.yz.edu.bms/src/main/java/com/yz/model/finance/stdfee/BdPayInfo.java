package com.yz.model.finance.stdfee;

import java.util.List;

import com.yz.model.finance.coupon.BdCoupon;

public class BdPayInfo {

	private String learnId;
	private String accDeduction; // 滞留账户抵扣金额
	private String zmDeduction; // 智米抵扣
	private String[] itemCodes;
	private String paidAmount;
	private String paymentType;
	private String outSerialNo;
	private String remark;
	private String[] years;
	private Object couponObj;
	private String tradeType;
	private String empId;

	private List<BdCoupon> coupons;
	private String couponsStr;

	public String getCouponsStr() {
		return couponsStr;
	}

	public void setCouponsStr(String couponsStr) {
		this.couponsStr = couponsStr;
	}

	public Object getCouponObj() {
		return couponObj;
	}

	public void setCouponObj(Object couponObj) {
		this.couponObj = couponObj;
	}

	public List<BdCoupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<BdCoupon> coupons) {
		this.coupons = coupons;
	}

	private boolean isGiveZhimi = true;

	public String getZmDeduction() {
		return zmDeduction;
	}

	public void setZmDeduction(String zmDeduction) {
		this.zmDeduction = zmDeduction;
	}

	public String[] getYears() {
		return years;
	}

	public void setYears(String[] years) {
		this.years = years;
	}

	public String getAccDeduction() {
		return accDeduction;
	}

	public void setAccDeduction(String accDeduction) {
		this.accDeduction = accDeduction;
	}

	public String getOutSerialNo() {
		return outSerialNo;
	}

	public void setOutSerialNo(String outSerialNo) {
		this.outSerialNo = outSerialNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

	public String[] getItemCodes() {
		return itemCodes;
	}

	public void setItemCodes(String[] itemCodes) {
		this.itemCodes = itemCodes;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public boolean getGiveZhimi() {
		return isGiveZhimi;
	}

	public void setGiveZhimi(boolean giveZhimi) {
		isGiveZhimi = giveZhimi;
	}
}
