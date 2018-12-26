package com.yz.model.admin;

import java.io.Serializable;

import com.yz.model.common.PubInfo;

public class BmsFunc extends PubInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5461077885684477452L;

	private String funcId;

	private String funcName;

	private String funcType;

	private String funcUrl;

	private String funcCode;

	private String pId;

	private String icon;

	private String updateUserId;

	private String updateUser;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName == null ? null : funcName.trim();
	}

	public String getFuncType() {
		return funcType;
	}

	public void setFuncType(String funcType) {
		this.funcType = funcType == null ? null : funcType.trim();
	}

	public String getFuncUrl() {
		return funcUrl;
	}

	public void setFuncUrl(String funcUrl) {
		this.funcUrl = funcUrl == null ? null : funcUrl.trim();
	}

	public String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(String funcCode) {
		this.funcCode = funcCode == null ? null : funcCode.trim();
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

}