package com.yz.model.condition.common;

/**
 * 下拉查询实体
 * @author Administrator
 *
 */
public class SelectQueryInfo {
	private String sId;
	private String sName;
	private String sCode;
	private int page;
	private int rows;
	
	private String ext1;
	private String ext2;
	private String ext3;
	
	public String getsId() {
		return sId;
	}
	public void setsId(String sId) {
		this.sId = sId == null ? null : sId.trim();
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName == null ? null : sName.trim();
	}
	public String getsCode() {
		return sCode;
	}
	public void setsCode(String sCode) {
		this.sCode = sCode == null ? null : sCode.trim();
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
}
