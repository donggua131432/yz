package com.yz.controller.oa;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.OaMonthExpense;
import com.yz.model.oa.OaMonthExpenseQuery;
import com.yz.service.oa.OaExpenseService;

@Controller
@RequestMapping("/monthExpense")
public class OaMonthExpenseController {

	@Autowired
	private OaExpenseService exService;

	/**
	 * 报销列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("monthExpense:query")
	public String toList(Model model) {
		return "/performance/expense/expense-list";
	}

	/**
	 * 报销列表页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	@Rule("monthExpense:query")
	@ResponseBody
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, OaMonthExpenseQuery query) {

		return exService.queryMonthExpenseByPage(start, length, query);
	}

	@RequestMapping("/balance")
	@Rule("monthExpense:insert")
	@ResponseBody
	public Object balance(OaMonthExpense ex) {

		return exService.getBalance(ex);
	}

	/**
	 * 报销详情页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toInsert")
	@Rule("monthExpense:insert")
	@Token(action = Flag.Save, groupId = "monthExpense:insert")
	public String toInsert(Model model) {
		return "/performance/expense/expense-edit";
	}

	@RequestMapping("/insert")
	@Rule("monthExpense:insert")
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "monthExpense:insert")
	public Object insert(OaMonthExpense ex) {
		exService.addExpense(ex);
		return "SUCCESS";
	}

	/**
	 * 报销详情页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toInfoList")
	@Rule("monthExpense:query")
	public String toInfoList(Model model, OaMonthExpense ex) {
		model.addAttribute("allAmount", exService.queryReplyedAmount(ex));
		model.addAttribute("empId", ex.getEmpId());
		model.addAttribute("year", ex.getYear());
		return "/performance/expense/expense-info-list";
	}

	/**
	 * 报销详情页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/infoList")
	@Rule("monthExpense:query")
	@ResponseBody
	public Object infoList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, OaMonthExpense ex) {

		return exService.queryExpenseInfo(start, length, ex);
	}

	/**
	 * 已报销合计
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/amount")
	@Rule("monthExpense:query")
	@ResponseBody
	public String amount(OaMonthExpense ex) {

		return exService.queryReplyedAmount(ex);
	}

	/**
	 * 招生老师
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/recruitList")
	@Rule("monthExpense:insert")
	@ResponseBody
	public Object recruitList(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) exService.recruitList(sName));
	}

}
