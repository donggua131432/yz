package com.yz.controller.admin;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsRole;
import com.yz.model.admin.BmsRoleResponse;
import com.yz.service.admin.FuncServiceImpl;
import com.yz.service.admin.RoleServiceImpl;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

@Controller
@RequestMapping("/auth")
public class RoleController {

	@Autowired
	private RoleServiceImpl roleService;

	@Autowired
	private FuncServiceImpl funcService;;

	@RequestMapping("/adminRole")
	@Rule("role:query")
	public String adminRole() {

		return "/system/admin-role";
	}

	@RequestMapping(value = "/roleList", method = RequestMethod.GET)
	@ResponseBody
	@Rule("role:query")
	public Object roleList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BmsRole role) {
		return roleService.queryRolesByPage(start, length, role);
	}

	@RequestMapping("/roleEdit")
	@Rule("role:update")
	public Object roleEdit(@RequestParam(name = "exType", required = true) String exType, String roleId, Model model) {
		BmsRoleResponse roleInfo = new BmsRoleResponse();
		if ("UPDATE".equalsIgnoreCase(exType)) {
			Assert.hasText(roleId, "角色ID不能为空");
			roleInfo = roleService.queryRole(roleId);
		}

		model.addAttribute("funcs", funcService.queryMenuAndFunc());
		model.addAttribute("roleInfo", roleInfo);
		model.addAttribute("exType", exType);
		return "/system/admin-role-add";
	}

	@RequestMapping("/roleUpdate")
	@ResponseBody
	@Log
	@Rule("role:update")
	public Object roleUpdate(@RequestParam(name = "exType", required = true) String exType, BmsRole role,
			String[] permissions) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();
		role.setUpdateUserId(user.getUserId());
		role.setUpdateUser(user.getRealName());
		if ("UPDATE".equalsIgnoreCase(deal)) {
			roleService.updateRole(role, permissions);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			roleService.insertRole(role, permissions);
		}

		return "success";
	}

	@RequestMapping("/roleDelete")
	@ResponseBody
	@Log
	@Rule("role:delete")
	public Object roleDelete(@RequestParam(name = "roleId", required = true) String roleId) {
		roleService.deleteRole(roleId);
		// 清除用户缓存
		return "SUCCESS";
	}

	@RequestMapping("/validateRole")
	@ResponseBody
	@Rule("role:update")
	public Object validate(@RequestParam(name = "exType", required = true) String exType, String roleName,
			String roleCode, HttpServletResponse resp) throws IOException {
		if ("UPDATE".equalsIgnoreCase(exType)) {
			return validRole(1, roleName, roleCode);
		} else if ("ADD".equalsIgnoreCase(exType)) {
			return validRole(0, roleName, roleCode);
		}

		return false;
	}

	private Object validRole(int num, String roleName, String roleCode) {
		if (StringUtil.hasValue(roleName) && roleService.isRoleNameExist(roleName) <= num) {
			return true;
		} else if (StringUtil.hasValue(roleCode) && roleService.isRoleCodeExist(roleCode) <= num) {
			return true;
		}
		return false;
	}
	
	/**
	 * 角色的下拉列表
	 * @param sName
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/selectList")
	@ResponseBody
	public Object findAllKeyValue(String sName,@RequestParam(value = "rows", defaultValue = "10000") int rows,
			@RequestParam(value = "page", defaultValue = "1")int page) {
		return roleService.findAllKeyValue(sName,rows,page);
	}

}
