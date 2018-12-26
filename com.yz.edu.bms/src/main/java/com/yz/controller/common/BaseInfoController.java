package com.yz.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.constants.SecurityConstant;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.service.common.BaseInfoService;
import com.yz.util.StringUtil;

@Controller
@RequestMapping("/baseinfo")
public class BaseInfoController {

	@Autowired
	private BaseInfoService baseInfoService;

	@RequestMapping("/sUnvs")
	@Rule({ "recruit:insert", "recruit:update", "standard:update", "standard:insert", "stdEnroll:enroll",
			"stdEnroll:enroll", "studentModify:insert", "studentModify:update", "studentChange:find","studyActivity:stuQuery" })
	@ResponseBody
	public Object getUnvsList(SelectQueryInfo queryInfo) {
		return baseInfoService.getUnvsSelectList(queryInfo);
	}

	@RequestMapping("/sPfsn")
	@Rule({ "recruit:insert", "recruit:update", "standard:update", "standard:insert", "stdEnroll:enroll",
			"stdEnroll:enroll", "studentChange:find","sendBooks:query","studyActivity:stuQuery" })
	@ResponseBody
	public Object getPfsnList(SelectQueryInfo queryInfo) {
		return baseInfoService.getPfsnSelectList(queryInfo);
	}

	@RequestMapping("/sTa")
	@Rule({ "recruit:insert", "recruit:update", "standard:update", "standard:insert", "studentChange:find" })
	@ResponseBody
	public Object getTaList(SelectQueryInfo queryInfo) {
		return baseInfoService.getTaSelectList(queryInfo);
	}

	@RequestMapping("/resetToken")
	@ResponseBody
	@Rule({ "standard:update", "standard:insert" })
	public Object resetToken(Model model, String groupId) {
		String tokenId = StringUtil.UUID();
		SessionUtil.pushToken(groupId, tokenId);
		model.addAttribute(SecurityConstant.WEB_TOKEN, tokenId);
		return tokenId;
	}

	@RequestMapping("/sCampus")
	@ResponseBody
	@Rule("")
	public Object getCampusList(SelectQueryInfo queryInfo) {
		return baseInfoService.getCampusList(queryInfo);
	}

	@RequestMapping("/sDepartment")
	@ResponseBody
	@Rule("")
	public Object getDepartmentList(SelectQueryInfo queryInfo) {
		return baseInfoService.getDepartmentList(queryInfo);
	}

	@RequestMapping("/sEmployee")
	@ResponseBody
	@Rule("")
	public Object getEmployeeList(SelectQueryInfo queryInfo) {
		return baseInfoService.getEmployeeList(queryInfo);
	}

	@RequestMapping("/sGroup")
	@ResponseBody
	@Rule("")
	public Object getGroupList(SelectQueryInfo queryInfo) {
		return baseInfoService.getGroupList(queryInfo);
	}

	@RequestMapping("/sFeeItem")
	@ResponseBody
	@Rule({ "standard:update", "standard:insert", "coupon:update", "coupon:insert", "offer:insert", "offer:update" })
	public Object getFeeItemList(SelectQueryInfo queryInfo) {
		return baseInfoService.getFeeItemList(queryInfo);
	}

}
