package com.yz.controller.stdService;

import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.stdService.StudentDegreeEnglishInfo;
import com.yz.model.stdService.StudentDegreeEnglishQuery;
import com.yz.service.stdService.StudentDegreeService;
import com.yz.util.Assert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 学服任务--学位英语跟进
 */
@Controller
@RequestMapping("/degree")
public class StudentDegreeEnglishController
{
	@Autowired
	private StudentDegreeService studentDegreeService;
	
	@RequestMapping("/toList")
	@Rule("degree:query")
	public String toList(Model model, HttpServletRequest request) {
		return "/stdservice/degreeEnglish/student_degree_list";
	}
	
	@RequestMapping("/findAllEngDegreeList")
	@Rule("degree:query")
	@ResponseBody
	public Object findAllEngDegreeList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,StudentDegreeEnglishQuery query) {
		PageHelper.offsetPage(start, length);
		return studentDegreeService.findAllDegreeEnglishList(query);
	}
	
	@RequestMapping("/toEdit")
	@Rule("degree:edit")
	public String toEdit(HttpServletRequest request,
			Model model) {
		StudentDegreeEnglishInfo degreeInfo  = new StudentDegreeEnglishInfo();
		String id = RequestUtil.getString("id");
		Assert.hasText(id, "参数名称不能为空");
		degreeInfo = studentDegreeService.getDegreeEnglishInfoById(id);
		model.addAttribute("dataInfo", degreeInfo);
		return "/stdservice/degreeEnglish/student_degree_edit";
	}
	
    @RequestMapping("/stuDegreeImport")
    @Rule("degree:import")
    public String stuDegreeEnglishDataImport(HttpServletRequest request) {
        return "stdservice/degreeEnglish/student_degree_import";
    }
    
    @RequestMapping("/uploadStuDegreeEnglish")
    @Rule("degree:import")
    @ResponseBody
    public Object uploadStuDegreeEnglish(
            @RequestParam(value = "stuDegreeImport", required = false) MultipartFile stuDegreeImport) {
    	 studentDegreeService.importStuDegreeDataInfo(stuDegreeImport);
    	 return "SUCCESS";
    }
    
    @RequestMapping("/exportDegreeInfo")
	@Rule("degree:export")
	public void exportDegreeInfo(StudentDegreeEnglishQuery query, HttpServletResponse response) {
    	studentDegreeService.exportDegreeEnglishInfo(query, response);
	}

	@RequestMapping("/editStuDegreeRemark")
	@Rule("degree:edit")
	@ResponseBody
	public Object updateRemark(String degreeId,String remark, String isSceneConfirm,HttpServletResponse response) {
		studentDegreeService.updateRemark(degreeId, remark,isSceneConfirm);
		return "SUCCESS";
	}

	@RequestMapping("/resetTask")
	@Rule("degree:reset")
	@ResponseBody
	public Object resetTask(String degreeId, String taskId, String learnId, HttpServletResponse response) {
		studentDegreeService.resetTask(degreeId,taskId,learnId);
		return "SUCCESS";
	}
}
