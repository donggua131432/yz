package com.yz.controller.stdService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.educational.BdStudentSendMap;
import com.yz.model.educational.OaTaskStudentInfo;
import com.yz.model.stdService.StudentExamAffirmInfo;
import com.yz.model.stdService.StudentGraduateDataInfo;
import com.yz.model.stdService.StudentGraduateDataQuery;
import com.yz.service.stdService.StudentGraduateDataService;
import com.yz.util.Assert;
import com.yz.util.ExcelUtil;
/**
 * 学服任务--毕业证资料
 * @author lx
 * @date 2018年1月26日 下午2:41:32
 */
@Controller
@RequestMapping("/graduateData")
public class StudentGraduateDataController
{
	@Autowired
	private StudentGraduateDataService graduateDataService;
	
	@RequestMapping("/toList")
	@Rule("graduateData:query")
	public String toList(Model model, HttpServletRequest request) {
		return "/stdservice/graduateData/student_graduate_data_list";
	}
	
	@RequestMapping("/getDataList")
	@Rule("graduateData:query")
	@ResponseBody
	public Object getStuDataList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,StudentGraduateDataQuery query) {
		return graduateDataService.queryStuGraduateDataList(start,length,query);
	}
	
	@RequestMapping("/toEdit")
	@Rule("graduateData:insert")
	public String toEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		StudentGraduateDataInfo dataInfo =new StudentGraduateDataInfo();
		String id = RequestUtil.getString("id");
		Assert.hasText(id, "参数名称不能为空");
		dataInfo = graduateDataService.getGraduateDataById(id);
		
		model.addAttribute("dataInfo", dataInfo);
		model.addAttribute("exType", exType);
		BaseUser user  =SessionUtil.getUser();
		//TODO 数据权限
		List<String> jtList = user.getJtList();
		if(jtList.contains("FDY")){ //班主任
			return "/stdservice/graduateData/student_graduate_data_edit_fdy";
		}else{
			return "/stdservice/graduateData/student_graduate_data_edit";
		}
		
	}
	
	@Log
	@RequestMapping("/editStuGraduateData")
	@Rule("graduateData:insert")
	@ResponseBody
	public Object editStuGraduateData(StudentGraduateDataInfo info) {
		graduateDataService.editStuGraduateData(info);
		return "SUCCESS";
	}
	
    @RequestMapping("/stuGraduateDataImport")
    @Rule("graduateData:import")
    public String stuGraduateDataImport(HttpServletRequest request) {
        return "stdservice/graduateData/student_graduate_data-import";
    }
    
    @RequestMapping("/uploadStuGraduateData")
    @Rule("graduateData:import")
    @ResponseBody
    public Object uploadStuGraduateData(
            @RequestParam(value = "stuGraduateDataImport", required = false) MultipartFile stuGraduateDataImport) {

		 graduateDataService.importStuGraduateDataInfo(stuGraduateDataImport);
    	 return "SUCCESS";
    }
    
    @RequestMapping("/exportStuGraduateData")
	@Rule("graduateData:export")
	public void exportStuGraduateData(StudentGraduateDataQuery queryMap, HttpServletResponse response) {
    	graduateDataService.exportStuGraduateData(queryMap, response);
	}
}
