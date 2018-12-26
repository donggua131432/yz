package com.yz.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.service.admin.UserSecurityServiceImpl;

@Controller
public class IndexController {

	@Autowired
	private UserSecurityServiceImpl baseUserService;

	@RequestMapping("/toLogin")
	public String toLogin() {
		return "login";
	}

	@RequestMapping("/index")
	@Rule(checkRule = false)
	public String index(Model model) {

		BaseUser user = SessionUtil.getUser();

		// 通过model传到页面
		model.addAttribute("user", user);
		model.addAttribute("funcs", baseUserService.queryMenuListByUserId(user.getUserId()));
		return "index";
	}

	@RequestMapping("/welcome")
	@Rule(checkRule = false)
	public String welcome() {
		return "welcome";
	}

	@RequestMapping("/errorPage")
	public String errorPage() {
		return "pub/error";
	}

}
