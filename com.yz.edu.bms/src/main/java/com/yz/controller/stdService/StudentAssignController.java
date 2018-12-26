package com.yz.controller.stdService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.common.EmpQueryInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.condition.stdService.StudentAssignQuery;
import com.yz.model.stdService.StudentAssignInfo;
import com.yz.service.common.BaseInfoService;
import com.yz.service.oa.OaEmployeeService;
import com.yz.service.stdService.StudentAssignService;
import com.yz.util.StringUtil;

/**
 * 学服管理-辅导员分配
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/assign")
public class StudentAssignController {

	@Autowired
	private StudentAssignService assignService;

	@RequestMapping("/toList")
	@Rule("assign")
	public String toAssginList(Model model, HttpServletRequest request) {
		return "/stdservice/assign/student_assign_list";
	}

	@RequestMapping("/findAssignList")
	@ResponseBody
	@Rule("assign")
	public Object findAssginList(StudentAssignQuery queryInfo) {
		return assignService.findAssignList(queryInfo);
	}

	@RequestMapping("/toAddPage")
	@Rule("assign:insert")
	@Token(groupId = "assign:insert", action = Flag.Save)
	public String toAssginInsert(Model model, @RequestParam(value = "addType", required = true) String addType,
			StudentAssignInfo assignInfo) {
		model.addAttribute("addType", addType);
		if(!addType.equals("3")) {
			List<Map<String, String>> studentList = assignService.getStudentList(assignInfo);
			model.addAttribute("studentList", studentList);
		}
		return "/stdservice/assign/student_assign_page";
	}
	
	@RequestMapping("/queryDistribution")
	@ResponseBody
	@Rule("assign")
	public Object queryDistribution(StudentAssignQuery queryInfo) {
		return assignService.getStudentListByquery(queryInfo);
	}
	
	@RequestMapping("/add")
	@Rule("assign:insert")
	@Token(groupId = "assign:insert", action = Flag.Remove)
	@ResponseBody
	@Log
	public Object add(HttpServletRequest request, StudentAssignInfo assignInfo) {
		assignService.addAssign(assignInfo);
		return null;
	}

	@Autowired
	private OaEmployeeService employeeService;

	@RequestMapping("/getTutorList")
	@Rule("assign:insert")
	@ResponseBody
	public Object getTutorList(HttpServletRequest request, SelectQueryInfo queryInfo) {
		return employeeService.getTutorList(queryInfo);
	}

	@Autowired
	private BaseInfoService baseInfoService;

	@RequestMapping("/getEmpList")
	@ResponseBody
	@Rule("assign:insert")
	public Object getEmpList(EmpQueryInfo queryInfo) {
		return baseInfoService.getEmpList(queryInfo);
	}

	@RequestMapping("/distribution")
	@ResponseBody
	@Rule("assign:insert")
	@Log
	public Object distribution(StudentAssignInfo assignInfo) {
		String[] learnIds = assignInfo.getLearnIds();
		
		if(learnIds == null)
			return null;
		
		assignService.addAssign(assignInfo);
		return null;
	}
}
