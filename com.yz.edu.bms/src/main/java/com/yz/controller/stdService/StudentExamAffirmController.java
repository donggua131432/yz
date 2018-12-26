package com.yz.controller.stdService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.stdService.StudentExamAffirmInfo;
import com.yz.model.stdService.StudentExamAffirmQuery;
import com.yz.service.stdService.StudentExamAffirmService;
import com.yz.util.Assert;
/**
 * 学服任务--考场确认
 * @author lx
 * @date 2017年12月7日 下午2:33:58
 */
@Controller
@RequestMapping("/examAffirm")
public class StudentExamAffirmController
{
	@Autowired
	private StudentExamAffirmService examAffirmService;
	
	@RequestMapping("/toList")
	@Rule("examAffirm:query")
	public String toAssginList(Model model, HttpServletRequest request) {
		return "/stdservice/examaffirm/student_examaffirm_list";
	}
	
	@RequestMapping("/getStuList")
	@Rule("examAffirm:query")
	@ResponseBody
	public Object getStuAffirmList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,StudentExamAffirmQuery query) {
		return examAffirmService.queryStudentAffirmList(start,length,query);
	}
	
	@RequestMapping("/toEdit")
	@Rule("examAffirm:insert")
	public String toEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		StudentExamAffirmInfo affirmInfo =new StudentExamAffirmInfo();
		String affirmId = RequestUtil.getString("affirmId");
		Assert.hasText(affirmId, "参数名称不能为空");
		affirmInfo = examAffirmService.getExamAffirmInfoById(affirmId);
		
		model.addAttribute("affirmInfo", affirmInfo);
		model.addAttribute("exType", exType);
		return "/stdservice/examaffirm/student_examaffirm_edit";
	}
	
	@RequestMapping("/getExamYear")
	@ResponseBody
	public Object getExamYear(@RequestParam(name = "status") String status) {
		return examAffirmService.getExamYear(status);
	}
	
	@RequestMapping("/getExamReason")
	@ResponseBody
	public Object getExamReason(@RequestParam(value = "eyId") String eyId) {
		return examAffirmService.getExamReason(eyId);
	}
	
	@Log
	@RequestMapping("/changeUnconfirmeReason")
	@Rule("examAffirm:insert")
	@ResponseBody
	public Object changeUnconfirmeReason(StudentExamAffirmInfo info) {
		examAffirmService.changeUnconfirmeReason(info);
		return "SUCCESS";
	}
	@RequestMapping("/resetResult")
	@ResponseBody
	public Object resetResult(@RequestParam(value = "affirmId") String affirmId,
			@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "learnId") String learnId) {
		examAffirmService.resetResult(affirmId,taskId,learnId);
		return "SUCCESS";
	}

	@RequestMapping("/examAffirmExport")
	@Rule("examAffirm:examAffirmExport")
	public void examAffirmExport(StudentExamAffirmQuery query,HttpServletResponse response){
		examAffirmService.examAffirmExport(query,response);
	}
}
