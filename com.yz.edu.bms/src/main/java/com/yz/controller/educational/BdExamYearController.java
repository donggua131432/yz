package com.yz.controller.educational;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.educational.BdExamYear;
import com.yz.service.educational.BdExamYearService;

@Controller
@RequestMapping("/examYear")
public class BdExamYearController {

	@Autowired
	private BdExamYearService examService;

	@RequestMapping("/toList")
	@Rule("examYear:query")
	public Object toList() {
		return "educational/examYear/exam-year-list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("examYear:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length) {
		return examService.selectExamYearByPage(start, length);
	}

	@RequestMapping("/toAdd")
	@Rule("examYear:insert")
	@Token(groupId = "examYear:insert", action = Flag.Save)
	public String toAdd(Model model) {
		model.addAttribute("exType", "ADD");
		model.addAttribute("year", new BdExamYear());
		return "educational/examYear/exam-year-edit";
	}

	@RequestMapping("/add")
	@ResponseBody
	@Rule("examYear:insert")
	@Log
	@Token(groupId = "examYear:insert", action = Flag.Remove)
	public Object add(BdExamYear exam) {
		examService.insertExamYear(exam);
		return "SUCCESS";
	}

	@RequestMapping("/toUpdate")
	@Rule("examYear:update")
	@Token(groupId = "examYear:update", action = Flag.Save)
	public String toUpdate(Model model, @RequestParam(name = "eyId") String eyId) {
		BdExamYear p = examService.selectExamYearById(eyId);
		model.addAttribute("year", p);
		model.addAttribute("exType", "UPDATE");
		return "educational/examYear/exam-year-edit";
	}

	@RequestMapping("/update")
	@Rule("examYear:update")
	@ResponseBody
	@Token(groupId = "examYear:update", action = Flag.Remove)
	@Log
	public Object update(BdExamYear exam) {
		examService.updateBdExamYear(exam);
		return "SUCCESS";
	}

	@RequestMapping("/block")
	@Rule("examYear:update")
	@ResponseBody
	public Object block(@RequestParam(name = "eyId") String eyId) {
		examService.updateYearStatus(eyId, GlobalConstants.STATUS_BLOCK);
		return "SUCCESS";
	}

	@RequestMapping("/start")
	@Rule("examYear:update")
	@ResponseBody
	public Object start(@RequestParam(name = "eyId") String eyId) {
		examService.updateYearStatus(eyId, GlobalConstants.STATUS_START);
		return "SUCCESS";
	}

	@RequestMapping("/delete")
	@Rule("examYear:delete")
	@ResponseBody
	public Object delete(@RequestParam(name = "eyId") String eyId) {
		examService.deleteExamYear(eyId);
		return "SUCCESS";
	}

	@RequestMapping("/deletes")
	@Rule("examYear:delete")
	@ResponseBody
	public Object deletes(@RequestParam(name = "eyIds[]") String[] eyIds) {
		for (String eyId : eyIds) {
			examService.deleteExamYear(eyId);
		}
		return "SUCCESS";
	}

	@RequestMapping("/validateYear")
	@Rule({ "examYear:insert", "examYear:update" })
	@ResponseBody
	public Object validateYear(@RequestParam(name = "examYear") String examYear,
			@RequestParam(name = "eyId") String eyId, @RequestParam(name = "exType") String exType) {
		if ("UPDATE".equals(exType)) {
			String oYear = examService.selectExamYearById(eyId).getExamYear();
			if (examYear.equals(oYear)) {
				return true;
			}
			
			int count = examService.selectExamYearCount(examYear);
			if (count > 0) {
				return false;
			}
			
			return true;
		} else {

			int count = examService.selectExamYearCount(examYear);
			if (count > 0) {
				return false;
			}
			return true;
		}
	}

}
