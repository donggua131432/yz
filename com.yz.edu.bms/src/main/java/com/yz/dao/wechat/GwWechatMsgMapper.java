package com.yz.dao.wechat;

import java.util.List;
import java.util.Map;

import com.yz.model.wechat.GwWechatMsgQuery;
import com.yz.model.wechat.GwWechatReply;

public interface GwWechatMsgMapper {

	List<Map<String, Object>> selectWechatMsgByPage(GwWechatMsgQuery query);

	int insertTextReply(GwWechatReply reply);

	int insertNewsReply(GwWechatReply reply);

	GwWechatReply selectWechatReply(String replyId);

	int deleteReply(String replyId);

	int updateTextReply(GwWechatReply reply);

	int updateNewsReply(GwWechatReply reply);

	int blockReply(GwWechatReply reply);

}
