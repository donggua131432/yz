package com.yz.core.security.model;

public class ResultInfo {

	private String errCode;
	private String errMsg;
	
	public ResultInfo(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}
}
