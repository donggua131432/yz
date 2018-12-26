package com.yz.controller.finance;

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

/**
 * 优惠类型管理
 * 
 * @author Administrator
 *
 */
@RequestMapping("/scholarship")
@Controller
public class ScholarshipController {

	@Autowired
	private SysdictPlusService dictService;

	@RequestMapping("/toPage")
	@Rule("scholarship")
	public String toPage() {
		return "/finance/scholarship/info";
	}

	/* ====================== 优惠分组 ============================= */
	@RequestMapping("/toSGList")
	@Rule("scholarship:query_sg")
	public String toSGList() {
		return "/finance/scholarship/group-list";
	}

	@RequestMapping("/getSGList")
	@Rule("scholarship:query_sg")
	@ResponseBody
	public Object getGroupList(SysDictPlus queryInfo) {
		queryInfo.setpId("sg");
		return dictService.getList(queryInfo);
	}

	@RequestMapping("/toSGAdd")
	@Rule("scholarship:add_sg")
	@Token(action = Flag.Save, groupId = "scholarship:add_sg")
	public String toSGAdd() {
		return "/finance/scholarship/group-add";
	}

	@RequestMapping("/toSGUpdate")
	@Rule("scholarship:query_sg")
	@Token(action = Flag.Save, groupId = "scholarship:update_sg")
	public String toSGUpdate(@RequestParam("dictId") String dictId, Model model) {
		model.addAttribute("sgInfo", dictService.getDictInfo(dictId));
		return "/finance/scholarship/group-edit";
	}

	@RequestMapping("/addSG")
	@Rule("scholarship:add_sg")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "scholarship:add_sg")
	public Object addGroup(SysDictPlus dict, PubInfo pubInfo) {
		dict.setUpdateUserId(pubInfo.getUpdateUserId());
		dict.setUpdateUser(pubInfo.getUpdateUser());
		dict.setCreateUser(pubInfo.getCreateUser());
		dict.setCreateUserId(pubInfo.getCreateUserId());
		dict.setpId("sg");
		dictService.addBySeq(dict);
		return null;
	}

	@RequestMapping("/updateSG")
	@Rule("scholarship:update_sg")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "scholarship:update_sg")
	public Object updateGroup(SysDictPlus dict, PubInfo pubInfo) {
		dict.setUpdateUserId(pubInfo.getUpdateUserId());
		dict.setUpdateUser(pubInfo.getUpdateUser());
		dict.setCreateUser(pubInfo.getCreateUser());
		dict.setCreateUserId(pubInfo.getCreateUserId());
		dict.setpId("sg");
		dictService.update(dict);
		return null;
	}

	/* ====================== 优惠类型 ============================= */

	@RequestMapping("/toList")
	@Rule("scholarship:query")
	public String toList() {
		return "/finance/scholarship/scholarship-list";
	}

	@RequestMapping("/getList")
	@Rule("scholarship:query")
	@ResponseBody
	public Object getList(SysDictPlus queryInfo) {
		queryInfo.setpId("scholarship");
		return dictService.getList(queryInfo);
	}

	@RequestMapping("/toAdd")
	@Rule("scholarship:add")
	@Token(action = Flag.Save, groupId = "scholarship:add")
	public String toAdd() {
		return "/finance/scholarship/scholarship-add";
	}

	@RequestMapping("/toUpdate")
	@Rule("scholarship:query")
	@Token(action = Flag.Save, groupId = "scholarship:update")
	public String toUpdate(@RequestParam("dictId") String dictId, Model model) {
		model.addAttribute("sInfo", dictService.getDictInfo(dictId));
		return "/finance/scholarship/scholarship-edit";
	}

	@RequestMapping("/add")
	@Rule("scholarship:add")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "scholarship:add")
	public Object add(SysDictPlus dict, PubInfo pubInfo) {
		dict.setUpdateUserId(pubInfo.getUpdateUserId());
		dict.setUpdateUser(pubInfo.getUpdateUser());
		dict.setCreateUser(pubInfo.getCreateUser());
		dict.setCreateUserId(pubInfo.getCreateUserId());
		dict.setpId("scholarship");
		dictService.addBySeq(dict);
		return null;
	}

	@RequestMapping("/update")
	@Rule("scholarship:update")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "scholarship:update")
	public Object update(SysDictPlus dict, PubInfo pubInfo) {
		dict.setUpdateUserId(pubInfo.getUpdateUserId());
		dict.setUpdateUser(pubInfo.getUpdateUser());
		dict.setCreateUser(pubInfo.getCreateUser());
		dict.setCreateUserId(pubInfo.getCreateUserId());
		dict.setpId("scholarship");
		dictService.update(dict);
		return null;
	}

	@RequestMapping("/validate")
	@Rule(value = { "scholarship:upd_group", "scholarship:add_group", "scholarship:update", "scholarship:add" })
	@ResponseBody
	public Object validate(@RequestParam(value = "dictName", required = true) String dictName,
			@RequestParam(value = "pId", required = true) String pId, @RequestParam(value = "oldName") String oldName) {
		return dictService.isExsit(dictName, pId, oldName);
	}

	@RequestMapping("/batch")
	@Rule({ "scholarship:enable_sg", "scholarship:enable" })
	@ResponseBody
	public Object batch(@RequestParam(value = "dictIds", required = true) String[] dictIds,
			@RequestParam(value = "isEnable", required = true) String isEnable, PubInfo pubInfo) {
		dictService.batch(dictIds, isEnable, pubInfo);
		return null;
	}

	@RequestMapping("/enable")
	@Rule({ "scholarship:enable_sg", "scholarship:enable" })
	@ResponseBody
	public Object change(@RequestParam(value = "dictId", required = true) String dictId,
			@RequestParam(value = "isEnable", required = true) String isEnable, PubInfo pubInfo) {
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
	
	@RequestMapping("/enableUnCache")
	@Rule({ "scholarship:enable_sg", "scholarship:enable" })
	@ResponseBody
	public Object changeUnCache(@RequestParam(value = "dictId", required = true) String dictId,
			@RequestParam(value = "isEnable", required = true) String isEnable, PubInfo pubInfo) {
		SysDictPlus dict = new SysDictPlus();
		dict.setDictId(dictId);
		dict.setIsEnable(isEnable);
		dict.setUpdateUserId(pubInfo.getUpdateUserId());
		dict.setUpdateUser(pubInfo.getUpdateUser());
		dict.setCreateUser(pubInfo.getCreateUser());
		dict.setCreateUserId(pubInfo.getCreateUserId());

		dictService.updateUnCache(dict);

		return null;
	}

}
