package com.yz.controller;


import javax.servlet.http.HttpServletResponse;

import com.yz.core.util.SessionUtil; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; 
import com.yz.core.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.service.BdsCourseService;
import com.yz.service.BdsLearnInfoService;
import com.yz.vo.LoginUser;


@Controller
public class BstLearnInfoController {
	
	@Autowired
	private BdsLearnInfoService learnService;
	@Autowired
	private BdsCourseService courseService;
	
	@Value("${oss.file.browser://yzims.oss-cn-shenzhen.aliyuncs.com/}")
	private String file_browser;
	

	@RequestMapping("/index")
	@Rule
	public String index(Model model) {
		LoginUser user = SessionUtil.getUser();
		// 通过model传到页面
		model.addAttribute("user", user);
		Object object= learnService.selectStudentInfo(user.getStdId());
		RequestUtil.setAttribute("_file_browser", file_browser);
		model.addAttribute("myprofile", object);	
		return "myProfile";
	}

	
	@RequestMapping("/resource")
	@Rule
	public String getMyResource(Model model,@RequestParam(name = "learnId", required = true) String learnId) {
		LoginUser user = SessionUtil.getUser();
		// 通过model传到页面
		model.addAttribute("user", user);
		Object object= courseService.selectCourseResource(learnId);
		model.addAttribute("resources", object);	
		model.addAttribute("learnId", learnId);
		return "resources";
	}
	
	
	/**
	 * @Description: 下载文件
	 * @param 资源文件id
	 */
	@RequestMapping("/course/down")
	@Rule
	public void down(String resourceId, HttpServletResponse res) {
		courseService.down(resourceId, res);
	}

	
	
}
