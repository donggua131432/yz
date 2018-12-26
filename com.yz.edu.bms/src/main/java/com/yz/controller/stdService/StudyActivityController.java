package com.yz.controller.stdService;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.condition.educational.OaTaskInfoQuery;
import com.yz.model.condition.educational.OaTaskTargetStudentQuery;
import com.yz.model.educational.OaTaskInfo;
import com.yz.service.educational.OaTaskInfoService;
import com.yz.util.Assert;
import com.yz.util.JsonUtil;


/**
 * 学服活动
 * @author lx
 * @date 2017年7月20日 下午5:59:06
 */
@RequestMapping("/studyActivity")
@Controller
public class StudyActivityController {

	@Autowired
	private OaTaskInfoService oaTaskInfoService;
	
	/**
	 * 学服活动页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("studyActivity:query")
	public String toList(Model model) {
		BaseUser user = SessionUtil.getUser();
		model.addAttribute("userId", user.getUserId());
		return "/stdservice/activity/activity_list";
	}
	/**
	 * 学服活动数据
	 * @param start
	 * @param length
	 * @param infoQuery
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("studyActivity:query")
	@ResponseBody
	public Object activityList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,OaTaskInfoQuery infoQuery) {
		return oaTaskInfoService.selectOaTaskInfoByPage(start,length,infoQuery);
	}
	/**
	 * 学服活动编辑
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEdit")
	@Rule("studyActivity:insert")
	public String toEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
	
		OaTaskInfo taskInfo =new OaTaskInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String taskId = RequestUtil.getString("taskId");
			Assert.hasText(taskId, "参数名称不能为空");
			taskInfo = oaTaskInfoService.getTaskInfoById(taskId);
		}
		model.addAttribute("taskInfo", taskInfo);
		model.addAttribute("exType", exType);
		return "/stdservice/activity/activity_edit";
	}
	/**
	 * 修改
	 * @param exType
	 * @param taskInfo
	 * @return
	 */
	@RequestMapping("/taskUpdate")
	@Rule("studyActivity:insert")
	@ResponseBody
	@Log
	public Object taskUpdate(@RequestParam(name = "exType", required = true) String exType, OaTaskInfo taskInfo) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();

		if ("UPDATE".equalsIgnoreCase(deal)) {
			taskInfo.setUpdateUserId(user.getUserId());
			taskInfo.setUpdateUser(user.getRealName());
			oaTaskInfoService.updateTask(taskInfo);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			taskInfo.setCreateUserId(user.getUserId());
			taskInfo.setCreateUser(user.getRealName());
			taskInfo.setIssuer(user.getUserId());
			taskInfo.setPrincipal(user.getUserId());
			oaTaskInfoService.insertTask(taskInfo);
		}

		return "success";
	}
	/**
	 * 学员数据
	 * @param start
	 * @param length
	 * @param studentQuery
	 * @return
	 */
	@RequestMapping(value = "/stuList", method = RequestMethod.GET)
	@Rule("studyActivity:stuQuery")
	@ResponseBody
	public Object stuList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,OaTaskTargetStudentQuery studentQuery) {
		return oaTaskInfoService.queryOaTaskStudentInfo(start, length,studentQuery);
	}
	/**
	 * 查看学员信息页面
	 * @param taskId
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toStu")
	@Rule("studyActivity:stuQuery")
	public String toStu(@RequestParam(name = "taskId", required = true) String taskId,
			@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		model.addAttribute("taskId", taskId);
		model.addAttribute("exType", exType);
		return "/stdservice/activity/stu_list";
	}
	/**
	 * 停用或者启用
	 * @return
	 */
	@RequestMapping("/activityBlock")
	@Rule("studyActivity:block")
	@Log(needLog = true)
	@ResponseBody
	public Object activityBlock() {

	    OaTaskInfo taskInfo = new OaTaskInfo();

		BaseUser principal = SessionUtil.getUser();
		String taskId = RequestUtil.getString("taskId");
		String exType = RequestUtil.getString("exType");
		taskInfo.setUpdateUserId(principal.getUserId());
		taskInfo.setUpdateUser(principal.getRealName());
		taskInfo.setPrincipal(principal.getUserId());
		taskInfo.setTaskId(taskId);
		if ("START".equalsIgnoreCase(exType)) {
			taskInfo.setIsAllow("1");
		} else if ("STOP".equalsIgnoreCase(exType)) {
			taskInfo.setIsAllow("0");

		}
		oaTaskInfoService.updateTask(taskInfo);
	
		return "SUCCESS";
	}
	/**
	 * 统计
	 * @param taskId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/getStatistics")
	@Rule("studyActivity:statistics")
	public String getStatistics(@RequestParam(name = "taskId", required = true) String taskId,
			                HttpServletRequest request,Model model) {
		Object obj = oaTaskInfoService.getOaTaskStatisticsInfo(taskId);
		model.addAttribute("resultInfo", JsonUtil.object2String(obj));
		model.addAttribute("taskId", taskId);
		return "/stdservice/activity/result_list";
	}
	
	/**
	 * 提醒某个任务未完成的学员信息
	 * @return
	 */
	@RequestMapping("/remindUnfinishedStudent")
	@Log(needLog = true)
	@Rule("studyActivity:remind")
	@ResponseBody
	public Object remindUnfinishedStudent(){
		String taskId = RequestUtil.getString("taskId");
		oaTaskInfoService.remindUnfinishedStudent(taskId);
		return "SUCCESS";
	}
	/**
	 * 导出某个任务为完成的学员信息
	 * @param taskId
	 * @param response
	 */
	@RequestMapping("/export")
	@Rule("studyActivity:export")
	public void export(String taskId, HttpServletResponse response) {
		oaTaskInfoService.exportUnfinishedStudent(taskId, response);
	}
	
	@RequestMapping("/findAllTaskInfo")
	@ResponseBody
	public Object findAllTaskInfo(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		return oaTaskInfoService.findAllTaskInfo(page, rows, sName);
	}
	
	@RequestMapping("/addAllStu")
	@Rule({"task:addStu","studyActivity:insert"})
	@ResponseBody
	public Object addAllStu(OaTaskTargetStudentQuery studentQuery) {
		Assert.hasText(studentQuery.getTaskId(), "参数名称不能为空");
		oaTaskInfoService.addAllStu(studentQuery);
		return "success";
	}
	@RequestMapping("/delAllStu")
	@Rule({"task:addStu","studyActivity:insert"})
	@ResponseBody
	public Object delAllStu(OaTaskTargetStudentQuery studentQuery) {
		Assert.hasText(studentQuery.getTaskId(), "参数名称不能为空");
		oaTaskInfoService.delAllStu(studentQuery);
		return "success";
	}
	
	@RequestMapping("/getGKExamYear")
	@ResponseBody
	public Object getGKExamYear() {
		return oaTaskInfoService.getGKExamYear();
	}
	
	@RequestMapping("/findGraduateDataTaskInfo")
	@ResponseBody
	public Object findGraduateDataTaskInfo(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		return oaTaskInfoService.findGraduateDataTaskInfo(page, rows, sName);
	}

	@RequestMapping("/findTaskInfo")
	@ResponseBody
	public Object findTaskInfoByType(String sName,String taskType ,@RequestParam(value = "rows", defaultValue = "7") int rows,
										   @RequestParam(value = "page", defaultValue = "1") int page) {
		return oaTaskInfoService.findTaskInfoByType(page, rows, sName,taskType);
	}
	
	/**
	 * 国开考场城市确认任务获取考试年度
	 * @param status
	 * @return
	 */
	@RequestMapping("/getExamYearForGK")
	@ResponseBody
	public Object getExamYearForGK(@RequestParam(name = "status") String status) {
		return oaTaskInfoService.getExamYearForGK(status);
	}
	/**
	 * 国开统考设置
	 * @param status
	 * @return
	 */
	@RequestMapping("/getGkUnifiedExam")
	@ResponseBody
	public Object getGkUnifiedExam(@RequestParam(name = "status") String status) {
		return oaTaskInfoService.getGkUnifiedExam(status);
	}
	
}
