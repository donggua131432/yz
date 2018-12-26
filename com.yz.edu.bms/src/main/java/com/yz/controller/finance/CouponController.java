package com.yz.controller.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.constants.FinanceConstants;
import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.finance.coupon.BdCouponEdit;
import com.yz.model.finance.coupon.BdCouponQuery;
import com.yz.model.finance.coupon.BdCouponResponse;
import com.yz.service.finance.BdCouponService;

@Controller
@RequestMapping("/coupon")
public class CouponController {

	@Autowired
	private BdCouponService couponService;

	@RequestMapping("/toList")
	@Rule("coupon:query")
	public String toList() {

		return "finance/coupon/coupon-list";
	}
	
	@RequestMapping("/validateCouponName")
	@ResponseBody
	@Rule("coupon:query")
	public Object validateCouponName(String couponName) {
		
		return couponService.validateCouponName(couponName);
	}
	

	@RequestMapping("/list")
	@ResponseBody
	@Rule("coupon:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdCouponQuery coupon) {

		return couponService.selectCouponByPage(start, length, coupon);
	}

	@RequestMapping("/toAdd")
	//@Token(action = Flag.Save, groupId = "coupon:insert")
	@Rule("coupon:insert")
	public Object toAdd(Model model) {
		
		BdCouponResponse coupon = new BdCouponResponse();
		model.addAttribute("coupon", coupon);
		model.addAttribute("exType", "ADD");
		return "finance/coupon/coupon-edit";
	}

	@RequestMapping("/add")
	@ResponseBody
	@Log
	@Rule("coupon:insert")
	//@Token(action = Flag.Remove, groupId = "coupon:insert")
	public Object add(BdCouponEdit coupon) {
		couponService.insertCoupon(coupon);
		return "SUCCESS";
	}

	@RequestMapping("/toEdit")
	@Rule("coupon:update")
	@Token(action = Flag.Save, groupId = "coupon:update")
	public Object toEdit(Model model, @RequestParam(name = "couponId", required = true) String couponId) {

		Object o = couponService.selectCouponById(couponId);
		model.addAttribute("coupon", o);
		model.addAttribute("exType", "UPDATE");
		return "finance/coupon/coupon-edit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Log
	@Rule("coupon:update")
	@Token(action = Flag.Remove, groupId = "coupon:update")
	public Object edit(BdCouponEdit coupon) {
		couponService.updateCoupon(coupon);
		return "SUCCESS";
	}

	@RequestMapping("/delete")
	@ResponseBody
	@Rule("coupon:delete")
	@Log
	public Object delete(@RequestParam(name = "couponId", required = true) String couponId) {
		couponService.deleteCoupon(couponId);
		return "SUCCESS";
	}

	@RequestMapping("/deleteCoupons")
	@ResponseBody
	@Rule("coupon:delete")
	@Log
	public Object deleteCoupons(@RequestParam(name = "couponIds[]", required = true) String[] couponIds) {
		couponService.deleteCoupons(couponIds);
		return "SUCCESS";
	}

	@RequestMapping("/block")
	@ResponseBody
	@Rule("coupon:update")
	@Log
	public Object block(@RequestParam(name = "couponId", required = true) String couponId) {
		couponService.blockCoupon(couponId, FinanceConstants.COUPON_NOT_ALLOW);
		return "SUCCESS";
	}

	@RequestMapping("/start")
	@ResponseBody
	@Rule("coupon:update")
	@Log
	public Object start(@RequestParam(name = "couponId", required = true) String couponId) {
		couponService.blockCoupon(couponId, FinanceConstants.COUPON_ALLOW);
		return "SUCCESS";
	}

}
