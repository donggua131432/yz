package com.yz.controller.recruit;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.conf.YzSysConfig;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.recruit.StudentWhitelistQuery;
import com.yz.model.recruit.StudentWhitelistEdit;
import com.yz.service.recruit.StudentWhiteListService;
import com.yz.util.Assert;

@Controller
@RequestMapping("/whitelist")
public class StudentWhitelistController {
	
	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private StudentWhiteListService whitelistService;

	@RequestMapping("/toList")
	@Rule("whitelist")
	public String toList(HttpServletRequest req, StudentWhitelistEdit wlInfo) {
		return "recruit/whitelist/whitelist_student";
	}

	@Log
	@RequestMapping("/setting")
	@ResponseBody
	@Rule("whitelist:setting")
	public Object setting(HttpServletRequest req, StudentWhitelistEdit wlInfo) {
		whitelistService.setting(wlInfo);
		return null;
	}

	@RequestMapping("/toSetting")
	@Rule("whitelist:setting")
	public String toSetting(Model model, HttpServletRequest req, @RequestParam("stdIds") String stdIds) {

		Assert.hasText(stdIds, "设置的学员ID不能为空");

		String[] stdArray = stdIds.split(",");
		List<String> whitelist = null;
		if (stdArray.length == 1) {
			whitelist = whitelistService.getWhitelist(stdArray[0]);
		}

		String discountPlan = yzSysConfig.getPlanSetting();
		// 圆梦、助学计划对应的
		String[] discountPlans = discountPlan.split(",");

		model.addAttribute("whitelist", whitelist);
		model.addAttribute("stdIds", stdArray);
		model.addAttribute("discountPlans", discountPlans);

		return "recruit/whitelist/whitelist_setting";
	}

	@RequestMapping("/getStudents")
	@ResponseBody
	@Rule("whitelist:setting")
	public Object getStudents(HttpServletRequest req, StudentWhitelistQuery queryInfo) {
		return whitelistService.getStudents(queryInfo);
	}
}
