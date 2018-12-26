package com.yz.controller.enroll;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.StudentConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.model.common.IPageInfo;
import com.yz.model.enroll.BdTestConfirmMap;
import com.yz.service.enroll.BdTestConfirmServiceImpl;

@Controller
@RequestMapping("/testConfirm")
public class BdTestConfirmController {

	@Autowired
	private BdTestConfirmServiceImpl testConfirmService;
	
	@RequestMapping("/list")
	@Rule("testConfirm:find")
	public String showList(HttpServletRequest request) {
		return "enroll/testconfirm/testConfirm-list";
	}
	
	/**
	 * @Description: 根据条件查询所有符合数据
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.system.SysDictController Note: Nothing much.
	 */
	@RequestMapping("/findAllTestConfirm")
	@ResponseBody
	@Rule("testConfirm:find")
	public Object findAllTestConfirm(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdTestConfirmMap testConfirmMap) {
		PageHelper.offsetPage(start, length);
		testConfirmMap.setStdStage(StudentConstants.STD_STAGE_CONFIRM);
		testConfirmMap.setIsOk("0");
		List<BdTestConfirmMap> resultList = testConfirmService.selectAll(testConfirmMap);
		return new IPageInfo((Page) resultList);
	}
	
	/**
	 * @Description: 根据条件查询所有符合数据
	 * @param :learnId 学业ID
	 */
	@RequestMapping("/okConfirm")
	@ResponseBody
	@Rule("testConfirm:update")
	public Object okConfirm(String learnId) {
		testConfirmService.okConfirm(learnId);
		return "SUCCESS";
	}
}
