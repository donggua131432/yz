package com.yz.controller.educational;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.yz.model.educational.BdTeacherRecommendQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yz.constants.StudentConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.condition.educational.TeacherInfoQuery;
import com.yz.model.oa.OaEmployeeAnnex;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.oa.OaEmployeeOtherInfo;
import com.yz.service.educational.TeacherManageExcelService;
import com.yz.service.educational.TeacherManageService;
import com.yz.service.oa.EmpAnnexCheckService;
import com.yz.service.oa.OaEmployeeService;
import com.yz.util.Assert;

/**
 * 教务管理--教师管理
 * @author lx
 * @date 2017年7月10日 下午6:08:35
 */
@RequestMapping("/teacher")
@Controller
public class TeacherManageController {
	
	@Autowired
	private TeacherManageService teacherManageService;
	
	@Autowired
	private OaEmployeeService employeeService;
	
	@Autowired
	private EmpAnnexCheckService empAnnexCheckService;
	
	@Autowired
	private TeacherManageExcelService teacherManageExcelService;
	
	/**
	 * 跳转到教师管理页面列表
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("teacher:query")
	public String toList() {
		return "/educational/teacherManage/teacher-list";
	}

	@RequestMapping("/toRecommendExport")
	public String toRecommendExport() {
		return "/educational/teacherManage/teacher_recommend-export";
	}

	/**
	 * 加载教师数据
	 * @param start
	 * @param length
	 * @param queryInfo
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("teacher:query")
	@ResponseBody
	public Object recruiterList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,TeacherInfoQuery queryInfo) {
		return teacherManageService.queryTeacherShowInfoByPage(start, length, queryInfo);
	}
	
	/**
	 * 跳转到教师编辑或者信息页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEdit")
	@Rule("teacher:insert")
	public String toEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String empId = RequestUtil.getString("empId");
			Assert.hasText(empId, "参数名称不能为空");
			model.addAttribute("empId", empId);
		}
		model.addAttribute("exType", exType);
		return "/educational/teacherManage/teacher_edit";
	}
	/**
	 * 跳转到基本信息页面
	 * @param exType
	 * @param empId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toBase")
	@Rule("teacher:query")
	public String toBase(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(value="empId", required=true) String empId,
			HttpServletRequest request,
			Model model) {
		OaEmployeeBaseInfo baseInfo = employeeService.getEmpBaseInfo(empId);
		OaEmployeeOtherInfo other = employeeService.getEmpOtherInfo(empId,StudentConstants.RECRUITER_ANNEX_TYPE_PHOTO);
		model.addAttribute("baseInfo", baseInfo);
		model.addAttribute("other", other);
		model.addAttribute("exType",exType);
		return "/educational/teacherManage/teacher_baseInfo";
	}
	
	/**
	 * 修改或者新增教师信息
	 * @param exType
	 * @param baseInfo
	 * @param otherInfo
	 * @return
	 */
	@RequestMapping("/baseUpdate")
	@Rule("teacher:insert")
	@ResponseBody
	@Log
	public Object baseUpdate(@RequestParam(name = "exType", required = true) String exType,
			OaEmployeeBaseInfo baseInfo,OaEmployeeOtherInfo otherInfo) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		
		if ("UPDATE".equalsIgnoreCase(deal)) {
			baseInfo.setUpdateUserId(user.getUserId());
			baseInfo.setUpdateUser(user.getRealName());
			teacherManageService.updateTeacherBaseInfo(baseInfo,otherInfo);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			baseInfo.setCreateUserId(user.getUserId());
			baseInfo.setCreateUser(user.getRealName());
			return teacherManageService.insertTeacherBaseInfo(baseInfo,otherInfo);
		}

		return "success";
	}
	/**
	 * 跳转到附件列表页面
	 * @param model
	 * @param empId
	 * @return
	 */
	@RequestMapping("/toAnnexList")
	@Rule("teacher:query")
	public String toAnnexList(Model model, @RequestParam(value="empId", required=true) String empId) {
		model.addAttribute("empId", empId);
		model.addAttribute("updateUrl", "teacher/updateAnnex.do");
		model.addAttribute("deleteUrl", "teacher/delAnnex.do");
		model.addAttribute("getDataUrl", "teacher/getAnnexList.do");
		return "/educational/teacherManage/teacher_annex_list";
	}
	/**
	 * 获取附件列表信息
	 * @param model
	 * @param empId
	 * @return
	 */
	@RequestMapping("/getAnnexList")
	@Rule("teacher:query")
	@ResponseBody
	public Object getAnnexList(Model model, @RequestParam(value="empId", required=true) String empId) {
		return empAnnexCheckService.getAnnexList(empId);
	}
	/**
	 * 修改附件信息
	 * @param mRequest
	 * @param annexInfo
	 * @return
	 */
	@Log
	@RequestMapping("/updateAnnex")
	@Rule("teacher:updateAnnex")
	@ResponseBody
	public Object updateAnnex(MultipartHttpServletRequest mRequest, OaEmployeeAnnex annexInfo) {
		return empAnnexCheckService.updateAnnexInfo(annexInfo);
	}
	
	/**
	 * 删除附件信息
	 * @param model
	 * @return
	 */
	@Log
	@RequestMapping("/delAnnex")
	@Rule("teacher:updateAnnex")
	@ResponseBody
	public Object deleteAnnex(OaEmployeeAnnex annexInfo) {
		empAnnexCheckService.delAnnexInfo(annexInfo);
		return null;
	}
	/**
	 * 跳转到导入
	 * @return
	 */
	@RequestMapping("/excelImport")
	@Rule("teacher:excelImport")
	public String excelImport() {
		return "/educational/teacherManage/teacher_import";
	}
	/**
	 * 导入
	 * @param excelTeacher
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/upload")
	@Rule("teacher:excelImport")
	@ResponseBody
	public Object upload(@RequestParam(value = "excelTeacher", required = false) MultipartFile excelTeacher) {	
		teacherManageExcelService.importTeacherInfo(excelTeacher);
   	    return "SUCCESS";
		
	}

	@RequestMapping("/recommandExport")
	@Rule("teacher:recommandExport")
	public void recommandExport(BdTeacherRecommendQuery recommendQuery, HttpServletResponse response) {
		teacherManageService.recommandExport(recommendQuery, response);
	}
	
	@RequestMapping("/deleteTeacher")
	@ResponseBody
	@Rule("teacher:delete")
	public Object deleteTeacher(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		teacherManageService.deleteTeacher(idArray);
		return "success";
	}
	
	@RequestMapping("/export")
	@Rule("teacher:export")
	public void export(TeacherInfoQuery queryInfo, HttpServletResponse response) {
		teacherManageService.exportTeacherInfo(queryInfo, response);
	}
}
