package com.yz.controller.oa;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.yz.core.util.IdCardVerifyUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.PageCondition;
import com.yz.model.oa.OaEmployeeAnnex;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.oa.OaEmployeeJobInfo;
import com.yz.model.oa.OaEmployeeOtherInfo;
import com.yz.model.oa.OaEmployeePerfQuery;
import com.yz.model.oa.RecruiterInfo;
import com.yz.service.oa.DepartmentService;
import com.yz.service.oa.EmpAnnexCheckService;
import com.yz.service.oa.OaEmployeePerfService;
import com.yz.service.oa.OaEmployeeService;
import com.yz.service.oa.RecruiterService;
import com.yz.util.Assert;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

/**
 * 招生老师管理
 * 
 * @author lx
 * @date 2017年7月3日 下午12:00:18
 */
@Controller
@RequestMapping("/recruiter")
public class RecruiterController {

	@Autowired
	private RecruiterService recruiterService;

	@Autowired
	private OaEmployeeService employeeService;

	@Autowired
	private EmpAnnexCheckService empAnnexCheckService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private OaEmployeePerfService perfService;

	/**
	 * 跳转到 招生老师页面列表
	 * 
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("recruiter:query")
	public String toList() {
		return "oa/recruiter/recruiter-list";
	}

	/**
	 * 分页获取所有的招生老师信息
	 * 
	 * @param start
	 * @param length
	 * @param recruiterInfo
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	@Rule("recruiter:query")
	public Object recruiterList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, RecruiterInfo recruiterInfo) {
		return recruiterService.queryRecruiterInfoByPage(start, length, recruiterInfo);
	}

	@RequestMapping("/toEdit")
	@Rule("recruiter:insert")
	public String toEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {

		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String empId = RequestUtil.getString("empId");
			Assert.hasText(empId, "参数名称不能为空");
			model.addAttribute("empId", empId);
		}
		model.addAttribute("exType", exType);
		return "/oa/recruiter/recruiter_edit";
	}

	@RequestMapping("/toBase")
	@Rule("recruiter:base")
	public String toBase(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(value = "empId", required = true) String empId, HttpServletRequest request, Model model) {
		OaEmployeeBaseInfo baseInfo = employeeService.getEmpBaseInfo(empId);
		OaEmployeeOtherInfo other = employeeService.getEmpOtherInfo(empId, StudentConstants.RECRUITER_ANNEX_TYPE_PHOTO);
		model.addAttribute("baseInfo", baseInfo);
		model.addAttribute("other", other);
		model.addAttribute("exType", exType);
		return "/oa/recruiter/recruiter_baseInfo";
	}

	@Log
	@ResponseBody
	@RequestMapping("/baseUpdate")
	@Rule("recruiter:insert")
	public Object baseUpdate(@RequestParam(name = "exType", required = true) String exType, OaEmployeeBaseInfo baseInfo,
			OaEmployeeOtherInfo otherInfo) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		if ("UPDATE".equalsIgnoreCase(deal)) {
			baseInfo.setUpdateUserId(user.getUserId());
			baseInfo.setUpdateUser(user.getRealName());
			recruiterService.updateEmpBaseInfo(baseInfo, otherInfo);
			// 清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			baseInfo.setCreateUserId(user.getUserId());
			baseInfo.setCreateUser(user.getRealName());
			// 由于前端未完全验证,再次验证
			if (!IdCardVerifyUtil.strongVerifyIdNumber(baseInfo.getIdCard())) {
				Assert.hasText(null, "请输入正确的身份证号码!!!");
			}
			return recruiterService.insertEmpBaseInfo(baseInfo, otherInfo);
		}

		return "success";
	}

	@RequestMapping("/toAnnexList")
	@Rule("recruiter:annex")
	public String toAnnexList(Model model, @RequestParam(value = "empId", required = true) String empId) {
		model.addAttribute("empId", empId);
		model.addAttribute("updateUrl", "recruiter/updateAnnex.do");
		model.addAttribute("deleteUrl", "recruiter/delAnnex.do");
		model.addAttribute("getDataUrl", "recruiter/getAnnexList.do");
		return "/oa/recruiter/recruiter_annex_list";
	}

	@RequestMapping("/getAnnexList")
	@ResponseBody
	@Rule("recruiter:annex")
	public Object getAnnexList(Model model, @RequestParam(value = "empId", required = true) String empId) {
		return empAnnexCheckService.getAnnexList(empId);
	}

	@Log
	@RequestMapping("/updateAnnex")
	@ResponseBody
	@Rule("recruiterAnnex:insert")
	public Object updateAnnex(MultipartHttpServletRequest mRequest, OaEmployeeAnnex annexInfo) {
		return empAnnexCheckService.updateAnnexInfo(annexInfo);
	}

	/**
	 * 更新附件信息
	 * 
	 * @param model
	 * @return
	 */
	@Log
	@RequestMapping("/delAnnex")
	@ResponseBody
	@Rule("recruiterAnnex:delete")
	public Object deleteAnnex(OaEmployeeAnnex annexInfo) {
		empAnnexCheckService.delAnnexInfo(annexInfo);
		return null;
	}

	@RequestMapping("/ifExistInfo")
	@ResponseBody
	public Object ifExistInfo(HttpServletRequest request) {
		String idType = RequestUtil.getParameter("idType");
		String idCard = RequestUtil.getParameter("idCard");

		if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idCard)) {
			return true;
		}

		return employeeService.ifExistInfo(idType, idCard);
	}
	
	@RequestMapping("/ifExistInfoByEmpType")
	@ResponseBody
	public Object ifExistInfoByEmpType(HttpServletRequest request) {
		String idType = RequestUtil.getParameter("idType");
		String idCard = RequestUtil.getParameter("idCard");
		String empType = RequestUtil.getParameter("empType");

		if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idCard)|| StringUtil.isEmpty(empType)) {
			return true;
		}
		return employeeService.ifExistInfoByEmpType(idType, idCard,empType);
	}

	@RequestMapping("/toJob")
	@Rule("recruiterJob:insert")
	public String toJob(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(value = "empId", required = true) String empId, HttpServletRequest request, Model model) {
		OaEmployeeJobInfo jobInfo = employeeService.getEmployeeJobInfo(empId);
		// 默认赋值
		if (StringUtil.isEmpty(jobInfo.getCampusId())) {
			jobInfo.setCampusId("888");
			jobInfo.setDpId("888");
		}

		// 封装员工职称
		List<Map<String, String>> jdIdMap = recruiterService.findEmpTitle(empId);

		if (null != jdIdMap && jdIdMap.size() > 0) {
			String[] jdIds = new String[jdIdMap.size()];
			for (int i = 0; i < jdIdMap.size(); i++) {
				jdIds[i] = jdIdMap.get(i).get("jtId");
			}
			jobInfo.setJdIds(jdIds);
		}
		// 封装员工所在部门的职称
		List<Map<String, String>> dpJdIdMap = departmentService.findDepTitle(jobInfo.getDpId());
		jobInfo.setDpJdIdList(dpJdIdMap);

		model.addAttribute("jobInfo", jobInfo);
		model.addAttribute("exType", exType);
		return "/oa/recruiter/recruiter_jobInfo";
	}

	@RequestMapping("/jobUpdate")
	@ResponseBody
	@Log
	@Rule("recruiterJob:update")
	public Object empJobUpdate(@RequestParam(name = "exType", required = true) String exType, OaEmployeeJobInfo jobInfo,
			String[] jdIds) throws ParseException {
		Assert.hasText(jobInfo.getEmpId(), "请先添加招生老师信息,再添加岗位!");
		BaseUser user = SessionUtil.getUser();
		if (StringUtil.hasValue(jobInfo.getEffectTime()) && "UPDATE".equals(exType)) {
			employeeService.empJobUpdateOnEffectTime(jobInfo, jdIds, user);
		} else {
			employeeService.empJobUpdate(jobInfo, jdIds, user);
		}
		return null;
	}

	@RequestMapping("/excelImport")
	@Rule("recruiter:import")
	public String excelImport() {
		return "/oa/recruiter/recruiter_import";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/upload")
	@ResponseBody
	public Object upload(@RequestParam(value = "excelTeacher", required = false) MultipartFile excelTeacher) {

		// 对导入工具进行字段填充
		ExcelUtil.IExcelConfig<OaEmployeeBaseInfo> testExcelCofing = new ExcelUtil.IExcelConfig<OaEmployeeBaseInfo>();
		testExcelCofing.setSheetName("index").setType(OaEmployeeBaseInfo.class)
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "empName"))
				.addTitle(new ExcelUtil.IExcelTitle("证件类型", "idType"))
				.addTitle(new ExcelUtil.IExcelTitle("证件号", "idCard"))
				.addTitle(new ExcelUtil.IExcelTitle("用户类型", "empType"))
				.addTitle(new ExcelUtil.IExcelTitle("联系电话", "mobile"));

		// 行数记录
		int index = 1;
		try {
			// 对文件进行分析转对象
			List<OaEmployeeBaseInfo> list = ExcelUtil.importWorkbook(excelTeacher.getInputStream(), testExcelCofing,
					excelTeacher.getOriginalFilename());

			// 遍历插入
			for (OaEmployeeBaseInfo empInfo : list) {
				recruiterService.insertEmpBaseInfo(empInfo, new OaEmployeeOtherInfo());
				index++;
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}

		return "SUCCESS";
	}

	@RequestMapping("/deleteRecruiter")
	@ResponseBody
	@Rule("recruiter:delete")
	@Log
	public Object deleteRecruiter(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		recruiterService.deleteRecruiter(idArray);
		return "success";
	}

	/**
	 * 验证远智编号是否存在
	 * 
	 * @param exType
	 * @param yzCode
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/validateYzCode")
	@ResponseBody
	public Object validateYzCode(@RequestParam(name = "exType", required = true) String exType, String yzCode,
			String empId, HttpServletResponse resp) throws IOException {
		//查找是否有对应的用户信息
		if(recruiterService.checkUserIfExistsByYzCode(yzCode) <1){
			return false;
		}else{
			if ("UPDATE".equalsIgnoreCase(exType)) {
				if (recruiterService.isSelfYzCode(yzCode, empId) > 0) {
					return true;
				} else if (recruiterService.isYzCodeExist(yzCode) < 1) {
					return true;
				}
				return false;
			}
			return false;
		}
	}

	/**
	 * 验证招生编码是否存在
	 * 
	 * @param exType
	 * @param recruitCode
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/validateRecruitCode")
	@ResponseBody
	public Object validateRecruitCode(@RequestParam(name = "exType", required = true) String exType, String recruitCode,
			String empId, HttpServletResponse resp) throws IOException {
		if ("UPDATE".equalsIgnoreCase(exType)) {
			// 先验证是不是自己的code
			if (recruiterService.isSelfRecruiteCode(recruitCode, empId) > 0) {
				return true;
			} else if (StringUtil.hasValue(recruitCode) && recruiterService.isRecruitCodeExist(recruitCode) < 1) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * 变更记录页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toModify")
	@Rule("recruiterJob:insert")
	public String toModify(Model model, @RequestParam(value = "empId", required = true) String empId,
			PageCondition page) {
		model.addAttribute("empId", empId);
		return "/oa/recruiter/recruiter_modify";
	}

	/**
	 * 获取变更记录
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getModifyList")
	@Rule("recruiterJob:insert")
	@ResponseBody
	public Object getModifyList(@RequestParam(value = "empId", required = true) String empId, PageCondition page) {
		return employeeService.getModifyList(empId, page);
	}

	/**
	 * 跳转到绩效变更
	 * 
	 * @param model
	 * @param empId
	 * @return
	 */
	@RequestMapping("/toPerfChange")
	@Rule("recruiterJob:insert")
	public String toPerfChange(Model model, @RequestParam(value = "empId", required = true) String empId) {
		model.addAttribute("modifyInfo", employeeService.getLastModifyInfo(empId));
		model.addAttribute("empId", empId);
		return "/oa/recruiter/recruiter_perf_change";
	}

	/**
	 * 待分配学员的列表
	 * 
	 * @param perfQuery
	 * @return
	 */
	@RequestMapping(value = "/perfList", method = RequestMethod.POST)
	@ResponseBody
	@Rule("recruiter:query")
	public Object getperfList(OaEmployeePerfQuery perfQuery) {
		return perfService.getLearnInfoPerfInfoList(perfQuery);
	}

	/**
	 * 重新分配学员
	 * 
	 * @param idArray
	 * @return
	 */
	@RequestMapping("/allocationLearn")
	@ResponseBody
	@Rule("recruiterJob:insert")
	@Log
	public Object allocationLearn(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			@RequestParam(value = "operType", required = true) String operType,
			@RequestParam(value = "empId", required = true) String empId,
			@RequestParam(value = "personType", required = true) String personType) {
		BaseUser user = SessionUtil.getUser();
		perfService.allocationLearn(idArray, operType, empId, user, personType);
		return "success";
	}
}
