package com.yz.service.wechat;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.GlobalConstants;
import com.yz.dao.wechat.GwWechatMsgMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.wechat.GwWechatArticle;
import com.yz.model.wechat.GwWechatMsgQuery;
import com.yz.model.wechat.GwWechatReply;

@Service
@Transactional
public class GwWechatMsgService {

	@Autowired
	private GwWechatMsgMapper msgMapper;

	public Object selectWechatMsgByPage(int start, int length, GwWechatMsgQuery query) {
		PageHelper.offsetPage(start, length);
		List<Map<String, Object>> list = msgMapper.selectWechatMsgByPage(query);

		return new IPageInfo<Map<String, Object>>((Page<Map<String, Object>>) list);
	}

	public void addWechatReply(GwWechatReply reply) {

		if (GlobalConstants.WECHAT_REPLY_TYPE_TEXT.equals(reply.getMsgType())) { // 文本消息
			reply.setReplyId(IDGenerator.generatorId());
			msgMapper.insertTextReply(reply);
		} else if (GlobalConstants.WECHAT_REPLY_TYPE_NEWS.equals(reply.getMsgType())) { // 图文消息
			reply.setArticleCount(String.valueOf(reply.getArticles().size()));
			reply.setReplyId(IDGenerator.generatorId());
			for (GwWechatArticle a : reply.getArticles()) {
				a.setArticleId(IDGenerator.generatorId());
			}
			msgMapper.insertNewsReply(reply);
		}

	}

	public GwWechatReply selectReply(String replyId) {
		return msgMapper.selectWechatReply(replyId);
	}

	public void updateWechatReply(GwWechatReply reply) {
		if (GlobalConstants.WECHAT_REPLY_TYPE_TEXT.equals(reply.getMsgType())) { // 文本消息
			msgMapper.updateTextReply(reply);
		} else if (GlobalConstants.WECHAT_REPLY_TYPE_NEWS.equals(reply.getMsgType())) { // 图文消息
			reply.setArticleCount(String.valueOf(reply.getArticles().size()));
			for (GwWechatArticle a : reply.getArticles()) {
				a.setArticleId(IDGenerator.generatorId());
			}
			msgMapper.updateNewsReply(reply);
		}
	}

	public void blockReply(GwWechatReply reply) {
		msgMapper.blockReply(reply);
	}

	public void deleteReply(String replyId) {
		msgMapper.deleteReply(replyId);
	}

}
