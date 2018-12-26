package com.yz.controller.recruit;

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
import com.yz.model.common.PageCondition;
import com.yz.model.condition.recruit.AllStudentQuery;
import com.yz.service.recruit.StudentAllService;
import com.yz.service.recruit.StudentAnnexCheckService;

/**
 * 全部学员
 * 
 * @author Administrator
 *
 */
@RequestMapping("/allStudent")
@Controller
public class StudentAllController {

	@Autowired
	private StudentAllService stdAllService;

	/**
	 * 全部学员列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("all_student")
	public String toList(Model model) {
		return "/recruit/student/student_all_list";
	}

	@RequestMapping("/findAllStudent")
	@Rule("all_student")
	@ResponseBody
	public Object findAllStudent(AllStudentQuery queryInfo) {
		return stdAllService.findAllStudent(queryInfo);
	}

	/**
	 * 编辑页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEdit")
	@Rule({ "all_student:query", "studentSearch:query" })
	public String toEdit(Model model, String stdId, String learnId, String recruitType) {
		model.addAttribute("stdId", stdId);
		model.addAttribute("learnId", learnId);
		model.addAttribute("recruitType", recruitType);
		return "/recruit/student/student_edit";
	}

	/**
	 * 编辑页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toBase")
	@Rule({ "all_student:query", "studentSearch:query" })
	public String toBase(Model model, String stdId, String learnId, String recruitType) {
		BdStudentBaseInfo baseInfo = stdAllService.getStudentBaseInfo(stdId);
		BdStudentOther other = stdAllService.getStudentOther(stdId);
		baseInfo.setLearnId(learnId);
		model.addAttribute("recruitType", recruitType);
		model.addAttribute("baseInfo", baseInfo);
		model.addAttribute("other", other);
		model.addAttribute("updateUrl", "allStudent/updateBase.do");
		return "/recruit/student/student_baseInfo";
	}

	/**
	 * 原学历信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toHistory")
	@Rule({ "all_student:query", "studentSearch:query" })
	public String toHistory(Model model, String learnId, String recruitType) {
		BdStudentHistory history = stdAllService.getStudentHistory(learnId);
		model.addAttribute("recruitType", recruitType);
		model.addAttribute("history", history);
		model.addAttribute("updateUrl", "allStudent/updateHistory.do");
		return "/recruit/student/student_history";
	}

	/**
	 * 报读信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toRecruit")
	@Rule({ "all_student:query", "studentSearch:query" })
	public String toRecruit(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		BdStudentEnroll enroll = stdAllService.getStudentEnroll(learnId);
		RecruitEmployeeInfo recruitEmpInfo = stdAllService.getRecruitEmpInfo(learnId);
		Map<String, Object> feeInfo = stdAllService.getFeeInfo(learnId);
		model.addAttribute("enroll", enroll);
		model.addAttribute("feeInfo", feeInfo);
		model.addAttribute("recruitEmpInfo", recruitEmpInfo);
		model.addAttribute("updateUrl", "allStudent/updateRecruit.do");
		return "/recruit/student/student_recruit";
	}

	/**
	 * 附件信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toAnnexList")
	@Rule({ "all_student:query", "studentSearch:query" })
	public String toAnnexList(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("learnId", learnId);
		model.addAttribute("updateUrl", "allStudent/updateAnnex.do");
		model.addAttribute("deleteUrl", "allStudent/delAnnex.do");
		model.addAttribute("getDataUrl", "allStudent/getAnnexList.do");
		model.addAttribute("chargeUrl", "allStudent/annexCharge.do");
		return "/recruit/student/student_annex_list";
	}

	@RequestMapping("/getAnnexList")
	@Rule({ "all_student:query", "studentSearch:query" })
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
	@Rule({ "all_student:query", "studentSearch:query" })
	public String toFee(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("learnId", learnId);
		model.addAttribute("grade", annexCheckService.getGrade(learnId));
		model.addAttribute("queryUrl", "allStudent/getFeeList.do");
		return "/recruit/student/student_fee";
	}

	/**
	 * 缴费信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getFeeList")
	@Rule({ "all_student:query", "studentSearch:query" })
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
	@Rule({"all_student:query", "studentSearch:query"})
	public String toScore(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("eScoreList", stdAllService.getEScoreList(learnId));
		model.addAttribute("tScoreList", stdAllService.getTScoreList(learnId));
		return "/recruit/student/student_score";
	}

	/**
	 * 审核记录页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toCharge")
	@Rule({ "all_student:query", "studentSearch:query" })
	public String toCharge(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("checkStatus", stdAllService.getCheckDataStatus(learnId));
		model.addAttribute("graduate", stdAllService.getGraduateStatus(learnId));
		return "/recruit/student/student_charge";
	}

	/**
	 * 变更记录页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toModify")
	@Rule({ "all_student:query", "studentSearch:query" })
	public String toModify(Model model, @RequestParam(value = "learnId", required = true) String learnId,
			PageCondition page) {
		model.addAttribute("learnId", learnId);
		return "/recruit/student/student_modify";
	}

	/**
	 * 获取变更记录
	 * 
	 * @return
	 */
	@RequestMapping("/getModifyList")
	@Rule({ "recruit:query", "all_student:query", "standard:query","studentSearch:query" })
	@ResponseBody
	public Object getModifyList(StudentModifyListInfo studentModifyInfo, PageCondition page) {
		return stdAllService.getModifyList(studentModifyInfo, page);
	}

	/**
	 * 更新基础信息
	 * 
	 * @return
	 */
	@Log
	@RequestMapping("/updateBase")
	@Rule({ "all_student:update", "studentSearch:query" })
	@ResponseBody
	public Object updateBase(MultipartHttpServletRequest request, BdStudentBaseInfo baseInfo, BdStudentOther other,
			String recruitType) {
		stdAllService.updateBaseInfo(baseInfo, recruitType);
		stdAllService.updateOther(other);
		return null;
	}

	/**
	 * 更新原学历信息
	 * 
	 * @return
	 */
	@Log
	@RequestMapping("/updateHistory")
	@Rule({ "all_student:update", "studentSearch:query" })
	@ResponseBody
	public Object updateHistory(BdStudentHistory history, String recruitType) {
		stdAllService.updateHistory(history, recruitType);
		return null;
	}

	/**
	 * 更新报读信息
	 * 
	 * @return
	 */
	@Log
	@RequestMapping("/updateRecruit")
	@Rule({ "all_student:update", "studentSearch:query" })
	@ResponseBody
	public Object updateRecruit(BdStudentRecruit recruitInfo) {
		stdAllService.updateRecruit(recruitInfo);
		return null;
	}

	@Autowired
	private StudentAnnexCheckService annexCheckService;

	/**
	 * 更新附件信息
	 * 
	 * @return
	 */
	@Log
	@RequestMapping("/updateAnnex")
	@Rule({ "all_student:upload", "studentSearch:query" })
	@ResponseBody
	public Object updateAnnex(MultipartHttpServletRequest mRequest, BdLearnAnnex annexInfo) {
		return annexCheckService.updateAnnexInfo(annexInfo);
	}

	/**
	 * 更新附件信息
	 * 
	 * @return
	 */
	@Log
	@RequestMapping("/delAnnex")
	@Rule({ "all_student:upload", "studentSearch:query" })
	@ResponseBody
	public Object deleteAnnex(BdLearnAnnex annexInfo) {
		annexCheckService.delAnnexInfo(annexInfo);
		return null;
	}

	/**
	 * 审核附件
	 * 
	 * @return
	 */
	@Log
	@RequestMapping("/annexCharge")
	@Rule({ "all_student:charge", "studentSearch:query" })
	@ResponseBody
	public Object annexCharge(BdLearnAnnex annexInfo) {
		annexCheckService.charge(annexInfo);
		return null;
	}

}
