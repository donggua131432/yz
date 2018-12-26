package com.yz.controller.oa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.service.oa.PerformanceService;

@Controller
@RequestMapping("/performance")
public class PerformanceController {

	@Autowired
	private PerformanceService perService;

	/**
	 * 我的绩效列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule({ "monthExpense:query" })
	public String toList(Model model, String empId) {
		model.addAttribute("empId", empId);
		return "/recruit/performance/performance-list";
	}
	
	/**
	 * 我的绩效列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toMyList")
	@Rule({ "recruit"})
	public String toMyList(Model model, String empId) {
		BaseUser user = SessionUtil.getUser();

		if (GlobalConstants.USER_LEVEL_SUPER.equals(user.getUserLevel())) {
			model.addAttribute("isSuper", GlobalConstants.TRUE);
		} else {
			List<BmsFunc> fncList = user.getFuncs();

			if (fncList != null) {
				for (BmsFunc func : fncList) {
					String code = func.getFuncCode();

					switch (code) {
					case "studentOutApproval:findDirector":
						model.addAttribute("isXJ", GlobalConstants.TRUE);
						break;
					case "studentOutApproval:findFinancial":
						model.addAttribute("isCW", GlobalConstants.TRUE);
						break;
					case "studentOutApproval:findSchoolManaged":
						model.addAttribute("isXB", GlobalConstants.TRUE);
						break;
					case "studentOutApproval:findSenate":
						model.addAttribute("isJW", GlobalConstants.TRUE);
						break;
					}

				}
			}
		}
		model.addAttribute("empId", empId);
		return "/recruit/performance/performance-list";
	}

	@RequestMapping("/myPerformance")
	@ResponseBody
	public Object myPerformance(String empId) {

		return perService.getMyPerformance(empId);
	}

	/**
	 * 学员列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toStudents")
	@Rule("recruit")
	public String toStudents(@RequestParam(name = "month", required = true) String month, Model model,String empId) {
		model.addAttribute("month", month);
		model.addAttribute("empId", empId);
		return "/recruit/performance/student-list";
	}

	/**
	 * 学员列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getStudents")
	@Rule("recruit")
	@ResponseBody
	public Object getStudents(@RequestParam(name = "month", required = true) String month,
			@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, Model model,
			@RequestParam(name = "empId", required = false) String empId) {

		return perService.getStudents(month, start, length, empId);
	}
	
	
	@RequestMapping("/sUnderEmpId")
	@Rule("recruit")
	@ResponseBody
	public Object sUnderEmpId(SelectQueryInfo queryInfo) {

		return perService.selectUnderEmpId(queryInfo);
	}

}
