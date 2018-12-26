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
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.wechat.GwWechatMenu;
import com.yz.model.wechat.GwWechatMenuQuery;
import com.yz.service.wechat.GwWechatMenuService;

@Controller
@RequestMapping("/wechatmenu")
public class WechatMenuController {

	@Autowired
	private GwWechatMenuService menuService;

	@RequestMapping("/toList")
	@Rule("wechatmenu:query")
	public Object toList() {
		return "wechat/menu/wechatmenu-list";
	}

	@RequestMapping("list")
	@ResponseBody
	@Rule("wechatmenu:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, GwWechatMenuQuery query) {
		return menuService.selectWechatMenuByPage(start, length, query);
	}

	@RequestMapping("/toAdd")
	@Token(action = Flag.Save, groupId = "wechatmenu:insert")
	@Rule("wechatmenu:insert")
	public Object toAdd(Model model) {
		GwWechatMenu wechat = new GwWechatMenu();
		model.addAttribute("menu", wechat);
		model.addAttribute("exType", "ADD");
		return "wechat/menu/wechatmenu-edit";
	}

	@RequestMapping("/add")
	@Token(action = Flag.Remove, groupId = "wechatmenu:insert")
	@Rule("wechatmenu:insert")
	@Log
	@ResponseBody
	public Object add(HttpServletRequest request, GwWechatMenu menu) {
		menuService.addWechatMenu(menu);

		return "SUCCESS";
	}
	
	@RequestMapping("/refreshWechatMenu")
	@Log
	@ResponseBody
	public Object refreshWechatMenu(HttpServletRequest request) {
		menuService.setYzWechatMenu();
		return "SUCCESS";
	}

	@RequestMapping("/toEdit")
	@Token(action = Flag.Save, groupId = "wechatmenu:update")
	@Rule("wechatmenu:update")
	public Object toEdit(@RequestParam(name = "id", required = true) String id, Model model) {
		GwWechatMenu menu = menuService.selectMenuById(id);
		model.addAttribute("menu", menu);
		model.addAttribute("exType", "UPDATE");
		return "wechat/menu/wechatmenu-edit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("wechatmenu:update")
	@Token(action = Flag.Remove, groupId = "wechatmenu:update")
	public Object edit(HttpServletRequest request, GwWechatMenu menu) {
		Assert.notNull(menu.getId(), "ID不能为空");
		menuService.updateWechatMenu(menu);
		return "SUCCESS";
	}

	@RequestMapping("/block")
	@ResponseBody
	@Log
	@Rule("wechatmenu:update")
	public Object block(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "id", required = true) String id) {
		GwWechatMenu menu = new GwWechatMenu();
		menu.setId(id);
		if ("BLOCK".equalsIgnoreCase(exType)) {
			menu.setIsAllow(GlobalConstants.STATUS_NOT_ALLOW);
		} else if ("START".equalsIgnoreCase(exType)) {
			menu.setIsAllow(GlobalConstants.STATUS_ALLOW);
		}
		menuService.updateWechatMenu(menu);
		return "SUCCESS";
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Log
	@Rule("wechatmenu:delete")
	public Object delete(@RequestParam(name = "id") String id) {
		menuService.deleteWechatMenu(id);
		return "SUCCESS";
	}

	@RequestMapping("/deletes")
	@ResponseBody
	@Log
	@Rule("wechatmenu:delete")
	public Object deletes(@RequestParam(name = "ids[]") String[] ids) {
		menuService.deleteMenus(ids);
		return "SUCCESS";
	}

	@RequestMapping("/sPublic")
	@ResponseBody
	@Rule({ "wechatmenu:update", "wechatmenu:insert", "wechatreply:update", "wechatreply:insert" })
	public Object sPublic(SelectQueryInfo sqInfo) {
		return menuService.queryWechatPublic(sqInfo);
	}

	@RequestMapping("/sMenu")
	@ResponseBody
	@Rule({ "wechatmenu:update", "wechatmenu:insert" })
	public Object sMenu(SelectQueryInfo sqInfo) {
		return menuService.sMennu(sqInfo);
	}

}
