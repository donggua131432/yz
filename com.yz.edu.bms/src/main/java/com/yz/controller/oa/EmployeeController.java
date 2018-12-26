package com.yz.controller.oa;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.common.IPageInfo;
import com.yz.service.oa.OaEmployeeService;

/**
 * 员工信息
 * @author lx
 * @date 2017年6月29日 上午9:26:48
 */
@Controller
@RequestMapping("/employ")
public class EmployeeController {

	@Autowired
	private OaEmployeeService employeeService;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/list")
	@ResponseBody
	public Object findAllKeyValue(String sName,@RequestParam(value = "rows", defaultValue = "10000") int rows,
			@RequestParam(value = "page", defaultValue = "1")int page) {
		PageHelper.startPage(page, rows);
		List<Map<String, String>> resultList = employeeService.findAllKeyValue(sName);
		if (null == resultList) {
			return "不存在!";
		}
		return new IPageInfo((Page) resultList);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/getEmpList")
	@ResponseBody
	public Object quitEmpList(String sName,@RequestParam(value = "rows", defaultValue = "10000") int rows,
								  @RequestParam(value = "page", defaultValue = "1")int page) {
		PageHelper.startPage(page, rows);
		List<Map<String, String>> resultList = employeeService.findKeyValueByStatus(sName);
		if (null == resultList) {
			return "不存在!";
		}
		return new IPageInfo((Page) resultList);
	}
	
	@RequestMapping("/selectAllList")
	@ResponseBody
	public Object selectAllList(String dpId) {
		List<Map<String, String>> resultList = employeeService.findAllListByDpId(dpId);
		if (null == resultList) {
			return "不存在!";
		}
		return resultList;
	}
}
