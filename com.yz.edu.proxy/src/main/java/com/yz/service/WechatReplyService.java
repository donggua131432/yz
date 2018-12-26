package com.yz.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.constants.WechatMsgConstants;
import com.yz.dao.WechatReplyMapper;
import com.yz.model.WechatArticle;
import com.yz.model.WechatNewsMsg;
import com.yz.model.WechatTextMsg;
import com.yz.util.StringUtil;
import com.yz.util.XmlUtil;

@Service
public class WechatReplyService {
	
	private static final Logger log = LoggerFactory.getLogger(WechatReplyService.class);

	@Autowired
	private WechatReplyMapper replyMapper;
	/**
	 * 远智服务公众号自动回复
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public String revceiveYzMsg(HttpServletRequest request) throws IOException {

		Map<String, String> map = XmlUtil.xmlToMap(request.getInputStream());

		log.debug("------------------------- 微信用户发送参数:" + map);

		// 发送方帐号（一个OpenID）
		String fromUserName = map.get("FromUserName");
		// 开发者微信号
		String toUserName = map.get("ToUserName");
		// 发送消息
		String msg = map.get("Content");
		// 默认回复一个"success"
		String responseMessage = "success";

		// 如果此处接受的信息为空,直接返回成功,不再操作数据库
		if (StringUtil.isEmpty(msg)) {
			log.debug("当接受消息为空时直接返回success----------");
			return responseMessage;
		}
		String msgType = replyMapper.selectMsgType(msg);

		if (WechatMsgConstants.MESSAGE_TEXT.equals(msgType)) {
			WechatTextMsg textMessage = new WechatTextMsg();

			String content = replyMapper.selectContent(msg);
			textMessage.setMsgType(WechatMsgConstants.MESSAGE_TEXT);
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(System.currentTimeMillis());
			textMessage.setContent(content);

			responseMessage = XmlUtil.objToXml(textMessage);
			log.debug("文本信息的请求----------");
		} else if (WechatMsgConstants.MESSAGE_NEWS.equals(msgType)) {
			WechatNewsMsg m = new WechatNewsMsg();

			m.setMsgType(WechatMsgConstants.MESSAGE_NEWS);
			m.setToUserName(toUserName);
			m.setFromUserName(fromUserName);
			m.setCreateTime(System.currentTimeMillis());

			List<WechatArticle> list = replyMapper.selectMsgNews(msg);
			m.setArticles(list);
			m.setArticleCount(list.size());
			responseMessage = m.toString();

			log.debug("图文信息的请求----------");
		} else {
			log.debug("直接返回success----------");
			return responseMessage;
		}

		log.debug("-------------------------  微信自动回复信息：" + responseMessage);

		return responseMessage;
	}
}
