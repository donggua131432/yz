package com.yz.controller.oa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.oa.OaCommissionQuery;
import com.yz.service.oa.OaCommissionService;
import com.yz.service.oa.PerformanceService;

/**
 * 月度提成 Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2018年1月27日.
 *
 */
@Controller
@RequestMapping("/commission")
public class CommissionController {

	@Autowired
	private OaCommissionService comService;
	
	@Autowired
	private PerformanceService perService;

	/**
	 * 月度提成列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("commission:query")
	public String toList(Model model) {
		return "/performance/commission/commission-list";
	}

	/**
	 * 月度提成列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	@Rule("commission:query")
	@ResponseBody
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, OaCommissionQuery query) {

		return comService.selectComminssionByPage(start, length, query);
	}

	/**
	 * 月度提成列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toCommissionList")
	@Rule({ "recruit", "commission:query" })
	public String toList(Model model, String empId) {
		model.addAttribute("empId", empId);
		return "/performance/commission/performance-list";
	}

	@RequestMapping("/myCommission")
	@ResponseBody
	public Object myPerformance(String empId) {

		return comService.myCommission(empId);
	}

	/**
	 * 学员列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toStudents")
	@Rule({ "recruit", "commission:query" })
	public String toStudents(@RequestParam(name = "month", required = true) String month, Model model, String empId) {
		model.addAttribute("month", month);
		model.addAttribute("empId", empId);
		return "/performance/commission/student-list";
	}
	
	/**
	 * 学员列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getStudents")
	@Rule({ "recruit", "commission:query" })
	@ResponseBody
	public Object getStudents(@RequestParam(name = "month", required = true) String month,
			@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, Model model,
			@RequestParam(name = "empId", required = false) String empId) {

		return perService.getStudents(month, start, length, empId);
	}

}
