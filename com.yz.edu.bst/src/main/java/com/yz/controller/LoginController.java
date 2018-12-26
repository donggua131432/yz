package com.yz.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yz.core.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.service.BstLoginService;
import com.yz.vo.LoginUser;
import com.yz.vo.ReturnModel;


@Controller
public class LoginController {
	
	@Autowired
	private BstLoginService loginService;
	

	@RequestMapping("/toLogin")
	public String toLogin() {	
		return "login";
	}

	
	
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
	public Object login(@RequestParam(name = "idCard", required = true) String idCard,
			@RequestParam(name = "password", required = true) String password,
			 HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		ReturnModel rqModel= loginService.login(idCard, password);		
		return rqModel;

	}

	@RequestMapping(value = "/logout")	
	public Object logout() {
		LoginUser user = SessionUtil.getUser();
		SessionUtil.clearUser(user.getStdId());
		return "toLogin";

	}

	
	
	
}
