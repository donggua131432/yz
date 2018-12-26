package com.yz.controller.educational;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.condition.educational.OaTaskInfoQuery;
import com.yz.model.condition.educational.OaTaskTargetStudentQuery;
import com.yz.model.educational.OaTaskInfo;
import com.yz.model.educational.OaTaskStudentInfo;
import com.yz.service.educational.OaTaskInfoService;
import com.yz.util.Assert;
import com.yz.util.ExcelUtil;
/**
 *  教务管理-教务任务
 * @author lx
 * @date 2017年7月19日 下午3:28:26
 */
@RequestMapping("/task")
@Controller
public class OaTaskInfoController {

	@Autowired
	private OaTaskInfoService oaTaskInfoService;
	
	/**
	 * 跳转到教务任务页面列表
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("task:query")
	public String toList() {
		return "/educational/task/task_list";
	}
	/**
	 * 获取教务任务数据
	 * @param start
	 * @param length
	 * @param infoQuery
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("task:query")
	@ResponseBody
	public Object taskList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,OaTaskInfoQuery infoQuery) {
		BaseUser user = SessionUtil.getUser();
		infoQuery.setIssuer(user.getUserId());
		return oaTaskInfoService.selectOaTaskInfoByPage(start,length,infoQuery);
	}
	/**
	 * 跳转编辑或者新增教务任务
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEdit")
	@Rule("task:insert")
	public String toEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		OaTaskInfo taskInfo =new OaTaskInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String taskId = RequestUtil.getString("taskId");
			Assert.hasText(taskId, "参数名称不能为空");
			taskInfo = oaTaskInfoService.getTaskInfoById(taskId);
		}
		model.addAttribute("taskInfo", taskInfo);
		model.addAttribute("exType", exType);
		return "/educational/task/task_edit";
	}
	/**
	 * 编辑或者新增教务任务
	 * @param exType
	 * @param taskInfo
	 * @return
	 */
	@RequestMapping("/taskUpdate")
	@Rule({ "task:insert", "studyActivity:insert" })
	@ResponseBody
	@Log
	public Object taskUpdate(@RequestParam(name = "exType", required = true) String exType, OaTaskInfo taskInfo) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		
		if ("UPDATE".equalsIgnoreCase(deal)) {
			taskInfo.setUpdateUserId(user.getUserId());
			taskInfo.setUpdateUser(user.getRealName());
			if(taskInfo.getTaskType().equals("5")){ //国开考试通知
				taskInfo.setEyId(taskInfo.getGkEyId());
			}
			if(taskInfo.getTaskType().equals("13")){ //国开城市确认
				taskInfo.setEyId(taskInfo.getExamEyIdGK());
			}
			if(taskInfo.getTaskType().equals("14")){  //国开统考
				taskInfo.setEyId(taskInfo.getGkUnifiedId());
			}
			oaTaskInfoService.updateTask(taskInfo);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			taskInfo.setCreateUserId(user.getUserId());
			taskInfo.setCreateUser(user.getRealName());
			taskInfo.setIssuer(user.getUserId());
			if(taskInfo.getTaskType().equals("5")){ //国开考试通知
				taskInfo.setEyId(taskInfo.getGkEyId());
			}
			if(taskInfo.getTaskType().equals("13")){ //国开城市确认
				taskInfo.setEyId(taskInfo.getExamEyIdGK());
			}
			if(taskInfo.getTaskType().equals("14")){  //国开统考
				taskInfo.setEyId(taskInfo.getGkUnifiedId());
			}
			oaTaskInfoService.insertTask(taskInfo);
		}

		return "success";
	}
	/**
	 * 分配学员-加载学员的信息
	 * @param start
	 * @param length
	 * @param studentQuery
	 * @return
	 */
	@RequestMapping(value = "/stuList", method = RequestMethod.GET)
	@ResponseBody
	@Rule("task:stuList")
	public Object stuList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,OaTaskTargetStudentQuery studentQuery) {
		return oaTaskInfoService.queryOaTaskStudentInfo(start, length,studentQuery);
	}
	/**
	 * 跳转到分配学员列表
	 * @param taskId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toStu")
	@Rule("task:stuList")
	public String toStu(@RequestParam(name = "taskId", required = true) String taskId, HttpServletRequest request,
			Model model) {
		model.addAttribute("taskId", taskId);
		return "/educational/task/stu_list";
	}
	/**
	 * 分配学员
	 * @param idArray
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/addStu")
	@Rule("task:addStu")
	@ResponseBody
	public Object addStu(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			             @RequestParam(name = "taskId", required = true) String taskId,
			             @RequestParam(name = "operType", required = true) String operType) {
		oaTaskInfoService.addStu(idArray,taskId,operType);
		return "success";
	}
	/**
	 * 删除已分配的学员
	 * @param idArray
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/delStu")
	@Rule("task:delStu")
	@ResponseBody
	public Object delStu(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			             @RequestParam(name = "taskId", required = true) String taskId) {
		oaTaskInfoService.delStu(idArray,taskId);
		return "success";
	}
	
	/**
	 * 跳转到导入学员页面
	 * @param taskId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/stuImport")
	@Rule("task:stuImport")
	public String stuImport(@RequestParam(name = "taskId", required = true) String taskId, HttpServletRequest request,
			Model model) {
		model.addAttribute("taskId", taskId);
		return "educational/task/stu_import";
	}
	/**
	 * 导入学员
	 * @param excelStu
	 * @param taskId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/upload")
	@Rule("task:stuImport")
	@ResponseBody
	public Object upload(@RequestParam(value = "excelStu", required = false) MultipartFile excelStu,
			@RequestParam(value = "taskId", required = false) String taskId) {
		
		//对导入工具进行字段填充
		ExcelUtil.IExcelConfig<OaTaskStudentInfo> testExcelCofing = new ExcelUtil.IExcelConfig<OaTaskStudentInfo>();
		testExcelCofing.setSheetName("index").setType(OaTaskStudentInfo.class)
				.addTitle(new ExcelUtil.IExcelTitle("学员姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证号", "ext1"))
				.addTitle(new ExcelUtil.IExcelTitle("入学年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("学期", "ext2"));		

		oaTaskInfoService.importTargetStuToTask(testExcelCofing,excelStu,taskId);
		
		return "SUCCESS";
	}

	@RequestMapping("/deletes")
	@Rule({"task:deletes","studyActivity:block"})
	@ResponseBody
	public Object deletes(@RequestParam(name = "taskIds[]") String[] taskIds) throws Exception {
		oaTaskInfoService.deleteTask(taskIds);
		return "SUCCESS";
	}
	
	
	@RequestMapping("/findAddressAffirmTaskInfo")
	@ResponseBody
	public Object findAddressAffirmTaskInfo(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		return oaTaskInfoService.findAddressAffirmTaskInfo(page, rows, sName);
	}
}
