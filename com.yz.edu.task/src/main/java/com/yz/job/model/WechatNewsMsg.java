package com.yz.job.model;

import java.util.List;

public class WechatNewsMsg extends BaseWechatMsg {

	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<WechatArticle> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<WechatArticle> getArticles() {
		return Articles;
	}

	public void setArticles(List<WechatArticle> articles) {
		Articles = articles;
	}

	@Override
	public String toString() {

		StringBuffer msg = new StringBuffer();
		msg.append("<xml>");
		msg.append("<ToUserName>" + getFromUserName() + "</ToUserName>");
		msg.append("<FromUserName>" + getToUserName() + "</FromUserName>");
		msg.append("<CreateTime>" + System.currentTimeMillis() + "</CreateTime>");
		msg.append("<MsgType>news</MsgType>");
		msg.append("<ArticleCount>" + Articles.size() + "</ArticleCount>");
		msg.append("<Articles>");

		for (WechatArticle a : Articles) {
			msg.append("<item>");
			msg.append("<Title>" + a.getTitle() + "</Title>");
			msg.append("<Description>" + a.getDescription() + "</Description>");
			msg.append("<PicUrl>" + a.getPicUrl() + "</PicUrl>");
			msg.append("<Url>" + a.getUrl() + "</Url>");
			msg.append("</item>");

		}

		msg.append("</Articles>");

		msg.append("</xml>");
		return msg.toString();
	}

}
