package com.yz.service.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.api.BdsPaymentApi;
import com.yz.api.UsInfoApi;
import com.yz.constants.FeeConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdStdRegisterMapper;
import com.yz.dao.finance.AtsAccountMapper;
import com.yz.dao.finance.BdFeeItemMapper;
import com.yz.dao.finance.BdStdPayFeeMapper;
import com.yz.dao.finance.StudentMpFlowMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.dao.us.UsFollowLogMapper;
import com.yz.dao.us.UsFollowMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.communi.Body;
import com.yz.model.finance.AtsAccount;
import com.yz.model.finance.BdOrder;
import com.yz.model.finance.BdSubOrder;
import com.yz.model.finance.coupon.BdCoupon;
import com.yz.model.finance.feeitem.BdFeeItem;
import com.yz.model.finance.stdfee.BdPayInfo;
import com.yz.model.finance.stdfee.BdPayableInfoResponse;
import com.yz.model.finance.stdfee.BdPayableQuery;
import com.yz.model.finance.stdfee.BdQRCodePayableInfoResponse;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.finance.stdfee.BdStudentSerial;
import com.yz.model.oa.OaEmployeeBaseInfo;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.us.UsFollow;
import com.yz.model.us.UsFollowLog;
import com.yz.redis.RedisService;
import com.yz.service.educational.BdStudentSendService;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.task.YzTaskConstants;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 学员缴费 Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年6月13日.
 *
 */
@Service
@Transactional
public class BdStdPayFeeService {

	private static Logger log = LoggerFactory.getLogger(BdStdPayFeeService.class);

	@Autowired
	private BdStdPayFeeMapper payMapper;

	@Autowired
	private BdFeeItemMapper itemMapper;

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;

	@Autowired
	private AtsAccountMapper accountMapper;

	@Autowired
	private BdStudentSendService sendService;

	@Autowired
	private StudentMpFlowMapper flowMapper;

	@Reference(version = "1.0")
	private UsInfoApi usApi;

	@Reference(version = "1.0")
	private BdsPaymentApi bdsPaymentApi;

	@Autowired
	private BdStudentModifyMapper studentModifyMapper;

	@Autowired
	private StudentRecruitService recruitService;

	@Reference(version = "1.0")
	private AtsAccountApi atdApi;

	@Autowired
	private BdStudentModifyService modifyService;

	@Autowired
	private BdTuitionService tuitionService;

	@Autowired
	private BdStdPayFeeService payService;

	/**
	 * 学员缴费信息分页查询
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object selectStdFeePayPage(int start, int length, BdPayableQuery query) {
		PageHelper.offsetPage(start, length);
		List<BdPayableInfoResponse> stdFee = payMapper.selectStdPayFeeByPage(query);

		if (stdFee != null) {
			for (BdPayableInfoResponse response : stdFee) {
				AtsAccount account = new AtsAccount();
				account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
				account.setStdId(response.getStdId());

				account = accountMapper.getAccount(account);

				String accAmount = "0.00";
				if (account != null) {
					accAmount = account.getAccAmount();
					if (StringUtil.isEmpty(accAmount)) {
						accAmount = "0.00";
					}
				}
				response.setAccAmount(accAmount);

				// 根据条件获取当前配置的收费标准
				String nowFeeId = studentModifyMapper.selectFeeStandard(response.getPfsnId(), response.getTaId(),
						response.getScholarship());
				response.setNowFeeId(nowFeeId);

				String feeName = payMapper.selectFeeNameByLearnId(response.getLearnId());
				response.setFeeName(feeName);

				String offerName = payMapper.selectOfferNameByLearnId(response.getLearnId());
				response.setOfferName(offerName);

			}

		}

		return new IPageInfo((Page) stdFee);
	}

	public Map<String, String> selectPayableInfoByStdId(String stdId) {
		Map<String, String> stdInfo = payMapper.selectStdInfoByStdId(stdId);

		AtsAccount account = new AtsAccount();
		account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
		account.setStdId(stdInfo.get("stdId"));

		account = accountMapper.getAccount(account);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.getAccAmount();
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}

		AtsAccount zmAccount = new AtsAccount();
		zmAccount.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		zmAccount.setStdId(stdInfo.get("stdId"));
		zmAccount = accountMapper.getAccount(zmAccount);
		String zmAmount = "0";
		if (zmAccount != null) {
			zmAmount = zmAccount.getAccAmount();
			if (StringUtil.isEmpty(zmAmount)) {
				zmAmount = "0";
			} else {
				double x = Double.parseDouble(zmAmount);
				zmAmount = String.valueOf((int) x);
			}
		}
		stdInfo.put("accAmount", accAmount);
		stdInfo.put("zmAmount", zmAmount);
		return stdInfo;
	}

	public BdPayableInfoResponse selectPayableInfoByLearnId(String learnId, String subOrderStatus) {
		BdPayableInfoResponse response = payMapper.selectPayableInfoByLearnId(learnId, subOrderStatus);

		AtsAccount account = new AtsAccount();
		account.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
		account.setStdId(response.getStdId());

		account = accountMapper.getAccount(account);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.getAccAmount();
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}

		AtsAccount zmAccount = new AtsAccount();
		zmAccount.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		zmAccount.setStdId(response.getStdId());
		zmAccount = accountMapper.getAccount(zmAccount);
		String zmAmount = "0";
		if (zmAccount != null) {
			zmAmount = zmAccount.getAccAmount();
			if (StringUtil.isEmpty(zmAmount)) {
				zmAmount = "0";
			} else {
				double x = Double.parseDouble(zmAmount);
				zmAmount = String.valueOf((int) x);
			}
		}

		response.setAccAmount(accAmount);
		response.setZmAmount(zmAmount);
		return response;
	}

	/**
	 * 查询对应状态的缴费科目信息
	 * 
	 * @param learnId
	 * @param subOrderStatus
	 * @return
	 */
	public Object selectPayableInfo(String learnId, String subOrderStatus) {
		return payMapper.selectPayableInfo(learnId, subOrderStatus);
	}

	/**
	 * 学员缴费
	 * 
	 * @param payInfo
	 * @param serial
	 */
	@SuppressWarnings("unchecked")
	/*
	 * public Object payFees(BdPayInfo payInfo, boolean isPrint) {
	 * 
	 * BdStudentSerial serial = new BdStudentSerial(); BaseUser user =
	 * SessionUtil.getUser();
	 * 
	 * serial.setEmpId(user.getUserId()); serial.setEmpName(user.getRealName());
	 * serial.setUpdateUser(user.getRealName());
	 * serial.setUpdateUserId(user.getUserId());
	 * serial.setCreateUser(user.getRealName());
	 * serial.setCreateUserId(user.getUserId()); // 实缴金额 BigDecimal paid =
	 * AmountUtil.str2Amount(payInfo.getPaidAmount());
	 * 
	 * if (paid.compareTo(BigDecimal.ZERO) < 0) { throw new
	 * BusinessException("E000052"); // 实付金额不能为负数 }
	 * 
	 * String learnId = payInfo.getLearnId();
	 * 
	 * BdOrder order = payMapper.selectOrder(learnId);
	 * 
	 * String isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_NO;
	 * 
	 * // 应付金额 List<FeeItemForm> items = payMapper.selectAmountByItems(learnId,
	 * payInfo.getItemCodes()); BigDecimal amount = BigDecimal.ZERO; for
	 * (FeeItemForm amt : items) { BigDecimal amtAmount =
	 * AmountUtil.str2Amount(amt.getAmount()); amount =
	 * BigDecimalUtil.add(amount, amtAmount); } // 总共优惠金额 BigDecimal couponCount
	 * = BigDecimal.ZERO;
	 * 
	 * BdCoupon coupon = new BdCoupon();
	 * 
	 * // 智米抵扣比例 BigDecimal zmScale =
	 * AmountUtil.str2Amount(SysParamUtil.getString(AppConstants.
	 * SYS_PARAM_ZHIMI_SCALE));
	 * 
	 * // 用户智米抵扣金额 BigDecimal zmDeduction =
	 * BigDecimalUtil.divide(AmountUtil.str2Amount(payInfo.getZmDeduction()),
	 * zmScale);
	 * 
	 * // 用户滞留账户抵扣金额 BigDecimal accDeduction =
	 * AmountUtil.str2Amount(payInfo.getAccDeduction());
	 * 
	 * // 应付金额 BigDecimal sum = amount; // 如果使用优惠券 if
	 * (StringUtil.hasValue(payInfo.getCouponId())) { // 优惠券信息 coupon =
	 * payMapper.selectCouponAmountToPay(payInfo.getCouponId(), learnId);
	 * 
	 * if (null == coupon) { throw new BusinessException("E000068"); // 无效优惠券 }
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
	 * if (paidAmount.compareTo(couponAmount) > 0) { // 应缴金额大于优惠金额
	 * 
	 * couponCount = couponCount.add(couponAmount); // 优惠金额则加对应优惠金额 couponAmount
	 * = BigDecimal.ZERO;
	 * 
	 * } else { // 应缴金额小于优惠金额 BigDecimal couponBalance =
	 * couponAmount.subtract(paidAmount); couponCount =
	 * couponCount.add(paidAmount); // 优惠金额则加上应付金额 couponAmount = couponBalance;
	 * } } } } } sum = BigDecimalUtil.substract(sum, couponCount); // 应付金额减去优惠金额
	 * if (sum.compareTo(BigDecimal.ZERO) < 0) { sum = BigDecimal.ZERO;
	 * zmDeduction = BigDecimal.ZERO; // 智米抵扣设为0 accDeduction = BigDecimal.ZERO;
	 * // 现金账户抵扣设为0 } payMapper.updateCouponUsed(coupon.getCouponId(),
	 * order.getStdId()); isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES;
	 * }
	 * 
	 * sum = BigDecimalUtil.substract(sum, zmDeduction); // 应付金额减去智米抵扣
	 * 
	 * if (sum.compareTo(BigDecimal.ZERO) > 0 &&
	 * zmDeduction.compareTo(BigDecimal.ZERO) > 0) { // 如果减去智米的应付金额大于0,并且智米大于0
	 * isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES; } else if
	 * (sum.compareTo(BigDecimal.ZERO) <= 0 &&
	 * zmDeduction.compareTo(BigDecimal.ZERO) > 0) {// 如果应缴小于等于0，智米大于0，智米抵扣所有应缴
	 * isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES; sum =
	 * BigDecimal.ZERO; } zmDeduction = BigDecimalUtil.multiply(zmDeduction,
	 * zmScale);
	 * 
	 * // 用户实付金额 + 滞留账户金额 BigDecimal paidAccAmount =
	 * paid.add(accDeduction).setScale(2, BigDecimal.ROUND_DOWN);
	 * 
	 * // 用户多缴纳金额 BigDecimal outCount = BigDecimalUtil.substract(paid, sum);
	 * 
	 * // 实付 应付校验 if (sum.compareTo(paidAccAmount) > 0) { throw new
	 * BusinessException("E000045"); // 实付金额不能少于应付金额 }
	 * 
	 * if (outCount.compareTo(BigDecimal.ZERO) < 0) { // 金额多缴，为组合支付 isAssembly =
	 * FinanceConstants.PAYMENT_IS_ASSEMBLY_YES; }
	 * 
	 * // 滞留账户 进出账 状态 String accAction = null; // 滞留账户操作金额 String accAmount =
	 * null;
	 * 
	 * serial.setPayable(paid.toString()); serial.setAmount(amount.toString());
	 * serial.setDeduction(couponCount.toString());
	 * serial.setPaymentType(payInfo.getPaymentType()); if
	 * (StringUtil.hasValue(payInfo.getOutSerialNo())) {
	 * serial.setOutSerialNo(payInfo.getOutSerialNo()); }
	 * serial.setSerialStatus(OrderConstants.SERIAL_STATUS_UNCHECK);
	 * serial.setMobile(order.getMobile());
	 * serial.setOrderNo(order.getOrderNo()); serial.setStdId(order.getStdId());
	 * serial.setStdName(order.getStdName()); serial.setIsAssembly(isAssembly);
	 * 
	 * String userId = payMapper.selectUserId(order.getStdId());
	 * serial.setUserId(userId);
	 * 
	 * String[] subOrderNos = payMapper.selectPaySubOrderNos(learnId,
	 * payInfo.getItemCodes());
	 * 
	 * OaCampusInfo campus = payMapper.selectCampusInfoByEmpId(user.getEmpId());
	 * 
	 * if (null != campus) { serial.setChargePlace(campus.getAddress());
	 * serial.setFinanceCode(campus.getFinanceNo());
	 * serial.setCampusName(campus.getCampusName()); } else {
	 * serial.setFinanceCode("YZ"); }
	 * 
	 * // 记录付款前滞留账户余额 String beforeAmount = null; Map<String, String> beforeMap
	 * = accountApi.getAccount(null, order.getStdId(), null,
	 * FinanceConstants.ACC_TYPE_DEMURRAGE); if (null != beforeMap) {
	 * beforeAmount = beforeMap.get("accAmount"); }
	 * 
	 * serial.setDemurrageBefore(beforeAmount);
	 * 
	 * // 插入流水 payMapper.insertOrderSerial(serial, subOrderNos);
	 * 
	 * BdSubSerial cash = new BdSubSerial(); cash.setAmount(paid.toString());
	 * cash.setPaymentType(FinanceConstants.PAYMENT_TYPE_CASH);
	 * cash.setSerialNo(serial.getSerialNo());
	 * cash.setSubSerialStatus(OrderConstants.SERIAL_STATUS_FINISHED);
	 * cash.setUnit(FinanceConstants.MONEY_UNIT_RMB);
	 * payMapper.insertSubSerial(cash);
	 * 
	 * // 如果优惠券抵扣金额大于0，插入优惠券流水 if (couponCount.compareTo(BigDecimal.ZERO) > 0) {
	 * BdSubSerial cSerial = new BdSubSerial();
	 * cSerial.setAmount(couponCount.toString());
	 * cSerial.setCouponId(coupon.getCouponId()); String scId =
	 * payMapper.selectScId(coupon.getCouponId(), order.getStdId());
	 * cSerial.setScId(scId); cSerial.setCouponName(coupon.getCouponName());
	 * cSerial.setPaymentType(FinanceConstants.PAYMENT_TYPE_COUPON);
	 * cSerial.setSerialNo(serial.getSerialNo());
	 * cSerial.setUnit(FinanceConstants.MONEY_UNIT_RMB);
	 * cSerial.setSubSerialStatus(OrderConstants.SERIAL_STATUS_FINISHED);
	 * payMapper.insertSubSerial(cSerial); }
	 * 
	 * if (outCount.compareTo(BigDecimal.ZERO) > 0) { // 如果用户多缴纳，往滞留账户进账
	 * accAmount = String.valueOf(outCount); accAction =
	 * FinanceConstants.ACC_ACTION_IN; } else if
	 * (outCount.compareTo(BigDecimal.ZERO) < 0) { // 用户滞留金抵扣，滞留账户出账 accAmount =
	 * BigDecimalUtil.multiply(outCount.toString(), "-1"); accAction =
	 * FinanceConstants.ACC_ACTION_OUT;
	 * 
	 * BdSubSerial delay = new BdSubSerial();
	 * delay.setAccType(FinanceConstants.ACC_TYPE_DEMURRAGE);
	 * delay.setAmount(accAmount.toString());
	 * delay.setPaymentType(FinanceConstants.PAYMENT_TYPE_DELAY);
	 * delay.setSerialNo(serial.getSerialNo());
	 * delay.setUnit(FinanceConstants.MONEY_UNIT_RMB);
	 * delay.setSubSerialStatus(OrderConstants.SERIAL_STATUS_FINISHED);
	 * payMapper.insertSubSerial(delay); }
	 * 
	 * List<Body> transList = new ArrayList<Body>();
	 * 
	 * if (zmDeduction.compareTo(BigDecimal.ZERO) > 0) { // 智米抵扣大于0，智米出账
	 * 
	 * // 插入子流水 BdSubSerial delay = new BdSubSerial();
	 * delay.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
	 * delay.setAmount(zmDeduction.toString());
	 * delay.setPaymentType(FinanceConstants.PAYMENT_TYPE_ZM);
	 * delay.setSerialNo(serial.getSerialNo());
	 * delay.setUnit(FinanceConstants.MONEY_UNIT_ZM);
	 * delay.setSubSerialStatus(OrderConstants.SERIAL_STATUS_FINISHED);
	 * payMapper.insertSubSerial(delay);
	 * 
	 * // 设置转账对象 Body body = new Body(); body.put("accType",
	 * FinanceConstants.ACC_TYPE_ZHIMI); body.put("stdId", order.getStdId());
	 * body.put("amount", zmDeduction.toString()); body.put("action",
	 * FinanceConstants.ACC_ACTION_OUT); body.put("excDesc", "学员缴费 智米抵扣");//
	 * TODO 描述补充 2017/7/28 to 倪宇鹏 body.put("mappingId", serial.getSerialNo());
	 * body.put("updateUser", serial.getUpdateUser()); body.put("updateUserId",
	 * serial.getUpdateUserId());
	 * 
	 * transList.add(body); // accountApi.trans(body); }
	 * 
	 *//**
		 * 滞留账户扣款
		 *//*
		 * if (StringUtil.hasValue(accAction)) { // 设置转账对象 Body body = new
		 * Body(); body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
		 * body.put("stdId", order.getStdId()); body.put("amount", accAmount);
		 * body.put("action", accAction); body.put("excDesc", "学员缴费 滞留账户扣款");//
		 * TODO 描述补充 2017/7/28 to 倪宇鹏 body.put("mappingId",
		 * serial.getSerialNo()); body.put("updateUser",
		 * serial.getUpdateUser()); body.put("updateUserId",
		 * serial.getUpdateUserId()); transList.add(body); //
		 * accountApi.trans(body); }
		 * 
		 * // 修改子订单状态 payMapper.updateSubOrderPaid(learnId,
		 * payInfo.getItemCodes());
		 * 
		 * 
		 * checkPayChange(learnId, payInfo.getItemCodes(),
		 * serial.getUpdateUser(), serial.getUpdateUserId(),
		 * payInfo.getYears());
		 * 
		 * 
		 * Map<String, String> result = new HashMap<String, String>(); if
		 * (isPrint) { // 需要打印收据，插入收据管理信息 result.put("stdId", order.getStdId());
		 * result.put("learnId", payInfo.getLearnId()); result.put("serialMark",
		 * serial.getSerialNo()); result.put("stdName", order.getStdName());
		 * String reptId = reptService.insertStudentRept(result);
		 * result.put("reptId", reptId); }
		 * 
		 * if (transList.size() > 0) { accountApi.transMore(transList); }
		 * 
		 * // 记录付款完成滞留账户余额 String afterAmount = null; Map<String, String>
		 * afterMap = accountApi.getAccount(null, order.getStdId(), null,
		 * FinanceConstants.ACC_TYPE_DEMURRAGE); if (null != afterMap) {
		 * afterAmount = afterMap.get("accAmount"); }
		 * serial.setDemurrageAfter(afterAmount);
		 * payMapper.updateAfterAmount(serial);
		 * 
		 * sendWechatMsg(serial.getSerialNo(), amount.toString(), learnId);
		 * 
		 * return result;
		 * 
		 * }
		 */

	/**
	 * 发送微信推送
	 * 
	 * @param stdName
	 * @param amount
	 * @param openId
	 * @param itemNames
	 * @param learnId
	 */
	private void sendWechatMsg(String serialNo, String amount, String learnId) {

		String[] itemNames = payMapper.selectItemNameBySerialNo(serialNo);
		String stdName = payMapper.selectStdNameBySerialNo(serialNo);

		Map<String, String> map = payMapper.selectTutorAndRecruitUserId(learnId);
		String recruitOpenId = null;
		String tutorOpenId = null;
		String openId = null;
		if (null != map) {
			if (StringUtil.hasValue(map.get("stdUserId"))) {
				Body body = new Body();
				body.put("userId", map.get("stdUserId"));
				Object obj = usApi.getOpenIdByUserId(body);
				if (null != obj) {
					openId = (String) obj;
				}
			}
			if (StringUtil.hasValue(map.get("recruitUserId"))) {
				Body body = new Body();
				body.put("userId", map.get("recruitUserId"));
				Object obj = usApi.getOpenIdByUserId(body);
				if (null != obj) {
					recruitOpenId = (String) obj;
				}
			}
			if (StringUtil.hasValue(map.get("tutorUserId"))) {
				Body body = new Body();
				body.put("userId", map.get("tutorUserId"));
				Object obj = usApi.getOpenIdByUserId(body);
				if (null != obj) {
					tutorOpenId = (String) obj;
				}
			}
			// 发送模板通知
			sendTuitionMsg(stdName, amount, null, itemNames, openId, learnId, tutorOpenId, recruitOpenId);
		}

	}

//	private List<CommunicationMap> chargeAward(String learnId, String[] itemCodes, String[] itemYears) {
//		List<CommunicationMap> mList = new ArrayList<CommunicationMap>();
//		MpCondition condition = flowMapper.getCondition(learnId);
//
//		String stdId = condition.getString("stdId");
//
//		List<Map<String, String>> learnList = flowMapper.getHistoryLearn(stdId, learnId);
//
//		condition.put("learnList", learnList);
//		condition.put("itemCodes", itemCodes);
//		condition.put("itemYears", itemYears);
//		// 个人缴费赠送流程
//		Flow iChargeFlow = MpContext.getiChargeFlow();
//		// 上线缴费赠送流程
//		Flow chargeFlow = MpContext.getChargeFlow();
//
//		MpResult iResult = null;
//		MpResult result = null;
//
//		if (iChargeFlow != null) {
//			iResult = iChargeFlow.match(condition);
//		}
//
//		if (chargeFlow != null) {
//			result = chargeFlow.match(condition);
//		}
//
//		if (iResult != null && iResult.hasValue()) {
//			mList.add(iResult.getTarget());
//		}
//
//		if (result != null && result.hasValue()) {
//			mList.add(result.getTarget());
//		}
//
//		return mList;
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object payDetail(int start, int length, String learnId) {
		PageHelper.offsetPage(start, length);
		List<BdStudentSerial> serials = payMapper.selectPayDetailByLearnId(learnId);
		return new IPageInfo((Page) serials);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object stdPayDetail(int start, int length, String stdId) {
		PageHelper.offsetPage(start, length);
		List<BdStudentSerial> serials = payMapper.selectPayDetailByStdId(stdId);
		return new IPageInfo((Page) serials);
	}

	public String getUserId(String stdId) {
		String userId = payMapper.getUserId(stdId);
		/*
		 * if (!StringUtil.hasValue(userId)) { throw new
		 * BusinessException("E000091"); // 学员暂未关联智米账户 }
		 */
		return userId;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findStudentInfo(int rows, int page, String sName) {
		PageHelper.startPage(page, rows);
		List<HashMap<String, String>> list = payMapper.findStudentInfo(sName);
		return new IPageInfo((Page) list);
	}

	public void addPayableItem(String itemCode, String amount, String learnId) {
		BdOrder order = payMapper.selectOrder(learnId);
		BdSubOrder subOrder = new BdSubOrder();
		subOrder.setItemCode(itemCode);
		subOrder.setFeeAmount(amount);
		subOrder.setMobile(order.getMobile());
		subOrder.setOrderNo(order.getOrderNo());
		subOrder.setPayable(amount);
		subOrder.setStdId(order.getStdId());
		subOrder.setStdName(order.getStdName());
		subOrder.setSubOrderStatus(FinanceConstants.ORDER_STATUS_UNPAID);
		subOrder.setSubOrderNo(IDGenerator.generatorId());
		payMapper.addPayableItem(subOrder);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findItemCodeHaveNot(int rows, int page, String sName, String learnId) {
		PageHelper.startPage(page, rows);

		List<BdStdPayInfoResponse> list = payMapper.findItemCodeHaveNot(sName, learnId);

		return new IPageInfo((Page) list);
	}

	@Autowired
	private BdStdRegisterMapper regMapper;

	public void checkPayChange(String learnId, String[] itemCodes, String updateUser, String updateUserId,
			String[] years) {

		/*
		 * // 判断是否为辅导费 if (StringUtil.hasValue(itemCodes,
		 * FinanceConstants.FEE_ITEM_CODE_Y0)) { // 辅导费缴纳，学员阶段变更为考前辅导
		 * 
		 * String subOrderStatus =
		 * payMapper.selectSubOrderStatusByItemCode(learnId,
		 * FinanceConstants.FEE_ITEM_CODE_Y0); if
		 * (FinanceConstants.ORDER_SATUS_UNPAID .equals(subOrderStatus)) {
		 * 
		 * payMapper.updateStdStage(learnId,
		 * StudentConstants.STD_STAGE_HELPING);
		 * 
		 * } else { throw new BusinessException("E000047"); // 订单状态异常 }
		 * 
		 * }
		 */

		String recruitType = payMapper.selectRecruitType(learnId);

		String stdStage = payMapper.selectStdStageByLearnId(learnId);

		if (null != years && years.length > 0) {
			for (String year : years) {
				if (FinanceConstants.FEE_ITEM_YEAR_FIRST.equals(year)) {
					if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
						if (StudentConstants.STD_STAGE_ENROLLED.equals(stdStage)) {
							payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_REGISTER);
							List<Map<String, String>> list = regMapper.selectTmpAddRecord(learnId, updateUser, updateUserId);
							for (Map<String, String> map : list) {
								map.put("register_id", IDGenerator.generatorId());
							}
							regMapper.insertFirstRegist(list);
						}
					} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
						if (StudentConstants.STD_STAGE_PURPOSE.equals(stdStage)) {
							payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_HELPING);
							// 修改学业辅导费审核时间
							payMapper.updateTutionTime(learnId);
						}
					}
				}

				// 发书
				for (String itemCode : itemCodes) {
					BdFeeItem item = itemMapper.selectItemInfoById(itemCode);
					if (FeeConstants.FEE_ITEM_TYPE_BOOK.equals(item.getItemType())) {
						sendService.sendBook(learnId, year, false);
					}
				}
			}
		}

		if (StudentConstants.STD_STAGE_PURPOSE.equals(stdStage)
				&& StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
			for (String itemCode : itemCodes) {
				BdFeeItem item = itemMapper.selectItemInfoById(itemCode);
				if (FeeConstants.FEE_ITEM_TYPE_COACH.equals(item.getItemType())) {
					payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_HELPING);
					//sendService.sendBook(learnId, null, true);
					// 修改学业辅导费审核时间
					payMapper.updateTutionTime(learnId);
					break;
				}
			}
		}
	}

	@Autowired
	private StudentAllMapper stdMapper;

	@Autowired
	private UsFollowMapper followMapper;

	@Autowired
	private UsFollowLogMapper fLogMapper;

	@Autowired
	private OaEmployeeMapper empMapper;

	private void refreshFollow(String learnId) {
		Map<String, String> recruit = stdMapper.getRecruitInfo(learnId);

		String mobile = recruit.get("mobile");

		String empId = recruit.get("empId");
		String dpId = recruit.get("dpId");
		String campusId = recruit.get("campusId");

		String userId = followMapper.selectUserIdByMobile(mobile);

		if (StringUtil.hasValue(empId)) {
			OaEmployeeBaseInfo empInfo = empMapper.getEmpBaseInfo(empId);

			if (empInfo != null) {
				// 如果员工离职了
				if (!"2".equals(empInfo.getEmpStatus())) {

					UsFollow follow = followMapper.selectByPrimaryKey(userId);

					if (follow == null) {
						follow = new UsFollow();
						follow.setUserId(userId);
						follow.setEmpId(empId);
						follow.setDpId(dpId);
						follow.setCampusId(campusId);
						followMapper.insertSelective(follow);
					} else {
						UsFollowLog fLog = new UsFollowLog();
						fLog.setOldEmpId(follow.getEmpId());
						fLog.setOldDpId(follow.getDpId());
						fLog.setOldCampusId(follow.getCampusId());
						fLog.setEmpId(empId);
						fLog.setDpId(dpId);
						fLog.setCampusId(campusId);
						fLog.setUserId(userId);
						fLog.setRecrodsNo(IDGenerator.generatorId());
						fLogMapper.insertSelective(fLog);

						follow.setEmpId(empId);
						follow.setDpId(dpId);
						follow.setCampusId(campusId);
						followMapper.updateByPrimaryKeySelective(follow);
					}
				}
			}
		}

		Map<String, String> enrollLog = stdMapper.getEnrollInfo(learnId);
		enrollLog.put("userId", userId);
		enrollLog.put("enrollId", IDGenerator.generatorId());
		followMapper.inertEnrollLog(enrollLog);
	}

	/**
	 * 学业ID查询拥有优惠券
	 * 
	 * @param learnId
	 * @return
	 */
	public Object selectCoupon(String learnId) {
		Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
		String userId = null;
		if(StringUtil.hasValue(std.get("userId"))){
			userId = std.get("userId");
		}
		List<BdCoupon> list = payMapper.selectAbleCouponByLearnId(std.get("stdId"),userId,learnId);
		return list;
	}

	public int selectCouponCount(String learnId) {
		Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
		String userId = null;
		if(StringUtil.hasValue(std.get("userId"))){
			userId = std.get("userId");
		}
		return payMapper.selectAbleCouponByLearnId(std.get("stdId"),userId,learnId).size();
	}

	public Object selectCouponAmount(String couponId) {

		return payMapper.selectCouponAmount(couponId);
	}

	public List<BdQRCodePayableInfoResponse> selectStdPayFeeByCondition(String condition, String empId) {
		return payMapper.selectStdPayFeeByCondition(condition, empId);
	}

	public BdPayableInfoResponse selectPayableInfoByLearnIdForQRCode(String learnId, String subOrderStatus) {
		BdPayableInfoResponse response = payMapper.selectPayableInfoByLearnId(learnId, subOrderStatus);
		Map<String, String> account = accountApi.getAccount(null, response.getStdId(), null,
				FinanceConstants.ACC_TYPE_DEMURRAGE);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.get("accAmount");
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}
		response.setAccAmount(accAmount);
		return response;
	}

	@SuppressWarnings("unchecked")
	public Object scanQRCodePay(BdPayInfo payInfo) {
		Body body = new Body();
		body.put("learnId", payInfo.getLearnId());
		body.put("coupons", payInfo.getCouponsStr());
		body.put("itemCodes", payInfo.getItemCodes());
		body.put("paymentType", payInfo.getPaymentType());
		body.put("years", payInfo.getYears());
		body.put("zmDeduction", payInfo.getZmDeduction());
		body.put("accDeduction", payInfo.getAccDeduction());
		body.put("tradeType", payInfo.getTradeType());
		body.put("empId", payInfo.getEmpId());
		return bdsPaymentApi.stdPayTuitionByQRCode(body);
	}

	public Object zmDetail(int start, int length, String stdId, String type) {
		String userId = payMapper.selectUserId(stdId);
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> list = accountMapper.getStudentAccountSerial(type, userId, stdId);
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) list);
	}
	
	public Object allSerials(int start, int length, String stdId) {
		String userId = payMapper.selectUserId(stdId);
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> list = accountMapper.getStudentAllAccountSerial(userId, stdId);
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) list);
	}

	public Object studentCoupon(String learnId) {
		Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
		String userId = null;
		if(StringUtil.hasValue(std.get("userId"))){
			userId = std.get("userId");
		}
		List<BdCoupon> s = payMapper.selectAbleCouponByLearnId(std.get("stdId"),userId,learnId);
		//Object o = JSONArray.fromObject(s);
		return s;
	}

	/**
	 * 批量刷新
	 * 
	 * @param query
	 */
	public void batchAfreshStdOrder(BdPayableQuery query) {
		List<String> learnIds = payMapper.getStdLearndIdsByCond(query);
		if (null != learnIds && learnIds.size() > 0) {
			for (String learnId : learnIds) {
				log.debug("---------------=" + learnId);
				afreshStudentOrder(learnId, "B");
			}
		}
	}

	/**
	 * 手动刷新学员订单
	 */
	public void afreshStudentOrder(String learnId, String operType) {
		BdLearnInfo afreshLeanrInfo = payMapper.selectLearnInfoByLearnId(learnId);
		if (null != afreshLeanrInfo) {
			// 获取当前最新的收费标准
			String nowFeeId = studentModifyMapper.selectFeeStandard(afreshLeanrInfo.getPfsnId(),
					afreshLeanrInfo.getTaId(), afreshLeanrInfo.getScholarship());

			if (!StringUtil.hasValue(nowFeeId)) {
				throw new BusinessException("E000077"); // 无收费标准
			}
			log.debug("学业:" + learnId + "对应的新的收费标准为:" + nowFeeId);
			// 老收费标准ID
			String oldFeeId = studentModifyMapper.selectNowFeeId(learnId);
			if (!StringUtil.hasValue(nowFeeId)) {
				throw new BusinessException("E000089"); // 老收费标准
			}
			log.debug("学业:" + learnId + "对应的旧的收费标准为:" + oldFeeId);

			if (operType.equals("S")) { // 单个刷新操作
				if (nowFeeId.equals(oldFeeId)) {
					throw new BusinessException("E000113"); // 如果二者相同则表示,没有配置新的,或者新老一样,无需刷新
				}
				executeAfresh(afreshLeanrInfo, nowFeeId);
			} else if (operType.equals("B")) {
				if (!nowFeeId.equals(oldFeeId)) { // 批量操作,容错没有新收费标准的
					executeAfresh(afreshLeanrInfo, nowFeeId);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void executeAfresh(BdLearnInfo afreshLeanrInfo, String nowFeeId) {
		// 设置转账对象
		Body body = new Body();
		// 如果有优惠政策
		String learnId = afreshLeanrInfo.getLearnId();
		String offerId = studentModifyMapper.selectOfferId(afreshLeanrInfo.getPfsnId(), afreshLeanrInfo.getTaId(),
				afreshLeanrInfo.getScholarship());
		log.debug("学业:" + learnId + "对应优惠政策:" + offerId);

		// 已缴金额
		BigDecimal paidAmount = BigDecimal.ZERO;

		BigDecimal delayAmount = BigDecimal.ZERO;

		String amount = studentModifyMapper.selectPaidAmountByLearnId(afreshLeanrInfo.getLearnId());
		if (StringUtil.hasValue(amount)) {
			paidAmount = AmountUtil.str2Amount(amount);
		}
		log.debug("学业:" + learnId + "在刷新新的收费标准前总缴费:" + paidAmount);

		studentModifyMapper.updateStdOrderStatusByLearnId(learnId);

		BdLearnInfo learnInfo = new BdLearnInfo();
		BaseUser user = SessionUtil.getUser();
		learnInfo.setUpdateUser(user.getRealName());
		learnInfo.setUpdateUserId(user.getUserId());

		learnInfo.setFeeId(nowFeeId);
		learnInfo.setOfferId(offerId);
		learnInfo.setLearnId(learnId);
		learnInfo.setUnvsId(afreshLeanrInfo.getUnvsId());
		learnInfo.setPfsnId(afreshLeanrInfo.getPfsnId());
		learnInfo.setTaId(afreshLeanrInfo.getTaId());
		learnInfo.setRecruitType(afreshLeanrInfo.getRecruitType());
		learnInfo.setStdId(afreshLeanrInfo.getStdId());
		learnInfo.setStdStage("1"); // 全部按照意向学员刷新订单
		recruitService.initStudentOrder(learnInfo);

		BdStudentModify bdStudentModify = new BdStudentModify();
		bdStudentModify.setLearnId(learnId);
		bdStudentModify.setRecruitType(afreshLeanrInfo.getRecruitType());
		bdStudentModify.setFeeId(nowFeeId);
		bdStudentModify.setOfferId(offerId);
		bdStudentModify.setStdId(afreshLeanrInfo.getStdId());

		if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
			delayAmount = autoPayTheFees(paidAmount, bdStudentModify, delayAmount);
		}
		log.debug("学业:" + learnId + "在自动交费后还剩滞留金:" + delayAmount);
		modifyService.updateBdLearnInfo(bdStudentModify);// 根据学业id修改学业信息
		/*
		 * if (delayAmount.compareTo(BigDecimal.ZERO) > 0) { // 设置转账对象
		 * body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
		 * body.put("stdId", learnInfo.getStdId()); body.put("amount",
		 * delayAmount); body.put("action", FinanceConstants.ACC_ACTION_IN);
		 * body.put("excDesc", "刷新新的收费标准，已缴费用退至滞留账户"); }
		 * 
		 * //TODO 动账待定 if (null != body && !body.isEmpty()) {
		 * atdApi.trans(body); }
		 */
	}

	/**
	 * 手动刷新学员订单-自动缴费
	 * 
	 * @param paidAmount
	 * @param bdStudentModify
	 * @param delayAmount
	 * @return
	 */
	private BigDecimal autoPayTheFees(BigDecimal paidAmount, BdStudentModify bdStudentModify, BigDecimal delayAmount) {

		BdPayInfo payInfo = new BdPayInfo();
		BigDecimal payableAmount = paidAmount;
		List<String> itemCodes = new ArrayList<String>();
		List<String> years = new ArrayList<String>();

		BdPayableInfoResponse response = payService.selectPayableInfoByLearnId(bdStudentModify.getLearnId(),
				FinanceConstants.ORDER_STATUS_UNPAID);
		BigDecimal tutorPayable = BigDecimal.ZERO;

		if (StudentConstants.RECRUIT_TYPE_CJ.equals(bdStudentModify.getRecruitType())
				&& paidAmount.compareTo(BigDecimal.ZERO) > 0 && null != response.getTutorPayInfos()
				&& response.getTutorPayInfos().size() > 0) { // 成教、有缴费、有辅导费的进入

			for (BdStdPayInfoResponse res : response.getTutorPayInfos()) {
				tutorPayable = BigDecimalUtil.add(tutorPayable, AmountUtil.str2Amount(res.getPayable()));
			}
			if (paidAmount.compareTo(tutorPayable) >= 0) { // 如果大于现有YO费用
				// 已缴总额减去Y0
				paidAmount = BigDecimalUtil.substract(paidAmount, tutorPayable);
				for (BdStdPayInfoResponse res : response.getTutorPayInfos()) { // 循环遍历加入支付科目
					itemCodes.add(res.getItemCode());
				}

			} else {
				delayAmount = payableAmount;
				paidAmount = BigDecimal.ZERO;
			}

		} else if (StudentConstants.RECRUIT_TYPE_GK.equals(bdStudentModify.getRecruitType())
				&& paidAmount.compareTo(BigDecimal.ZERO) > 0) { // 国开 比对所有科目
			BigDecimal firstPayable = BigDecimal.ZERO;
			// 第一年
			if (null != response.getFirstPayInfos() && response.getFirstPayInfos().size() > 0) {

				for (BdStdPayInfoResponse res : response.getFirstPayInfos()) {
					firstPayable = BigDecimalUtil.add(firstPayable, AmountUtil.str2Amount(res.getPayable()));
				}

				if (paidAmount.compareTo(firstPayable) >= 0) { // 已缴金额大于应缴金额，自动缴费，插入流水
					paidAmount = BigDecimalUtil.substract(paidAmount, firstPayable);
					for (BdStdPayInfoResponse res : response.getFirstPayInfos()) { // 循环遍历加入支付科目
						itemCodes.add(res.getItemCode());
					}
					years.add(FinanceConstants.FEE_ITEM_YEAR_FIRST);
					delayAmount = paidAmount;
				} else {
					delayAmount = paidAmount;
					paidAmount = BigDecimal.ZERO;
				}
			}
			// 第二年
			if (null != response.getSecondPayInfos() && response.getSecondPayInfos().size() > 0) {
				if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal secondPayable = BigDecimal.ZERO;
					for (BdStdPayInfoResponse res : response.getSecondPayInfos()) {
						secondPayable = BigDecimalUtil.add(secondPayable, AmountUtil.str2Amount(res.getPayable()));
					}

					if (paidAmount.compareTo(secondPayable) >= 0) {
						paidAmount = BigDecimalUtil.substract(paidAmount, secondPayable);
						for (BdStdPayInfoResponse res : response.getSecondPayInfos()) { // 循环遍历加入支付科目
							itemCodes.add(res.getItemCode());
						}
						years.add(FinanceConstants.FEE_ITEM_YEAR_SECOND);
						delayAmount = paidAmount;
					} else {
						delayAmount = paidAmount;
						paidAmount = BigDecimal.ZERO;
					}
				}
			}
			// 第三年
			if (null != response.getThirdPayInfos() && response.getThirdPayInfos().size() > 0) {
				if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal thirdPayable = BigDecimal.ZERO;
					for (BdStdPayInfoResponse res : response.getThirdPayInfos()) {
						thirdPayable = BigDecimalUtil.add(thirdPayable, AmountUtil.str2Amount(res.getPayable()));
					}

					if (paidAmount.compareTo(thirdPayable) >= 0) {
						paidAmount = BigDecimalUtil.substract(paidAmount, thirdPayable);
						for (BdStdPayInfoResponse res : response.getThirdPayInfos()) { // 循环遍历加入支付科目
							itemCodes.add(res.getItemCode());
						}
						years.add(FinanceConstants.FEE_ITEM_YEAR_THIRD);
						delayAmount = paidAmount;
					} else {
						delayAmount = paidAmount;
						paidAmount = BigDecimal.ZERO;
					}
				}
			}
			// TODO 第四年

			// 其他
			if (null != response.getOtherPayInfos() && response.getOtherPayInfos().size() > 0) {
				if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal otherPayable = BigDecimal.ZERO;
					for (BdStdPayInfoResponse res : response.getOtherPayInfos()) {
						otherPayable = BigDecimalUtil.add(otherPayable, AmountUtil.str2Amount(res.getPayable()));
					}
					if (paidAmount.compareTo(otherPayable) >= 0) {
						paidAmount = BigDecimalUtil.substract(paidAmount, otherPayable);
						for (BdStdPayInfoResponse res : response.getOtherPayInfos()) { // 循环遍历加入支付科目
							itemCodes.add(res.getItemCode());
						}
					} else {
						delayAmount = paidAmount;
						paidAmount = BigDecimal.ZERO;
					}
				}
			}
		}
		if (null != itemCodes && itemCodes.size() > 0) {
			payInfo.setItemCodes(itemCodes.toArray(new String[itemCodes.size()]));
			payInfo.setLearnId(bdStudentModify.getLearnId());
			payInfo.setZmDeduction("0.00");
			payInfo.setPaidAmount(BigDecimalUtil.substract(payableAmount, delayAmount).toString());
			payInfo.setPaymentType(FinanceConstants.PAYMENT_TYPE_DELAY);
			payInfo.setAccDeduction("0.00");
			if (null != years && years.size() > 0) {
				payInfo.setYears(years.toArray(new String[years.size()]));
			}
			tuitionService.selfPayTuition(payInfo);
		}
		/*
		 * //先把够自动缴费的滞留金退到滞留账户中,然后再从中拿出来 Body body = new Body(); // 设置转账对象
		 * body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
		 * body.put("stdId", bdStudentModify.getStdId()); body.put("amount",
		 * BigDecimalUtil.substract(payableAmount, delayAmount).toString());
		 * body.put("action", FinanceConstants.ACC_ACTION_IN);
		 * body.put("excDesc", "刷新订单,已缴费退滞留账户");
		 * 
		 * atdApi.trans(body);
		 * 
		 * if (null != itemCodes && itemCodes.size() > 0) {
		 * payInfo.setItemCodes(itemCodes.toArray(new
		 * String[itemCodes.size()]));
		 * payInfo.setLearnId(bdStudentModify.getLearnId());
		 * payInfo.setZmDeduction("0.00");
		 * payInfo.setPaidAmount(BigDecimalUtil.substract(payableAmount,
		 * delayAmount).toString());
		 * payInfo.setPaymentType(FinanceConstants.PAYMENT_TYPE_DELAY);
		 * payInfo.setAccDeduction("0.00"); if (null != years && years.size() >
		 * 0) { payInfo.setYears(years.toArray(new String[years.size()])); }
		 * tuitionService.selfPayTuition(payInfo); }
		 */
		return delayAmount;
	}

	public Map<String, String> selectStdInfoByStdId(String stdId) {
		return payMapper.selectStdInfoByStdId(stdId);
	}

	/**
	 * 获取缴费总额
	 * 
	 * @param stdId
	 * @return
	 */
	public String getPaidSum(String stdId) {
		return payMapper.selectPaidSumByStdId(stdId);
	}

	/**
	 * 获取提现总额
	 * 
	 * @param stdId
	 * @return
	 */
	public String getWithdrawSum(String stdId) {
		return payMapper.selectWithdrawByStdId(stdId);
	}

	/**
	 * 获取余额
	 * 
	 * @param stdId
	 * @param accType
	 * @return
	 */
	public Object getAccount(String stdId, String accType) {
		AtsAccount account = new AtsAccount();
		account.setAccType(accType);
		account.setStdId(stdId);

		account = accountMapper.getAccount(account);

		String accAmount = "0.00";
		if (account != null) {
			accAmount = account.getAccAmount();
			if (StringUtil.isEmpty(accAmount)) {
				accAmount = "0.00";
			}
		}

		return accAmount;
	}
	/**
	 * 缴费成功推送微信公众号提醒信息
	 * @param stdName
	 * @param amount
	 * @param serialNo
	 * @param itemNames
	 * @param learnId
	 * @param openId
	 * @param tutorOpenId
	 * @param recruitOpenId
	 */
	public void sendTuitionMsg(String stdName, String amount, String serialNo, String[] itemNames, String learnId,
			String openId, String tutorOpenId, String recruitOpenId){
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
		if (StringUtil.hasValue(openId)) {  //给当前缴费学员推送微信公众号信息
			msgVo.setExt1(learnId);
			msgVo.setTouser(openId);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}
		msgVo.addData("firstWord", "学员缴费通知");
		if (StringUtil.hasValue(tutorOpenId)) { //辅导费给辅导员推送公众号信息
			msgVo.setTouser(tutorOpenId);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}

		if (StringUtil.hasValue(recruitOpenId)) { //给招生老师推送公众号信息
			msgVo.setTouser(recruitOpenId);
			RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
		}
	}

}
