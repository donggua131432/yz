package com.yz.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;


/**
 * @desc 微信推送msg
 * @author Administrator
 *
 */
public class WechatMsgVo extends BaseEvent implements java.io.Serializable {
 

	/**
	 * 
	 */
	private static final long serialVersionUID = -3256296268659214120L;

	private String touser;                    //openId

	private String templateId;               //对应数据库里的templateId,通过其获取微信对应模板id

	private List<NameValuePari> data;        //待替换的数据(模板消息)
	
	private Map<String, String> contentData; //文本数据(客服消息)
	
	private boolean ifUseTemplateUlr;        //是否使用模板url
	
	private String ext1;                    //预扩展字段
	 
	private String ext2;                    //预扩展字段 
	
	private Map<String, String> postBackData;  //回传数据
	
	public WechatMsgVo() {
		this.data = Lists.newArrayList();
		setTrace(false);
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public List<NameValuePari> getData() {
		return data;
	}

	public void setData(List<NameValuePari> data) {
		this.data = data;
	}
	public WechatMsgVo addData(String key, String value) {
		NameValuePari nv = new NameValuePari();
		nv.setKey(key);
		nv.setValue(value);
		this.data.add(nv);
		return this;
	}

	public Map<String, String> getContentData() {
		return contentData;
	}

	public void setContentData(Map<String, String> contentData) {
		this.contentData = contentData;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public boolean isIfUseTemplateUlr() {
		return ifUseTemplateUlr;
	}

	public void setIfUseTemplateUlr(boolean ifUseTemplateUlr) {
		this.ifUseTemplateUlr = ifUseTemplateUlr;
	}

	public Map<String, String> getPostBackData() {
		return postBackData;
	}

	public void setPostBackData(Map<String, String> postBackData) {
		this.postBackData = postBackData;
	}
}
