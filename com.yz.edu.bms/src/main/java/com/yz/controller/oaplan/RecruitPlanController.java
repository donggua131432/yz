package com.yz.controller.oaplan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.RecruitConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.recruit.OaPlan;
import com.yz.service.recruit.OaPlanServiceImpl;
import com.yz.util.Assert;

/**
 * 
 * Description:招生计划 
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月25日.
 *
 */
@Controller
@RequestMapping("/recruit")
public class RecruitPlanController {

	@Autowired
	private OaPlanServiceImpl planService;

	@RequestMapping("/toList")
	public String toList() {

		return "recruit/oaplan/oaplan-list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("oaPlan:query")
	public Object oaPlanListPage(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,
			@RequestParam(name = "planType", required = true) String planType, OaPlan plan) {

		if (RecruitConstants.RSS_PLAN_TYPE_MONTH.equals(planType)) {
			return planService.queryPlanByPage(start, length, planType, plan);
		} else if (RecruitConstants.RSS_PLAN_TYPE_YEAR.equals(planType)) {
			return planService.queryPlanByPage(start, length, planType, plan);
		}
		return null;
	}

	@RequestMapping("/toEdit")
	@Rule("oaPlan:update")
	public Object edit(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "planType", required = true) String planType, String planId, Model model) {
		OaPlan plan = new OaPlan();
		if ("UPDATE".equalsIgnoreCase(exType)) {
			Assert.hasText(planId, "计划ID不能为空");
			plan = planService.queryPlanById(planId);
		}
		model.addAttribute("planInfo", plan);
		model.addAttribute("planType", planType);
		model.addAttribute("exType", exType);
		return "recruit/oaplan/oaplan-edit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule({"oaPlan:update","oaPlan:insert"})
	public Object editOaPlan(@RequestParam(name = "exType", required = true) String exType, OaPlan plan) {

		BaseUser user = SessionUtil.getUser();
		plan.setUpdateUserId(user.getUserId());
		plan.setUpdateUser(user.getRealName());

		if ("UPDATE".equalsIgnoreCase(exType)) {
			planService.updatePlan(plan);
		} else if ("ADD".equalsIgnoreCase(exType)) {
			plan.setCreateUser(user.getRealName());
			plan.setCreateUserId(user.getUserId());
			planService.addPlan(plan);
		}

		return "success";
	}

	@RequestMapping("/deletePlans")
	@ResponseBody
	@Log(needLog = true)
	@Rule("oaPlan:delete")
	public Object deleteAllPlan(@RequestParam(name = "planIds[]", required = true) String[] planIds) {
		planService.deletePlanPlans(planIds);
		return "success";
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Log(needLog = true)
	@Rule("oaPlan:delete")
	public Object delete(@RequestParam(name = "planId", required = true) String planId) {
		planService.deletePlan(planId);
		return "success";
	}
	

}
