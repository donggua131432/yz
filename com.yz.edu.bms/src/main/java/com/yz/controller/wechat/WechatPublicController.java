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
import com.yz.model.wechat.GwWechatPublic;
import com.yz.model.wechat.GwWechatPublicQuery;
import com.yz.service.wechat.GwWechatPublicService;

@Controller
@RequestMapping("/wechatpub")
public class WechatPublicController {

	@Autowired
	private GwWechatPublicService wechatService;

	@RequestMapping("/toList")
	@Rule("wechatpub:query")
	public Object toList() {
		return "wechat/wechatpub/wechatpub-list";
	}

	@RequestMapping("list")
	@ResponseBody
	@Rule("wechatpub:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, GwWechatPublicQuery query) {
		return wechatService.selectWechatPublicByPage(start, length, query);
	}

	@RequestMapping("/toAdd")
	@Token(action = Flag.Save, groupId = "wechatpub:insert")
	@Rule("wechatpub:insert")
	public Object toAdd(Model model) {
		GwWechatPublic wechat = new GwWechatPublic();
		model.addAttribute("wechat", wechat);
		model.addAttribute("exType", "ADD");
		return "wechat/wechatpub/wechatpub-edit";
	}

	@RequestMapping("/add")
	@Token(action = Flag.Remove, groupId = "wechatpub:insert")
	@Rule("wechatpub:insert")
	@Log
	@ResponseBody
	public Object add(HttpServletRequest request, GwWechatPublic wechat) {
		Assert.notNull(wechat.getPubId(), "公众号ID不能为空");
		wechatService.addWechatPublic(wechat);
		
		return "SUCCESS";
	}

	@RequestMapping("/toEdit")
	@Token(action = Flag.Save, groupId = "wechatpub:update")
	@Rule("wechatpub:update")
	public Object toEdit(@RequestParam(name = "pubId", required = true) String pubId, Model model) {
		GwWechatPublic wechat = wechatService.selectWechatPubById(pubId);
		model.addAttribute("wechat", wechat);
		model.addAttribute("exType", "UPDATE");
		return "wechat/wechatpub/wechatpub-edit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("wechatpub:update")
	@Token(action = Flag.Remove, groupId = "wechatpub:update")
	public Object edit(HttpServletRequest request, GwWechatPublic wechat) {
		Assert.notNull(wechat.getPubId(), "公众号ID不能为空");
		wechatService.updateWechatPublic(wechat);
		return "SUCCESS";
	}

	@RequestMapping("/block")
	@ResponseBody
	@Log
	@Rule("wechatpub:update")
	public Object block(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "pubId", required = true) String pubId) {
		GwWechatPublic wechat = new GwWechatPublic();
		wechat.setPubId(pubId);
		if ("BLOCK".equalsIgnoreCase(exType)) {
			wechat.setIsAllow(GlobalConstants.STATUS_NOT_ALLOW);
		} else if ("START".equalsIgnoreCase(exType)) {
			wechat.setIsAllow(GlobalConstants.STATUS_ALLOW);
		}
		wechatService.updateWechatPublic(wechat);
		return "SUCCESS";
	}

}
