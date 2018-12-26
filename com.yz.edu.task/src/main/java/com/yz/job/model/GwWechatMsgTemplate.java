package com.yz.job.model;

public class GwWechatMsgTemplate {

	private String templateId;

	private String templateCode;

	private String title;

	private String url;
	
	private String dataTemplate;  //数据模板

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDataTemplate() {
		return dataTemplate;
	}

	public void setDataTemplate(String dataTemplate) {
		this.dataTemplate = dataTemplate;
	}

}
