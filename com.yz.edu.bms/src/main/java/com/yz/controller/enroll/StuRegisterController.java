package com.yz.controller.enroll;

import javax.servlet.http.HttpServletRequest;

import com.yz.model.enroll.stdenroll.BdStdEnrollQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.recruit.StudentInfoQuery;
import com.yz.service.enroll.StuRegisterServiceImpl;
import com.yz.util.JsonUtil;

/**
 * 
 * Description: 学员注册
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月25日.
 *
 */
@Controller
@RequestMapping("/stuRegister")
public class StuRegisterController {

	private static final Logger log = LoggerFactory.getLogger(StuRegisterController.class);

	@Autowired
	private StuRegisterServiceImpl regService;

	@RequestMapping("/toList")
	@Rule("stdRegister:query")
	public String toList() {

		return "enroll/register/stu-register-list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("stdRegister:query")
	public Object oaPlanListPage(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,
			@RequestParam(name = "registerTimer", required = true) String registerTimer, BdStdEnrollQuery bdStdEnrollQuery) {
		Object o = regService.queryRegisterInfoByPage(start, length, bdStdEnrollQuery, registerTimer);
		log.debug("-----------注册信息：" + JsonUtil.object2String(o));
		return o;
	}

	@RequestMapping("/registerBatch")
	@ResponseBody
	@Rule("stdRegister:update")
	public Object registerBatch(@RequestParam(name = "learnIds[]", required = true) String[] learnIds,
			@RequestParam(name = "grade", required = true) String grade) {
		regService.stdRegisterBatchs(learnIds, grade);
		return "SUCCESS";
	}

	@RequestMapping("/register")
	@ResponseBody
	@Rule("stdRegister:update")
	public Object register(@RequestParam(name = "learnId", required = true) String learnId,
			@RequestParam(name = "grade", required = true) String grade) {
		regService.stdRegisterBatch(learnId, grade);
		return "SUCCESS";
	}

	@RequestMapping("/toRegister")
	@Rule("stdRegister:update")
	public String toRegister(Model model, @RequestParam(name = "learnId", required = true) String learnId,
			@RequestParam(name = "grade", required = true) String grade) {
		Object o = regService.queryRegisterInfoById(learnId, grade);
		model.addAttribute("regInfo", o);
		model.addAttribute("grade", grade);

		log.debug(
				"---------------------------------------------------------------- 学员注册详情" + JsonUtil.object2String(o));
		return "enroll/register/stu-register-reg";
	}

	@RequestMapping("/toExcelImport")
	@Rule("stdRegister:import")
	public String teachPlanImport(HttpServletRequest request) {
		return "enroll/register/register-import";
	}

	@RequestMapping("/toExcelStdNoImport")
	@Rule("stdRegister:import")
	public String toExcelStdNoImport(HttpServletRequest request) {
		return "enroll/register/register_stdno-import";
	}

	@RequestMapping("/excelImport")
	@ResponseBody
	@Rule("stdRegister:import")
	public Object excelImport(@RequestParam(name = "excelRegist", required = false) MultipartFile excelRegist,String stdNo) {
		if (null != excelRegist) {
			regService.importRegister(excelRegist,stdNo);
		}
		return "SUCCESS";
	}

}
