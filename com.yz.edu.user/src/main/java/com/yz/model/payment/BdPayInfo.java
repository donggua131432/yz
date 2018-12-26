package com.yz.model.payment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.yz.model.coupon.BdCoupon;

public class BdPayInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3007211617657721083L;
	private String learnId;
	private String accDeduction; // 现金账户抵扣金额
	private String zmDeduction;
	private ArrayList<String> itemCodes;
	private String paidAmount;
	private String paymentType;
	private String outSerialNo;
	private String remark;
	private String openId;
	private String userId;
	private ArrayList<String> years;
	private List<BdCoupon> coupons;

	public List<BdCoupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<BdCoupon> coupons) {
		this.coupons = coupons;
	}

	private String tradeType;

	private String empId;

	public String getOpenId() {
		return openId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getZmDeduction() {
		return zmDeduction;
	}

	public void setZmDeduction(String zmDeduction) {
		this.zmDeduction = zmDeduction;
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

	public ArrayList<String> getItemCodes() {
		return itemCodes;
	}

	public void setItemCodes(ArrayList<String> itemCodes) {
		this.itemCodes = itemCodes;
	}

	public ArrayList<String> getYears() {
		return years;
	}

	public void setYears(ArrayList<String> years) {
		this.years = years;
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

}
