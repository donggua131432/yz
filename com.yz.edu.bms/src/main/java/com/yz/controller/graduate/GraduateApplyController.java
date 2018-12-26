package com.yz.controller.graduate;


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
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.graduate.GraduateApplyQuery;
import com.yz.model.graduate.GraduateStudentInfo;
import com.yz.service.graduate.GraduateApplyService;

/**
 * 毕业发起 & 学员毕业
 * @author lx
 * @date 2017年7月12日 下午3:32:20
 */
@Controller
@RequestMapping("/graduate")
public class GraduateApplyController {

	@Autowired
	private GraduateApplyService graduateApplyService;
	/**
	 * 跳转到 毕业申请列表页
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("graduate:query")
	public String toList() {
		return "graduate/apply/apply-list";
	}
	
	/**
	 * 分页获取毕业申请数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("graduate:query")
	@ResponseBody
	public Object applyList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GraduateApplyQuery query) {
		return graduateApplyService.queryApplyInfosByPage(start, length, query);
	}
	/**
	 * 跳转到 发起毕业申请页面
	 * @return
	 */
	@RequestMapping("/edit")
	@Rule("graduate:insert")
	public String edit() {
		return "graduate/apply/apply_add";
	}
	
	/**
	 * 根据 手机号，身份证号，姓名，查询学员信息(自己)
	 * @param condition
	 * @return
	 */
	@RequestMapping("/queryStudentByCondition")
	@ResponseBody
	public Object queryStudentByCondition(String condition) {
		BaseUser user = SessionUtil.getUser();
		return  graduateApplyService.queryStudentByCondition(condition,user.getEmpId());
	}
	/**
	 * 新增毕业申请
	 * @param studentInfo
	 * @return
	 */
	@RequestMapping("/apply")
	@Rule("graduate:insert")
	@ResponseBody
	@Log
	public Object graduateApply(GraduateStudentInfo studentInfo) {
		BaseUser user = SessionUtil.getUser();
		
		studentInfo.setCreateUserId(user.getUserId());
		studentInfo.setCreateUser(user.getRealName());
	    graduateApplyService.insertGraduateApply(studentInfo);

		return "success";
	}
	/**
	 * 批量删除毕业申请
	 * @param idArray
	 * @return
	 */
	@RequestMapping("/deleteGraduate")
	@Rule("graduate:delete")
	@ResponseBody
	public Object deleteGraduate(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		graduateApplyService.deleteGraduate(idArray);
		return "success";
	}
	
	/**
	 * 跳转到 学员毕业 页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/studentGraduate")
	@Rule("graduate:query")
	public String studentGraduate(HttpServletRequest request,Model model) {
		return "/graduate/check/graduate_list";
	}
	
	/**
	 * 毕业学员信息列表
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "/graduateList", method = RequestMethod.GET)
	@ResponseBody
	@Rule("graduate:query")
	public Object graduateList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,GraduateApplyQuery query) {
		return graduateApplyService.queryGraduateStudentInfosByPage(start, length, query);
	}
	/**
	 * 跳转到毕业确认页面
	 * @param stdId
	 * @param learnId
	 * @param stdName
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toGraduate")
	@Rule("graduate:query")
	public String toGraduate(@RequestParam(name = "stdId", required = true) String stdId,
            @RequestParam(name = "learnId", required = true) String learnId,
            @RequestParam(name = "stdName", required = true) String stdName,
            HttpServletRequest request,Model model) {
		
		model.addAttribute("stdId", stdId);
		model.addAttribute("learnId", learnId);
		model.addAttribute("stdName", stdName);
		return "graduate/check/confirm_graduate";
	}
	
	/**
	 * 确认学员毕业
	 * @param learnId
	 * @return
	 */
	@RequestMapping("/confirmGraduate")
	@Rule("graduate:confirm")
	@ResponseBody
	public Object confirmGraduate(@RequestParam(name = "learnId", required = true) String learnId) {
		graduateApplyService.confirmGraduate(learnId);
		return "success";
	}
	
}
