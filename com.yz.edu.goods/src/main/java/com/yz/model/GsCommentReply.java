package com.yz.model;

import java.io.Serializable;

/**
 * 评论回复表
 * @author lx
 * @date 2017年7月24日 下午6:04:34
 */
public class GsCommentReply implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1616930323641647590L;
	private String replyContent;
	private String replyTime;
	private String userName;
	private String headImgUrl;
	
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
}
