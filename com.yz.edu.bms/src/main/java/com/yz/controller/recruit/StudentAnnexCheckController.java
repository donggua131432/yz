package com.yz.controller.recruit;

import java.util.List;
import java.util.Map;

import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.recruit.StudentAnnexCheckQuery;
import com.yz.model.recruit.BdLearnAnnex;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentEnroll;
import com.yz.model.recruit.BdStudentHistory;
import com.yz.model.recruit.BdStudentOther;
import com.yz.model.recruit.BdStudentRecruit;
import com.yz.model.recruit.RecruitEmployeeInfo;
import com.yz.model.recruit.StudentCheckRecord;
import com.yz.service.recruit.StudentAllService;
import com.yz.service.recruit.StudentAnnexCheckService;

@RequestMapping("/annexCheck")
@Controller
public class StudentAnnexCheckController {

	@Autowired
	private StudentAnnexCheckService annexCheckService;
	
	@Autowired
	private StudentAllService stdAllService;

	@RequestMapping("/toList")
	@Rule("annex_check")
	public String toList() {
		return "/recruit/annexCheck/student_annex_check_list";
	}

	@RequestMapping("/getAnnexCheckList")
	@Rule("annex_check")
	@ResponseBody
	public Object getStudentAnnexCheckList(StudentAnnexCheckQuery queryInfo) {
		return annexCheckService.getStudentAnnexCheckList(queryInfo);
	}

	@RequestMapping("/toCheckEdit")
	@Rule("annex_check:query")
	public String toCheckEdit(Model model, String learnId, String stdId, String recruitType) {
		model.addAttribute("learnId", learnId);
		model.addAttribute("stdId", stdId);
		model.addAttribute("recruitType", recruitType);
		return "/recruit/annexCheck/student_check_edit";
	}
	
	@RequestMapping("/toCheck")
	@Rule("annex_check:query")
	public String toCheck(Model model, String learnId, String recruitType) {
		BaseUser user = SessionUtil.getUser();
		List<String> jtIdList = user.getJtList();
		model.addAttribute("jtIdList",jtIdList);
		model.addAttribute("learnId", learnId);
		model.addAttribute("recruitType", recruitType);
		model.addAttribute("checkStatus", stdAllService.getCheckDataStatus(learnId));
		return "/recruit/annexCheck/student_check";
	}
	
	@RequestMapping("/check")
	@Rule("annex_check:check")
	@ResponseBody
	public Object check(StudentCheckRecord check) {
		return annexCheckService.check(check);
	}
	
	@RequestMapping("/showAnnexList")
	@Rule("annex_check:query")
	public String showAnnexList(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		model.addAttribute("learnId", learnId);
		return "/recruit/annexCheck/student_annex_list";
	}

	@RequestMapping("/getAnnexList")
	@Rule("annex_check:query")
	@ResponseBody
	public Object getAnnexList(Model model, @RequestParam(value = "learnId", required = true) String learnId) {
		return annexCheckService.getAnnexList(learnId);
	}

	@RequestMapping("/updateAnnexInfo")
	@Rule("annex_check:update_annex")
	@ResponseBody
	public Object updateAnnexInfo(MultipartHttpServletRequest mRequest, BdLearnAnnex annexInfo) {
		return annexCheckService.updateAnnexInfo(annexInfo);
	}

	@RequestMapping("/delAnnexInfo")
	@Rule("annex_check:delete")
	@ResponseBody
	public Object delAnnexInfo(BdLearnAnnex annexInfo) {
		annexCheckService.delAnnexInfo(annexInfo);
		return null;
	}

	@RequestMapping("/charge")
	@Rule("annex_check:charge")
	@ResponseBody
	public Object charge(BdLearnAnnex annexInfo) {
		annexCheckService.charge(annexInfo);
		return null;
	}
	
	/**
	 * 编辑页
	 * @param model
	 * @return
	 */
	@RequestMapping("/toBase")
	@Rule("annex_check:query")
	public String toBase(Model model, String stdId, String learnId, String recruitType) {
		BdStudentBaseInfo baseInfo = stdAllService.getStudentBaseInfoAnnex(stdId);
		BdStudentOther other = stdAllService.getStudentOther(stdId);
		baseInfo.setLearnId(learnId);

		model.addAttribute("recruitType", recruitType);
		model.addAttribute("baseInfo", baseInfo);
		model.addAttribute("other", other);
		model.addAttribute("updateUrl", "annexCheck/updateBase.do");
		return "/recruit/student/student_baseInfo";
	}

	/**
	 * 原学历信息页
	 * @param model
	 * @return
	 */
	@RequestMapping("/toHistory")
	@Rule("annex_check:query")
	public String toHistory(Model model, String learnId, String recruitType) {
		BdStudentHistory history = stdAllService.getStudentHistory(learnId);
		model.addAttribute("history", history);
		model.addAttribute("recruitType", recruitType);
		model.addAttribute("updateUrl", "annexCheck/updateHistory.do");
		return "/recruit/student/student_history";
	}
	/**
	 * 报读信息页
	 * @param model
	 * @return
	 */
	@RequestMapping("/toRecruit")
	@Rule("annex_check:query")
	public String toRecruit(Model model, @RequestParam(value="learnId", required=true) String learnId) {
		BdStudentEnroll enroll = stdAllService.getStudentEnroll(learnId);
		RecruitEmployeeInfo recruitEmpInfo = stdAllService.getRecruitEmpInfo(learnId);
		Map<String, Object> feeInfo = stdAllService.getFeeInfo(learnId); 
		model.addAttribute("enroll", enroll);
		model.addAttribute("feeInfo", feeInfo);
		model.addAttribute("recruitEmpInfo", recruitEmpInfo);
		model.addAttribute("updateUrl", "annexCheck/updateRecruit.do");
		return "/recruit/student/student_recruit";
	}
	
	/**
	 * 更新基础信息
	 * @return
	 */
	@Log
	@RequestMapping("/updateBase")
	@Rule("annex_check:update_info")
	@ResponseBody
	public Object updateBase(MultipartHttpServletRequest request,BdStudentBaseInfo baseInfo, BdStudentOther other,String recruitType) {
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
	@Rule("annex_check:update_info")
	@ResponseBody
	public Object updateHistory(BdStudentHistory history, String recruitType) {
		stdAllService.updateHistory(history, recruitType);
		return null;
	}
	/**
	 * 更新报读信息
	 * @return
	 */
	@Log
	@RequestMapping("/updateRecruit")
	@Rule("annex_check:update_info")
	@ResponseBody
	public Object updateRecruit(BdStudentRecruit recruitInfo) {
		stdAllService.updateRecruit(recruitInfo);
		return null;
	}
	
}
