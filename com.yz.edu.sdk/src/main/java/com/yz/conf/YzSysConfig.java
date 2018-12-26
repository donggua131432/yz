package com.yz.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@SuppressWarnings("serial")
@ConfigurationProperties(prefix = "yzsysconfig")
public class YzSysConfig implements java.io.Serializable {
	private String smsSwitch; // 发送短信开关
	private String paymentCouponSwitch; // 优惠券使用开关
	private String zhiMiScale; // 智米兑换现金比例
	private String tempBucket;//阿里云临时BUCKET名称
	private String planSetting;//圆梦、助学计划对应的字典值
	private String bucket;//阿里云正式BUCKET名称
	private String reptAmount;//收据快递费用
	private String addressMaxSize;//添加地址最大数量
	private String signNumber;//签到基数
	private String fileBrowserUrl;//文件浏览路径
	private String salesRemindTime;//兑换/抽奖默认开启提醒时间
	private String submitState;//京东下单,不预占库存 0 预占(可通过接口取消),1 不预占
	private String regCode;//开发票的纳税号
	private String invoicePhone;//京东开票 收增票人电话
	private String invoiceType;//京东发票类型:1 普通发票 2 增值税发票 3 电子发票
	private String recruitYear;//招生年度
	private String lotteryCode;//抽奖活动编码
	
	public String getSmsSwitch() {
		return smsSwitch;
	}
	public void setSmsSwitch(String smsSwitch) {
		this.smsSwitch = smsSwitch;
	}
	public String getPaymentCouponSwitch() {
		return paymentCouponSwitch;
	}
	public void setPaymentCouponSwitch(String paymentCouponSwitch) {
		this.paymentCouponSwitch = paymentCouponSwitch;
	}
	public String getZhiMiScale() {
		return zhiMiScale;
	}
	public void setZhiMiScale(String zhiMiScale) {
		this.zhiMiScale = zhiMiScale;
	}
	public String getTempBucket() {
		return tempBucket;
	}
	public void setTempBucket(String tempBucket) {
		this.tempBucket = tempBucket;
	}
	public String getPlanSetting() {
		return planSetting;
	}
	public void setPlanSetting(String planSetting) {
		this.planSetting = planSetting;
	}
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getReptAmount() {
		return reptAmount;
	}
	public void setReptAmount(String reptAmount) {
		this.reptAmount = reptAmount;
	}
	public String getAddressMaxSize() {
		return addressMaxSize;
	}
	public void setAddressMaxSize(String addressMaxSize) {
		this.addressMaxSize = addressMaxSize;
	}
	public String getSignNumber() {
		return signNumber;
	}
	public void setSignNumber(String signNumber) {
		this.signNumber = signNumber;
	}
	public String getFileBrowserUrl() {
		return fileBrowserUrl;
	}
	public void setFileBrowserUrl(String fileBrowserUrl) {
		this.fileBrowserUrl = fileBrowserUrl;
	}
	public String getSalesRemindTime() {
		return salesRemindTime;
	}
	public void setSalesRemindTime(String salesRemindTime) {
		this.salesRemindTime = salesRemindTime;
	}
	public String getSubmitState() {
		return submitState;
	}
	public void setSubmitState(String submitState) {
		this.submitState = submitState;
	}
	public String getRegCode() {
		return regCode;
	}
	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}
	public String getInvoicePhone() {
		return invoicePhone;
	}
	public void setInvoicePhone(String invoicePhone) {
		this.invoicePhone = invoicePhone;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getRecruitYear() {
		return recruitYear;
	}
	public void setRecruitYear(String recruitYear) {
		this.recruitYear = recruitYear;
	}
	public String getLotteryCode() {
		return lotteryCode;
	}
	public void setLotteryCode(String lotteryCode) {
		this.lotteryCode = lotteryCode;
	}
}
