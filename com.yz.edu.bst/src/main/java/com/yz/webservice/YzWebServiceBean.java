package com.yz.webservice;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 
 * @author Administrator
 *
 */
@ConfigurationProperties(prefix="webservice")
public class YzWebServiceBean {

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	
}
