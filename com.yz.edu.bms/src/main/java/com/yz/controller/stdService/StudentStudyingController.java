package com.yz.controller.stdService;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import com.yz.model.condition.stdService.StudentStudyingQuery;
import com.yz.model.educational.MakeSchedule;
import com.yz.model.recruit.BdLearnRemark;
import com.yz.model.recruit.BdLearnRemarkLog;
import com.yz.model.stdService.StudyingContacts;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.service.stdService.StudentStudyingService;
import com.yz.util.StringUtil;

@RequestMapping("/studying")
@Controller
public class StudentStudyingController {
	
	@Autowired
	private StudentStudyingService studyingService;
	
	@Autowired
	private StudentRecruitService studentRecruitService;

	@RequestMapping("/toList")
	@Rule("studying")
	public String toList(Model model) {
		return "/stdservice/studying/student_studying_list";
	}
	
	@RequestMapping("/getStudyingList")
	@Rule("studying")
	@ResponseBody
	public Object getStudyingList(StudentStudyingQuery queryInfo) {
		return studyingService.getStudyingList(queryInfo);
	}


	@RequestMapping("/getStudyingListJoinAcc")
	@Rule("studying")
	@ResponseBody
	public Object getStudyingListJoinAcc(StudentStudyingQuery queryInfo) {
		return studyingService.getStudyingListJoinAcc(queryInfo);
	}
	
//	@Log
//	@RequestMapping("/changeRemark")
//	@ResponseBody
//	@Rule("studying:changeReamrk")
//	public Object changeRemark(BdLearnRemark remarkInfo) {
//		studentRecruitService.changeRemark(remarkInfo);
//		return null;
//	}
	
	@RequestMapping("/showPaymentDetail")
	@Rule("studying:paymentDetail")
	public String showPaymentDetail(Model model, @RequestParam(value="learnId", required=true) String learnId, @RequestParam(value="stdId", required=true) String stdId) {
		Map<String, Object> resultMap = studyingService.getPaymentDetail(learnId, stdId);
		
		Set<Entry<String, Object>> vSet = resultMap.entrySet();
		
		Iterator<Entry<String, Object>> it = vSet.iterator();
		
		while(it.hasNext()) {
			Entry<String, Object> e = it.next();
			model.addAttribute(e.getKey(), e.getValue());
		}
		
		return "/stdservice/studying/studying_paymentDetail";
	}
	
	@RequestMapping("/showContacts")
	@Rule("studying:queryContact")
	@Token(groupId="studying:updateContact", action=Flag.Save)
	public String showConcats(Model model, @RequestParam(value="learnId", required=true) String learnId) {
		model.addAttribute("studentConcats", studyingService.getStudyingContacts(learnId));
		return "/stdservice/studying/studying_contacts";
	}
	
	@Log
	@RequestMapping("/updateContacts")
	@Rule("studying:updateContact")
	@Token(groupId="studying:updateContact", action=Flag.Remove)
	@ResponseBody
	public Object updateContacts(HttpServletRequest requset, StudyingContacts contacts) {
		studyingService.updateContacts(contacts);
		return null;
	}
	
	@RequestMapping("/showServices")
	@Rule("studying:queryServices")
	public String showServices(Model model, @RequestParam(value="learnId", required=true) String learnId) {
		model.addAttribute("learnId", learnId);
		return "/stdservice/studying/studying_service";
	}
	
	@RequestMapping("/showServicesData")
	@Rule("studying:queryServices")
	@ResponseBody
	public Object showServicesData(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, @RequestParam(value="learnId", required=true) String learnId) {
		return studyingService.getStudentServiceLog(start,length,learnId);
	}
	
	@RequestMapping("/changeRemark")
	@Rule("studying:changeReamrk")
	public String showRemark(Model model, @RequestParam(value="learnId", required=true) String learnId) {
		model.addAttribute("learnId", learnId);
		return "/stdservice/studying/studying_remark";
	}
	
	@RequestMapping("/insertRemark")
	@ResponseBody
	@Rule("studying:changeReamrk")
	public Object insertRemark(String learnId,String content) {
		BdLearnRemarkLog remark=new BdLearnRemarkLog();
		remark.setContent(content);
		remark.setLearnId(learnId);
		studyingService.insertLearnRemarkLogs(remark);
		return  null;
	}
	
	@RequestMapping("/toInsertRemark")
	@Rule("studying:changeReamrk")
	public Object toInsertRemark(Model model, @RequestParam(value="learnId", required=true) String learnId) {
		model.addAttribute("learnId", learnId);
		return "/stdservice/studying/studying_remark_add";
	}
	
	@RequestMapping("/showRemarkData")
	@Rule("studying:changeReamrk")
	@ResponseBody
	public Object showRemarkData(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, @RequestParam(value="learnId", required=true) String learnId) {
		return studyingService.findLearnRemarkLog(start, length, learnId);
	}
	
	
	@RequestMapping("/showClassPlan")
	@Rule("studying:queryClassPlan")
	public String showClassPlan(Model model) {
		return "/stdservice/studying/studying_classPlan";
	}
	
	@RequestMapping("/showClassPlanData")
	@Rule("studying:queryClassPlan")
	@ResponseBody
	public Object showClassPlanData(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,MakeSchedule makeSchedule) {
		return studyingService.getStudentClassPlan(start, length, makeSchedule);
	}
	
	@RequestMapping("/showClassPlanPfsn")
	@Rule("studying:queryClassPlan")
	public String showClassPlanPfsn(Model model,@RequestParam(value="courseId", required=true) String courseId) {
		model.addAttribute("courseId", courseId);
		return "/stdservice/studying/studying_classplan_pfsn";
	}
	
	@RequestMapping("/showClassPlanPfsnData")
	@Rule("studying:queryClassPlan")
	@ResponseBody
	public Object showClassPlanPfsnData(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,String courseId) {
		return studyingService.getStudentClassPlanPfsn(start, length, courseId);
	}
	
	@RequestMapping("/showClassPlanStudent")
	@Rule("studying:queryClassPlan")
	public String showClassPlanStudent(Model model, @RequestParam(value="pfsnId", required=true) String pfsnId,
			@RequestParam(value="unvsId", required=true) String unvsId,
			@RequestParam(value="grade", required=true) String grade) {
		model.addAttribute("pfsnId", pfsnId);
		model.addAttribute("unvsId", unvsId);
		model.addAttribute("grade", grade);
		return "/stdservice/studying/studying_classplan_student";
	}
	
	@RequestMapping("/showClassPlanStudentData")
	@Rule("studying:queryClassPlan")
	@ResponseBody
	public Object showClassPlanStudentData(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,String pfsnId,String unvsId,String grade) {
		return studyingService.getStudentInfoByPfsn(start, length, pfsnId,unvsId,grade);
	}
	
	
}
