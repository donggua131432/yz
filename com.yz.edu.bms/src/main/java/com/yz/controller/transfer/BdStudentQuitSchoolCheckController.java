package com.yz.controller.transfer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.transfer.BdStudentQuitQuery;
import com.yz.service.transfer.BdStudentQuitService;
/**
 * 休学管理-审批
 * @author lx
 * @date 2018年5月24日 上午11:48:53
 */
@Controller
@RequestMapping("/quitSchoolCheck")
public class BdStudentQuitSchoolCheckController {

	@Autowired
	private BdStudentQuitService studentQuitService;
	/**
	 * 跳转到休学申请页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	@Rule("quitSchoolCheck:query")
	public String showList(HttpServletRequest request) {
		return "transfer/quit/student-quit-check";
	}
	
	/**
	 * 加载休学申请的列表
	 * @param start
	 * @param length
	 * @param quitQuery
	 * @return
	 */
	@RequestMapping("/findAllBdStudentQuit")
	@ResponseBody
	@Rule("quitSchoolCheck:query")
	public Object findAllBdStudentQuitForCheck(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length,BdStudentQuitQuery quitQuery){
		return studentQuitService.findAllBdStudentQuitForCheck(start, length, quitQuery);
	}
	/**
	 * 查看要审核的信息
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/lookQuitSchool")
	@Rule("quitSchoolCheck:query")
	public String lookQuitSchoolApply(HttpServletRequest request, Model model) {
		String id = RequestUtil.getString("id");
		model.addAttribute("quitSchoolInfo", studentQuitService.getQuitSchoolApplyInfo(id));
		return "transfer/quit/student_quit_approval";
	}
	
	/**
	 * 审核
	 * @param id
	 * @param rejectReason
	 * @param checkStatus
	 * @param learnId
	 * @return
	 */
	@RequestMapping("/checkStudentQuitSchoolApply")
	@ResponseBody
	@Rule("quitSchoolCheck:check")
	public Object checkStudentQuitSchoolApply(@RequestParam(name = "id") String id,
			@RequestParam(name = "rejectReason") String rejectReason,
			@RequestParam(name = "checkStatus") String checkStatus,
			@RequestParam(name = "learnId") String learnId){
	   studentQuitService.checkStudentQuitSchoolApply(id,rejectReason,checkStatus,learnId);
	   return "SUCCESS";
	}
	
	/**
	 * 批量操作界面
	 * @param request
	 * @return
	 */
	@RequestMapping("/toSelectChecks")
	@Rule("quitSchoolCheck:check")
	public String toSelectChecks(HttpServletRequest request) {
		return "transfer/quit/student_quit_batchoper";
	}
	
	/**
	 * 批量审核
	 * @param ids
	 * @param rejectReason
	 * @param checkStatus
	 * @return
	 */
	@RequestMapping("/batchCheck")
	@ResponseBody
	@Rule("quitSchoolCheck:check")
	public Object batchCheck(@RequestParam(name = "ids[]") String[] ids,
			@RequestParam(name = "rejectReason") String rejectReason,
			@RequestParam(name = "checkStatus") String checkStatus){
	   studentQuitService.batchCheck(ids,rejectReason,checkStatus);
	   return "SUCCESS";
	}
	
	/**
	 * 导出
	 * @param query
	 * @param response
	 */
	@RequestMapping("/export")
	@Rule("quitSchoolCheck:export")
	public void export(BdStudentQuitQuery quitQuery, HttpServletResponse response) {
		studentQuitService.exportQuitSchoolApply(quitQuery, response);
	}
}
