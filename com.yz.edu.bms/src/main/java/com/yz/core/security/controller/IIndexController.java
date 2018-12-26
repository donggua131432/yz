package com.yz.core.security.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log; 
import com.yz.core.security.context.RuleContext;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.security.service.ISecurityService;
import com.yz.core.util.ValidCodeUtil;
import com.yz.exception.BusinessException;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc; 
import com.yz.util.CodeUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Controller
public class IIndexController {

	private String loginSuccessUrl;

	private int validCodeTimeout;

	private int sessionTimeout;

	@Autowired
	private ISecurityService securityService;

	/**
	 * 登录
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param validCode
	 *            验证码
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	@Log
	public Object login(@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "password", required = true) String password,
			@RequestParam(name = "validCode", required = true) String validCode, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		String vc = SessionUtil.getValidCode();

		if (!StringUtil.hasValue(vc)) {
			throw new BusinessException("E000032");
			// 验证码超时 重新获取
		}

		if (!vc.equalsIgnoreCase(validCode)) {
			throw new BusinessException("E000033");
		}

		BaseUser user = securityService.getUser(username);

		if (user != null) {
			String pwd = user.getPassword(); 
			
			BmsFunc func = new BmsFunc();
			func.setFuncCode("login");
			func.setFuncName("学员系统登录");
			RuleContext.setFunctionInfo(func);
			
			if (CodeUtil.MD5.encrypt(password).equals(pwd)) {
				user = securityService.assembly(user);
				String userId = user.getUserId();
				SessionUtil.setKey(userId);// 存入用户唯一登录标识
				SessionUtil.setUser(user);// 存入session对象
				return "SUCCESS";
			}
		}

		// 用户名不存在或者密码错误
		throw new BusinessException("E000031");

	}

	@RequestMapping(value = "/logout")
	@Log
	public Object logout(String userId) {
		 
		BmsFunc func = new BmsFunc();
		func.setFuncCode("login");
		func.setFuncName("退出系统");
		RuleContext.setFunctionInfo(func);

		SessionUtil.delKey(userId);
		SessionUtil.delUser();

		return "toLogin";
	}

	/**
	 * 获取验证码
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/validCode")
	public void validCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ValidCodeUtil.createValidCode(response, validCodeTimeout);
	}
	
	/**
	 * 钉钉应用里的登录(后期可以改为钉钉授权登录)
	 * @param username
	 * @param password
	 * @param validCode
	 * @param req
	 * @param resp
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/ddLogin")
	@ResponseBody
	@Log
	public Object ddLogin(@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "password", required = true) String password,
			@RequestParam(name = "validCode", required = true) String validCode, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		String vc = SessionUtil.getValidCode();

		if (!StringUtil.hasValue(vc)) {
			throw new BusinessException("E000032");
			// 验证码超时 重新获取
		}

		if (!vc.equalsIgnoreCase(validCode)) {
			throw new BusinessException("E000033");
		}

		BaseUser user = securityService.getUser(username);

		if (user != null) {
			String pwd = user.getPassword();
			
			BmsFunc func = new BmsFunc();
			func.setFuncCode("login");
			func.setFuncName("钉钉登录");
			RuleContext.setFunctionInfo(func);
			
			if (CodeUtil.MD5.encrypt(password).equals(pwd)) {
				return JsonUtil.object2String(user);
			}
		}

		// 用户名不存在或者密码错误
		throw new BusinessException("E000031");

	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getValidCodeTimeout() {
		return validCodeTimeout;
	}

	public void setValidCodeTimeout(int validCodeTimeout) {
		this.validCodeTimeout = validCodeTimeout;
	}

	public String getLoginSuccessUrl() {
		return loginSuccessUrl;
	}

	public void setLoginSuccessUrl(String loginSuccessUrl) {
		this.loginSuccessUrl = loginSuccessUrl;
	}

}
