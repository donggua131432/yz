package com.yz.vo;

import java.io.Serializable;
import java.util.List;

public class LoginUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7808369757449516436L;
	/** 用户Id */
	private String userId;
	/** 用户名 */
	private String username;

	private String password;
	
	private String phone;
	
	private String idcard;
	
	private String stdId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}
	
	
	
	
}
