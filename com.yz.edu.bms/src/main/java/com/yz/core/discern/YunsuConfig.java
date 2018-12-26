package com.yz.core.discern;

public class YunsuConfig {

	private String username;
	private String password;
	private String softid;
	private String softkey;
	
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
		this.password = YunSu.MD5(password);
	}
	public String getSoftid() {
		return softid;
	}
	public void setSoftid(String softid) {
		this.softid = softid;
	}
	public String getSoftkey() {
		return softkey;
	}
	public void setSoftkey(String softkey) {
		this.softkey = softkey;
	}
}
