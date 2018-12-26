package com.yz.controller.recruit;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.conf.YzSysConfig;
import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.recruit.JudgeQueryInfo;
import com.yz.model.recruit.StudentJudgeInfo;
import com.yz.model.system.SysDict;
import com.yz.service.recruit.StudentJudgeService;
import com.yz.service.system.SysDictService;
import com.yz.util.Assert;

@RequestMapping("/judge")
@Controller
public class StudentYYJudgeController {
	
	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
    private SysDictService sysDictService;
	
	@Autowired
	private StudentJudgeService judgeService;

	@RequestMapping("/toList")
	@Rule("judge:query")
	public String toList(HttpServletRequest req, Model model) {
		String dp = yzSysConfig.getPlanSetting();
		String[] scholarships = dp.split(",");
		
		List<SysDict> dicts = new ArrayList<SysDict>();
		
		for(String s : scholarships) {
			dicts.add(sysDictService.getDict("scholarship." + s));
		}
		
		model.addAttribute("scholarships", dicts);
		
		return "/recruit/judge/judge_list";
	}
	
	@RequestMapping("/getList")
	@Rule("judge:query")
	@ResponseBody
	public Object getList(HttpServletRequest req, JudgeQueryInfo queryInfo) {
		String dp = yzSysConfig.getPlanSetting();
		String[] scholarships = dp.split(",");
		queryInfo.setScholarships(scholarships);
		
		return judgeService.getList(queryInfo);
	}
	
	@RequestMapping("/toJudge")
	@Rule("judge:query")
	public String toJudge(Model model, @RequestParam(value="learnId", required=true) String learnId) {
		
		StudentJudgeInfo judgeInfo = judgeService.getJudgeInfo(learnId);
		
		model.addAttribute("learnId", learnId);
		model.addAttribute("judgeInfo", judgeInfo);
		return "/recruit/judge/judge_edit";
	}
	
	@RequestMapping("/check")
	@Rule("judge:check")
	@ResponseBody
	public Object check(StudentJudgeInfo judgeInfo) {
		String[] learnIds = judgeInfo.getLearnIds();
		Assert.notEmpty(learnIds, "学业ID不能为空");
		
		judgeService.check(judgeInfo);
		
		return null;
	}
	
	
}
