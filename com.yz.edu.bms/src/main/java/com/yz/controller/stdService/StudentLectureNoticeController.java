package com.yz.controller.stdService;

import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.stdService.StudentLectureNoticeInfo;
import com.yz.service.stdService.StudentLectureNoticeService;
import com.yz.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 学服任务--开课通知确认跟进
 */
@Controller
@RequestMapping("/lecture")
public class StudentLectureNoticeController
{
	@Autowired
	private StudentLectureNoticeService studentLectureService;
	
	@RequestMapping("/toList")
	@Rule("lecture:query")
	public String toList(Model model, HttpServletRequest request) {
		return "/stdservice/lectureNotice/student_lecture_list";
	}
	
	@RequestMapping("/findAllLectureNoticeList")
	@Rule("lecture:query")
	@ResponseBody
	public Object findAllLectureNoticeList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,StudentLectureNoticeInfo query) {
		PageHelper.offsetPage(start, length);
		return studentLectureService.findAllLectureNoticeList(query);
	}
	
	@RequestMapping("/toEdit")
	@Rule("lecture:edit")
	public String toEdit(HttpServletRequest request,
			Model model) {
		StudentLectureNoticeInfo lectureInfo  = new StudentLectureNoticeInfo();
		String id = RequestUtil.getString("id");
		Assert.hasText(id, "参数名称不能为空");
		lectureInfo = studentLectureService.getLectureNoticeInfoById(id);
		model.addAttribute("dataInfo", lectureInfo);
		return "/stdservice/lectureNotice/student_lecture_edit";
	}

    @RequestMapping("/exportLectureNoticeInfo")
	@Rule("lecture:export")
	public void exportLectureNoticeInfo(StudentLectureNoticeInfo query, HttpServletResponse response) {
    	studentLectureService.exportLectureNoticeInfo(query, response);
	}

	@RequestMapping("/editStuLectureRemark")
	@Rule("lecture:edit")
	@ResponseBody
	public Object updateRemark(String lectureId,String remark,HttpServletResponse response) {
		studentLectureService.updateRemark(lectureId, remark);
		return "SUCCESS";
	}

	@RequestMapping("/resetTask")
	@Rule("lecture:reset")
	@ResponseBody
	public Object resetTask(String lectureId, String taskId, String learnId, HttpServletResponse response) {
		studentLectureService.resetTask(lectureId,taskId,learnId);
		return "SUCCESS";
	}
}
