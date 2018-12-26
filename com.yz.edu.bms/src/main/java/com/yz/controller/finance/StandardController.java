package com.yz.controller.finance;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.StudentConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.condition.fee.BdFeeQuery;
import com.yz.model.finance.fee.BdFeeEdit;
import com.yz.model.finance.fee.FeeInfoResponse;
import com.yz.model.finance.fee.FeeItemResponse;
import com.yz.model.finance.fee.PfsnInfoResponse;
import com.yz.service.finance.BdFeeStandardService;

@Controller
@RequestMapping("/standard")
public class StandardController {

	@Autowired
	private BdFeeStandardService feeService;

	@RequestMapping("/toList")
	@Rule("standard:query")
	public String toList() {

		return "finance/standard/standard-list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("standard:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdFeeQuery fee) {

		return feeService.selectStandardByPage(start, length, fee);
	}

	@RequestMapping("/toAdd")
	@Token(action = Flag.Save, groupId = "standard:insert")
	@Rule("standard:insert")
	public Object toAdd(Model model) {
		FeeInfoResponse fee = new FeeInfoResponse();
		List<PfsnInfoResponse> pfsnInfo = new ArrayList<PfsnInfoResponse>();
		List<FeeItemResponse> feeItem = new ArrayList<FeeItemResponse>();
		fee.setPfsnInfo(pfsnInfo);
		fee.setFeeItem(feeItem);
		model.addAttribute("fee", fee);
		model.addAttribute("exType", "ADD");
		return "finance/standard/standard-edit";
	}

	@RequestMapping("/toEdit")
	@Rule("standard:update")
	@Token(action = Flag.Save, groupId = "standard:update")
	public Object toEdit(Model model, @RequestParam(name = "feeId", required = true) String feeId) {

		Object o = feeService.selectStandardById(feeId);
		model.addAttribute("fee", o);
		model.addAttribute("exType", "UPDATE");
		return "finance/standard/standard-edit";
	}

	@RequestMapping("/add")
	@ResponseBody
	@Log
	@Rule("standard:insert")
	@Token(action = Flag.Remove, groupId = "standard:insert")
	public Object add(BdFeeEdit fee) {

		feeService.insertBdFee(fee);

		return "SUCCESS";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("standard:update")
	@Token(action = Flag.Remove, groupId = "standard:update")
	public Object edit(BdFeeEdit fee) {
		feeService.updateBdFee(fee);
		return "SUCCESS";
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Rule("standard:delete")
	@Log
	public Object delete(@RequestParam(name = "feeId", required = true) String feeId) {
		feeService.deleteBdFee(feeId);
		return "SUCCESS";
	}

	@RequestMapping("/deleteFees")
	@ResponseBody
	@Rule("standard:delete")
	@Log
	public Object deleteFees(@RequestParam(name = "feeIds[]", required = true) String[] feeIds) {
		feeService.deleteBdFees(feeIds);
		return "SUCCESS";
	}

	@RequestMapping("/block")
	@ResponseBody
	@Rule("standard:update")
	@Log
	public Object block(@RequestParam(name = "feeId", required = true) String feeId) {
		feeService.blockFee(feeId, StudentConstants.FEE_STATUS_BLOCK);
		return "SUCCESS";
	}

	@RequestMapping("/start")
	@ResponseBody
	@Rule("standard:update")
	@Log
	public Object start(@RequestParam(name = "feeId", required = true) String feeId) {
		feeService.blockFee(feeId, StudentConstants.FEE_STATUS_START);
		return "SUCCESS";
	}

}
