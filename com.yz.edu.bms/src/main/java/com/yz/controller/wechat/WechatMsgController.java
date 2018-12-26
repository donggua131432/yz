package com.yz.controller.wechat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.Assert;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.wechat.GwWechatMsgQuery;
import com.yz.model.wechat.GwWechatReply;
import com.yz.service.wechat.GwWechatMsgService;

@Controller
@RequestMapping("/wechatreply")
public class WechatMsgController {

	@Autowired
	private GwWechatMsgService msgService;

	@RequestMapping("/toList")
	@Rule("wechatreply:query")
	public Object toList() {
		return "wechat/reply/wechatreply-list";
	}

	@RequestMapping("list")
	@ResponseBody
	@Rule("wechatreply:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, GwWechatMsgQuery query) {
		return msgService.selectWechatMsgByPage(start, length, query);
	}

	@RequestMapping("/toAdd")
	@Token(action = Flag.Save, groupId = "wechatreply:insert")
	@Rule("wechatreply:insert")
	public Object toAdd(Model model) {
		GwWechatReply wechat = new GwWechatReply();
		model.addAttribute("reply", wechat);
		model.addAttribute("exType", "ADD");

		return "wechat/reply/wechatreply-edit";
	}

	@RequestMapping("/add")
	@Token(action = Flag.Remove, groupId = "wechatreply:insert")
	@Rule("wechatreply:insert")
	@Log
	@ResponseBody
	public Object add(HttpServletRequest request, GwWechatReply reply) {

		msgService.addWechatReply(reply);

		return "SUCCESS";
	}

	@RequestMapping("/toEdit")
	@Token(action = Flag.Save, groupId = "wechatreply:update")
	@Rule("wechatreply:update")
	public Object toEdit(@RequestParam(name = "replyId", required = true) String replyId, Model model) {
		GwWechatReply wechat = msgService.selectReply(replyId);
		model.addAttribute("reply", wechat);
		model.addAttribute("exType", "UPDATE");
		return "wechat/reply/wechatreply-edit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("wechatreply:update")
	@Token(action = Flag.Remove, groupId = "wechatreply:update")
	public Object edit(HttpServletRequest request, GwWechatReply reply) {
		Assert.notNull(reply.getReplyId(), "ID不能为空");
		msgService.updateWechatReply(reply);
		return "SUCCESS";
	}

	@RequestMapping("/block")
	@ResponseBody
	@Log
	@Rule("wechatreply:update")
	public Object block(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "replyId", required = true) String replyId) {
		GwWechatReply reply = new GwWechatReply();
		reply.setReplyId(replyId);
		if ("BLOCK".equalsIgnoreCase(exType)) {
			reply.setStatus(GlobalConstants.STATUS_BLOCK);
		} else if ("START".equalsIgnoreCase(exType)) {
			reply.setStatus(GlobalConstants.STATUS_ALLOW);
		}
		msgService.blockReply(reply);
		return "SUCCESS";
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	@Log
	@Rule("wechatreply:delete")
	public Object delete(@RequestParam(name = "replyId") String replyId) {
		msgService.deleteReply(replyId);
		return "SUCCESS";
	}

}
