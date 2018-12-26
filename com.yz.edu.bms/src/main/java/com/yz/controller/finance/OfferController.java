package com.yz.controller.finance;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.yz.core.security.manager.SessionUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.finance.fee.FeeItemForm;
import com.yz.model.finance.offer.BdOffer;
import com.yz.model.finance.offer.BdOfferQuery;
import com.yz.model.finance.offer.BdOfferResponse;
import com.yz.service.finance.BdFeeItemService;
import com.yz.service.finance.BdOfferService;

@Controller
@RequestMapping("/offer")
public class OfferController {

	@Autowired
	private BdOfferService offerService;
	
	@Autowired
	private BdFeeItemService itemService;

	@RequestMapping("/toList")
	@Rule("offer:query")
	public String toList() {

		return "finance/offer/offer-list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("offer:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdOfferQuery offer) {

		return offerService.selectOfferByPage(start, length, offer);
	}

	@RequestMapping("/toAdd")
	@Token(action = Flag.Save, groupId = "offer:insert")
	@Rule("offer:insert")
	public Object toAdd(Model model) {
		BdOfferResponse offer = new BdOfferResponse();
		List<HashMap<String, String>> testArea = new ArrayList<HashMap<String, String>>();
		offer.setTestArea(testArea);
		ArrayList<FeeItemForm> items = new ArrayList<FeeItemForm>();
		offer.setItems(items);
		model.addAttribute("offer", offer);
		model.addAttribute("exType", "ADD");
		return "finance/offer/offer-edit";
	}

	@RequestMapping("/add")
	@ResponseBody
	@Log
	@Rule("offer:insert")
	@Token(action = Flag.Remove, groupId = "offer:insert")
	public Object add(BdOffer offer) {
		BaseUser user = SessionUtil.getUser();
		offer.setUpdateUserId(user.getUserId());
		offer.setUpdateUser(user.getRealName());
		offer.setCreateUser(user.getRealName());
		offer.setCreateUserId(user.getUserId());

		offerService.insertOffer(offer);
		return "success";
	}

	@RequestMapping("/toEdit")
	@Token(action = Flag.Save, groupId = "offer:update")
	@Rule("offer:update")
	public Object toEdit(Model model, @RequestParam(name = "offerId", required = true) String offerId) {

		Object o = offerService.selectOfferById(offerId);

		model.addAttribute("offer", o);
		model.addAttribute("exType", "UPDATE");
		return "finance/offer/offer-edit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("offer:update")
	@Token(action = Flag.Remove, groupId = "offer:update")
	public Object edit(BdOffer offer) {
		offerService.updateOffer(offer);
		return "SUCCESS";
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Log
	@Rule("offer:delete")
	public Object delete(@RequestParam(name = "offerId", required = true) String offerId) {
		offerService.deleteOffer(offerId);
		return "SUCCESS";
	}

	@RequestMapping("/deleteOffers")
	@ResponseBody
	@Log
	@Rule("offer:delete")
	public Object deleteOffers(@RequestParam(name = "offerIds[]", required = true) String[] offerIds) {
		offerService.deleteOffers(offerIds);
		return "SUCCESS";
	}

	@RequestMapping("/block")
	@ResponseBody
	@Rule("offer:update")
	@Log
	public Object block(@RequestParam(name = "offerId", required = true) String offerId) {
		offerService.blockOffer(offerId, StudentConstants.FEE_STATUS_BLOCK);
		return "SUCCESS";
	}

	@RequestMapping("/start")
	@ResponseBody
	@Rule("offer:update")
	@Log
	public Object start(@RequestParam(name = "offerId", required = true) String offerId) {
		offerService.blockOffer(offerId, StudentConstants.FEE_STATUS_START);
		return "SUCCESS";
	}
	
	@RequestMapping("/items")
	@ResponseBody
	@Rule({"offer:update","offer:insert"})
	public Object items(@RequestParam(name = "recruitType", required = true) String recruitType) {
		return itemService.selectFeeItemByRecruitType(recruitType);
	}
	
	@RequestMapping("/getSg")
	@ResponseBody
	@Rule({"offer:update","offer:insert"})
	public Object getSg(@RequestParam(name = "scholarship", required = true) String scholarship) {
		return offerService.selectSgByScholarship(scholarship);
	}

}
