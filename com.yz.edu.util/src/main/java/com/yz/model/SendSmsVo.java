package com.yz.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @desc 发送短信vo
 * @author lingdian
 *
 */
@SuppressWarnings("serial")
public class SendSmsVo extends BaseEvent {

	private List<String> mobiles; // 需要发送的电话号码

	private String templateId; // 模板Id

	private Map<String, String> content; // 发送参数

	private boolean voice = false; // 语音短信

	public SendSmsVo() {
		mobiles = Lists.newArrayList();
		content = Maps.newHashMap();
		setTrace(false);
	}

	public boolean isVoice() {
		return voice;
	}

	public void setVoice(boolean voice) {
		this.voice = voice;
	}

	public List<String> getMobiles() {
		return mobiles;
	}

	public void setMobiles(List<String> mobiles) {
		this.mobiles = mobiles;
	}

	public void addMobiles(List<String> mobiles) {
		this.mobiles.addAll(mobiles);
	}

	public void setMobiles(String mobile) {
		this.mobiles.add(mobile);
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Map<String, String> getContent() {
		return content;
	}

	public void setContent(Map<String, String> content) {
		this.content = content;
	}

}
