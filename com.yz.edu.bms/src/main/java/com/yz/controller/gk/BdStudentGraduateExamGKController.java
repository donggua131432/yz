package com.yz.controller.gk;

import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.gk.StudentGraduateExamGKInfo;
import com.yz.model.gk.StudentGraduateExamGkQuery;
import com.yz.service.gk.BdStudentGraduateExamGKService;
import com.yz.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 国开本科统考跟进
 * @author juliet
 * @version 1.0
 */
@Controller
@RequestMapping("/studentGraduateExamGK")
public class BdStudentGraduateExamGKController {
    @Autowired
    BdStudentGraduateExamGKService graduateExamGKService;

    @RequestMapping("/tolist")
    @Rule("graduateExamGK:query")
    public String showList(HttpServletRequest request) {
        return "gk/exam/graduate_exam_task_list";
    }
    
    @RequestMapping("/toEdit")
    @Rule("graduateExamGK:edit")
    public String editRemark(HttpServletRequest request,
			Model model) {
    	String followId = RequestUtil.getString("followId");
		Assert.hasText(followId, "参数名称不能为空");
		StudentGraduateExamGKInfo info = graduateExamGKService.getGraduateExamGKInfoById(followId);
		model.addAttribute("info",info);
        return "gk/exam/graduate_exam_task_edit";
    }

    @RequestMapping("/findStudentGraduateExamGK")
	@Rule("graduateExamGK:query")
	@ResponseBody
	public Object findStudentGraduateExamGKList(StudentGraduateExamGkQuery query) {
		return graduateExamGKService.findStudentGraduateExamGKList(query);
	}
    
    
    @RequestMapping("/editRemark")
	@Rule("graduateExamGK:edit")
	@ResponseBody
	public Object updateRemark(String followId,String remark,HttpServletResponse response) {
    	graduateExamGKService.updateRemark(followId, remark);
		return "SUCCESS";
	}
    
    @RequestMapping("/synchronous")
	@Rule("graduateExamGK:synchronous")
	@ResponseBody
	public Object synchronous(StudentGraduateExamGkQuery query) {
		 graduateExamGKService.synchronous(query);
		 return "success";
	}
    
}
