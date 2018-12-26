package com.yz.controller.recruit;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.util.IdCardVerifyUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.recruit.StudentRecruitQuery;
import com.yz.model.recruit.BdLearnRemark;
import com.yz.model.recruit.BdRecruitStudentListInfo;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentHistory;
import com.yz.model.recruit.BdStudentOther;
import com.yz.model.recruit.BdStudentRecruit;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.util.StringUtil;

@Controller
@RequestMapping("/recruit")
public class StudentRecruitController {

	@Autowired
	private StudentRecruitService studentRecruitService;

	@RequestMapping("/toStudentList")
	@Rule("recruit")
	public String toListPage() {
		return "/recruit/student/student_recruit_list";
	}

	@RequestMapping("/findRecruitStudents")
	@ResponseBody
	public Object findRecruitStudent(StudentRecruitQuery queryInfo) {
		IPageInfo<BdRecruitStudentListInfo> result = studentRecruitService.findRecruitStudents(queryInfo);
		return result;
	}

	@RequestMapping("/toRecruitAdd")
	@Rule("recruit:insert")
	@Token(groupId = "student:recruitAdd", action = Flag.Save)
	public String toRecruitAdd(Model model) {
		model.addAttribute("recruitType", RequestUtil.getParameter("recruitType"));
		return "/recruit/student/student_recruit_add";
	}

	@Log
	@RequestMapping("/recruitAdd")
	@ResponseBody
	@Rule("recruit:insert")
	@Token(groupId = "student:recruitAdd", action = Flag.Remove)
	public Object recruit(HttpServletRequest request, BdStudentBaseInfo baseInfo, BdStudentOther other,BdStudentRecruit recruit, BdStudentHistory history) {
		baseInfo.setStdSource("X"); //学员系统录入
		studentRecruitService.recruit(baseInfo, other, recruit, history);
		return null;
	}

	@RequestMapping("/ifExistInfo")
	@ResponseBody
	@Rule({ "recruit:insert" })
	public Object ifExistInfo(HttpServletRequest request) {
		String idType = RequestUtil.getParameter("idType");
		String idCard = RequestUtil.getParameter("idCard");
		String oldIdCard = RequestUtil.getParameter("oldIdCard");
		String recruitType = RequestUtil.getParameter("recruitType");

		if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idCard)) {
			return null;
		}
		//验证身份证是否合法
		if("1".equals(idType)){
			if(!IdCardVerifyUtil.strongVerifyIdNumber(idCard)){
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("errCode", "E00097");
				return result;
			}
		}

		return studentRecruitService.ifExistInfo(idType, idCard, oldIdCard,recruitType);
	}

	@RequestMapping("/showFeeList")
	@ResponseBody
	@Rule("recruit:insert")
	public Object showFeeList(@RequestParam("pfsnId") String pfsnId, @RequestParam("scholarship") String scholarship,
			@RequestParam("taId") String taId, @RequestParam("recruitType") String recruitType) {
		if (StringUtil.isEmpty(pfsnId) || StringUtil.isEmpty(scholarship) || StringUtil.isEmpty(taId)
				|| StringUtil.isEmpty(recruitType)) {
			return null;
		}
		return studentRecruitService.findFeeOfferInfo(pfsnId, scholarship, taId, recruitType);
	}
	
	@RequestMapping("/getScholarships")
	@ResponseBody
	public Object getScholarships(@RequestParam("pfsnId") String pfsnId, @RequestParam("taId") String taId) {
		if (StringUtil.isEmpty(pfsnId) || StringUtil.isEmpty(taId)) {
			return null;
		}
		return studentRecruitService.getScholarships(pfsnId, taId);
	}

	@Log
	@RequestMapping("/changeRemark")
	@ResponseBody
	@Rule("recruit:insert")
	public Object changeRemark(BdLearnRemark remarkInfo) {
		studentRecruitService.changeRemark(remarkInfo);
		return null;
	}

	@RequestMapping("/validateBaseInfo")
	@ResponseBody
	@Rule({ "recruit:insert", "student:update" })
	public Object validateBaseInfo() {
		String idType = RequestUtil.getParameter("idType");
		String idCard = RequestUtil.getParameter("idCard");
		String oldIdCard = RequestUtil.getParameter("oldIdCard");
		String grade = RequestUtil.getParameter("grade");

		Map<String, String> param = new HashMap<String, String>();
		param.put("idType", idType);
		param.put("idCard", idCard);
		param.put("oldIdCard", oldIdCard);
		param.put("grade", grade);

		return studentRecruitService.isExist(param) == false;
	}

	@RequestMapping("/validateRecruit")
	@ResponseBody
	@Rule({ "recruit:insert", "student:update" })
	public Object validateRecruit() {
		String stdId = RequestUtil.getParameter("stdId");
		String scholarship = RequestUtil.getParameter("scholarship");
		return studentRecruitService.check(stdId, scholarship);
	}
	
	/**
	 * 给201709 国开学员发送短信
	 * @param remarkInfo
	 * @return
	 */
	@Log
	@RequestMapping("/sendSmsToStudent")
	@ResponseBody
	public Object sendSmsToStudent(BdLearnRemark remarkInfo) {
		studentRecruitService.sendSmsToStudent();
		return "SUCCESS";
	}
	/**
	 * 验证手机号是否存在
	 * @param request
	 * @return
	 */
	@RequestMapping("/ifMobileExistInfo")
	@ResponseBody
	@Rule({ "recruit:insert" })
	public Object ifMobileExistInfo(HttpServletRequest request) {
		String mobile = RequestUtil.getParameter("mobile");
		if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(mobile)) {
			return true;
		}
		return studentRecruitService.ifMobileExistInfo(mobile)<1;
	}
}