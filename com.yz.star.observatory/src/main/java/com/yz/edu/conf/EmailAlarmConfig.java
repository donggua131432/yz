package com.yz.edu.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@SuppressWarnings("serial")
@ConfigurationProperties(prefix = "es.alarm")
public class EmailAlarmConfig implements java.io.Serializable {

	private String host; // 钉邮主机 
	
	private int port; // 端口号 

	private String username;  // 用户名

	private String password; // 密码 
	
	private String receiver;// 接受者列表
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public String getReceiver() {
		return receiver;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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
}
