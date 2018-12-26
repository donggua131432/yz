package com.yz.controller.transfer;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.util.RequestUtil;
import com.yz.model.transfer.BdStudentQuitInfo;
import com.yz.model.transfer.BdStudentQuitQuery;
import com.yz.service.transfer.BdStudentQuitService;
/**
 * 休学管理
 * @author lx
 * @date 2018年5月24日 上午11:48:53
 */
@Controller
@RequestMapping("/quitSchool")
public class BdStudentQuitSchoolController {

	@Autowired
	private BdStudentQuitService studentQuitService;
	/**
	 * 跳转到休学申请页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	@Rule("quitSchool:query")
	public String showList(HttpServletRequest request) {
		return "transfer/quit/student-quit-list";
	}
	
	/**
	 * 加载休学申请的列表
	 * @param start
	 * @param length
	 * @param quitQuery
	 * @return
	 */
	@RequestMapping("/findAllBdStudentQuit")
	@ResponseBody
	@Rule("quitSchool:query")
	public Object findAllBdStudentQuit(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length,BdStudentQuitQuery quitQuery){
		return studentQuitService.findAllBdStudentQuit(start, length, quitQuery);
	}
	
	/**
	 * 跳转到添加页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/edit")
	@Rule("quitSchool:insert")
	@Token(groupId = "quitSchool:insert", action = Flag.Save)
	public String edit(HttpServletRequest request) {
		return "transfer/quit/student-quit-edit";
	}

	/**
	 * 添加申请
	 * @param request
	 * @param studentQuit
	 * @param attachment
	 * @return
	 */
	@RequestMapping("/addStudentQuit")
	@ResponseBody
	@Rule("quitSchool:insert")
	@Token(groupId = "quitSchool:insert", action = Flag.Remove)
	public Object addStudentQuit(HttpServletRequest request, BdStudentQuitInfo studentQuit,
			@RequestParam(name = "attachment", required = false) MultipartFile attachment) {
		studentQuitService.insertQuitSchoolApply(studentQuit, attachment);
		return "SUCCESS";
	}
	
	/**
	 * 查找学员信息
	 * @param sName
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/findStudentInfo")
	@ResponseBody
	@Rule("quitSchool:query")
	public Object findStudentInfo(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		return studentQuitService.findStudentInfo(page, rows, sName);
	}
	
	/**
	 * 取消申请
	 * @param ids
	 * @return
	 */
	@RequestMapping("/cancelApply")
	@ResponseBody
	@Rule("quitSchool:delete")
	public Object cancelApply(@RequestParam(name = "ids[]", required = true) String[] ids) {
		if(null !=ids && ids.length >0){
			for(String id :ids){
				studentQuitService.delStudentQuitSchoolApply(id);	
			}
		}
		return "SUCCESS";
	}
	
	/**
	 * 查看休学申请
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/lookQuitSchool")
	@Rule("quitSchool:query")
	public String lookQuitSchoolApply(HttpServletRequest request, Model model) {
		String id = RequestUtil.getString("id");
		model.addAttribute("quitSchoolInfo", studentQuitService.getQuitSchoolApplyInfo(id));
		return "transfer/quit/quit-apply-look";
	}
}
