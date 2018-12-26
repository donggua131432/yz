package com.yz.dao;

import java.util.List;

import com.yz.model.WechatArticle;

public interface WechatReplyMapper { 

	String selectMsgType(String msg);

	String selectContent(String msg);

	List<WechatArticle> selectMsgNews(String msg); 
}