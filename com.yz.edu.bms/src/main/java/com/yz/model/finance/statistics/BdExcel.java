package com.yz.model.finance.statistics;

import java.util.Date;

import com.yz.model.common.PubInfo;

public class BdExcel extends PubInfo {
	private String excelId;

	private String excelName;

	private String excelUrl;

	private String excelType;

	private String exportStatus;

	private String remark;

	private String updateTime;

	private String createTime;

	public String getExcelId() {
		return excelId;
	}

	public void setExcelId(String excelId) {
		this.excelId = excelId == null ? null : excelId.trim();
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName == null ? null : excelName.trim();
	}

	public String getExcelUrl() {
		return excelUrl;
	}

	public void setExcelUrl(String excelUrl) {
		this.excelUrl = excelUrl == null ? null : excelUrl.trim();
	}

	public String getExcelType() {
		return excelType;
	}

	public void setExcelType(String excelType) {
		this.excelType = excelType == null ? null : excelType.trim();
	}

	public String getExportStatus() {
		return exportStatus;
	}

	public void setExportStatus(String exportStatus) {
		this.exportStatus = exportStatus == null ? null : exportStatus.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}