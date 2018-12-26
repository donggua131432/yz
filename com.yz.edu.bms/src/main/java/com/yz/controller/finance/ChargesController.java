package com.yz.controller.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.finance.feeitem.BdFeeItem;
import com.yz.service.finance.BdFeeItemService;

@Controller
@RequestMapping("charges")
public class ChargesController {

	@Autowired
	private BdFeeItemService feeItemService;

	@RequestMapping("toList")
	@Rule("charges:query")
	public String toList() {

		return "finance/charges/charges-list";
	}

	@RequestMapping("list")
	@ResponseBody
	@Rule("charges:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdFeeItem item) {

		return feeItemService.queryFeeItemByPage(start, length, item);
	}

	@RequestMapping("/toAdd")
	@Token(action = Flag.Save, groupId = "charges:insert")
	@Rule("charges:insert")
	public Object toAdd(Model model) {
		BdFeeItem itemInfo = new BdFeeItem();
		model.addAttribute("itemInfo", itemInfo);
		model.addAttribute("exType", "ADD");
		return "finance/charges/charges-edit";
	}

	@RequestMapping("/toEdit")
	@Token(action = Flag.Save, groupId = "charges:update")
	@Rule("charges:update")
	public Object toEdit(@RequestParam(name = "itemCode", required = true) String itemCode, Model model) {
		model.addAttribute("itemInfo", feeItemService.selectFeeItemByItemCode(itemCode));
		model.addAttribute("exType", "UPDATE");
		return "finance/charges/charges-edit";
	}

	@RequestMapping("/add")
	@ResponseBody
	@Log
	@Rule("charges:insert")
	@Token(action = Flag.Remove, groupId = "charges:insert")
	public Object add(BdFeeItem item, String[] recruitTypes) {
		BaseUser user = SessionUtil.getUser();
		item.setUpdateUserId(user.getUserId());
		item.setUpdateUser(user.getRealName());
		item.setCreateUser(user.getRealName());
		item.setCreateUserId(user.getUserId());

		feeItemService.insertFeeItem(item, recruitTypes);
		return "success";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("charges:update")
	@Token(action = Flag.Remove, groupId = "charges:update")
	public Object edit(BdFeeItem item, String[] recruitTypes) {

		BaseUser user = SessionUtil.getUser();
		item.setUpdateUserId(user.getUserId());
		item.setUpdateUser(user.getRealName());

		feeItemService.updateFeeItem(item, recruitTypes);

		return "success";
	}

	@RequestMapping("/block")
	@ResponseBody
	@Log
	@Rule("charges:update")
	public Object block(@RequestParam(name = "exType", required = true) String exType,
			@RequestParam(name = "itemCode", required = true) String itemCode) {
		BdFeeItem item = new BdFeeItem();
		item.setItemCode(itemCode);
		if ("BLOCK".equalsIgnoreCase(exType)) {
			item.setStatus(GlobalConstants.STATUS_BLOCK);
		} else if ("START".equalsIgnoreCase(exType)) {
			item.setStatus(GlobalConstants.STATUS_START);
		}
		feeItemService.blockFeeItem(item);
		return "SUCCESS";
	}

	@RequestMapping("/deleteFeeItems")
	@ResponseBody
	@Log
	@Rule("charges:delete")
	public Object deleteFeeItems(@RequestParam(name = "itemCodes[]", required = true) String[] itemCodes) {
		feeItemService.deleteFeeItems(itemCodes);
		return "success";
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Log
	@Rule("charges:delete")
	public Object deleteFeeItem(@RequestParam(name = "itemCode", required = true) String itemCode) {
		feeItemService.deleteFeeItem(itemCode);
		return "success";
	}

	@RequestMapping("validateItemCode")
	@ResponseBody
	@Rule("charges:query")
	public Object validateItemCode(@RequestParam(name = "itemCode", required = true) String itemCode,
			@RequestParam(name = "exType", required = true) String exType) {
		if ("ADD".equals(exType)) {
			if (null != feeItemService.selectFeeItemByItemCode(itemCode)) {
				return false;
			}
		} else if ("UPDATE".equals(exType)) {
			return true;
		} else {
			return false;
		}
		return true;
	}

}
