package com.yz.controller.markting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.common.PageCondition;
import com.yz.model.markting.BdMarktingJar;
import com.yz.service.markting.MarktingPlanService;

@RequestMapping("/mpjar")
@Controller
public class MarktingPlanController {

	@Autowired
	private MarktingPlanService mpService;

	@RequestMapping("/toList")
	@Rule("mpjar")
	public String toMarktingJarList() {
		return "/markting/jar/jar-list";
	}

	@RequestMapping("/getList")
	@ResponseBody
	@Rule("mpjar")
	public Object getMpJarList(PageCondition pc) {
		return mpService.getMpJarList(pc);
	}

	@RequestMapping("/toAdd")
	@Rule("mpjar")
	public String toAdd(Model model) {
		return "/markting/jar/jar-edit";
	}

	@RequestMapping("/add")
	@ResponseBody
	@Rule("mpjar:add")
	public Object excute(BdMarktingJar jarInfo) {
		mpService.add(jarInfo);
		return null;
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Rule("mpjar:delete")
	public Object delete(@RequestParam("jarName") String jarName) {
		mpService.delete(jarName);
		return null;
	}

	@RequestMapping("/start")
	@ResponseBody
	@Rule("mpjar:start")
	public Object start(@RequestParam("jarName") String jarName) {
		mpService.start(jarName);
		return null;
	}

	@RequestMapping("/validate")
	@ResponseBody
	public Object validate(@RequestParam("jarName") String jarName) {
		return mpService.isExsit(jarName);
	}

}
