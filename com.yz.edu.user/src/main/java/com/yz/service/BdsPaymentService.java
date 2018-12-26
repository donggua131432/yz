package com.yz.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.FeeConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.OrderConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.dao.BdFeeItemMapper;
import com.yz.dao.BdsLearnMapper;
import com.yz.dao.BdsPaymentMapper;
import com.yz.edu.paging.common.PageHelper;
import com.yz.generator.IDGenerator;
import com.yz.model.AtsAccount;
import com.yz.model.WechatMsgVo;
import com.yz.model.communi.Body;
import com.yz.model.coupon.BdCoupon;
import com.yz.model.payment.BdFeeItem;
import com.yz.model.payment.BdOrder;
import com.yz.model.payment.BdPayInfo;
import com.yz.model.payment.BdPayableInfoResponse;
import com.yz.model.payment.BdStudentSerial;
import com.yz.model.payment.BdSubSerial;
import com.yz.model.payment.OaCampusInfo;
import com.yz.pay.GwPaymentService;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.AmountToCNUtil;
import com.yz.util.AmountUtil;
import com.yz.util.Assert;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
public class BdsPaymentService {

	private static final Logger log = LoggerFactory.getLogger(BdsPaymentService.class);

	@Autowired
	private BdsPaymentMapper payMapper;

	@Autowired
	private AccountService accountService;

	@Autowired
	private GwPaymentService paymentService;

	@Autowired
	private GwWechatService wechatService;

	@Autowired
	private UsInfoService infoService;

	@Autowired
	private BdsLearnMapper learnMapper;

	@Autowired
	private BdsStudentSendService sendService;

	@Autowired
	private BdFeeItemMapper feeItemMapper;

	/**
	 * 学员已缴详情
	 * 
	 * @param learnId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Object selectStudentSerial(String learnId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize, false);
		List<HashMap<String, Object>> list = payMapper.selectPayDetailByLearnId(learnId);
		for (HashMap<String, Object> s : list) {
			String cnAmount = AmountToCNUtil.number2CNMontrayUnit(AmountUtil.str2Amount(s.get("amount").toString()));
			s.put("chnAmount", cnAmount);
		}
		return list;
	}

	/**
	 * 我的优惠券
	 * 
	 * @param learnId
	 * @return
	 */
	public Object selectCoupon(String userId) {
		return JSONArray.fromObject(payMapper.selectCouponByLearnId(userId));
	}

	/**
	 * 任务录取页---待缴费页面数据
	 * 
	 * @param learnId
	 * @param userId
	 * @param subOrderStatus
	 * @return
	 */
	public Object selectEnrollPayableInfoByLearnId(String learnId, String userId) {
		BdPayableInfoResponse response = payMapper.selectStuPayableInfoResultMap(learnId,
				FinanceConstants.ORDER_STATUS_UNPAID);
		// 确定缴费时间
		// 已入围
		if (response != null && response.getInclusionStatus() != null && response.getInclusionStatus().equals("2")) {
			response.setPayFeeTime("2017年12月*日至2017年12月*日");
		} else if (response != null && response.getInclusionStatus() != null
				&& response.getInclusionStatus().equals("3")) {// 非入围
			response.setPayFeeTime("2017年12月*日至2017年12月*日");
		} else {
			if (response.getPfsnLevel() != null && response.getPfsnLevel().equals("1")) {// 本科
				response.setPayFeeTime("2017年12月*日至2017年12月*日");
			} else {
				response.setPayFeeTime("2017年12月*日至2017年12月*日");
			}
		}
		return JSONObject.fromObject(response);
	}

	/**
	 * 缴费页面数据
	 * 
	 * @param learnId
	 * @param userId
	 * @param subOrderStatus
	 * @return
	 */
	public Object selectPayableInfoByLearnId(String learnId, String userId) {
		BdPayableInfoResponse response = payMapper.selectPayableInfoByLearnId(learnId,
				FinanceConstants.ORDER_STATUS_UNPAID);
		// 2017-10-25 有现金 变为滞留
		// Map<String, String> account = atsApi.getAccount(userId, null,
		// null,FinanceConstants.ACC_TYPE_DEMURRAGE);
		Assert.isTrue(StringUtil.hasValue(userId), "用户ID不能为空");

		AtsAccount acc = new AtsAccount();
		acc.setStdId(null);
		acc.setUserId(userId);
		acc.setEmpId(null);
		acc.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
		Map<String, String> account = accountService.getAccount(acc);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.get("accAmount");
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}

		// Map<String, String> zmAccount = atsApi.getAccount(userId, null, null,
		// FinanceConstants.ACC_TYPE_ZHIMI);

		AtsAccount zmacc = new AtsAccount();
		zmacc.setStdId(null);
		zmacc.setUserId(userId);
		zmacc.setEmpId(null);
		zmacc.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		Map<String, String> zmAccount = accountService.getAccount(zmacc);

		String zmAmount = "0";
		if (zmAccount != null) {
			zmAmount = zmAccount.get("accAmount");
			if (StringUtil.isEmpty(zmAmount)) {
				zmAmount = "0";
			} else {
				double x = Double.parseDouble(zmAmount);
				zmAmount = String.valueOf((int) x);
			}
		}
		response.setZmAmount(zmAmount);
		response.setAccAmount(accAmount);
		return JSONObject.fromObject(response);
	}

	/**
	 * 查询当前可用优惠券
	 * 
	 * @param learnId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Object selectAbleCoupon(String learnId) {
		Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
		String userId = null;
		if (StringUtil.hasValue(std.get("userId"))) {
			userId = std.get("userId");
		}
		List<BdCoupon> s = payMapper.selectAbleCouponByLearnId(std.get("stdId"), userId, learnId);
		// Object o = JSONArray.fromObject(s);
		return s;
	}

	/**
	 * 支付学费
	 * 
	 * @param payInfo
	 * @return
	 */
	/*
	 * public Map<String, Object> payFees(BdPayInfo payInfo, String userId) { if
	 * (StringUtil.hasValue(payInfo.getCouponId())) { throw new
	 * BusinessException("E60024"); // 优惠券暂时无法使用 }
	 * 
	 * Map<String, Object> result = new HashMap<String, Object>();
	 * 
	 * BdStudentSerial serial = new BdStudentSerial();
	 * 
	 * // 学业ID String learnId = payInfo.getLearnId();
	 * 
	 * // 订单信息 BdOrder order = payMapper.selectOrder(learnId); Map<String,
	 * String> object = learnMapper.selectLearnInfoByLearnId(learnId); String
	 * grade = object.get("grade"); // 是否为组合支付 String isAssembly =
	 * FinanceConstants.PAYMENT_IS_ASSEMBLY_NO;
	 * 
	 * // 应付金额 List<FeeItemForm> items = payMapper.selectAmountByItems(learnId,
	 * payInfo.getItemCodes()); // 计算应付金额 BigDecimal amount = BigDecimal.ZERO;
	 * for (FeeItemForm amt : items) { BigDecimal amtAmount =
	 * AmountUtil.str2Amount(amt.getAmount()); amount =
	 * BigDecimalUtil.add(amount, amtAmount); if
	 * (FeeConstants.FEE_ITEM_TYPE_COACH.equals(amt.getItemType()) &&
	 * AmountUtil.str2Amount(payInfo.getZmDeduction()).compareTo(BigDecimal.
	 * ZERO) > 0) { throw new BusinessException("E60025"); // 智米不能抵扣辅导费用 } }
	 * 
	 * // 总共优惠金额 BigDecimal couponCount = BigDecimal.ZERO;
	 * 
	 * // 用户滞留账户抵扣金额 BigDecimal accDeduction =
	 * AmountUtil.str2Amount(payInfo.getAccDeduction());
	 * 
	 * // 智米抵扣比例 BigDecimal zmScale =
	 * AmountUtil.str2Amount(sysParameterService.getString(SystemParamConstants.
	 * US_BELONG_PARAM, AppConstants.SYS_PARAM_ZHIMI_SCALE));
	 * 
	 * // 用户智米抵扣金额 BigDecimal zmDeduction =
	 * BigDecimalUtil.divide(AmountUtil.str2Amount(payInfo.getZmDeduction()),
	 * zmScale);
	 * 
	 * if (payInfo.getTradeType().equalsIgnoreCase(FinanceConstants.
	 * TRADE_TYPE_NATIVE)) {
	 * 
	 * SessionInfo session = AppSessionHolder.getSessionInfo(userId,
	 * AppSessionHolder.RPC_SESSION_OPERATOR); List<String> userTypes =
	 * session.getiUserTypes();
	 * 
	 * if (null != userTypes &&
	 * userTypes.contains(UsConstants.I_USER_TYPE_EMPLOYEE)) { if
	 * (BigDecimal.ZERO.compareTo(zmDeduction) < 0) { throw new
	 * BusinessException("E60026"); // 员工无法使用智米 } } }
	 * 
	 * BdCoupon coupon = new BdCoupon();
	 * 
	 * // 动账集合 List<Body> transList = new ArrayList<Body>();
	 * 
	 * // 应付金额 BigDecimal sum = amount; // 如果使用优惠券 if
	 * (StringUtil.hasValue(payInfo.getCouponId())) {
	 * 
	 * // 优惠券信息 coupon =
	 * payMapper.selectCouponAmountToPay(payInfo.getCouponId(), learnId);
	 * 
	 * if (null == coupon) { throw new BusinessException("E60021"); // 无效优惠券 }
	 * 
	 * // 优惠券优惠金额 BigDecimal couponAmount =
	 * AmountUtil.str2Amount(coupon.getAmount());
	 * 
	 * // 循环优惠券优惠科目 for (String itemCode : coupon.getItemCodes()) { // 循环付款科目
	 * for (FeeItemForm amt : items) { // 如果付款与优惠科目对应 、计算优惠金额 if
	 * (amt.getItemCode().equals(itemCode)) { BigDecimal paidAmount =
	 * AmountUtil.str2Amount(amt.getAmount()); if
	 * (couponAmount.compareTo(BigDecimal.ZERO) > 0) { // 如果优惠券抵扣余额大于0
	 * 
	 * if (paidAmount.compareTo(couponAmount) > 0) { // 应缴金额大于优惠金额 couponCount =
	 * couponCount.add(couponAmount); // 优惠金额则加对应优惠金额 couponAmount =
	 * BigDecimal.ZERO; } else { // 应缴金额小于优惠金额 BigDecimal couponBalance =
	 * couponAmount.subtract(paidAmount); couponCount =
	 * couponCount.add(paidAmount); // 优惠金额则加上应付金额 couponAmount = couponBalance;
	 * }
	 * 
	 * } } } }
	 * 
	 * // 组合支付 if (couponCount.compareTo(BigDecimal.ZERO) > 0) { // 优惠券抵扣接大于0
	 * 为组合支付 isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES; }
	 * 
	 * sum = BigDecimalUtil.substract(sum, couponCount); // 应付金额减去优惠金额 if
	 * (sum.compareTo(BigDecimal.ZERO) <= 0) { // 如果优惠券抵扣所有金额 sum =
	 * BigDecimal.ZERO; // 应付金额设为0 zmDeduction = BigDecimal.ZERO; // 智米抵扣设为0
	 * accDeduction = BigDecimal.ZERO; // 现金账户抵扣设为0
	 * 
	 * // 修改优惠券为已使用 payMapper.updateCouponUsed(coupon.getCouponId(),
	 * order.getStdId(), FinanceConstants.COUPON_USED, userId);
	 * 
	 * // 直接完成支付 insertSerial(serial, sum, amount, couponCount, payInfo, order,
	 * isAssembly, learnId, OrderConstants.SERIAL_STATUS_UNCHECK);
	 * 
	 * insertCouponSubSerial(couponCount, coupon, serial.getSerialNo(),
	 * order.getStdId(), OrderConstants.SERIAL_STATUS_UNCHECK);
	 * 
	 * // 完成订单 finishOrder(learnId, payInfo, transList, serial.getSerialNo(),
	 * sum.toString());
	 * 
	 * result.put("resCode", "SUCCESS"); return result; } }
	 * 
	 * // 应缴减去智米抵扣 sum = BigDecimalUtil.substract(sum, zmDeduction); if
	 * (sum.compareTo(BigDecimal.ZERO) <= 0) { // 抵扣所有金额 isAssembly =
	 * FinanceConstants.PAYMENT_IS_ASSEMBLY_YES;
	 * 
	 * accDeduction = BigDecimal.ZERO; // 现金账户抵扣设为0 // 设置智米抵扣 zmDeduction =
	 * BigDecimalUtil.multiply(zmDeduction, zmScale); sum = BigDecimal.ZERO;
	 * 
	 * // 直接完成支付 insertSerial(serial, sum, amount, couponCount, payInfo, order,
	 * isAssembly, learnId, OrderConstants.SERIAL_STATUS_UNCHECK);
	 * 
	 * // 插入优惠券流水 insertCounponSerial(couponCount, coupon, serial,
	 * order.getStdId(), userId, true);
	 * 
	 * // 智米流水插入 insertZhiMiSerial(zmDeduction, serial, order.getStdId());
	 * 
	 * // 动账集合添加 transList.add(amountZmTrans(payInfo.getUserId(),
	 * zmDeduction.toString(), FinanceConstants.ACC_ACTION_OUT,
	 * serial.getSerialNo(), String.format("缴纳%s级学费，智米账户抵扣", grade),
	 * FinanceConstants.ACC_TYPE_ZHIMI));
	 * 
	 * // 完成订单 finishOrder(learnId, payInfo, transList, serial.getSerialNo(),
	 * sum.toString());
	 * 
	 * result.put("resCode", "SUCCESS"); return result; } else if
	 * (zmDeduction.compareTo(BigDecimal.ZERO) < 0) { throw new
	 * BusinessException("E60014"); // 现金抵扣不能小于0 } else if
	 * (zmDeduction.compareTo(BigDecimal.ZERO) > 0) { isAssembly =
	 * FinanceConstants.PAYMENT_IS_ASSEMBLY_YES; }
	 * 
	 * zmDeduction = BigDecimalUtil.multiply(zmDeduction, zmScale);
	 * 
	 * BigDecimal accSum = sum; // 应缴减去现金抵扣 sum = BigDecimalUtil.substract(sum,
	 * accDeduction); if (sum.compareTo(BigDecimal.ZERO) <= 0) { //
	 * 现金抵扣大于应缴，完成订单 isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES;
	 * 
	 * accDeduction = accSum; sum = BigDecimal.ZERO;
	 * 
	 * insertSerial(serial, BigDecimal.ZERO, amount, couponCount, payInfo,
	 * order, isAssembly, learnId, OrderConstants.SERIAL_STATUS_UNCHECK);
	 * 
	 * // 插入优惠券流水 insertCounponSerial(couponCount, coupon, serial,
	 * order.getStdId(), userId, true);
	 * 
	 * // 智米流水插入 insertZhiMiSerial(zmDeduction, serial, order.getStdId());
	 * 
	 * // 插入现金账户支付子流水 insertAccNormalSerial(accDeduction, serial,
	 * order.getStdId());
	 * 
	 * // 如果智米有抵扣，加动账 if (zmDeduction.compareTo(BigDecimal.ZERO) > 0) { //
	 * 动账集合添加 transList .add(amountZmTrans(payInfo.getUserId(),
	 * zmDeduction.toString(), FinanceConstants.ACC_ACTION_OUT,
	 * serial.getSerialNo(), String.format("缴纳%s级学费，智米账户抵扣", grade),
	 * FinanceConstants.ACC_TYPE_ZHIMI)); }
	 * 
	 * // 动账集合添加 transList.add(amountTrans(order.getStdId(),
	 * accDeduction.toString(), FinanceConstants.ACC_ACTION_OUT,
	 * serial.getSerialNo(), String.format("缴纳%s级学费，滞留账户抵扣", grade),
	 * FinanceConstants.ACC_TYPE_DEMURRAGE));
	 * 
	 * // 完成订单 finishOrder(learnId, payInfo, transList, serial.getSerialNo(),
	 * sum.toString());
	 * 
	 * result.put("resCode", "SUCCESS"); return result;
	 * 
	 * } else if (accDeduction.compareTo(BigDecimal.ZERO) < 0) { throw new
	 * BusinessException("E60013"); // 现金抵扣不能小于0 } else if
	 * (accDeduction.compareTo(BigDecimal.ZERO) > 0) { isAssembly =
	 * FinanceConstants.PAYMENT_IS_ASSEMBLY_YES; }
	 * 
	 * // 插入主流水 insertSerial(serial, sum, amount, couponCount, payInfo, order,
	 * isAssembly, learnId, OrderConstants.SERIAL_STATUS_PROCESS);
	 * 
	 * insertSubSerial(sum, serial.getSerialNo(),
	 * OrderConstants.SERIAL_STATUS_PROCESS,
	 * FinanceConstants.PAYMENT_TYPE_WECHAT, null,
	 * FinanceConstants.MONEY_UNIT_RMB);
	 * 
	 * // 插入优惠券流水 insertCounponSerial(couponCount, coupon, serial,
	 * order.getStdId(), userId, false);
	 * 
	 * // 智米流水插入 insertZhiMiSerial(zmDeduction, serial, order.getStdId());
	 * 
	 * // 插入现金账户支付子流水 insertAccNormalSerial(accDeduction, serial,
	 * order.getStdId());
	 * 
	 * // 修改订单状态为支付中 // payMapper.updateSubOrderStatus(learnId,
	 * payInfo.getItemCodes(), // FinanceConstants.ORDER_STATUS_UNPAID);
	 * 
	 * Map<String, String> postData = new HashMap<String, String>();
	 * postData.put("orderAmount", sum.toString()); postData.put("serialNo",
	 * serial.getSerialNo()); postData.put("payType",
	 * FinanceConstants.PAYMENT_TYPE_WECHAT); postData.put("dealType",
	 * FinanceConstants.PAYMENT_DEAL_TYPE_TUITION); postData.put("openId",
	 * payInfo.getOpenId());
	 * 
	 * // 新增-2017-10-10 扫码 postData.put("tradeType", payInfo.getTradeType());
	 * postData.put("productId", serial.getSerialNo()); // 微信支付 result =
	 * paymentService.payment(postData);
	 * 
	 * if (!"SUCCESS".equalsIgnoreCase(result.get("return_code"))) {
	 * log.debug("--------------------------  微信调起失败,serialNo:" +
	 * serial.getSerialNo() + result.toString());
	 * 
	 * throw new BusinessException("E60012"); // 支付掉起失败 }
	 * 
	 * result.put("resCode", "WECHAT");
	 * 
	 * // 保存二维码链接 if (!payInfo.getTradeType().equalsIgnoreCase(FinanceConstants.
	 * TRADE_TYPE_JSAPI)) { String codeUrl = (String) result.get("payinfo");
	 * 
	 * payMapper.updateSerialCodeUrl(serial.getSerialNo(), codeUrl);
	 * 
	 * BdPayInfoDetail detail = payMapper.getPayDetail(serial.getSerialNo()); if
	 * (null != detail) { result.put("amount", detail.getAmount());
	 * result.put("grade", detail.getGrade()); result.put("idCard",
	 * detail.getIdCard()); result.put("mobile", detail.getMobile());
	 * result.put("pfsnLevel", detail.getPfsnLevel()); result.put("pfsnName",
	 * detail.getPfsnName()); result.put("stdName", detail.getStdName());
	 * result.put("taName", detail.getTaName()); result.put("unvsName",
	 * detail.getUnvsName()); result.put("payable", detail.getPayable());
	 * 
	 * } }
	 * 
	 * return result;
	 * 
	 * }
	 */
	private Body amountZmTrans(String userId, String accAmount, String accAction, String serialNo, String excDesc,
			String accType) {

		// 设置转账对象
		Body body = new Body();
		body.put("accType", accType);
		body.put("userId", userId);
		body.put("amount", accAmount);
		body.put("action", accAction);
		body.put("excDesc", excDesc);// TODO 描述补充 2017/7/28 to 倪宇鹏
		body.put("mappingId", serialNo);

		// atsApi.trans(body);
		return body;
	}

	private Body amountTrans(String stdId, String accAmount, String accAction, String serialNo, String excDesc,
			String accType) {

		// 设置转账对象
		Body body = new Body();
		body.put("accType", accType);
		body.put("stdId", stdId);
		body.put("amount", accAmount);
		body.put("action", accAction);
		body.put("excDesc", excDesc);// TODO 描述补充 2017/7/28 to 倪宇鹏
		body.put("mappingId", serialNo);

		// atsApi.trans(body);
		return body;
	}

	private void finishOrder(String learnId, BdPayInfo payInfo, List<Body> transList, String serialNo, String amount) {
		// 修改子订单状态
		payMapper.updateSubOrderPaid(learnId, payInfo.getItemCodes());
		// 完成支付后学员相应状态修改
		// checkPayChange(learnId, payInfo.getItemCodes(), payInfo.getYears());

		String[] itemNames = payMapper.selectItemNameBySerialMark(serialNo);
		String stdName = payMapper.selectStdNameBySerialNo(serialNo);

		/**
		 * 发送微信推送
		 */
		sendWechatMsg(stdName, amount, payInfo.getOpenId(), itemNames, learnId);

		// 动账
		if (transList.size() > 0) {
			// atsApi.transMore(transList);
			accountService.transMore(transList);
		}
	}

	/**
	 * 发送微信推送
	 * 
	 * @param stdName
	 * @param amount
	 * @param openId
	 * @param itemNames
	 * @param learnId
	 */
	private void sendWechatMsg(String stdName, String amount, String openId, String[] itemNames, String learnId) {
		Map<String, String> map = learnMapper.selectTutorAndRecruitUserId(learnId);
		String recruitOpenId = null;
		String tutorOpenId = null;
		if (null != map) {
			if (StringUtil.hasValue(map.get("recruitUserId"))) {
				// Body body = new Body();
				// body.put("userId", map.get("recruitUserId"));
				// Object obj = usApi.getOpenIdByUserId(body);
				Object obj = infoService.getOpenIdByUserId(map.get("recruitUserId"));

				if (null != obj) {
					recruitOpenId = (String) obj;
				}
			}
			if (StringUtil.hasValue(map.get("tutorUserId"))) {
				// Body body = new Body();
				// body.put("userId", map.get("tutorUserId"));
				// Object obj = usApi.getOpenIdByUserId(body);
				Object obj = infoService.getOpenIdByUserId(map.get("tutorUserId"));

				if (null != obj) {
					tutorOpenId = (String) obj;
				}
			}
			// 发送模板通知
			sendTuitionMsg(stdName, amount, null, itemNames, openId, learnId, tutorOpenId, recruitOpenId);
		}

	}

	private void insertCounponSerial(BigDecimal couponCount, BdCoupon coupon, BdStudentSerial serial, String stdId,
			String userId, boolean isFinished) {
		// 如果优惠券抵扣金额大于0，插入优惠券流水
		if (couponCount.compareTo(BigDecimal.ZERO) > 0) {
			insertCouponSubSerial(couponCount, coupon, serial.getSerialNo(), stdId,
					OrderConstants.SERIAL_STATUS_PROCESS);
			if (isFinished) {
				// 修改优惠券为已使用
				payMapper.updateCouponUsed(coupon.getScId(), FinanceConstants.COUPON_USED);
			}
		}
	}

	private void insertZhiMiSerial(BigDecimal zmDeduction, BdStudentSerial serial, String stdId) {
		if (zmDeduction.compareTo(BigDecimal.ZERO) > 0) {
			insertSubSerial(zmDeduction, serial.getSerialNo(), OrderConstants.SERIAL_STATUS_PROCESS,
					FinanceConstants.PAYMENT_TYPE_ZM, FinanceConstants.ACC_TYPE_ZHIMI, FinanceConstants.MONEY_UNIT_ZM);

			// 智米扣除余额
			/*
			 * amountTrans(stdId, zmDeduction.toString(),
			 * FinanceConstants.ACC_ACTION_OUT, serial.getSerialNo(),
			 * "学员缴费智米抵扣", FinanceConstants.ACC_TYPE_ZHIMI);
			 */
		}
	}

	private void insertAccNormalSerial(BigDecimal accDeduction, BdStudentSerial serial, String stdId) {
		if (accDeduction.compareTo(BigDecimal.ZERO) > 0) { // 插入现金账户支付子流水
			insertSubSerial(accDeduction, serial.getSerialNo(), OrderConstants.SERIAL_STATUS_PROCESS,
					FinanceConstants.PAYMENT_TYPE_DELAY, FinanceConstants.ACC_TYPE_DEMURRAGE,
					FinanceConstants.MONEY_UNIT_RMB);
			// 账户扣除余额
			/*
			 * amountTrans(stdId, accDeduction.toString(),
			 * FinanceConstants.ACC_ACTION_OUT, serial.getSerialNo(),
			 * "学员缴费现金抵扣", FinanceConstants.ACC_TYPE_NORMAL);
			 */
		}
	}

	private void insertSerial(BdStudentSerial serial, BigDecimal sum, BigDecimal amount, BigDecimal couponCount,
			BdPayInfo payInfo, BdOrder order, String isAssembly, String learnId, String serialStatus) {

		OaCampusInfo campus = payMapper.selectCampusInfoByLearnId(learnId);

		if (null != campus) {
			serial.setChargePlace(campus.getAddress());
			serial.setFinanceCode(campus.getFinanceNo());
			serial.setCampusName(campus.getCampusName());
		}
		serial.setPayable(sum.toString());
		serial.setAmount(amount.toString());
		serial.setDeduction(couponCount.toString());

		if (FinanceConstants.PAYMENT_IS_ASSEMBLY_YES.equals(isAssembly)) {
			serial.setPaymentType(FinanceConstants.PAYMENT_TYPE_GROUP);
		} else {
			serial.setPaymentType(payInfo.getPaymentType());
		}

		serial.setSerialStatus(serialStatus);
		serial.setMobile(order.getMobile());
		serial.setOrderNo(order.getOrderNo());
		serial.setStdId(order.getStdId());
		serial.setStdName(order.getStdName());
		serial.setIsAssembly(isAssembly);
		serial.setSerialNo(IDGenerator.generatororderNo(serial.getFinanceCode()));
		String userId = payMapper.selectUserId(order.getStdId());
		serial.setUserId(userId);

		String[] subOrderNos = payMapper.selectPaySubOrderNos(learnId, payInfo.getItemCodes());

		// 插入流水
		payMapper.insertOrderSerial(serial, subOrderNos);
	}

	private void insertSubSerial(BigDecimal sum, String serialNo, String serialStatus, String paymentType,
			String accType, String unit) {
		BdSubSerial serial = new BdSubSerial();
		serial.setAccType(accType);
		serial.setAmount(sum.toString());
		serial.setPaymentType(paymentType);
		serial.setSerialNo(serialNo);
		serial.setSubSerialStatus(serialStatus);
		serial.setUnit(unit);
		payMapper.insertSubSerial(serial);
	}

	private void insertCouponSubSerial(BigDecimal couponCount, BdCoupon coupon, String serialNo, String stdId,
			String serialStatus) {
		BdSubSerial cSerial = new BdSubSerial();
		cSerial.setAmount(couponCount.toString());
		cSerial.setCouponId(coupon.getCouponId());
		String scId = payMapper.selectScId(coupon.getCouponId(), stdId);
		cSerial.setScId(scId);
		cSerial.setCouponName(coupon.getCouponName());
		cSerial.setPaymentType(FinanceConstants.PAYMENT_TYPE_COUPON);
		cSerial.setSerialNo(serialNo);
		cSerial.setUnit(FinanceConstants.MONEY_UNIT_RMB);
		cSerial.setSubSerialStatus(serialStatus);
		payMapper.insertSubSerial(cSerial);
	}

	public void checkPayChange(String learnId, List<String> itemCodes, List<String> years) {

		for (String year : years) {
			if (FinanceConstants.FEE_ITEM_YEAR_FIRST.equals(year)) {
				payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_REGISTER);
				payMapper.insertFirstRegist(learnId, null, null);
			}
			sendService.sendBook(learnId, year, false);
		}
		Map<String, String> learnMap = learnMapper.selectLearnInfoByLearnId(learnId);
		if (null != learnMap) {
			String stdStage = learnMap.get("stdStage");
			if (StudentConstants.STD_STAGE_PURPOSE.equals(stdStage)) {
				for (String itemCode : itemCodes) {
					BdFeeItem item = feeItemMapper.selectItemInfoById(itemCode);
					if (FeeConstants.FEE_ITEM_TYPE_COACH.equals(item.getItemType())) {
						payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_HELPING);
						sendService.sendBook(learnId, null, true);
					}
				}
			}
		}

	}

	public Object selectWithdrawSerial(String stdId) {

		return payMapper.selectWithdrawSerial(stdId);
	}

	/**
	 * 获取微信jsapi签名
	 * 
	 * @param url
	 * @return
	 */
	public Map<String, String> jsapiSign(String url) {
		return wechatService.jsapiSign(url);
	}

	/**
	 * 缴费成功推送微信公众号提醒信息
	 * 
	 * @param stdName
	 * 
	 * @param amount
	 * @param serialNo
	 * @param itemNames
	 * @param learnId
	 * @param openId
	 * @param tutorOpenId
	 * @param recruitOpenId
	 */
	public void sendTuitionMsg(String stdName, String amount, String serialNo, String[] itemNames, String learnId,
			String openId, String tutorOpenId, String recruitOpenId) {
		WechatMsgVo msgVo = new WechatMsgVo();
		msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_TUITION);
		msgVo.addData("stdName", stdName);
		msgVo.addData("amount", amount);
		msgVo.addData("now", DateUtil.getNowDateAndTime());
		msgVo.addData("firstWord", "恭喜您，缴费成功！\n");
		String item = "";
		for (String itemName : itemNames) {
			item = item + itemName + ",";
		}
		if (StringUtil.hasValue(item)) {
			item = item.substring(0, item.length() - 1);
		}
		msgVo.addData("item", item);
		if (StringUtil.hasValue(openId)) { // 给当前缴费学员推送微信公众号信息
			msgVo.setExt1(learnId);
			msgVo.setTouser(openId);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}
		msgVo.addData("firstWord", "学员缴费通知");
		if (StringUtil.hasValue(tutorOpenId)) { // 辅导费给辅导员推送公众号信息
			msgVo.setTouser(tutorOpenId);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}

		if (StringUtil.hasValue(recruitOpenId)) { // 给招生老师推送公众号信息
			msgVo.setTouser(recruitOpenId);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}
	}
}
