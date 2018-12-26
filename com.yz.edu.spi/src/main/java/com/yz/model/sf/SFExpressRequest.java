package com.yz.model.sf;

import java.io.Serializable;

public class SFExpressRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5935352183156106121L;

	private String head;

	/** 系统订单号 */
	private String orderid;
	/** 寄件人省份 */
	private String jProvince;
	/** 寄件人城市 */
	private String jCity;
	/** 寄件人地区 */
	private String jCountry;
	/** 寄件人地址 */
	private String jAddress;
	/** 寄件人公式 */
	private String jCompany;
	/** 寄件人名称 */
	private String jContact;
	/** 寄件人电话 */
	private String jTel;
	/** 寄件人手机 -非必填 */
	private String jMobile;
	/** 收件人姓名 */
	private String dContact;
	/** 收件省份 */
	private String dProvince;
	/** 收件城市 */
	private String dCity;
	/** 收件区/县 */
	private String dCountry;
	/** 收件电话 */
	private String dTel;
	/** 收件人手机 -非必填 */
	private String dMobile;
	/** 收件详细地址 */
	private String dAddress;
	/** 备注 -非必填 */
	private String remark;
	/** 顺丰月结卡号 */
	private String custid;

	
	/** 学业id */
	private String learnId;
	
	/** 招生类型*/
	private String recruitType;
	
	/**学期 */
	private String semester;
	
	/**教材类型 */
	private String textbookType;
	/** 付款方式 */
	private String payMethod;


	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getjProvince() {
		return jProvince;
	}

	public void setjProvince(String jProvince) {
		this.jProvince = jProvince;
	}

	public String getjCity() {
		return jCity;
	}

	public void setjCity(String jCity) {
		this.jCity = jCity;
	}

	public String getjCountry() {
		return jCountry;
	}

	public void setjCountry(String jCountry) {
		this.jCountry = jCountry;
	}

	public String getjAddress() {
		return jAddress;
	}

	public void setjAddress(String jAddress) {
		this.jAddress = jAddress;
	}

	public String getjCompany() {
		return jCompany;
	}

	public void setjCompany(String jCompany) {
		this.jCompany = jCompany;
	}

	public String getdProvince() {
		return dProvince;
	}

	public void setdProvince(String dProvince) {
		this.dProvince = dProvince;
	}

	public String getdCity() {
		return dCity;
	}

	public void setdCity(String dCity) {
		this.dCity = dCity;
	}

	public String getdCountry() {
		return dCountry;
	}

	public void setdCountry(String dCountry) {
		this.dCountry = dCountry;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head == null ? "" : head.trim();
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid == null ? "" : orderid.trim();
	}

	public String getjContact() {
		return jContact;
	}

	public void setjContact(String jContact) {
		this.jContact = jContact == null ? "" : jContact.trim();
	}

	public String getjTel() {
		return jTel;
	}

	public void setjTel(String jTel) {
		this.jTel = jTel == null ? "" : jTel.trim();
	}

	public String getjMobile() {
		return jMobile;
	}

	public void setjMobile(String jMobile) {
		this.jMobile = jMobile == null ? "" : jMobile.trim();
	}

	public String getdContact() {
		return dContact;
	}

	public void setdContact(String dContact) {
		this.dContact = dContact == null ? "" : dContact.trim();
	}

	public String getdTel() {
		return dTel;
	}

	public void setdTel(String dTel) {
		this.dTel = dTel == null ? "" : dTel.trim();
	}

	public String getdMobile() {
		return dMobile;
	}

	public void setdMobile(String dMobile) {
		this.dMobile = dMobile == null ? "" : dMobile.trim();
	}

	public String getdAddress() {
		return dAddress;
	}

	public void setdAddress(String dAddress) {
		this.dAddress = dAddress == null ? "" : dAddress.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? "" : remark.trim();
	}

	public String getLearnId()
	{
		return learnId;
	}

	public void setLearnId(String learnId)
	{
		this.learnId = learnId;
	}

	public String getRecruitType()
	{
		return recruitType;
	}

	public void setRecruitType(String recruitType)
	{
		this.recruitType = recruitType;
	}

	public String getSemester()
	{
		return semester;
	}

	public void setSemester(String semester)
	{
		this.semester = semester;
	}

	public String getTextbookType()
	{
		return textbookType;
	}

	public void setTextbookType(String textbookType)
	{
		this.textbookType = textbookType;
	}

}
