package com.yz.controller.admin;

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
import com.yz.model.admin.BmsUser;
import com.yz.model.admin.BmsUserResponse;
import com.yz.service.admin.RoleServiceImpl;
import com.yz.service.admin.UserServiceImpl;
import com.yz.util.Assert;
import com.yz.util.CodeUtil;
import com.yz.util.StringUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private RoleServiceImpl roleService;

	@RequestMapping("/userIndex")
	@Rule("admin:query")
	public String userIndex() {
		return "/system/admin-list";
	}

	@RequestMapping("/userList")
	@ResponseBody
	@Rule("admin:query")
	public Object userList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BmsUser user) {
		return userService.searchUserByPage(start, length, user);
	}

	@RequestMapping("/userEdit")
	@Token(action = Flag.Save, groupId = "user:edit")
	@Rule("admin:update")
	@Log
	public Object userEdit(@RequestParam(name = "exType", required = true) String exType, String userId, Model model) {
		BmsUserResponse user = new BmsUserResponse();
		if ("UPDATE".equalsIgnoreCase(exType)) {
			Assert.hasText(userId, "用户ID不能为空");
			user = userService.queryUserById(userId);

		}
		if (StringUtil.isEmpty(user.getCampusId())) {
			user.setCampusId("888");
			user.setDpId("888");
		}

		model.addAttribute("allRoles", roleService.queryAllRoles());

		model.addAttribute("userInfo", user);
		model.addAttribute("exType", exType);
		return "/system/admin-add";
	}

	@RequestMapping("/userBlock")
	@Log
	@ResponseBody
	@Rule("admin:update")
	public Object userBlock(@RequestParam(name = "exType", required = true) String exType, String userId, Model model) {

		BmsUser user = new BmsUser();

		BaseUser principal = SessionUtil.getUser();

		user.setUpdateUserId(principal.getUserId());
		user.setUpdateUser(principal.getRealName());
		user.setUserId(userId);
		if ("BLOCK".equalsIgnoreCase(exType)) {
			user.setIsBlock("1");
		} else if ("START".equalsIgnoreCase(exType)) {
			user.setIsBlock("0");

		}
		userService.editUser(user);
		// 清除用户缓存
		SessionUtil.clearUser(userId);

		return "SUCCESS";
	}

	@RequestMapping("/userUpdate")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "user:edit")
	@Log
	@Rule("admin:update")
	public Object userUpdate(@RequestParam(name = "exType", required = true) String exType, BmsUser user,
			@RequestParam(name = "roleIds", required = true) String[] roleIds) {

		String deal = exType.trim();
		BaseUser principal = SessionUtil.getUser();
		user.setUpdateUserId(principal.getUserId());
		user.setUpdateUser(principal.getRealName());

		if ("UPDATE".equalsIgnoreCase(deal)) {
			userService.updateUserAndRoles(user, roleIds);
			// 清除用户缓存
			SessionUtil.clearUser(user.getUserId());
		} else if ("ADD".equalsIgnoreCase(exType)) {
			userService.insertUser(user, roleIds);
		}
		return "success";
	}

	@RequestMapping("/userDelete")
	@ResponseBody
	@Log
	@Rule("admin:delete")
	public Object userDelete(@RequestParam(name = "userId", required = true) String userId) {

		userService.deleteUser(userId);

		// 清除用户缓存
		SessionUtil.clearUser(userId);

		return "SUCCESS";
	}

	@RequestMapping("/validateUser")
	@ResponseBody
	@Rule("admin:update")
	public Object validateUser(String userName, String oldUserName) {
		// 修改操作
		if (StringUtil.hasValue(userName)) {
			return userService.isUserNameExist(userName, oldUserName);
		}
		return false;
	}

	@RequestMapping("/toEditPwd")
	@Rule("admin:updatepwd")
	public String toEditPwd(@RequestParam(value = "userId", required = true) String userId, Model model) {

		String realName = userService.queryUserById(userId).getRealName();
		model.addAttribute("realName", realName);
		model.addAttribute("userId", userId);
		return "/system/change-password";
	}

	@RequestMapping("/editPwd")
	@ResponseBody
	@Rule("admin:updatepwd")
	@Log
	public Object editPwd(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "newpassword", required = true) String newpassword) {
		String newPwd = CodeUtil.MD5.encrypt(newpassword);
		BmsUser user = new BmsUser();
		user.setUserId(userId);
		user.setUserPwd(newPwd);

		userService.editUser(user);

		// 清除用户缓存
		SessionUtil.clearUser(userId);

		return "SUCCESS";
	}

	@RequestMapping("/toUserPwd")
	public String toUserPwd(@RequestParam(value = "userId", required = true) String userId, Model model) {
		String realName = userService.queryUserById(userId).getRealName();
		model.addAttribute("realName", realName);
		model.addAttribute("userId", userId);
		return "/system/user-password";
	}

	@RequestMapping("/userPwd")
	@ResponseBody
	public Object userPwd(@RequestParam(value = "userId", required = true) String userId,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "newpassword", required = true) String newpassword) {

		userService.editUserPwd(password, newpassword, userId);
		return "SUCCESS";
	}

}
