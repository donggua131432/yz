package com.yz.controller.finance;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.SfCode128CUtil;
import com.yz.model.finance.receipt.BdReceiptQuery;
import com.yz.service.finance.BdReptService;

/**
 * 收据管理 Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年7月12日.
 *
 */
@Controller
@RequestMapping("/rept")
public class StdReptController {

	@Autowired
	private BdReptService reptService;
	
	@Autowired
	private SfCode128CUtil sfCode128CUtil;

	@RequestMapping("/toList")
	@Rule("rept:query")
	public Object toList() {
		return "finance/rept/rept-list";
	}

	@RequestMapping("/printSiteRept")
	@ResponseBody
	public Object printSiteRept(@RequestParam(name = "serialMark", required = true) String serialMark) {

		return reptService.applySiteRept(serialMark);
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("rept:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdReceiptQuery query) {

		return reptService.selectReptByPage(start, length, query);
	}
	
	@RequestMapping("/count")
	@ResponseBody
	public Object count(BdReceiptQuery query) {
		return reptService.countAmount(query);
	}

	@RequestMapping("/reptOrder")
	@ResponseBody
	@Log
	@Rule("rept:sfOrder")
	public Object reptOrder(@RequestParam(name = "reptId", required = true) String reptId) throws IOException {
		reptService.sfOrder(reptId);
		return "SUCCESS";
	}

	@RequestMapping("/reptOrders")
	@ResponseBody
	@Log
	@Rule("rept:sfOrder")
	public Object reptOrders(@RequestParam(name = "reptIds[]", required = true) String[] reptIds) throws IOException {
		reptService.sfOrders(reptIds);
		return "SUCCESS";
	}

	@RequestMapping("/sended")
	@ResponseBody
	@Log
	@Rule("rept:update")
	public Object sended(@RequestParam(name = "reptIds[]", required = true) String[] reptIds) {
		reptService.reptMailed(reptIds);
		return "SUCCESS";
	}

	@RequestMapping("/sendReptInform")
	@ResponseBody
	@Log
	@Rule("rept:update")
	public Object sendReptInform(@RequestParam(name = "reptId", required = true) String reptId) {
		reptService.sendReptInform(reptId);
		return "SUCCESS";
	}
	
	@RequestMapping("/picked")
	@ResponseBody
	@Rule("rept:update")
	public Object picked(@RequestParam(name = "reptId", required = true) String reptId) {
		String[] reptIds = new String[]{reptId};
		reptService.reptProcessed(reptIds);
		return "SUCCESS";
	}
	
	@RequestMapping("/pickeds")
	@ResponseBody
	@Rule("rept:update")
	public Object pickeds(@RequestParam(name = "reptIds[]", required = true) String reptIds[]) {
		reptService.reptProcessed(reptIds);
		return "SUCCESS";
	}
	

	@RequestMapping("/reptPrint")
	@Rule("rept:print")
	public Object reptPrint(@RequestParam(name = "reptId", required = true) String reptId, Model model) {
		model.addAttribute("repts", reptService.reptPrintInfos(new String[] { reptId }));
		return "/finance/rept/receipt-print";
	}

	@RequestMapping("/sfPrint")
	@Rule("rept:print")
	public Object sfPrint(@RequestParam(name = "reptId", required = true) String reptId, Model model) {
		model.addAttribute("sfs", reptService.sfPrintInfo(reptId));
		return "/finance/rept/sf-print";
	}

	@RequestMapping("/reptPrints")
	@Rule("rept:print")
	public Object reptPrints(@RequestParam(name = "reptIds[]", required = true) String[] reptIds, Model model) {
		model.addAttribute("repts", reptService.reptPrintInfos(reptIds));
		return "/finance/rept/receipt-print";
	}

	@RequestMapping("/sfPrints")
	@Rule("rept:print")
	public Object sfPrints(@RequestParam(name = "reptIds[]", required = true) String[] reptIds, Model model) {
		model.addAttribute("sfs", reptService.sfPrintInfos(reptIds));
		return "/finance/rept/sf-print";
	}

	@RequestMapping("/barCode")
	@Rule("rept:print")
	public void validCode(@RequestParam(name = "mailno", required = true) String mailno, HttpServletResponse response)
			throws IOException {
		sfCode128CUtil.kiCode128C(mailno, response);
	}
}
