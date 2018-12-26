package com.yz.controller.course;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.course.GsCourseClassInfo;
import com.yz.model.oa.OaCampusInfo;
import com.yz.service.course.GsCourseClassService;
import com.yz.util.Assert;

/**
 * 课程开班
 * @author lx
 * @date 2017年8月15日 下午3:11:20
 */
@Controller
@RequestMapping("/class")
public class GsCourseClassController
{
	@Autowired
	private GsCourseClassService gsCourseClassService;

	@RequestMapping("/toList")
	@Rule("class:query")
	public String toList(Model model) {
		return "/course/course_class_list";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("class:query")
	@ResponseBody
	public Object goodsAuctionList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length) {
		return gsCourseClassService.getGsCourseClassInfo(start, length);
	}
	
	@RequestMapping("/toClassEdit")
	@Token(action = Flag.Save, groupId = "classEdit")
	@Rule("class:insert")
	public String toClassEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		GsCourseClassInfo classInfo = new GsCourseClassInfo();
		if ("UPDATE".equals(exType.trim().toUpperCase()) || "LOOK".equals(exType.trim().toUpperCase())) {
			String classId = RequestUtil.getString("classId");
			Assert.hasText(classId, "参数名称不能为空");
			//获取信息
			classInfo =gsCourseClassService.getGsCourseClassDetailInfo(classId);
		}
		model.addAttribute("classInfo", classInfo);
		model.addAttribute("exType", exType);
		return "/course/course_class_edit";
	}
	
	@RequestMapping("/classUpdate")
	@ResponseBody
	@Log
	@Token(action = Flag.Remove, groupId = "classEdit")
	@Rule("class:insert")
	public Object classUpdate(@RequestParam(name = "exType", required = true) String exType,GsCourseClassInfo classInfo) {
		String deal = exType.trim();
		BaseUser user = SessionUtil.getUser();

		if ("UPDATE".equalsIgnoreCase(deal)) {
			classInfo.setUpdateUserId(user.getUserId());
			classInfo.setUpdateUser(user.getRealName());
			gsCourseClassService.updateClass(classInfo);
			//清除缓存
		} else if ("ADD".equalsIgnoreCase(exType)) {
			classInfo.setCreateUserId(user.getUserId());
			classInfo.setCreateUser(user.getRealName());
			gsCourseClassService.insertClass(classInfo);
		}

		return "success";
	}
	@RequestMapping("/salesList")
	@ResponseBody
	public Object findAllKeyValue(String sName,@RequestParam(value = "rows", defaultValue = "10000") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		return gsCourseClassService.findAllKeyValue(rows,page,sName);
	}
}
