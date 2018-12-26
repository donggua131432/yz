package com.yz.controller.enroll;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.security.annotation.Rule;
import com.yz.model.enroll.stdenroll.BdStdEnrollQuery;
import com.yz.service.enroll.BdStdEnrollService;

@Controller
@RequestMapping("/stdEnroll")
public class StudentEnrollController {

	@Autowired
	private BdStdEnrollService enrollService;

	@RequestMapping("/toList")
	@Rule("stdEnroll:query")
	public String toList() {

		return "enroll/stdEnroll/std-enroll-list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("stdEnroll:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdStdEnrollQuery enroll) {

		return enrollService.queryStdEnrollByPage(start, length, enroll);
	}

	@RequestMapping("/toEnroll")
	@Rule("stdEnroll:enroll")
	public Object toEnroll(@RequestParam(name = "learnId", required = true) String learnId, Model model,
			@RequestParam(name = "exType", required = true) String exType) {
		model.addAttribute("enrollInfo", enrollService.queryStdEnrollInfo(learnId));
		model.addAttribute("exType", exType);
		return "enroll/stdEnroll/std-enroll";
	}

	@RequestMapping("/enroll")
	@Rule("stdEnroll:enroll")
	@ResponseBody
	public Object enroll(@RequestParam(name = "learnId", required = true) String learnId,
			@RequestParam(name = "unvsId", required = true) String unvsId,
			@RequestParam(name = "pfsnId", required = true) String pfsnId) {
		enrollService.studentEnroll(learnId, unvsId, pfsnId);
		return "SUCCESS";
	}

	@RequestMapping("/enrolledList")
	@ResponseBody
	@Rule("stdEnroll:query")
	public Object enrolledList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdStdEnrollQuery enroll) {

		return enrollService.queryEnrolledByPage(start, length, enroll);
	}

	@RequestMapping("/update")
	@Rule("stdEnroll:update")
	@ResponseBody
	public Object update(@RequestParam(name = "learnId", required = true) String learnId,
			@RequestParam(name = "unvsId", required = true) String unvsId,
			@RequestParam(name = "pfsnId", required = true) String pfsnId,
			@RequestParam(name = "campusId",required = true) String campusId) {
		enrollService.studentEnrollUpdate(learnId, unvsId, pfsnId,campusId, "", "");
		return "SUCCESS";
	}
	
	@RequestMapping("/toExcelImport")
	@Rule("enroll:import")
	public String teachPlanImport(HttpServletRequest request) {
		return "enroll/stdEnroll/enroll-import";
	}

	@RequestMapping("/excelImport")
	@ResponseBody
	@Rule("enroll:import")
	public Object excelImport(@RequestParam(name = "enrollExcel", required = false) MultipartFile enrollExcel) {
		if (null != enrollExcel) {
			enrollService.importExcel(enrollExcel);
		}
		return "SUCCESS";
	}
	
	@RequestMapping("/GKCheck")
	@ResponseBody
	@Rule("stdEnroll:update")
	public Object gkCheck(@RequestParam(name = "learnId", required = true) String learnId) {
		enrollService.gkCheck(learnId);
		return "SUCCESS";
	}
	
	@RequestMapping("/GKChecks")
	@ResponseBody
	@Rule("stdEnroll:update")
	public Object gkChecks(@RequestParam(name = "learnIds[]", required = true) String[] learnIds) {
		enrollService.gkChecks(learnIds);
		return "SUCCESS";
	}
	
	@RequestMapping("/queryAllocation")
	@ResponseBody
	@Rule("stdEnroll:update")
	public Object queryAllocation(BdStdEnrollQuery enroll) {
	  enrollService.queryAllocation(enroll);
	  return "SUCCESS";
	}

	@RequestMapping("/getHomeCampusInfo")
	@ResponseBody
	public Object getHomeCampusInfo(@RequestParam(value = "isStop",required = true) String isStop) {
		return enrollService.getHomeCampusInfo(isStop);
	}
	
	@RequestMapping("/toFpCampus")
	@Rule("stdEnroll:update")
	public String toFenPeiHomeCampus(HttpServletRequest request) {
		return "enroll/stdEnroll/std-campus";
	}
	
	@RequestMapping("/toSelectFpCampus")
	@Rule("stdEnroll:update")
	public String toSelectFpCampus(HttpServletRequest request) {
		return "enroll/stdEnroll/std-select-campus";
	}
	
	@RequestMapping("/selectAllocation")
	@ResponseBody
	@Rule("stdEnroll:update")
	public Object selectAllocation(@RequestParam(name = "idArray[]", required = true) String[] idArray,
			@RequestParam(value = "homeCampusId",required = true) String homeCampusId) {
		enrollService.selectAllocation(idArray,homeCampusId);
		return "success";
	}
	/**
	 * 筛选后批量录取
	 * @param enroll
	 * @return
	 */
	@RequestMapping("/queryBatchAdmit")
	@Rule("stdEnroll:enroll")
	@ResponseBody
	public Object queryBatchAdmit(BdStdEnrollQuery enroll) {
		enrollService.queryBatchAdmit(enroll);
		return "SUCCESS";
	}
	
	/**
	 * 筛选后批量录取
	 * @param enroll
	 * @return
	 */
	@RequestMapping("/checkBatchAdmit")
	@Rule("stdEnroll:enroll")
	@ResponseBody
	public Object checkBatchAdmit(@RequestParam(name = "learnIds[]", required = true) String[] learnIds) {
		enrollService.checkBatchAdmit(learnIds);
		return "SUCCESS";
	}
}
