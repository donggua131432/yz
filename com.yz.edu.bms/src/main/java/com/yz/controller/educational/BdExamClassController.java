package com.yz.controller.educational;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.educational.BdExamClass;
import com.yz.model.educational.BdExamClassQuery;
import com.yz.service.educational.BdExamClassService;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/examClass")
public class BdExamClassController {

	@Autowired
	private BdExamClassService classService;

	@RequestMapping("/toList")
	@Rule("examClass:query")
	public Object toList() {
		return "educational/examClass/class-list";
	}

	@RequestMapping("/toClassExport")
	public Object toClassExport() {
		return "educational/examClass/class-export";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("examClass:query")
	public Object selectExamClassByPage(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdExamClassQuery query) {

		return classService.selectExamClassByPage(start, length, query);

	}

	@RequestMapping("/toDivide")
	@Rule("examClass:update")
	@Token(groupId = "examClass:update", action = Flag.Save)
	public String toAdd(@RequestParam(value = "pyId", required = true) String pyId, Model model) {
		BdExamClass examClass = classService.selectExamClass(pyId);
		model.addAttribute("examClass", examClass);
		return "educational/examClass/class-edit";
	}

	@RequestMapping("/divide")
	@Rule("examRoom:update")
	@ResponseBody
	@Token(groupId = "examClass:update", action = Flag.Remove)
	public Object divide(BdExamClass examClass) {
		classService.divideExamClass(examClass);
		return "SUCCESS";
	}

	@RequestMapping("/selectEpName")
	@ResponseBody
	public Object selectEpName(String eyId,String cityCode,String districtCode) {
		return classService.selectEpName(eyId, cityCode, districtCode);
	}

	@RequestMapping("/classExport")
	@Rule("examRoom:classExport")
	public void recordExport(String eyId, String cityCode,String districtCode,String epId,HttpServletResponse response){
		classService.classExport(eyId,cityCode,districtCode,epId,response);
	}
}
