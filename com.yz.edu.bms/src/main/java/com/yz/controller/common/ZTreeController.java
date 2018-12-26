package com.yz.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.service.common.ZTreeService;

@Controller
@RequestMapping("/ztree")
public class ZTreeController {

	@Autowired
	private ZTreeService treeService;

	@RequestMapping("/sPfsn")
	@ResponseBody
	@Rule({ "standard:update", "standard:insert", "offer:insert", "offer:update" })
	public Object sPfsn(SelectQueryInfo pfsn, @RequestParam(name = "pfsnIds[]", required = false) String[] pfsnIds) {

		return treeService.searchPfsn(pfsn, pfsnIds);
	}

	@RequestMapping("/sUnvs")
	@ResponseBody
	@Rule({ "standard:update", "standard:insert", "offer:insert", "offer:update" })
	public Object sUnvsRecruitType(String unvsId) {

		return treeService.searchUnvs(unvsId);
	}

	@RequestMapping("/sTa")
	@ResponseBody
	@Rule({ "standard:update", "standard:insert", "offer:insert", "offer:update" })
	public Object sTa(@RequestParam(name = "pfsnIds[]", required = false) String[] pfsnIds,
			@RequestParam(name = "taIds[]", required = false) String[] taIds) {
		if (null == pfsnIds || pfsnIds.length < 1) {
			return null;
		}
		return treeService.searchTa(pfsnIds,taIds);
	}

}
