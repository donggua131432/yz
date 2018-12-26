package com.yz.controller.graduate;


import javax.servlet.http.HttpServletRequest;

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
import com.yz.model.admin.BaseUser;
import com.yz.model.graduate.BdGraduateRecordsInfo;
import com.yz.model.graduate.GraduateApplyQuery;
import com.yz.service.graduate.GraduateCheckService;
import com.yz.util.JsonUtil;

/**
 * 毕业审核
 * @author lx
 * @date 2017年7月12日 下午3:32:20
 */
@Controller
@RequestMapping("/check")
public class GraduateCheckController {

	@Autowired
	private GraduateCheckService graduateCheckService;
	/**
	 * 跳转到 毕业审核列表页
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("check:query")
	public String toList() {
		return "graduate/check/check_list";
	}
	
	
	/**
	 * 跳转到资料审核的页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toData")
	@Rule("check:query")
	public String toData(HttpServletRequest request,Model model) {
		return "/graduate/check/data_list";
	}
	/**
	 * 分页获取毕业资料,成绩核查
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/dataList", method = RequestMethod.GET)
	@Rule("check:query")
	@ResponseBody
	public Object graduateDataCheckList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GraduateApplyQuery query) {
		return graduateCheckService.queryGraduateDataCheckByPage(start, length, query);
	}
	/**
	 * 跳转到某个学员的资料审核页面
	 * @param checkId
	 * @param learnId
	 * @param stdName
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/checkData")
	@Rule("check:query")
	public String checkData(@RequestParam(name = "checkId", required = true) String checkId,
			                @RequestParam(name = "learnId", required = true) String learnId,
			                @RequestParam(name = "stdName", required = true) String stdName,
			                @RequestParam(name = "exType", required = true) String exType,
			                HttpServletRequest request,Model model) {
		Object obj = graduateCheckService.getEducationalTaskInfo(learnId);
		model.addAttribute("taskInfo", JsonUtil.object2String(obj));
		model.addAttribute("checkId", checkId);
		model.addAttribute("learnId", learnId);
		model.addAttribute("stdName", stdName);
		model.addAttribute("recordInfo",graduateCheckService.getBdGraduateRecordsInfo(checkId));
		model.addAttribute("exType", exType);
		return "/graduate/check/look_task_list";
	}
    
	/**
	 * 跳转到成绩审核页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toScore")
	@Rule("check:query")
	public String toScore(HttpServletRequest request,Model model) {
		return "/graduate/check/score_list";
	}
	/**
	 * 某个学员的成绩审核页面
	 * @param checkId
	 * @param stdId
	 * @param learnId
	 * @param stdName
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/checkScore")
	@Rule("check:query")
	public String checkScore(@RequestParam(name = "checkId", required = true) String checkId,
							@RequestParam(name = "stdId", required = true) String stdId,
			                @RequestParam(name = "learnId", required = true) String learnId,
			                @RequestParam(name = "stdName", required = true) String stdName,
			                @RequestParam(name = "exType", required = true) String exType,
			                HttpServletRequest request,Model model) {
		Object obj = graduateCheckService.getBdStudentScoreInfo(learnId, stdId);
		model.addAttribute("checkId", checkId);
		model.addAttribute("scoreInfo", obj);
		model.addAttribute("recordInfo",graduateCheckService.getBdGraduateRecordsInfo(checkId));
		model.addAttribute("stdName", stdName);
		model.addAttribute("exType", exType);
		return "/graduate/check/look_score_list";
	}
	
	/**
	 * 论文审核页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toPaper")
	@Rule("check:query")
	public String toPaper(HttpServletRequest request,Model model) {
		return "/graduate/check/paper_list";
	}
	/**
	 * 加载论文审核记录
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/paperList", method = RequestMethod.GET)
	@Rule("check:query")
	@ResponseBody
	public Object graduatePaperCheckList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GraduateApplyQuery query) {
		return graduateCheckService.queryGraduatePaperCheckByPage(start, length, query);
	}
	/**
	 * 跳转到某个学员的论文审核页面
	 * @param checkId
	 * @param learnId
	 * @param stdName
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/checkPaper")
	@Rule("check:query")
	public String checkPaper(@RequestParam(name = "checkId", required = true) String checkId,
			                @RequestParam(name = "learnId", required = true) String learnId,
			                @RequestParam(name = "stdName", required = true) String stdName,
			                @RequestParam(name = "exType", required = true) String exType,
			                HttpServletRequest request,Model model) {
		Object obj = graduateCheckService.getBdStudentPaperInfo(learnId);
		model.addAttribute("checkId", checkId);
		model.addAttribute("paperInfo", obj);
		model.addAttribute("stdName", stdName);
		model.addAttribute("recordInfo",graduateCheckService.getBdGraduateRecordsInfo(checkId));
		model.addAttribute("exType", exType);
		return "/graduate/check/look_paper_list";
	}
	
	/**
	 * 跳转到学费审核页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toFee")
	@Rule("check:query")
	public String toFee(HttpServletRequest request,Model model) {
		return "/graduate/check/fee_list";
	}
	/**
	 * 学费审核记录
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/feeList", method = RequestMethod.GET)
	@Rule("check:query")
	@ResponseBody
	public Object graduateFeeCheckList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GraduateApplyQuery query) {
		return graduateCheckService.queryFeeCheckDataInfoByPage(start, length, query);
	}
	/**
	 * 审核某个学员的学费信息
	 * @param checkId
	 * @param learnId
	 * @param stdName
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/checkFee")
	@Rule("check:query")
	public String checkFee(@RequestParam(name = "checkId", required = true) String checkId,
			                @RequestParam(name = "learnId", required = true) String learnId,
			                @RequestParam(name = "stdName", required = true) String stdName,
			                @RequestParam(name = "exType", required = true) String exType,
			                HttpServletRequest request,Model model) {
		Object obj = graduateCheckService.selectPayInfo(learnId);
		model.addAttribute("checkId", checkId);
		model.addAttribute("grade", graduateCheckService.selectGrade(learnId));
		model.addAttribute("feeInfo", JsonUtil.object2String(obj));
		model.addAttribute("recordInfo",graduateCheckService.getBdGraduateRecordsInfo(checkId));
		model.addAttribute("stdName", stdName);
		model.addAttribute("exType", exType);
		return "/graduate/check/look_fee_list";
	}
	
	/**
	 * 跳转到 图像采集
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toPicture")
	@Rule("check:query")
	public String toPicture(HttpServletRequest request,Model model) {
		return "/graduate/check/picture_list";
	}
	/**
	 * 图像采集信息列表
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/pictureList", method = RequestMethod.GET)
	@Rule("check:query")
	@ResponseBody
	public Object queryGraduatePictureCheckByPage(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GraduateApplyQuery query) {
		return graduateCheckService.queryGraduatePictureCheckByPage(start, length, query);
	}
	/**
	 * 某个学员的图像采集信息审核
	 * @param checkId
	 * @param learnId
	 * @param stdName
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/checkPicture")
	@Rule("check:query")
	public String checkPicture(@RequestParam(name = "checkId", required = true) String checkId,
			                @RequestParam(name = "learnId", required = true) String learnId,
			                @RequestParam(name = "stdName", required = true) String stdName,
			                @RequestParam(name = "exType", required = true) String exType,
			                HttpServletRequest request,Model model) {
		Object obj = graduateCheckService.getBdGraduateCollectInfo(learnId);
		model.addAttribute("checkId", checkId);
		model.addAttribute("pictureInfo", obj);
		model.addAttribute("stdName", stdName);
		model.addAttribute("recordInfo",graduateCheckService.getBdGraduateRecordsInfo(checkId));
		model.addAttribute("exType", exType);
		return "/graduate/check/look_picture_list";
	}
	/**
	 * 审核操作
	 * @param checkId
	 * @param remark
	 * @param checkStatus
	 * @return
	 */
	@RequestMapping("/checkStatus")
	@Rule("check:check")
	@ResponseBody
	@Log
	public Object checkStatus(@RequestParam(name = "checkId") String checkId,
			@RequestParam(name = "remark", required = true) String remark,
			@RequestParam(name="checkStatus") String checkStatus) {
		BaseUser user = SessionUtil.getUser();
		BdGraduateRecordsInfo recordsInfo = new BdGraduateRecordsInfo();
		recordsInfo.setCheckId(checkId);
		recordsInfo.setCheckUserId(user.getUserId());
		recordsInfo.setCheckUserName(user.getRealName());
		recordsInfo.setRemark(remark);
		recordsInfo.setCheckStatus(checkStatus);
		graduateCheckService.checkStatus(recordsInfo);

		return "success";
	}
	
	/**
	 * 跳转到毕业核准列表页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toApproval")
	@Rule("check:query")
	public String toApproval(HttpServletRequest request,Model model) {
		return "/graduate/approval/approval_list";
	}
	/**
	 * 毕业核准信息列表
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/approvalList", method = RequestMethod.GET)
	@Rule("check:query")
	@ResponseBody
	public Object graduateApprovalList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GraduateApplyQuery query) {
		return graduateCheckService.queryBdGraduateApprovalInfo(start, length, query);
	}
	
	/**
	 * 某个学员的毕业核准
	 * @param graduateId
	 * @param learnId
	 * @param stdName
	 * @param stdId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/resultApproval")
	@Rule("check:query")
	public String resultApproval(@RequestParam(name = "graduateId", required = true) String graduateId,
			                @RequestParam(name = "learnId", required = true) String learnId,
			                @RequestParam(name = "stdName", required = true) String stdName,
			                @RequestParam(name = "stdId", required = true) String stdId,
			                HttpServletRequest request,Model model) {
		Object obj = graduateCheckService.getBdCheckResultShowInfo(graduateId);
		model.addAttribute("graduateId",graduateId);
		model.addAttribute("resultInfo", obj);
		model.addAttribute("stdName", stdName);
		model.addAttribute("learnId", learnId);
		model.addAttribute("stdId", stdId);
		return "/graduate/approval/look_result_list";
	}

	
	/**
	 * 确认核准
	 * @param graduateId
	 * @param learnId
	 * @param remark
	 * @param checkStatus
	 * @return
	 */
	@RequestMapping("/checkAffirm")
	@Rule("check:check")
	@ResponseBody
	@Log
	public Object checkAffirm(@RequestParam(name = "graduateId") String graduateId,
			@RequestParam(name = "learnId", required = true) String learnId,
			@RequestParam(name = "remark", required = true) String remark,
			@RequestParam(name="checkStatus") String checkStatus) {
	
		graduateCheckService.checkAffirm(graduateId,checkStatus,remark,learnId);

		return "success";
	}
	
	
	
}
