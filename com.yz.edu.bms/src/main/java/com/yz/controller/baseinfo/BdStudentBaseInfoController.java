package com.yz.controller.baseinfo;

import java.util.Map;

import com.yz.model.recruit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.model.common.PageCondition;
import com.yz.service.recruit.StudentAllService;
import com.yz.service.recruit.StudentAnnexCheckService;

@Controller
@RequestMapping("/studentBase")
public class BdStudentBaseInfoController {

	@Autowired
	private StudentAllService stdAllService;

	@Autowired
	private StudentAnnexCheckService annexCheckService;

	/**
	 * 编辑页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEdit")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact", "jStudying:queryServices"})
	public String toEdit(Model model, @RequestParam(value = "stdId", required = true) String stdId,
			@RequestParam(value = "learnId", required = true) String learnId) {
		String t = RequestUtil.getParameter("t");
		model.addAttribute("stdId", stdId);
		model.addAttribute("learnId", learnId);
		model.addAttribute("t", t);
		return "/baseinfo/student/student_edit";
	}

	@RequestMapping("/toRecruitEdit")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	public String toEditPage(Model model, @RequestParam(value = "learnId", required = true) String learnId,
			@RequestParam(value = "stdId", required = true) String stdId, String tabName) {
		model.addAttribute("learnId", learnId);
		model.addAttribute("stdId", stdId);
		model.addAttribute("tabName", tabName);
		return "/baseinfo/student/student_recruit_edit";
	}

	/**
	 * 编辑页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toBase")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	public String toBase(Model model, @RequestParam(value = "stdId", required = true) String stdId,
			@RequestParam(value = "learnId", required = true) String learnId) {
		BdStudentBaseInfo baseInfo = stdAllService.getStudentBaseInfo(stdId);
		BdStudentOther other = stdAllService.getStudentOther(stdId);
		baseInfo.setLearnId(learnId);
		model.addAttribute("baseInfo", baseInfo);
		model.addAttribute("other", other);
		return "/baseinfo/student/student_baseInfo";
	}

	/**
	 * 原学历信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toHistory")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	public String toHistory(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		BdStudentHistory history = stdAllService.getStudentHistory(learnId);
		model.addAttribute("history", history);
		model.addAttribute("updateUrl", "recruit/updateHistory.do");
		return "/baseinfo/student/student_history";
	}

	/**
	 * 报读信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toRecruit")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	public String toRecruit(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		BdStudentEnroll enroll = stdAllService.getStudentEnroll(learnId);
		RecruitEmployeeInfo recruitEmpInfo = stdAllService.getRecruitEmpInfo(learnId);
		Map<String, Object> feeInfo = stdAllService.getFeeInfo(learnId);
		model.addAttribute("enroll", enroll);
		model.addAttribute("feeInfo", feeInfo);
		model.addAttribute("recruitEmpInfo", recruitEmpInfo);
		model.addAttribute("updateUrl", "recruit/updateRecruit.do");
		return "/baseinfo/student/student_recruit";
	}

	/**
	 * 附件信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toAnnexList")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	public String toAnnexList(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		
		String t = RequestUtil.getParameter("t");
		
		model.addAttribute("learnId", learnId);
		model.addAttribute("updateUrl", "studentBase/updateAnnex.do");
		model.addAttribute("deleteUrl", "studentBase/delAnnex.do");
		model.addAttribute("getDataUrl", "studentBase/getAnnexList.do");
//		model.addAttribute("chargeUrl", "recruit/annexCharge.do");
		if("MODI".equals(t)) {
			return "/recruit/student/student_annex_list";
		}
		return "/baseinfo/student/student_annex_list";
	}
	
	/**
	 * 更新附件信息
	 * 
	 * @param annexInfo
	 * @return
	 */
	@Log
	@RequestMapping("/updateAnnex")
	@Rule({"studying:upload"})
	@ResponseBody
	public Object updateAnnex(MultipartHttpServletRequest mRequest, BdLearnAnnex annexInfo) {
		return annexCheckService.updateAnnexInfo(annexInfo);
	}

	/**
	 * 更新附件信息
	 * 
	 * @param annexInfo
	 * @return
	 */
	@Log
	@RequestMapping("/delAnnex")
	@Rule({"studying:upload"})
	@ResponseBody
	public Object deleteAnnex(BdLearnAnnex annexInfo) {
		annexCheckService.delAnnexInfo(annexInfo);
		return null;
	}

	@RequestMapping("/getAnnexList")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	@ResponseBody
	public Object getAnnexList(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		return annexCheckService.getAnnexList(learnId);
	}

	/**
	 * 缴费信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toFee")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	public String toFee(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("learnId", learnId);
		model.addAttribute("grade", annexCheckService.getGrade(learnId));
		model.addAttribute("queryUrl", "recruit/getFeeList.do");
		return "/baseinfo/student/student_fee";
	}

	/**
	 * 缴费信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getFeeList")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	@ResponseBody
	public Object getFeeList(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		return stdAllService.getFeeList(learnId);
	}

	/**
	 * 考试成绩页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toScore")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	public String toScore(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("eScoreList", stdAllService.getEScoreList(learnId));
		model.addAttribute("tScoreList", stdAllService.getTScoreList(learnId));
		return "/baseinfo/student/student_score";
	}

	/**
	 * 审核记录页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toCharge")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact" })
	public String toCharge(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("checkStatus", stdAllService.getCheckDataStatus(learnId));
		model.addAttribute("graduate", stdAllService.getGraduateStatus(learnId));
		return "/baseinfo/student/student_charge";
	}

	/**
	 * 变更记录页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toModify")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices" })
	public String toModify(Model model, @RequestParam(value = "learnId", required = true) String learnId,
			PageCondition page) {
		model.addAttribute("learnId", learnId);
		return "/baseinfo/student/student_modify";
	}

	/**
	 * 获取变更记录
	 * 
	 * @param studentModifyInfo
	 * @return
	 */
	@RequestMapping("/getModifyList")
	@Rule({ "standard:query", "feeReview:query", "stdfee:query", "studying:queryContact","jStudying:queryServices"})
	@ResponseBody
	public Object getModifyList(StudentModifyListInfo studentModifyInfo, PageCondition page) {
		return stdAllService.getModifyList(studentModifyInfo, page);
	}

}
