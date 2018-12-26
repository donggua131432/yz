package com.yz.model.wechat;

import java.util.HashMap;
import java.util.Map;

import com.yz.util.JsonUtil;

public class GwWechatButton {

	private String name;
	private String type;
	private String key;
	private String url;

	private GwWechatSubButton[] sub_button;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public GwWechatSubButton[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(GwWechatSubButton[] sub_button) {
		this.sub_button = sub_button;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public static void main(String[] args) {
		GwWechatButton[] button = { new GwWechatButton() };

		button[0].setName("菜单");

		GwWechatSubButton suba = new GwWechatSubButton();
		suba.setName("百度");
		suba.setType("view");
		suba.setUrl("http://www.baidu.com/");
		GwWechatSubButton[] sub_button = { suba };

		button[0].setSub_button(sub_button);
		Map<String, Object> o = new HashMap<String, Object>();
		o.put("button", button);

		System.err.println(JsonUtil.object2String(o));

	}

}
