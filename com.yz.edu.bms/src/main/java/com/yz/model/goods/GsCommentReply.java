package com.yz.model.goods;

import java.io.Serializable;
/**
 * 评论回复
 * @author lx
 * @date 2017年8月3日 上午9:10:23
 */
public class GsCommentReply implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5453608216310720295L;
	
	private String replyId;
	private String replyContent;
	private String replyTime;
	private String userId;
	private String headImgUrl;
	private String userName;
	private String commentId;
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

}
