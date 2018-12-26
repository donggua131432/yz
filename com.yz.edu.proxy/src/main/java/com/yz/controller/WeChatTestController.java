package com.yz.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.service.WechatReplyService;

@Controller
public class WeChatTestController {

	private static final String TOKEN = "feixiangdehelanren123";

	private static final Logger log = LoggerFactory.getLogger(WeChatTestController.class);
	
	@Autowired
	private WechatReplyService replyService;

	@RequestMapping(value = "/wTest", method = RequestMethod.GET)
	@ResponseBody
	public String test(HttpServletRequest request) {
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");

		String signature = request.getParameter("signature");
		log.debug("请求wTest的get方法----------");
		log.debug("-------------------- timestamp : " + timestamp + " | nonce : " + nonce + " | echostr : " + echostr
				+ " | signature : " + signature);

		String[] temp = { TOKEN, timestamp, nonce };

		Arrays.sort(temp);

		String tempstr = "";
		for (String ss : temp) {
			tempstr += ss;
		}

		String sign = DigestUtils.sha1Hex(tempstr);
		log.debug("-------------------- sign : " + sign);

		if (sign.equals(signature)) {

			return echostr;
		}

		return null;

	}
	
	@RequestMapping(value = "/wTest", method = RequestMethod.POST)
	@ResponseBody
	public String wechatMsg(HttpServletRequest request) throws IOException {
		log.debug("请求wTest的post方法----------");
		return replyService.revceiveYzMsg(request);
	}
	
}
