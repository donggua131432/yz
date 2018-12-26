package com.yz.model.finance.receipt;

public class BdSfPrint {

	private String mailno;
	private String mailnoSpace;
	private String destCode;
	private String dAddress;
	private String dContact;
	private String dMobile;
	private String grade;
	private String unvsName;
	private String pfsnCode;
	private String pfsnName;
	private String pfsnLevel;
	private String recruitType;

	private String province;
	private String city;
	private String district;

	private String custid;
	
	private String goodsName;
	private String goodsNum;

	private String textbookType;
	private String empName;
	private String telNo;
	private String sendId;
	private String textBookName;
	private String textbookPfsncode;//教材专业编码
	
	//京东打印字段
	private String sourcetSortCenterName;// 始 发 分 拣 中 心 名 称 
	private String originalCrossCode; //始发道口号
	private String originalTabletrolleyCode;//始发笼车号
	private String targetSortCenterName;// 目 的 分 拣 中 心 名 称
	private String destinationCrossCode;//：目的道口号
	private String destinationTabletrolleyCode;//-目的笼车号
	private String siteName;//站点名称
	private String road;//路区
	private String printdate;
	private String agingName;//时效名称 
	private Integer aging;//时效
	public String getCustid() {
		return custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getMailnoSpace() {
		return mailnoSpace;
	}

	public void setMailnoSpace(String mailnoSpace) {
		this.mailnoSpace = mailnoSpace;
	}

	public String getMailno() {
		return mailno;
	}

	public void setMailno(String mailno) {
		this.mailno = mailno;
	}

	public String getDestCode() {
		return destCode;
	}

	public void setDestCode(String destCode) {
		this.destCode = destCode;
	}

	public String getdAddress() {
		return dAddress;
	}

	public void setdAddress(String dAddress) {
		this.dAddress = dAddress;
	}

	public String getdContact() {
		return dContact;
	}

	public void setdContact(String dContact) {
		this.dContact = dContact;
	}

	public String getdMobile() {
		return dMobile;
	}

	public void setdMobile(String dMobile) {
		this.dMobile = dMobile;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getUnvsName() {
		return unvsName;
	}

	public void setUnvsName(String unvsName) {
		this.unvsName = unvsName;
	}

	public String getPfsnCode() {
		return pfsnCode;
	}

	public void setPfsnCode(String pfsnCode) {
		this.pfsnCode = pfsnCode;
	}

	public String getPfsnName() {
		return pfsnName;
	}

	public void setPfsnName(String pfsnName) {
		this.pfsnName = pfsnName;
	}

	public String getPfsnLevel() {
		return pfsnLevel;
	}

	public void setPfsnLevel(String pfsnLevel) {
		this.pfsnLevel = pfsnLevel;
	}

	public String getRecruitType() {
		return recruitType;
	}

	public void setRecruitType(String recruitType) {
		this.recruitType = recruitType;
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

	public String getGoodsName()
	{
		return goodsName;
	}

	public void setGoodsName(String goodsName)
	{
		this.goodsName = goodsName;
	}

	public String getGoodsNum()
	{
		return goodsNum;
	}

	public void setGoodsNum(String goodsNum)
	{
		this.goodsNum = goodsNum;
	}

	public String getTextbookType()
	{
		return textbookType;
	}

	public void setTextbookType(String textbookType)
	{
		this.textbookType = textbookType;
	}

	public String getEmpName()
	{
		return empName;
	}

	public void setEmpName(String empName)
	{
		this.empName = empName;
	}

	public String getTelNo()
	{
		return telNo;
	}

	public void setTelNo(String telNo)
	{
		this.telNo = telNo;
	}

	public String getSendId()
	{
		return sendId;
	}

	public void setSendId(String sendId)
	{
		this.sendId = sendId;
	}

	public String getTextBookName()
	{
		return textBookName;
	}

	public void setTextBookName(String textBookName)
	{
		this.textBookName = textBookName;
	}

	public String getSourcetSortCenterName() {
		return sourcetSortCenterName;
	}

	public void setSourcetSortCenterName(String sourcetSortCenterName) {
		this.sourcetSortCenterName = sourcetSortCenterName;
	}

	public String getOriginalCrossCode() {
		return originalCrossCode;
	}

	public void setOriginalCrossCode(String originalCrossCode) {
		this.originalCrossCode = originalCrossCode;
	}

	public String getOriginalTabletrolleyCode() {
		return originalTabletrolleyCode;
	}

	public void setOriginalTabletrolleyCode(String originalTabletrolleyCode) {
		this.originalTabletrolleyCode = originalTabletrolleyCode;
	}

	public String getTargetSortCenterName() {
		return targetSortCenterName;
	}

	public void setTargetSortCenterName(String targetSortCenterName) {
		this.targetSortCenterName = targetSortCenterName;
	}

	public String getDestinationCrossCode() {
		return destinationCrossCode;
	}

	public void setDestinationCrossCode(String destinationCrossCode) {
		this.destinationCrossCode = destinationCrossCode;
	}

	public String getDestinationTabletrolleyCode() {
		return destinationTabletrolleyCode;
	}

	public void setDestinationTabletrolleyCode(String destinationTabletrolleyCode) {
		this.destinationTabletrolleyCode = destinationTabletrolleyCode;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getRoad() {
		return road;
	}

	public void setRoad(String road) {
		this.road = road;
	}

	public String getPrintdate() {
		return printdate;
	}

	public void setPrintdate(String printdate) {
		this.printdate = printdate;
	}

	public String getAgingName() {
		return agingName;
	}

	public void setAgingName(String agingName) {
		this.agingName = agingName;
	}

	public Integer getAging() {
		return aging;
	}

	public void setAging(Integer aging) {
		this.aging = aging;
	}

	public String getTextbookPfsncode() {
		return textbookPfsncode;
	}

	public void setTextbookPfsncode(String textbookPfsncode) {
		this.textbookPfsncode = textbookPfsncode;
	}
	
	
	
}
