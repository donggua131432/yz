package com.yz.controller.stdService;

import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentGraduateDataInfo;
import com.yz.model.stdService.StudentGraduateDataQuery;
import com.yz.model.stdService.StudentXuexinInfo;
import com.yz.model.stdService.StudentXuexinQuery;
import com.yz.service.stdService.StudentGraduateDataService;
import com.yz.service.stdService.StudentXuexinService;
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
import java.util.List;

/**
 * 学服任务--学信网信息核对
 * @author jyt
 */
@Controller
@RequestMapping("/xuexin")
public class StudentXuexinController
{
	@Autowired
	private StudentXuexinService studentXuexinService;
	
	@RequestMapping("/toList")
	@Rule("xuexin:query")
	public String toList(Model model, HttpServletRequest request) {
		return "/stdservice/xuexin/student_xuexin_list";
	}
	
	@RequestMapping("/findAllXuexinList")
	@Rule("xuexin:query")
	@ResponseBody
	public Object findAllXuexinList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,StudentXuexinQuery query) {
		PageHelper.offsetPage(start, length);
		return studentXuexinService.findAllXuexinList(query);
	}
	
	@RequestMapping("/toEdit")
	@Rule("xuexin:edit")
	public String toEdit(HttpServletRequest request,
			Model model) {
		StudentXuexinInfo xuexinInfo  = new StudentXuexinInfo();
		String id = RequestUtil.getString("id");
		Assert.hasText(id, "参数名称不能为空");
		xuexinInfo = studentXuexinService.getXuexinInfoById(id);
		model.addAttribute("dataInfo", xuexinInfo);
		return "/stdservice/xuexin/student_xuexin_edit";
	}
	
    @RequestMapping("/stuXuexinImport")
    @Rule("xuexin:import")
    public String stuGraduateDataImport(HttpServletRequest request) {
        return "stdservice/xuexin/student_xuexin-import";
    }
    
    @RequestMapping("/uploadStuXuexin")
    @Rule("xuexin:import")
    @ResponseBody
    public Object uploadStuGraduateData(
            @RequestParam(value = "stuXuexinImport", required = false) MultipartFile stuXuexinImport) {
		studentXuexinService.importStuGraduateDataInfo(stuXuexinImport);
    	 return "SUCCESS";
    }
    
    @RequestMapping("/exportXunxinInfo")
	@Rule("xuexin:export")
	public void exportXuexinInfo(StudentXuexinQuery query, HttpServletResponse response) {
		studentXuexinService.exportXuexinInfo(query, response);
	}

	@RequestMapping("/editStuXuexinRemark")
	@Rule("xuexin:edit")
	@ResponseBody
	public Object updateRemark(String xuexinId,String remark, HttpServletResponse response) {
		studentXuexinService.updateRemark(xuexinId, remark);
		return "SUCCESS";
	}

	@RequestMapping("/resetTask")
	@Rule("xuexin:reset")
	@ResponseBody
	public Object resetTask(String xuexinId, String taskId, String learnId, HttpServletResponse response) {
		studentXuexinService.resetTask(xuexinId,taskId,learnId);
		return "SUCCESS";
	}
}
