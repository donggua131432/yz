package com.yz.controller.baseinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.common.PubInfo;
import com.yz.model.system.SysDictPlus;
import com.yz.service.system.SysdictPlusService;

@RequestMapping("/grade")
@Controller
public class GradeController {
	
	@Autowired
	private SysdictPlusService dictService;
	
	@RequestMapping("/showList")
	@Rule("grade:query")
	public String showList() {
		return "/baseinfo/grade/grade-list";
	}
	
	@RequestMapping("/getList")
	@Rule("grade:query")
	@ResponseBody
	public Object getList(SysDictPlus queryInfo) {
		queryInfo.setpId("grade");
		return dictService.getList(queryInfo);
	}
	
	@RequestMapping("/toAdd")
	@Rule("grade:add")
	@Token(action=Flag.Save, groupId="grade:add")
	public String toAdd() {
		return "/baseinfo/grade/grade-add";
	}
	
	@RequestMapping("/toUpdate")
	@Rule("grade:update")
	@Token(action=Flag.Save, groupId="grade:update")
	public String toUpdate(@RequestParam("dictId") String dictId, Model model) {
		model.addAttribute("gradeInfo", dictService.getDictInfo(dictId));
		return "/baseinfo/grade/grade-edit";
	}
	
	@RequestMapping("/add")
	@Rule("grade:add")
	@ResponseBody
	@Token(action=Flag.Remove, groupId="grade:add")
	public Object add(SysDictPlus dict, PubInfo pubInfo) {
		dict.setUpdateUserId(pubInfo.getUpdateUserId());
		dict.setUpdateUser(pubInfo.getUpdateUser());
		dict.setCreateUser(pubInfo.getCreateUser());
		dict.setCreateUserId(pubInfo.getCreateUserId());
		dict.setpId("grade");
		dictService.add(dict);
		return null;
	}
	
	@RequestMapping("/update")
	@Rule("grade:update")
	@ResponseBody
	@Token(action=Flag.Remove, groupId="grade:update")
	public Object update(SysDictPlus dict, PubInfo pubInfo) {
		dict.setUpdateUserId(pubInfo.getUpdateUserId());
		dict.setUpdateUser(pubInfo.getUpdateUser());
		dict.setCreateUser(pubInfo.getCreateUser());
		dict.setCreateUserId(pubInfo.getCreateUserId());
		dict.setpId("grade");
		dictService.update(dict);
		return null;
	}
	
	@RequestMapping("/validate")
	@Rule(value={"grade:update", "grade:add"})
	@ResponseBody
	public Object validate(@RequestParam(value = "dictId", required = true) String dictId) {
		return dictService.isExsit(dictId);
	}
	
	@RequestMapping("/batch")
	@Rule("grade:enable")
	@ResponseBody
	public Object batch(@RequestParam(value="dictIds", required=true) String[] dictIds, @RequestParam(value="isEnable", required=true) String isEnable, PubInfo pubInfo) {
		
		dictService.batch(dictIds, isEnable, pubInfo);
		
		return null;
	}
	
	@RequestMapping("/enable")
	@Rule("grade:enable")
	@ResponseBody
	public Object change(@RequestParam(value="dictId", required=true) String dictId, @RequestParam(value="isEnable", required=true) String isEnable, PubInfo pubInfo) {
		SysDictPlus dict = new SysDictPlus();
		dict.setDictId(dictId);
		dict.setIsEnable(isEnable);
		dict.setUpdateUserId(pubInfo.getUpdateUserId());
		dict.setUpdateUser(pubInfo.getUpdateUser());
		dict.setCreateUser(pubInfo.getCreateUser());
		dict.setCreateUserId(pubInfo.getCreateUserId());
		
		dictService.update(dict);
		
		return null;
	}
	
	
}
