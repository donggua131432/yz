package com.yz.controller.refund;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.refund.BdWithdrawQuery;
import com.yz.service.refund.StdWithdrawService;

@Controller
@RequestMapping("/withdraw")
public class StdWithdrawController {

	@Autowired
	private StdWithdrawService drawService;

	@RequestMapping("/toList")
	@Rule("withdraw:query")
	public Object toList() {
		return "refund/withdraw/withdraw-list";
	}

	@RequestMapping("/toCheck")
	@Rule("withdraw:check")
	@Token(action = Flag.Save, groupId = "withdraw:check")
	public Object toCheck(@RequestParam(name = "withdrawId", required = true) String withdrawId, Model model) {
		model.addAttribute("withdraw", drawService.selectWithdraw(withdrawId));
		return "refund/withdraw/withdraw-check";
	}

	@RequestMapping("/toRejectReason")
	@Rule("withdraw:check")
	public Object toRejectReason(@RequestParam(name = "withdrawId", required = true) String withdrawId,
			@RequestParam(name = "remark", required = false) String remark, Model model) {
		model.addAttribute("withdrawId", withdrawId);
		model.addAttribute("remark", remark);
		return "refund/withdraw/withdraw-reject";
	}

	@RequestMapping("list")
	@ResponseBody
	@Rule("withdraw:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdWithdrawQuery query) {
		return drawService.selectWithdrawByPage(start, length, query);
	}

	@RequestMapping("/check")
	@Rule("withdraw:check")
	@Log
	@ResponseBody
	@Token(action = Flag.Remove, groupId = "withdraw:check")
	public Object checkWithdraw(@RequestParam(name = "withdrawId", required = true) String withdrawId, String remark) {
		drawService.checkPass(withdrawId, remark);
		return "SUCCESS";
	}

	@RequestMapping("/reject")
	@Rule("withdraw:check")
	@Log
	@ResponseBody
	public Object reject(@RequestParam(name = "withdrawId", required = true) String withdrawId, String remark,
			String rejectReason) {
		drawService.reject(withdrawId, remark, rejectReason);
		return "SUCCESS";
	}

	@RequestMapping("/checks")
	@Rule("withdraw:check")
	@Log
	@ResponseBody
	public Object checkWithdraws(@RequestParam(name = "withdrawIds[]", required = true) String[] withdrawIds,
			String remark) {
		drawService.checkPassBatch(withdrawIds,remark);
		return "SUCCESS";
	}
	
	@RequestMapping("/export")
	@Rule("withdraw:check")
	public void export(BdWithdrawQuery query, HttpServletResponse response) {
		drawService.exportWithdraw(query, response);
	}

	@RequestMapping("/toSelectChecks")
	@Rule("withdraw:check")
	public String toSelectChecks(HttpServletRequest request) {
		return "refund/withdraw/std-withdraw-check";
	}
}
