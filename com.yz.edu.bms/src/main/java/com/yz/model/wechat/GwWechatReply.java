package com.yz.model.wechat;

import java.util.List;

import com.yz.model.common.PubInfo;

public class GwWechatReply extends PubInfo {
	private String replyId;

	private String wechatId;

	private String keyword;

	private String msgType;

	private String content;

	private String mediaId;

	private String articleCount;

	private String status;

	List<GwWechatArticle> articles;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<GwWechatArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<GwWechatArticle> articles) {
		this.articles = articles;
	}

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId == null ? null : replyId.trim();
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId == null ? null : wechatId.trim();
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword == null ? null : keyword.trim();
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType == null ? null : msgType.trim();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId == null ? null : mediaId.trim();
	}

	public String getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(String articleCount) {
		this.articleCount = articleCount == null ? null : articleCount.trim();
	}

}