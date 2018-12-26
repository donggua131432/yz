package com.yz.controller.finance;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.finance.coupon.BdCoupon;
import com.yz.model.finance.stdfee.BdPayInfo;
import com.yz.model.finance.stdfee.BdPayableQuery;
import com.yz.service.finance.BdStdPayFeeService;
import com.yz.service.finance.BdTuitionService;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/stdFee")
public class StdFeeController {

	private static final Logger log = LoggerFactory.getLogger(StdFeeController.class);

	@Autowired
	private BdStdPayFeeService payService;

	@Autowired
	private BdTuitionService tuitionService;

	@RequestMapping("/toList")
	@Rule("stdfee:query")
	public String toList() {

		return "finance/stdfee/stdfee-list";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("stdfee:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdPayableQuery query) {
		Object o = payService.selectStdFeePayPage(start, length, query);
		return o;
	}

	@RequestMapping("/toPay")
	@Rule("stdfee:update")
	@Token(action = Flag.Save, groupId = "stdfee:update")
	public Object toPay(Model model, @RequestParam(name = "learnId", required = true) String learnId) {
		Object o = payService.selectPayableInfoByLearnId(learnId, FinanceConstants.ORDER_STATUS_UNPAID);
		model.addAttribute("stdFee", o);
		return "/finance/stdfee/stdfee-pay";
	}

	@RequestMapping("/pay")
	@ResponseBody
	@Log
	@Rule("stdfee:update")
	@Token(action = Flag.Remove, groupId = "stdfee:update")
	public Object pay(BdPayInfo payInfo) {

		List<BdCoupon> coupons = JsonUtil.str2List(payInfo.getCouponsStr(), BdCoupon.class);
		payInfo.setCoupons(coupons);
		tuitionService.payTuition(payInfo, false, false);
		return "SUCCESS";
	}

	@RequestMapping("/payAndPrint")
	@ResponseBody
	@Log
	@Rule("stdfee:update")
	@Token(action = Flag.Remove, groupId = "stdfee:update")
	public Object payAndPrint(BdPayInfo payInfo, Model model) {

		return tuitionService.payTuition(payInfo, true, false);
	}

	@RequestMapping("/toPayDetail")
	@Rule("stdfee:query")
	public Object toPayDetail(@RequestParam(name = "learnId", required = true) String learnId, Model model) {

		model.addAttribute("stdInfo",
				payService.selectPayableInfoByLearnId(learnId, FinanceConstants.ORDER_STATUS_UNPAID));

		return "/finance/stdfee/stdfee-detail";
	}

	@RequestMapping("/toAccount")
	@Rule({ "stdfee:query", "feeReview:query", "rept:query" })
	public Object toAccount(@RequestParam(name = "stdId", required = true) String stdId, Model model) {

		model.addAttribute("stdId", stdId);
		Map<String, String> stdInfo = payService.selectStdInfoByStdId(stdId);
		model.addAttribute("stdName", stdInfo.get("stdName"));
		model.addAttribute("idCard", stdInfo.get("idCard"));

		return "/finance/stdfee/account-list";
	}

	@RequestMapping("/toTuition")
	@Rule("stdfee:query")
	public Object toTuition(@RequestParam(name = "stdId", required = true) String stdId, Model model) {

		model.addAttribute("stdId", stdId);

		String paidSum = payService.getPaidSum(stdId);
		String withdrawSum = payService.getWithdrawSum(stdId);

		if (!StringUtil.hasValue(paidSum)) {
			paidSum = "0.00";
		}

		if (!StringUtil.hasValue(withdrawSum)) {
			withdrawSum = "0.00";
		}

		model.addAttribute("paidSum", paidSum);
		model.addAttribute("withdrawSum", withdrawSum);
		return "/finance/stdfee/tuition-list";
	}

	@RequestMapping("/toZMAccount")
	@Rule("stdfee:query")
	public Object toZMAccount(@RequestParam(name = "stdId", required = true) String stdId, Model model) {

		String userId = payService.getUserId(stdId);

		model.addAttribute("userId", userId);
		model.addAttribute("stdId", stdId);
		model.addAttribute("zmAmount", payService.getAccount(stdId, FinanceConstants.ACC_TYPE_ZHIMI));

		return "/finance/stdfee/zm-list";
	}

	@RequestMapping("/toAllSerials")
	@Rule("stdfee:query")
	public Object toAllSerials(@RequestParam(name = "stdId", required = true) String stdId, Model model) {

		String userId = payService.getUserId(stdId);

		String paidSum = payService.getPaidSum(stdId);
		String withdrawSum = payService.getWithdrawSum(stdId);

		if (!StringUtil.hasValue(paidSum)) {
			paidSum = "0.00";
		}

		if (!StringUtil.hasValue(withdrawSum)) {
			withdrawSum = "0.00";
		}

		model.addAttribute("paidSum", paidSum);
		model.addAttribute("withdrawSum", withdrawSum);

		model.addAttribute("userId", userId);
		model.addAttribute("stdId", stdId);

		return "/finance/stdfee/all-list";
	}

	@RequestMapping("/toDemurrageAccount")
	@Rule("stdfee:query")
	public Object toDemurrageAccount(@RequestParam(name = "stdId", required = true) String stdId, Model model) {

		String userId = payService.getUserId(stdId);

		model.addAttribute("userId", userId);
		model.addAttribute("stdId", stdId);
		model.addAttribute("demurrageAmount", payService.getAccount(stdId, FinanceConstants.ACC_TYPE_DEMURRAGE));

		return "/finance/stdfee/demurrage-list";
	}

	@RequestMapping("/toCashAccount")
	@Rule("stdfee:query")
	public Object toCashAccount(@RequestParam(name = "stdId", required = true) String stdId, Model model) {

		String userId = payService.getUserId(stdId);

		model.addAttribute("userId", userId);
		model.addAttribute("stdId", stdId);
		model.addAttribute("cashAmount", payService.getAccount(stdId, FinanceConstants.ACC_TYPE_NORMAL));

		return "/finance/stdfee/cash-list";
	}

	/**
	 * 账户流水列表
	 * 
	 * @param start
	 * @param length
	 * @param userId
	 * @param type
	 * @return
	 */
	@RequestMapping("/zmDetail")
	@ResponseBody
	@Rule({ "stdfee:query", "feeReview:query" })
	public Object zmDetail(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,
			@RequestParam(name = "stdId", required = true) String stdId,
			@RequestParam(name = "type", required = true) String type) {
		Object o = payService.zmDetail(start, length, stdId, type);
		return o;
	}

	@RequestMapping("/allSerials")
	@ResponseBody
	@Rule({ "stdfee:query", "feeReview:query" })
	public Object allSerials(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,
			@RequestParam(name = "stdId", required = true) String stdId) {
		Object o = payService.allSerials(start, length, stdId);
		return o;
	}

	@RequestMapping("/payDetail")
	@ResponseBody
	@Rule("stdfee:query")
	public Object payDetail(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,
			@RequestParam(name = "learnId", required = true) String learnId) {
		Object o = payService.payDetail(start, length, learnId);
		return o;
	}

	@RequestMapping("/stdPayDetail")
	@ResponseBody
	@Rule("stdfee:query")
	public Object stdPayDetail(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,
			@RequestParam(name = "stdId", required = true) String stdId) {
		Object o = payService.stdPayDetail(start, length, stdId);
		return o;
	}

	@RequestMapping("/toAdd")
	@Rule("stdfee:insert")
	@Token(action = Flag.Save, groupId = "stdfee:insert")
	public Object toAdd() {

		return "/finance/stdfee/stdfee-add";
	}

	@RequestMapping("/add")
	@ResponseBody
	@Log
	@Rule("stdfee:insert")
	@Token(action = Flag.Remove, groupId = "stdfee:insert")
	public Object add(@RequestParam(value = "itemCode", required = true) String itemCode,
			@RequestParam(value = "amount", required = true) String amount,
			@RequestParam(value = "learnId", required = true) String learnId) {
		payService.addPayableItem(itemCode, amount, learnId);
		return "SUCCESS";
	}

	@RequestMapping("/findStudentInfo")
	@ResponseBody
	@Rule("stdfee:query")
	public Object findStudentInfo(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		return payService.findStudentInfo(rows, page, sName);
	}

	@RequestMapping("/findItemCodeHaveNot")
	@ResponseBody
	@Rule("stdfee:query")
	public Object findItemCodeHaveNot(String sName, String learnId,
			@RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {

		return payService.findItemCodeHaveNot(rows, page, sName, learnId);
	}

	@RequestMapping("/stdCoupon")
	@ResponseBody
	@Rule("stdfee:update")
	public Object stdCoupon(@RequestParam(name = "learnId", required = true) String learnId) {
		return payService.selectCoupon(learnId);
	}

	@RequestMapping("/stdCouponCount")
	@ResponseBody
	@Rule("stdfee:update")
	public Object stdCouponCount(@RequestParam(name = "learnId", required = true) String learnId) {
		return payService.selectCouponCount(learnId);
	}

	@RequestMapping("/couponAmount")
	@ResponseBody
	@Rule("stdfee:update")
	public Object couponAmount(@RequestParam(name = "couponId", required = true) String couponId) {

		return payService.selectCouponAmount(couponId);
	}

	/**
	 * 根据 姓名/手机号码/身份证号 -查询缴费学员
	 * 
	 * @param condition
	 * @return
	 */
	@RequestMapping("/searchStuPay")
	@ResponseBody
	public Object searchStuPay(@RequestParam(name = "condition", required = true) String condition,
			@RequestParam(name = "empId", required = true) String empId) {
		Object o = payService.selectStdPayFeeByCondition(condition, empId);
		return JsonUtil.object2String(o);
	}

	/**
	 * 某个专业缴费
	 * 
	 * @param model
	 * @param learnId
	 * @return
	 */
	@RequestMapping("/toQRCodePay")
	@ResponseBody
	public Object toQRCodePay(@RequestParam(name = "learnId", required = true) String learnId) {
		Object o = payService.selectPayableInfoByLearnIdForQRCode(learnId, FinanceConstants.ORDER_STATUS_UNPAID);
		return JsonUtil.object2String(o);
	}

	/**
	 * 扫码支付
	 * 
	 * @param payInfo
	 * @return
	 */
	@RequestMapping("/scanQRCodePay")
	@ResponseBody
	public Object scanQRCodePay(@RequestParam(name = "payData", required = true) String payData) {
		BdPayInfo payInfo = new BdPayInfo();
		JSONObject obj = JSONObject.fromObject(payData);
		payInfo.setLearnId(obj.getString("learnId"));

		JSONArray arryList = obj.getJSONArray("itemCodes");
		String[] itemCodes = new String[arryList.size()];
		if (arryList.size() > 0) {
			for (int i = 0; i < arryList.size(); i++) {
				itemCodes[i] = (String) arryList.get(i);
			}
		}
		JSONArray yearsList = obj.getJSONArray("years");
		String[] years = new String[yearsList.size()];
		if (yearsList.size() > 0) {
			for (int i = 0; i < yearsList.size(); i++) {
				years[i] = (String) yearsList.get(i);
			}
		}
		payInfo.setItemCodes(itemCodes);
		payInfo.setYears(years);
		payInfo.setCouponsStr(obj.getString("coupons"));
		payInfo.setPaymentType(obj.getString("paymentType"));
		payInfo.setTradeType(obj.getString("tradeType"));
		payInfo.setAccDeduction(obj.getString("accDeduction"));
		payInfo.setEmpId(obj.getString("empId"));
		return payService.scanQRCodePay(payInfo);
	}

	@RequestMapping("/studentCoupon")
	@ResponseBody
	public Object studentCoupon(@RequestParam(name = "learnId", required = true) String learnId) {
		return payService.studentCoupon(learnId);
	}

	/**
	 * 刷新学员信息
	 * 
	 * @param learnId
	 * @return
	 */
	@RequestMapping("/afreshStdOrder")
	@ResponseBody
	@Rule("stdfee:query")
	public Object afreshStudentOrder(@RequestParam(name = "learnId", required = true) String learnId) {
		payService.afreshStudentOrder(learnId, "S");
		return "SUCCESS";
	}

	@RequestMapping("/batchAfreshStdOrder")
	@ResponseBody
	@Rule("stdfee:query")
	public Object batchAfreshStdOrder(BdPayableQuery query) {
		payService.batchAfreshStdOrder(query);
		return "SUCCESS";
	}
}
