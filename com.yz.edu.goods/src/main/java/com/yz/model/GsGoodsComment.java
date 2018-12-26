package com.yz.model;

import java.io.Serializable;
import java.util.List;

/**
 * 评论信息表
 * @author lx
 * @date 2017年7月24日 下午5:59:05
 */
public class GsGoodsComment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4523097728364300108L;
	
	private String userName;
	private String commentTime;
	private String commentContent;
	private String headImgUrl;
	private String commentId;
	
	private List<GsCommentReply> reply;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public List<GsCommentReply> getReply()
	{
		return reply;
	}

	public void setReply(List<GsCommentReply> reply)
	{
		this.reply = reply;
	}

}
