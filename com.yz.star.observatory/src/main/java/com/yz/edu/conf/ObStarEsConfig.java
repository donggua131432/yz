package com.yz.edu.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@SuppressWarnings("serial")
@ConfigurationProperties(prefix = "es.cluster")
public class ObStarEsConfig implements java.io.Serializable{
   
	 private String name; // 集群名称
	 
	 private String addr; // 集群地址 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	} 
}
