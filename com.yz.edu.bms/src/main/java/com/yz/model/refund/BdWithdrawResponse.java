package com.yz.model.refund;

public class BdWithdrawResponse extends BdStudentWithdraw {

	private String stdName;
	private String sex;
	private String mobile;
	private String idCard;
	private String empName;
	private String bankOpen;
	private String provinceCode;
	private String cityCode;
	private String unvsName;

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getStdName() {
		return stdName;
	}

	public void setStdName(String stdName) {
		this.stdName = stdName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBankOpen()
	{
		return bankOpen;
	}

	public void setBankOpen(String bankOpen)
	{
		this.bankOpen = bankOpen;
	}

	public String getProvinceCode()
	{
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode)
	{
		this.provinceCode = provinceCode;
	}

	public String getCityCode()
	{
		return cityCode;
	}

	public void setCityCode(String cityCode)
	{
		this.cityCode = cityCode;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}
	
}
