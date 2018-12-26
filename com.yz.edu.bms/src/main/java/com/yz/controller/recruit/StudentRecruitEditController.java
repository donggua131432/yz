package com.yz.controller.recruit;

import java.util.Map;

import com.yz.model.recruit.*;
import com.yz.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.model.common.EmpQueryInfo;
import com.yz.model.common.PageCondition;
import com.yz.service.common.BaseInfoService;
import com.yz.service.recruit.StudentAllService;
import com.yz.service.recruit.StudentAnnexCheckService;

@Controller
@RequestMapping("/recruit")
public class StudentRecruitEditController {

	@Autowired
	private StudentAllService stdAllService;

	@RequestMapping("/toRecruitEdit")
	@Rule("recruit:update")
	public String toEditPage(Model model, String learnId, String stdId, String recruitType) {
		model.addAttribute("learnId", learnId);
		model.addAttribute("stdId", stdId);
		model.addAttribute("recruitType", recruitType);
		return "/recruit/student/student_recruit_edit";
	}

	/**
	 * 编辑页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toBase")
	@Rule("recruit:query")
	public String toBase(Model model, String stdId, String learnId, String recruitType) {
		BdStudentBaseInfo baseInfo = stdAllService.getStudentBaseInfo(stdId);
		String isSuppose = stdAllService.getIsSuppose(stdId);
		BdStudentOther other = stdAllService.getStudentOther(stdId);
		baseInfo.setLearnId(learnId);
		if(!baseInfo.getS_idCard().equals(baseInfo.getIdCard())) {
			isSuppose="0";
		}
		model.addAttribute("recruitType", recruitType);
		model.addAttribute("isSuppose", isSuppose);
		model.addAttribute("baseInfo", baseInfo);
		model.addAttribute("other", other);
		model.addAttribute("updateUrl", "recruit/updateBase.do");
		return "/recruit/student/student_baseInfo";
	}

	/**
	 * 原学历信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toHistory")
	@Rule("recruit:query")
	public String toHistory(Model model, String stdId, String learnId, String recruitType) {
		BdStudentHistory history = stdAllService.getStudentHistory(learnId);
		model.addAttribute("recruitType", recruitType);
		history.setLearnId(learnId);
		history.setStdId(stdId);
		model.addAttribute("history", history);
		model.addAttribute("updateUrl", "recruit/updateHistory.do");
		return "/recruit/student/student_history";
	}

	/**
	 * 报读信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toRecruit")
	@Rule("recruit:query")
	public String toRecruit(Model model, String learnId) {
		BdStudentEnroll enroll = stdAllService.getStudentEnroll(learnId);
		RecruitEmployeeInfo recruitEmpInfo = stdAllService.getRecruitEmpInfo(learnId);
		Map<String, Object> feeInfo = stdAllService.getFeeInfo(learnId);
		model.addAttribute("enroll", enroll);
		model.addAttribute("feeInfo", feeInfo);
		model.addAttribute("recruitEmpInfo", recruitEmpInfo);
		model.addAttribute("updateUrl", "recruit/updateRecruit.do");
		return "/recruit/student/student_recruit";
	}

	/**
	 * 附件信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toAnnexList")
	@Rule("recruit:query")
	public String toAnnexList(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("learnId", learnId);
		model.addAttribute("updateUrl", "recruit/updateAnnex.do");
		model.addAttribute("deleteUrl", "recruit/delAnnex.do");
		model.addAttribute("getDataUrl", "recruit/getAnnexList.do");
		model.addAttribute("chargeUrl", "recruit/annexCharge.do");
		return "/recruit/student/student_annex_list";
	}

	@RequestMapping("/getAnnexList")
	@Rule("recruit:query")
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
	@Rule("recruit:query")
	public String toFee(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("learnId", learnId);
		model.addAttribute("grade", annexCheckService.getGrade(learnId));
		model.addAttribute("queryUrl", "recruit/getFeeList.do");
		return "/recruit/student/student_fee";
	}

	/**
	 * 缴费信息页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getFeeList")
	@Rule("recruit:query")
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
	@Rule("recruit:query")
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
	@Rule("recruit:query")
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
	@Rule("recruit:query")
	public String toModify(Model model, @RequestParam(value = "learnId", required = true) String learnId,
			PageCondition page) {
		model.addAttribute("learnId", learnId);
		return "/recruit/student/student_modify";
	}

	/**
	 * 获取变更记录
	 * @return
	 */
	@RequestMapping("/getModifyList")
	@Rule({ "recruit:query","all_student:query"})
	@ResponseBody
	public Object getModifyList(StudentModifyListInfo studentModifyInfo, PageCondition page) {
		return stdAllService.getModifyList(studentModifyInfo, page);
	}

	/**
	 * 更新基础信息
	 * @return
	 */
	@Log
	@RequestMapping("/updateBase")
	@Rule("recruit:update")
	@ResponseBody
	public Object updateBase(MultipartHttpServletRequest request, BdStudentBaseInfo baseInfo, BdStudentOther other, String recruitType) {
		stdAllService.updateBaseInfo(baseInfo,recruitType);
		stdAllService.updateOther(other);
		return null;
	}

	/**
	 * 更新原学历信息
	 * @return
	 */
	@Log
	@RequestMapping("/updateHistory")
	@Rule("recruit:update")
	@ResponseBody
	public Object updateHistory(BdStudentHistory history, String recruitType) {
		stdAllService.updateHistory(history,recruitType);
		return null;
	}

	/**
	 * 更新报读信息
	 * @return
	 */
	@Log
	@RequestMapping("/updateRecruit")
	@Rule("recruit:update")
	@ResponseBody
	public Object updateRecruit(BdStudentRecruit recruitInfo) {
		stdAllService.updateRecruit(recruitInfo);
		return null;
	}

	@Autowired
	private StudentAnnexCheckService annexCheckService;

	/**
	 * 更新附件信息
	 * @return
	 */
	@Log
	@RequestMapping("/updateAnnex")
	@Rule("recruit:upload")
	@ResponseBody
	public Object updateAnnex(MultipartHttpServletRequest mRequest, BdLearnAnnex annexInfo) {
		return annexCheckService.updateAnnexInfo(annexInfo);
	}

	/**
	 * 更新附件信息
	 * @return
	 */
	@Log
	@RequestMapping("/delAnnex")
	@Rule("recruit:upload")
	@ResponseBody
	public Object deleteAnnex(BdLearnAnnex annexInfo) {
		annexCheckService.delAnnexInfo(annexInfo);
		return null;
	}

	/**
	 * 审核附件
	 * @return
	 */
	@Log
	@RequestMapping("/annexCharge")
	@Rule("recruit:charge")
	@ResponseBody
	public Object annexCharge(BdLearnAnnex annexInfo) {
		annexCheckService.charge(annexInfo);
		return null;
	}
	
	@Log
	@RequestMapping("/distribution")
	@Rule("recruit:distribution")
	@ResponseBody
	public Object distribution(BdLearnRules learnRules) {
		if(StringUtil.hasValue(learnRules.getLearnId())){
			stdAllService.updateLearnRules(learnRules);
		}else{
			return learnRules;
		}
		return "success";
	}
	
	@Autowired
	private BaseInfoService baseInfoService;
	
	@RequestMapping("/getEmpList")
	@ResponseBody
	@Rule("recruit:distribution")
	public Object getEmpList(EmpQueryInfo queryInfo) {
		return baseInfoService.getEmpList(queryInfo);
	}
	
	@RequestMapping("/toEmpList")
	@Rule("recruit:distribution")
	public String toEmpList(@RequestParam(name="type") String type, @RequestParam(name="learnId") String learnId, Model model) {
		model.addAttribute("type", type);
		model.addAttribute("learnId", learnId);
		//model.addAttribute("assignFlag", assignFlag);
		return "recruit/student/student_distribution";
	}
}
