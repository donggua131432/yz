package com.yz.model;

/**
 * 批量推送微信公众号消息
 * @author lx
 * @date 2018年4月11日 下午2:47:48
 */
@SuppressWarnings("serial")
public class MsgPubVo extends BaseEvent {

	private String msgId;              //对应的操作id(例如：提醒未完成,即代表当前任务id)
	
	private String msgType;           //消息类型,决定去哪里取数据
	
	private String msgTitle;          //标题
	
	private String msgContent;        //内容
	
	private String msgName;          //消息名称
	
	private String msgCode;          //code("YZ")
	
	private String url;              //跳转url
	
	private String ext1;             //预留字段1
	
	private String ext2;            //预留字段2
	
	public MsgPubVo()
	{
		setTrace(false);
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getMsgName() {
		return msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}
}
