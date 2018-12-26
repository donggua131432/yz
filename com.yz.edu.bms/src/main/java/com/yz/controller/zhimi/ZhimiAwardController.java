package com.yz.controller.zhimi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.zhimi.ZhimiAwardQueryInfo;
import com.yz.model.condition.zhimi.ZhimiAwardRecordsQuery;
import com.yz.model.zhimi.ZhimiAwardInfo;
import com.yz.service.zhimi.ZhimiAwardService;

@Controller
@RequestMapping("/award")
public class ZhimiAwardController {
	
	@Autowired
	private ZhimiAwardService awardService;

	@RequestMapping("/toList")
	@Rule("award")
	public String toList(Model model) {
		
		return "zhimi/award/award-list";
	}
	
	@RequestMapping("/getList")
	@Rule("award")
	@ResponseBody
	public Object getList(ZhimiAwardQueryInfo queryInfo) {
		return awardService.getList(queryInfo);
	}
	
	@RequestMapping("/toAdd")
	@Rule("award:add")
	public String toAdd(Model model) {
		ZhimiAwardInfo awardInfo = new ZhimiAwardInfo();
		awardInfo.setAttrList(awardService.getRuleGroupAttrList());
		model.addAttribute("awardInfo", awardInfo);
		model.addAttribute("type", "ADD");
		return "zhimi/award/award-edit";
	}
	
	@RequestMapping("/toUpdate")
	@Rule("award:update")
	public String toUpdate(Model model, @RequestParam(value="ruleCode", required=true) String ruleCode) {
		ZhimiAwardInfo awardInfo = awardService.getAwardInfo(ruleCode);
		model.addAttribute("type", "UPDATE");
		model.addAttribute("awardInfo", awardInfo);
		return "zhimi/award/award-edit";
	}
	
	@RequestMapping("/add")
	@Rule("award:add")
	@ResponseBody
	public Object add(ZhimiAwardInfo awardInfo) {
		awardService.add(awardInfo);
		return null;
	}
	
	@RequestMapping("/update")
	@Rule("award:update")
	@ResponseBody
	public Object update(ZhimiAwardInfo awardInfo) {
		awardService.update(awardInfo);
		return null;
	}
	
	@RequestMapping("/toggle")
	@Rule("award:update")
	@ResponseBody
	public Object toggle(ZhimiAwardInfo awardInfo) {
		awardService.zhimiAwardToggle(awardInfo);
		return null;
	}
	
	@RequestMapping("/validateRuleCode")
	@Rule("award:add")
	@ResponseBody
	public Object validateRuleCode(@RequestParam(value="ruleCode", required=true) String ruleCode) {
		return awardService.isExsit(ruleCode);
	}
	
	@RequestMapping("/toRecordsList")
	public String toRecordsList(Model model) {
		return "zhimi/award/award-records";
	}

	@RequestMapping("/toRecordsDetail")
	public String toRecordsDetail(Model model, String yzCode) {
		model.addAttribute("yzCode", yzCode);
		return "zhimi/award/award-records-detail";
	}
	
	@RequestMapping("/getRecordsList")
	@ResponseBody
	public Object getRecordsList(ZhimiAwardRecordsQuery queryInfo) {
		return awardService.getRecordsList(queryInfo);
	}
	
}
