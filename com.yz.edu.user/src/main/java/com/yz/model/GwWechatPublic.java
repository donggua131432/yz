package com.yz.model;

public class GwWechatPublic {
	private String pubId;

	private String pubName;

	private String pubType;

	private String appId;

	private String appSecret;

	private String welcome;

	private String accessToken;

	private String tokenExpire;

	private String jsapiTicket;

	private String ticketExpire;

	private String isAllow;

	public String getJsapiTicket() {
		return jsapiTicket;
	}

	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}

	public String getTicketExpire() {
		return ticketExpire;
	}

	public void setTicketExpire(String ticketExpire) {
		this.ticketExpire = ticketExpire;
	}

	public String getPubId() {
		return pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId == null ? null : pubId.trim();
	}

	public String getPubName() {
		return pubName;
	}

	public void setPubName(String pubName) {
		this.pubName = pubName == null ? null : pubName.trim();
	}

	public String getPubType() {
		return pubType;
	}

	public void setPubType(String pubType) {
		this.pubType = pubType == null ? null : pubType.trim();
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId == null ? null : appId.trim();
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret == null ? null : appSecret.trim();
	}

	public String getWelcome() {
		return welcome;
	}

	public void setWelcome(String welcome) {
		this.welcome = welcome == null ? null : welcome.trim();
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken == null ? null : accessToken.trim();
	}

	public String getTokenExpire() {
		return tokenExpire;
	}

	public void setTokenExpire(String tokenExpire) {
		this.tokenExpire = tokenExpire == null ? null : tokenExpire.trim();
	}

	public String getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(String isAllow) {
		this.isAllow = isAllow == null ? null : isAllow.trim();
	}

}