package com.yz.controller.recruit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.recruit.AllStudentQuery;
import com.yz.service.recruit.StudentAllService;

/**
 * 学员搜索菜单
 * Description: 
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2018年3月2日.
 *
 */
@Controller
@RequestMapping("/studentSearch")
public class StudentSearchController {

	
	@Autowired
	private StudentAllService stdAllService;

	/**
	 * 全部学员列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("studentSearch:query")
	public String toList(Model model) {
		return "/recruit/student/student_search";
	}
	
	@RequestMapping("/findAllStudent")
	@Rule("studentSearch:query")
	@ResponseBody
	public Object findAllStudent(AllStudentQuery queryInfo) {
		return stdAllService.searchStudent(queryInfo);
	}
}
