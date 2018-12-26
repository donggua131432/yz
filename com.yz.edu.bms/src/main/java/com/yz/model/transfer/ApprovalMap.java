package com.yz.model.transfer;

import java.util.List;

import com.yz.model.finance.stdfee.BdStdPayInfoResponse;

public class ApprovalMap {
	private String outId;
	private String checkStatus;
	private String reason;
	
	private String financial_remark;

	private String learnId;
	private List<BdStdPayInfoResponse> items;


	public String getFinancial_remark() {
		return financial_remark;
	}

	public void setFinancial_remark(String financial_remark) {
		this.financial_remark = financial_remark;
	}

	public List<BdStdPayInfoResponse> getItems() {
		return items;
	}

	public void setItems(List<BdStdPayInfoResponse> items) {
		this.items = items;
	}

	public String getOutId() {
		return outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLearnId() {
		return learnId;
	}

	public void setLearnId(String learnId) {
		this.learnId = learnId;
	}

}
