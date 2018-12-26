package com.yz.model.transfer;

import com.yz.model.common.PubInfo;

/**
 * Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年8月1日.
 *
 */
public class BdStudentOut extends PubInfo {
	private String outId;

	private String learnId;

	private String stdId;

	private String reason;

	private String checkOrder;

	private String checkType;

	private String stdStage;

	private String remark;

	private String fileName;

	private String fileUrl;

	private String isComplete;

	private String createTime;

	private String updateTime;

	private String ext1;

	private String ext2;

	private String ext3;
	
	private String financial_remark;
	
	

	public String getFinancial_remark() {
		return financial_remark;
	}

	public void setFinancial_remark(String financial_remark) {
		this.financial_remark = financial_remark;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId == null ? null : outId.trim();
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId == null ? null : learnId.trim();
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId == null ? null : stdId.trim();
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}

	public String getCheckOrder() {
		return checkOrder;
	}

	public void setCheckOrder(String checkOrder) {
		this.checkOrder = checkOrder == null ? null : checkOrder.trim();
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType == null ? null : checkType.trim();
	}

	public String getStdStage() {
		return stdStage;
	}

	public void setStdStage(String stdStage) {
		this.stdStage = stdStage;
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete == null ? null : isComplete.trim();
	}

}