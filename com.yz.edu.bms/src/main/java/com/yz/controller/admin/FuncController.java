package com.yz.controller.admin;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.service.admin.FuncServiceImpl;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

@Controller
@RequestMapping("/auth")
public class FuncController {

	@Autowired
	private FuncServiceImpl funcService;

	@RequestMapping("/adminPermission.do")
	@Rule("func:query")
	public String adminPermission() {

		return "/system/admin-permission";
	}

	@RequestMapping("/permissionList")
	@ResponseBody
	@Rule("func:query")
	public Object permissionList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BmsFunc func) {

		return funcService.queryFuncsByPage(start, length, func);
	}

	@RequestMapping("/permissionEdit")
	@Token(action = Flag.Save, groupId = "funcEdit")
	@Rule("func:update")
	public Object permissionEdit(@RequestParam(name = "exType", required = true) String exType, String funcId, Model model) {
		BmsFunc funcInfo = new BmsFunc();
		if ("UPDATE".equalsIgnoreCase(exType)) {
			Assert.hasText(funcId, "权限ID不能为空");
			
			funcInfo = funcService.queryFunc(funcId);
		}

		model.addAttribute("funcInfo", funcInfo);
		model.addAttribute("exType", exType);
		return "/system/admin-permission-add";
	}

	@RequestMapping("/permissionUpdate")
	@ResponseBody
	@Log
	@Token(action = Flag.Remove, groupId = "funcEdit")
	@Rule("func:update")
	public Object permissionUpdate(@RequestParam(name = "exType", required = true) String exType, BmsFunc func) {

		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		func.setUpdateUserId(user.getUserId());
		func.setUpdateUser(user.getRealName());

		if ("UPDATE".equalsIgnoreCase(deal)) {
			funcService.editFunc(func);
			// 清除用户缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			funcService.addFunc(func);
		}
		return "success";
	}

	@RequestMapping("/funcDelete")
	@Log(needLog = true)
	@ResponseBody
	@Rule("func:delete")
	public Object funcDelete(@RequestParam(name = "funcId", required = true) String funcId) {
		funcService.deleteFunc(funcId);
		// 清除用户缓存
		return "SUCCESS";
	}

	@RequestMapping("/validateFunc")
	@ResponseBody
	@Rule("func:update")
	public Object validateFunc(@RequestParam(name = "exType", required = true) String exType, String funcName,
			String oldFuncName, String funcCode, String oldFuncCode) throws IOException {
 
		// 修改操作
		if (StringUtil.hasValue(funcName)) {
			return funcService.isFuncNameExist(funcName, oldFuncName);
		}
		if (StringUtil.hasValue(funcCode)) {
			return funcService.isRoleCodeExist(funcCode, oldFuncCode);
		}

		return "false";
	}

}
