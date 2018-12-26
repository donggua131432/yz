package com.yz.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.GsLotteryApi;
import com.yz.conf.YzSysConfig;
import com.yz.constants.CheckConstants;
import com.yz.constants.EducationConstants;
import com.yz.constants.FeeConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.OrderConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.constants.BdsConstants;
import com.yz.core.util.UsRelationUtil;
import com.yz.dao.BdStudentSendMapper;
import com.yz.dao.BdsLearnMapper;
import com.yz.dao.BdsPaymentMapper;
import com.yz.dao.StudentAllMapper;
import com.yz.edu.trace.annotation.YzTrace;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.BdSerialSurplus;
import com.yz.model.BdStudentBaseInfo;
import com.yz.model.SessionInfo;
import com.yz.model.WechatMsgVo;
import com.yz.model.communi.Body;
import com.yz.model.coupon.BdCoupon;
import com.yz.model.coupon.BdCouponDetail;
import com.yz.model.educational.BdStudentSend;
import com.yz.model.payment.BdOrder;
import com.yz.model.payment.BdPayInfo;
import com.yz.model.payment.BdPayInfoDetail;
import com.yz.model.payment.BdStudentSerial;
import com.yz.model.payment.BdSubSerial;
import com.yz.model.payment.FeeItemForm;
import com.yz.model.payment.OaCampusInfo;
import com.yz.pay.GwPaymentService;
import com.yz.redis.RedisService;
import com.yz.session.AppSessionHolder;
import com.yz.task.YzTaskConstants;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service(value="bdsTuitionService")
@Transactional
public class BdsTuitionService {

	private static final Logger log = LoggerFactory.getLogger(BdsTuitionService.class);

	@Autowired
	private BdsPaymentMapper payMapper;

	@Autowired
	private AccountService accountService;

	@Autowired
	private GwPaymentService paymentService;

	@Autowired
	private UsInfoService infoService;

	@Reference(version = "1.0")
	private GsLotteryApi lotteryApi;

	@Autowired
	private BdsLearnMapper learnMapper;

	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private BdStudentSendMapper studentSendMapper;

	@Autowired
	private StudentAllMapper stdAllMapper;

	@Autowired
	private BdsPaymentBindService bindService;

	@Autowired
	private UsTaskCardService usTaskCardService;

	@YzTrace(remark = "第三方支付")
	public Map<String, Object> payTuition(BdPayInfo payInfo, String userId) {

		Map<String, Object> result = new HashMap<String, Object>();
		boolean hasCouPons = hasCoupons(payInfo);
		// 学业ID
		String learnId = payInfo.getLearnId();
		Map<String, String> object = learnMapper.selectLearnInfoByLearnId(learnId);
		String grade = object.get("grade");
		// 获取订单信息，并判断是否存在已缴科目
		BdOrder order = getOrder(learnId, payInfo.getItemCodes());
		// 应付金额
		List<FeeItemForm> items = payMapper.selectAmountByItems(learnId, payInfo.getItemCodes());
		// 计算总金额，并且判断是否有使用智米抵扣辅导费（智米不允许抵扣辅导费）
		BigDecimal allAmount = getAllAmount(learnId, payInfo.getZmDeduction(), items);
		// 获取支付批次号
		String serialMark = getSerialMark(order.getLearnId());
		// 总共应缴
		BigDecimal allPayable = BigDecimal.ZERO;
		// 用户滞留账户抵扣金额
		BigDecimal accDeduction = AmountUtil.str2Amount(payInfo.getAccDeduction());
		if (accDeduction.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException("E60013"); // 现金抵扣不能小于0
		}
		// 智米抵扣比例
		BigDecimal zmScale = AmountUtil.str2Amount(yzSysConfig.getZhiMiScale());
		// 用户智米抵扣金额(员工不允许使用智米抵扣)
		BigDecimal zmDeduction = getZmDeduction(payInfo.getZmDeduction(), zmScale, payInfo.getTradeType(), userId);
		// 动账集合
		List<Body> transList = new ArrayList<Body>();
		// 优惠券优惠金额
		// BigDecimal couponAmount = BigDecimal.ZERO;
		// 校验优惠券有效性
		checkCoupons(payInfo.getCoupons(), learnId, userId);
		List<BdStudentSerial> serials = new ArrayList<BdStudentSerial>();
		// 生成流水
		serials = createSerials(items, payInfo.getTradeType(), payInfo.getEmpId(), serialMark);
		/**
		 * 智米、优惠券抵扣 并且生成流水
		 */
		zmDeduction=zmDeductionDetail(serials, hasCouPons, payInfo, order, zmDeduction, zmScale);
		// 智米
		zmDeduction = BigDecimalUtil.multiply(zmDeduction, zmScale);
		// 根据滞留抵扣顺序排序
		Collections.sort(serials, new Comparator<BdStudentSerial>() {
			public int compare(BdStudentSerial arg0, BdStudentSerial arg1) {
				// 定义如何比较
				return Integer.valueOf(arg0.getDelayNum()) - Integer.valueOf(arg1.getDelayNum());
			}
		});
		/**
		 * 滞留抵扣
		 */
		allPayable = accDeduction(serials, accDeduction, order.getStdId(), allPayable);
		// 记录操作的智米
		BdSerialSurplus surplus = new BdSerialSurplus();
		if (zmDeduction.compareTo(BigDecimal.ZERO) > 0) {
			surplus.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
			surplus.setAmount(zmDeduction.toString());
			surplus.setSerialMark(serialMark);
			surplus.setUnit(FinanceConstants.ACC_UNIT_ZHIMI);
			payMapper.insertSerialSurplus(surplus);
		}
		// 生成支付串
		if (allPayable.compareTo(BigDecimal.ZERO) > 0) {
			// 插入流水
			insertSerial(serials);
			Map<String, String> postData = new HashMap<String, String>();
			postData.put("orderAmount", allPayable.toString());
			postData.put("serialNo", serialMark);
			postData.put("payType", FinanceConstants.PAYMENT_TYPE_WECHAT);
			postData.put("dealType", FinanceConstants.PAYMENT_DEAL_TYPE_TUITION);
			postData.put("openId", payInfo.getOpenId());
			// 新增-2017-10-10 扫码
			postData.put("tradeType", payInfo.getTradeType());
			postData.put("productId", serialMark);
			log.debug("---------------------------------- 用户下单，userId ： " + userId);
			// 微信支付
			result = paymentService.payment(postData);
			result.put("resCode", "WECHAT");
			// 保存二维码链接
			if (!payInfo.getTradeType().equalsIgnoreCase(FinanceConstants.TRADE_TYPE_JSAPI)) {
				String codeUrl = (String) result.get("payinfo");
				payMapper.updateSerialCodeUrl(serialMark, codeUrl);
				BdPayInfoDetail detail = payMapper.getPayDetail(serials.get(0).getSerialNo());
				if (null != detail) {
					result.put("amount", allAmount.toString());
					result.put("grade", detail.getGrade());
					result.put("idCard", detail.getIdCard());
					result.put("mobile", detail.getMobile());
					result.put("pfsnLevel", detail.getPfsnLevel());
					result.put("pfsnName", detail.getPfsnName());
					result.put("stdName", detail.getStdName());
					result.put("taName", detail.getTaName());
					result.put("unvsName", detail.getUnvsName());
					result.put("payable", allPayable.toString());
				}
			}
		} else {
			if (!StringUtil.hasValue(userId)) {
				userId = payMapper.selectUserId(order.getStdId());
			}
			// 全部抵扣 完成订单
			for (BdStudentSerial serial : serials) {
				serial.setPayTime(DateUtil.getNowDateAndTime());
				serial.setSerialStatus(OrderConstants.SERIAL_STATUS_UNCHECK);
				for (BdSubSerial subSerial : serial.getSubSerials()) {
					subSerial.setSubSerialStatus(OrderConstants.SERIAL_STATUS_UNCHECK);
					if (FinanceConstants.PAYMENT_TYPE_ZM.equals(subSerial.getPaymentType())) {
						// 动账集合添加
						transList.add(amountTrans(userId, subSerial.getAmount(), FinanceConstants.ACC_ACTION_OUT,
								serial.getSerialNo(), String.format("缴纳%s级学费，智米账户抵扣", grade),
								FinanceConstants.ACC_TYPE_ZHIMI));
						continue;
					} else if (FinanceConstants.PAYMENT_TYPE_COUPON.equals(subSerial.getPaymentType())) {
						// 修改优惠券为已使用
						for (BdCoupon coupon : payInfo.getCoupons()) {
							// 修改优惠券为已使用
							payMapper.updateCouponUsed(coupon.getScId(), FinanceConstants.COUPON_USED);
						}
						continue;
					} else if (FinanceConstants.PAYMENT_TYPE_DELAY.equals(subSerial.getPaymentType())) {
						// 动账集合添加
						transList.add(amountTrans(userId, subSerial.getAmount(), FinanceConstants.ACC_ACTION_OUT,
								serial.getSerialNo(), String.format("缴纳%s级学费，滞留账户抵扣", grade),
								FinanceConstants.ACC_TYPE_DEMURRAGE));
						continue;
					}
				}
			}
			if (null != surplus) {
				if (FinanceConstants.ACC_TYPE_ZHIMI.equals(surplus.getAccType())) {
					// 动账集合添加
					transList.add(amountTrans(userId, surplus.getAmount(), FinanceConstants.ACC_ACTION_OUT, serialMark,
							String.format("缴纳%s级学费，智米账户抵扣", grade), FinanceConstants.ACC_TYPE_ZHIMI));
				}
				surplus.setStatus(FinanceConstants.STUDENT_SERIAL_STATUS_FINISHED);
				payMapper.updateSerialSurplus(surplus);
			}
			// 插入流水
			insertSerial(serials);
			// 修改子订单状态
			payMapper.updateSubOrderPaid(learnId, payInfo.getItemCodes());
			// 动账
			if (transList.size() > 0) {
				accountService.transMore(transList);
			}
			try {
				String[] itemNames = payMapper.selectItemNameBySerialMark(serialMark);
				/**
				 * 发送微信推送
				 */
				sendWechatMsg(order.getStdName(), allPayable.toString(), payInfo.getOpenId(), itemNames, learnId);
				// 直接发放辅导书
				initBookSend(learnId, true);
			} catch (Exception e) {
				log.debug("--------------------------------- 学员缴费消息推送失败：" + e.getMessage());
			}
			result.put("resCode", "SUCCESS");
			String[] s_itemCodes = new String[payInfo.getItemCodes().size()];
			String[] s_years = new String[payInfo.getYears().size()];
			payInfo.getItemCodes().toArray(s_itemCodes);
			payInfo.getYears().toArray(s_years);
			bindService.bindAndAward(s_itemCodes, s_years, serialMark, order.getOrderNo(), "0.00", learnId);
			// 赠送抽奖机会
			lotteryApi.gainLoterryTicket(userId);
			// 任务卡活动
			usTaskCardService.addUsTaskCardDetail(userId, s_itemCodes, learnId);
		}
		return result;
	}

	/**
	 * 智米和优惠券抵扣
	 * 
	 * @param serials
	 *            流水
	 * @param hasCouPons
	 *            是否有优惠券
	 * @param payInfo
	 *            下单对象
	 * @param order
	 *            订单
	 * @param zmDeduction
	 *            智米抵扣
	 * @param zmScale
	 *            智米比例
	 */
	@YzTrace(remark = "智米和优惠券抵扣")
	private BigDecimal zmDeductionDetail(List<BdStudentSerial> serials, Boolean hasCouPons, BdPayInfo payInfo, BdOrder order,
			BigDecimal zmDeduction, BigDecimal zmScale) {
		for (BdStudentSerial serial : serials) {
			// 计算应付金额
			serial.setAmount(serial.getItemAmount());
			// 应付金额
			BigDecimal sum = AmountUtil.str2Amount(serial.getItemAmount());
			// 子流水集合
			List<BdSubSerial> subSerials = new ArrayList<BdSubSerial>();
			// 是否为组合支付
			String isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_NO;
			// 总共优惠金额
			BigDecimal couponCount = BigDecimal.ZERO;
			// 优惠券抵扣
			if (hasCouPons) {
				couponCount = deductionCoupons(payInfo.getCoupons(), serial);
				// 组合支付
				if (couponCount.compareTo(BigDecimal.ZERO) > 0) { // 优惠券抵扣接大于0
																	// 为组合支付
					isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES;
				}
				sum = BigDecimalUtil.substract(sum, couponCount); // 应付金额减去优惠金额
				if (sum.compareTo(BigDecimal.ZERO) <= 0) { // 如果优惠券抵扣所有金额
					sum = BigDecimal.ZERO; // 应付金额设为0
				}
				// 子流水赋值
				BdSubSerial cSerial = getCouponSerial(couponCount, payInfo.getCoupons(), order,
						OrderConstants.SERIAL_STATUS_PROCESS);
				if (null != cSerial) {
					subSerials.add(cSerial);
				}
			}
			// 智米抵扣
			if (sum.compareTo(BigDecimal.ZERO) > 0 && zmDeduction.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal zmSubDeduct = BigDecimal.ZERO;
				// 如果应缴大于0，智米抵扣大于0
				// 智米抵扣前余额，用于计算智米抵扣数
				BigDecimal zmDeduct = sum;
				// 智米当前流水抵扣
				// 应缴减去智米抵扣
				sum = BigDecimalUtil.substract(sum, zmDeduction);
				if (sum.compareTo(BigDecimal.ZERO) <= 0) { // 抵扣所有金额
					isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES;
					// 设置智米抵扣
					sum = BigDecimal.ZERO;
					// 子流水智米抵扣值
					zmSubDeduct = zmDeduct;
					// 智米总抵扣值计算
					zmDeduction = BigDecimalUtil.substract(zmDeduction, zmDeduct);
				} else { // 应缴大于0，智米抵完
					// 子流水智米抵扣值
					zmSubDeduct = zmDeduction;
					// 智米总抵扣，扣完
					zmDeduction = BigDecimal.ZERO;
				}
				if (zmDeduction.compareTo(BigDecimal.ZERO) > 0) {
					isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES;
				}
				// 智米流水
				BdSubSerial zmSerial = getZMSerial(zmSubDeduct, serial, order.getStdId(), zmScale.toString());
				if (null != zmSerial) {
					subSerials.add(zmSerial);
				}
			}
			// 流水赋值
			serial = getSerial(serial, order, sum.toString(), couponCount.toString(), isAssembly, payInfo,
					OrderConstants.SERIAL_STATUS_PROCESS);
			serial.setSubSerials(subSerials);
		}
		return zmDeduction;
	}

	/**
	 * 滞留金抵扣
	 * 
	 * @param serials
	 * @param accDeduction
	 * @param stdId
	 * @param allPayable
	 * @return
	 */
	@YzTrace(remark = "滞留金抵扣")
	private BigDecimal accDeduction(List<BdStudentSerial> serials, BigDecimal accDeduction, String stdId,
			BigDecimal allPayable) {
		for (BdStudentSerial serial : serials) {
			// 应付金额
			BigDecimal sum = AmountUtil.str2Amount(serial.getPayable());
			// 是否为组合支付
			String isAssembly = serial.getIsAssembly();
			// 子流水集合
			List<BdSubSerial> subSerials = serial.getSubSerials();
			// 滞留子流水抵扣额
			BigDecimal accSubDeduct = BigDecimal.ZERO;
			// 如果应缴大于0 进入滞留抵扣
			if (accDeduction.compareTo(BigDecimal.ZERO) > 0 && sum.compareTo(BigDecimal.ZERO) > 0) {
				// 滞留抵扣前余额，用于计算滞留抵扣数
				BigDecimal accDeduct = sum;
				// 应缴减去滞留抵扣
				sum = BigDecimalUtil.substract(sum, accDeduction);
				if (sum.compareTo(BigDecimal.ZERO) <= 0) { // 现金抵扣大于应缴，完成订单
					isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES;
					sum = BigDecimal.ZERO;
					// 滞留抵扣大于
					accDeduction = BigDecimalUtil.substract(accDeduction, accDeduct);
					// 子流水抵扣值
					accSubDeduct = accDeduct;
				} else {
					// 子流水抵扣值
					accSubDeduct = accDeduction;
					// 总抵扣为0
					accDeduction = BigDecimal.ZERO;
				}
				if (accDeduction.compareTo(BigDecimal.ZERO) > 0) {
					isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES;
				}
				// 流水赋值
				serial.setPayable(sum.toString());
				serial.setIsAssembly(isAssembly);
				BdSubSerial accSerial = getAccSerial(accSubDeduct, serial, stdId);
				if (null == subSerials) {
					subSerials = new ArrayList<BdSubSerial>();
				}
				if (null != accSerial) {
					subSerials.add(accSerial);
				}
			}
			if (sum.compareTo(BigDecimal.ZERO) > 0
					|| AmountUtil.str2Amount(serial.getItemAmount()).compareTo(BigDecimal.ZERO) == 0) {
				BdSubSerial cashSerial = getCashSerial(sum, serial, stdId);
				if (null != cashSerial) {
					subSerials.add(cashSerial);
				}
			}
			serial.setSubSerials(subSerials);
			allPayable = BigDecimalUtil.add(allPayable, sum);
		}
		return allPayable;
	}

	/**
	 * 计算优惠券金额
	 * 
	 * @param coupons
	 * @param serial
	 * @return
	 */
	@YzTrace(remark = "计算优惠券金额")
	private BigDecimal deductionCoupons(List<BdCoupon> coupons, BdStudentSerial serial) {
		// 总共优惠金额
		BigDecimal couponCount = BigDecimal.ZERO;
		if (!coupons.isEmpty()) {
			// 根据scId 去重
			for (int i = 0; i < coupons.size() - 1; i++) {
				for (int j = coupons.size() - 1; j > i; j--) {
					if (coupons.get(j).getScId().equals(coupons.get(i).getScId())) {
						coupons.remove(j);
					}
				}
			}
			for (BdCoupon coupon : coupons) {

				List<BdCouponDetail> couponDetail = coupon.getCouponDetails();
				if (!couponDetail.isEmpty()) {
					// 循环优惠券抵扣科目信息
					// 循环科目抵扣
					for (BdCouponDetail detail : couponDetail) {
						// 如果科目和流水科目对应，优惠金额加上对应
						if (detail.getItemCode().equals(serial.getItemCode())) {
							String amount = detail.getPrice();
							couponCount = couponCount.add(AmountUtil.str2Amount(amount)); // 优惠金额则加上应付金额
						}
					}
				}

			}
			/*
			 * BigDecimal paidAmount = sum; for (BdCoupon coupon : coupons) { if
			 * (paidAmount.compareTo(BigDecimal.ZERO) <= 0) { break; }
			 * BigDecimal couponAmount =
			 * AmountUtil.str2Amount(coupon.getAmount()); // 循环优惠券优惠科目 for
			 * (String itemCode : coupon.getItemCodes()) { // 如果付款与优惠科目对应
			 * 、计算优惠金额 if (serial.getItemCode().equals(itemCode)) { if
			 * (couponAmount.compareTo(BigDecimal.ZERO) > 0) { // 如果优惠券抵扣余额大于0
			 * 
			 * if (paidAmount.compareTo(couponAmount) > 0) { // 应缴金额大于优惠金额 //
			 * 科目应付金额减去优惠金额 paidAmount = paidAmount.subtract(couponAmount);
			 * couponCount = couponCount.add(couponAmount); // 优惠金额则加对应优惠金额
			 * couponAmount = BigDecimal.ZERO; } else { // 应缴金额小于优惠金额 BigDecimal
			 * couponBalance = couponAmount.subtract(paidAmount); couponCount =
			 * couponCount.add(paidAmount); // 优惠金额则加上应付金额 paidAmount =
			 * BigDecimal.ZERO; couponAmount = couponBalance; }
			 * coupon.setAmount(couponAmount.toString()); } } } }
			 */
		}
		return couponCount;
	}

	/**
	 * 生成流水
	 * 
	 * @param items
	 *            缴费科目
	 * @param tradeType
	 *            缴费类型
	 * @param payEmpId
	 *            发起支付时empid
	 * @param serialMark
	 *            批次号
	 * @return
	 */
	@YzTrace(remark = "生成流水")
	private List<BdStudentSerial> createSerials(List<FeeItemForm> items, String tradeType, String payEmpId,
			String serialMark) {
		List<BdStudentSerial> serials = new ArrayList<>();
		String empId = null;
		String empName = null;
		if (tradeType.equalsIgnoreCase(FinanceConstants.TRADE_TYPE_NATIVE)) {// 扫码支付,获取当前招生老师的信息
			Map<String, String> empMap = payMapper.getEmpInfoByEmpId(payEmpId);
			empId = empMap.get("empId");
			empName = empMap.get("empName");
		}
		for (FeeItemForm item : items) {
			BdStudentSerial serial = new BdStudentSerial();
			serial.setItemCode(item.getItemCode());
			serial.setItemAmount(item.getAmount());
			serial.setItemType(item.getItemType());
			serial.setZmCouponNum(item.getZmCouponNum());
			serial.setDelayNum(item.getDelayNum());
			serial.setSerialMark(serialMark);
			serial.setEmpId(empId);
			serial.setEmpName(empName);
			serials.add(serial);
		}
		// 根据智米优惠券优惠顺序排序
		Collections.sort(serials, new Comparator<BdStudentSerial>() {
			public int compare(BdStudentSerial arg0, BdStudentSerial arg1) {
				// 定义如何比较
				return Integer.valueOf(arg0.getZmCouponNum()) - Integer.valueOf(arg1.getZmCouponNum());
			}
		});
		return serials;
	}

	/**
	 * 校验优惠券有效性
	 * 
	 * @param coupons
	 *            前端传过来的优惠券信息
	 * @param learnId
	 *            学业ID
	 * @param userId
	 *            用户ID
	 */
	@YzTrace(remark = "校验优惠券有效性")
	private void checkCoupons(List<BdCoupon> coupons, String learnId, String userId) {
		if (!coupons.isEmpty()) {
			for (BdCoupon bdCoupon : coupons) {
				Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
				BdCoupon couponTmp = payMapper.selectCouponAmountToPay(bdCoupon.getCouponId(), bdCoupon.getScId(),
						learnId, std.get("stdId"), userId);
				// 优惠券抵扣总额
				BigDecimal deductAmt = BigDecimal.ZERO;
				// 计算优惠券抵扣总额
				for (BdCouponDetail detail : bdCoupon.getCouponDetails()) {
					// 如果前端传过来的itemCode在优惠券里并没有，抛错
					if (!StringUtil.hasValue(detail.getItemCode())) {
						throw new BusinessException("E60043"); // 优惠券抵扣金额错误
					}
					String amt = detail.getPrice();
					deductAmt = deductAmt.add(AmountUtil.str2Amount(amt));
				}
				// 如果抵扣总额大于优惠券总额，抛出错误
				if (deductAmt.compareTo(AmountUtil.str2Amount(couponTmp.getAmount())) > 0) {
					throw new BusinessException("E60043"); // 优惠券抵扣金额错误
				}
				bdCoupon.setAmount(couponTmp.getAmount());
				bdCoupon.setCouponName(couponTmp.getCouponName());
			}
			// 优惠券信息
			if (coupons.isEmpty()) {
				throw new BusinessException("E60021"); // 无效优惠券
			}
		}
	}

	/**
	 * 计算智米抵扣金额（员工不允许使用智米抵扣）
	 * 
	 * @return
	 */
	@YzTrace(remark = "计算智米抵扣金额")
	private BigDecimal getZmDeduction(String zmDeductions, BigDecimal zmScale, String tradeType, String userId) {
		BigDecimal zmDeduction = BigDecimalUtil.divide(AmountUtil.str2Amount(zmDeductions), zmScale);
		if (!tradeType.equalsIgnoreCase(FinanceConstants.TRADE_TYPE_NATIVE)) {
			SessionInfo session = AppSessionHolder.getSessionInfo(userId, AppSessionHolder.RPC_SESSION_OPERATOR);
			if (UsRelationUtil.isEmp(session.getRelation())) {
				if (BigDecimal.ZERO.compareTo(zmDeduction) < 0) {
					throw new BusinessException("E60026"); // 员工无法使用智米
				}
			}
		}
		if (zmDeduction.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException("E60014"); // 智米抵扣不能小于0
		}
		return zmDeduction;
	}

	/**
	 * 生成支付批次号（流水统一标识）
	 * 
	 * @param learnId
	 * @return
	 */
	@YzTrace(remark = "生成支付批次号")
	private String getSerialMark(String learnId) {
		OaCampusInfo campus = payMapper.selectCampusInfoByLearnId(learnId);
		String financeCode = null;
		if (null != campus) {
			financeCode = campus.getFinanceNo();
		}
		// 所有流水统一标识
		String serialMark = IDGenerator.generatororderNo(financeCode);
		return serialMark;
	}

	/**
	 * 判断是否有使用优惠券，如果使用了优惠券，判断能不能使用优惠券
	 * 
	 * @param payInfo
	 * @return
	 */
	@YzTrace(remark = "判断能不能使用优惠券")
	private boolean hasCoupons(BdPayInfo payInfo) {
		boolean has = false;
		if (!payInfo.getCoupons().isEmpty()) {
			String couponSwitch = yzSysConfig.getPaymentCouponSwitch();
			has = true;
			if (BdsConstants.PAYMENT_COUPON_SWITCH_CLOSE.equals(couponSwitch)) {
				throw new BusinessException("E60024"); // 优惠券暂时无法使用
			}
		}
		return has;
	}

	/**
	 * 获取订单信息，并判断是否存在已缴科目
	 * 
	 * @param learnId
	 * @param itemcodes
	 * @return
	 */
	@YzTrace(remark = "获取订单")
	private BdOrder getOrder(String learnId, ArrayList<String> itemcodes) {
		// 订单信息
		BdOrder order = payMapper.selectOrder(learnId);
		int paidCount = payMapper.selectPaidCountByItemCodes(order.getOrderNo(), itemcodes);
		// 存在已缴科目
		if (paidCount > 0) {
			throw new BusinessException("E60039"); // 当前科目已缴清，请勿重复缴费
		}
		return order;
	}

	/**
	 * 计算总金额，并且判断是否使用了智米（智米不能抵扣辅导费）
	 * 
	 * @param learnId
	 * @param zmDeduction
	 * @param itemcodes
	 * @return
	 */
	@YzTrace(remark = "计算总金额")
	private BigDecimal getAllAmount(String learnId, String zmDeduction, List<FeeItemForm> items) {
		BigDecimal allAmount = BigDecimal.ZERO;
		// 计算应付金额
		for (FeeItemForm amt : items) {
			BigDecimal amtAmount = AmountUtil.str2Amount(amt.getAmount());
			allAmount = BigDecimalUtil.add(allAmount, amtAmount);
			if (FeeConstants.FEE_ITEM_TYPE_COACH.equals(amt.getItemType())
					&& AmountUtil.str2Amount(zmDeduction).compareTo(BigDecimal.ZERO) > 0) {
				throw new BusinessException("E60025"); // 智米不能抵扣辅导费用
			}
		}
		return allAmount;
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
				Object obj = infoService.getOpenIdByUserId(map.get("recruitUserId"));
				if (null != obj) {
					recruitOpenId = (String) obj;
				}
			}
			/*
			 * if (StringUtil.hasValue(map.get("tutorUserId"))) { Body body =
			 * new Body(); body.put("userId", map.get("tutorUserId")); Object
			 * obj = usApi.getOpenIdByUserId(body); if (null != obj) {
			 * tutorOpenId = (String) obj; } }
			 */

			String grade = payMapper.selectGradeByLearnId(learnId);
			if ("201803".equals(grade) || "201703".equals(grade) || "201809".equals(grade)) {
				for (int i = 0; i < itemNames.length; i++) {
					itemNames[i] = itemNames[i].replaceAll("年", "学期");
				}
			}
			// 发送模板通知
			sendTuitionMsg(stdName, amount, null, itemNames, learnId, openId, tutorOpenId, recruitOpenId);
		}

	}

	private void insertSerial(List<BdStudentSerial> serials) {
		for (BdStudentSerial serial : serials) {
			serial.setSerialNo(IDGenerator.generatororderNo(serial.getFinanceCode()));
			payMapper.insertSerialBatch(serial);
		}
	}

	@SuppressWarnings("unchecked")
	private Body amountTrans(String userId, String accAmount, String accAction, String serialNo, String excDesc,
			String accType) {

		// 设置转账对象
		Body body = new Body();
		body.put("accType", accType);
		body.put("userId", userId);
		body.put("amount", accAmount);
		body.put("action", accAction);
		body.put("excDesc", excDesc);
		body.put("mappingId", serialNo);

		return body;
	}

	private BdStudentSerial getSerial(BdStudentSerial serial, BdOrder order, String itemPayable, String couponCount,
			String isAssembly, BdPayInfo payInfo, String serialStatus) {
		OaCampusInfo campus = payMapper.selectCampusInfoByLearnId(order.getLearnId());

		if (null != campus) {
			serial.setChargePlace(campus.getAddress());
			serial.setFinanceCode(campus.getFinanceNo());
			serial.setCampusName(campus.getCampusName());
		}
		serial.setPayable(itemPayable);
		serial.setAmount(serial.getAmount());
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
		serial.setPayeeId(order.getPayeeId());

		String stdUserId = payMapper.selectUserId(order.getStdId());
		serial.setUserId(stdUserId);

		String subOrderNo = payMapper.selectPaySubOrderNo(order.getLearnId(), serial.getItemCode());
		serial.setSubOrderNo(subOrderNo);

		return serial;
	}

	private BdSubSerial getCouponSerial(BigDecimal couponCount, List<BdCoupon> coupons, BdOrder order,
			String serialStatus) {

		if (couponCount.compareTo(BigDecimal.ZERO) > 0) {

			BdSubSerial cSerial = new BdSubSerial();
			cSerial.setAmount(couponCount.toString());
			for (BdCoupon coupon : coupons) {

				if (StringUtil.hasValue(cSerial.getCouponId())) {
					cSerial.setCouponId(cSerial.getCouponId() + "," + coupon.getCouponId());
				} else {
					cSerial.setCouponId(coupon.getCouponId());
				}
				cSerial.setScId(cSerial.getScId() + "," + coupon.getScId());
				cSerial.setCouponName(cSerial.getCouponName() + "," + coupon.getCouponName());
			}
			cSerial.setPaymentType(FinanceConstants.PAYMENT_TYPE_COUPON);
			cSerial.setUnit(FinanceConstants.MONEY_UNIT_RMB);
			cSerial.setSubSerialStatus(serialStatus);
			return cSerial;
		} else {
			return null;
		}
	}

	private BdSubSerial getZMSerial(BigDecimal zmDeduction, BdStudentSerial serial, String stdId, String zmScale) {
		if (zmDeduction.compareTo(BigDecimal.ZERO) > 0) {
			zmDeduction = BigDecimalUtil.multiply(zmDeduction, AmountUtil.str2Amount(zmScale));
			return getSubSerial(zmDeduction, serial.getSerialNo(), OrderConstants.SERIAL_STATUS_PROCESS,
					FinanceConstants.PAYMENT_TYPE_ZM, FinanceConstants.ACC_TYPE_ZHIMI, FinanceConstants.MONEY_UNIT_ZM,
					zmScale);
		}
		return null;
	}

	private BdSubSerial getSubSerial(BigDecimal sum, String serialNo, String serialStatus, String paymentType,
			String accType, String unit, String zmScale) {
		BdSubSerial serial = new BdSubSerial();
		serial.setAccType(accType);
		serial.setAmount(sum.toString());
		serial.setPaymentType(paymentType);
		serial.setSerialNo(serialNo);
		serial.setSubSerialStatus(serialStatus);
		serial.setUnit(unit);
		serial.setZmScale(zmScale);
		return serial;
	}

	private BdSubSerial getAccSerial(BigDecimal accDeduction, BdStudentSerial serial, String stdId) {
		if (accDeduction.compareTo(BigDecimal.ZERO) > 0) { // 插入现金账户支付子流水
			return getSubSerial(accDeduction, serial.getSerialNo(), OrderConstants.SERIAL_STATUS_PROCESS,
					FinanceConstants.PAYMENT_TYPE_DELAY, FinanceConstants.ACC_TYPE_DEMURRAGE,
					FinanceConstants.MONEY_UNIT_RMB, null);
		}
		return null;
	}

	private BdSubSerial getCashSerial(BigDecimal sum, BdStudentSerial serial, String stdId) {
		return getSubSerial(sum, serial.getSerialNo(), OrderConstants.SERIAL_STATUS_PROCESS,
				FinanceConstants.PAYMENT_TYPE_WECHAT, null, FinanceConstants.MONEY_UNIT_RMB, null);
	}

	/**
	 * 初始化辅导书发放记录
	 * 
	 * @param learnId
	 * @param isFD
	 */
	public void initBookSend(String learnId, boolean isFD) {

		BdStudentSend studentSend = new BdStudentSend();
		studentSend.setLearnId(learnId);
		studentSend.setTextbookType(isFD ? EducationConstants.TEXT_BOOK_TYPE_FD : EducationConstants.TEXT_BOOK_TYPE_XK);
		studentSend.setReceiveStatus(StudentConstants.RECEIVE_STATUS_RECEIVED);
		studentSend.setOrderBookStatus(OrderConstants.ORDER_BOOK_NO_SEND);

		String[] testSub = studentSendMapper.selectTestSubByLearnId(learnId);

		int subCount = studentSendMapper.selectTestBookCount(testSub);
		if (subCount > 0) {

			// 不走班主任审核

			BdStudentBaseInfo stdInfo = stdAllMapper.getStudentBaseInfoByLearnId(learnId);
			studentSend.setAddress(stdInfo.getAddress());
			studentSend.setMobile(stdInfo.getMobile());
			studentSend.setUserName(stdInfo.getStdName());
			studentSend.setProvinceCode(stdInfo.getNowProvinceCode());
			studentSend.setCityCode(stdInfo.getNowCityCode());
			studentSend.setDistrictCode(stdInfo.getNowDistrictCode());
			if (StringUtil.hasValue(stdInfo.getAddressEditTime())
					&& DateUtil.judgeIfNewAddresByYear(stdInfo.getGrade())) {
				if (DateUtil.judgeIfNewAddresByYear(stdInfo.getGrade()) && StringUtil.hasValue(stdInfo.getAddress())) {
					studentSend.setAddressStatus(CheckConstants.CHECK_SENATE_PASS_5);
					studentSend.setLogisticsName("jd");
				} else {
					studentSend.setAddressStatus(CheckConstants.CHECK_SENATE_4);
				}
			} else {
				if (StringUtil.hasValue(stdInfo.getAddress()) && StringUtil.isEmpty(stdInfo.getAddressEditTime())) {
					studentSend.setAddressStatus(CheckConstants.CHECK_SENATE_PASS_5);
					studentSend.setLogisticsName("jd");
				} else {
					studentSend.setAddressStatus(CheckConstants.CHECK_SENATE_4);
				}
			}

			studentSendMapper.insertSelective(studentSend);
			studentSendMapper.insertBdTextBookSendFD(studentSend.getSendId(), testSub);
		}

	}

	/**
	 * 缴费成功推送微信公众号提醒信息
	 * 
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
