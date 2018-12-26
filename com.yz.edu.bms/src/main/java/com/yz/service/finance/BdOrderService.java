package com.yz.service.finance;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.constants.FinanceConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.common.BaseInfoMapper;
import com.yz.dao.finance.BdCouponMapper;
import com.yz.dao.finance.BdOrderMapper;
import com.yz.dao.finance.BdPfsnPayeeMapper;
import com.yz.dao.finance.BdStdPayFeeMapper;
import com.yz.dao.finance.BdSubOrderMapper;
import com.yz.dao.finance.StudentMpFlowMapper;
import com.yz.dao.recruit.BdLearnAnnexMapper;
import com.yz.dao.recruit.StudentRecruitMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.communi.Body;
import com.yz.model.finance.BdStudentOrder;
import com.yz.model.finance.BdSubOrder;
import com.yz.model.finance.coupon.BdStdCoupon;
import com.yz.model.finance.order.BdOrderRefund;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.recruit.BdFeeInfo;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdOrderService {

	private static final Logger log = LoggerFactory.getLogger(BdOrderService.class);

	@Autowired
	private BdOrderMapper orderMapper;

	@Autowired
	private BdSubOrderMapper subOrderMapper;

	@Autowired
	private BaseInfoMapper baseInfoMapper;

	@Autowired
	private BdPfsnPayeeMapper payeeMapper;

	@Autowired
	private StudentRecruitMapper studentRecruitMapper;

	@Autowired
	private StudentMpFlowMapper flowMapper;

	@Autowired
	private BdStdPayFeeMapper stdPayFeeMapper;

	@Reference(version = "1.0")
	private AtsAccountApi atsApi;

	@Reference(version = "1.0")
	private AtsAccountApi accountApi;

	private BdLearnAnnexMapper learnAnnexMapper;

	public void initStudentOrder(BdLearnInfo learnInfo) {
		BdStudentOrder orderInfo = new BdStudentOrder();
		orderInfo.setCreateUser(learnInfo.getCreateUser());
		orderInfo.setCreateUserId(learnInfo.getCreateUserId());
		orderInfo.setFinanceCode(learnInfo.getFinanceCode());
		orderInfo.setLearnId(learnInfo.getLearnId());
		orderInfo.setMobile(learnInfo.getMobile());
		orderInfo.setStdName(learnInfo.getStdName());
		orderInfo.setStdId(learnInfo.getStdId());
		orderInfo.setUpdateUser(learnInfo.getUpdateUser());
		orderInfo.setUpdateUserId(learnInfo.getUpdateUserId());
		orderInfo.setUnvsId(learnInfo.getUnvsId());
		orderInfo.setPfsnId(learnInfo.getPfsnId());
		orderInfo.setTaId(learnInfo.getTaId());
		// 查询院校、专业、考区名称
		Map<String, String> names = baseInfoMapper.getUPTName(learnInfo.getUnvsId(), learnInfo.getPfsnId(),
				learnInfo.getTaId());

		String payeeId = payeeMapper.selectPayeeId(learnInfo.getPfsnId());

		orderInfo.setPayeeId(payeeId);
		orderInfo.setUnvsName(names.get("unvsName"));
		orderInfo.setPfsnName(names.get("pfsnName"));
		orderInfo.setTaName(names.get("taName"));
		orderInfo.setOrderStatus(FinanceConstants.ORDER_STATUS_UNPAID);
		String orderNo = orderMapper.selectOrderNoByLearnId(learnInfo.getLearnId());
		if (!StringUtil.hasValue(orderNo)) {
			orderInfo.setOrderNo(IDGenerator.generatororderNo(orderInfo.getFinanceCode()));
			orderMapper.createOrder(orderInfo);
			log.debug("-------------------------- initStudentOrder(1)：学员订单创建成功");
		} else {
			orderInfo.setOrderNo(orderNo);
		}

		// TODO 根据学员阶段 查询需要生成的收费科目订单
		List<BdFeeInfo> itemInfos = orderMapper.getRequireItemCodes(learnInfo);

		if (itemInfos == null || itemInfos.isEmpty()) {
			log.error("-------------------------- initStudentOrder(2)：尚未设置收费科目或者未设置科目与阶段的对应关系，无法创建订单");
			throw new BusinessException("E000040");// 请先配置收费科目
		}

		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal discount = BigDecimal.ZERO;
		BigDecimal payable = BigDecimal.ZERO;

		for (BdFeeInfo feeInfo : itemInfos) {
			BdFeeInfo amountInfo = orderMapper.getFeeInfo(learnInfo.getFeeId(), learnInfo.getOfferId(),
					feeInfo.getItemCode());
			if (amountInfo == null)
				continue;

			if (StringUtil.isEmpty(amountInfo.getAmount()))
				continue;

			feeInfo.setAmount(amountInfo.getAmount());

			String subDiscount = "0.00";

			if (StringUtil.hasValue(amountInfo.getDiscount())) {
				if (FinanceConstants.OFFER_DISCOUNT_TYPE_DISCOUNT.equals(amountInfo.getDiscountType())) { // 折扣
					String count = "0.00";
					count = BigDecimalUtil.multiply(amountInfo.getAmount(), amountInfo.getDiscount());
					subDiscount = BigDecimalUtil.substract(amountInfo.getAmount(), count);

				} else if (FinanceConstants.OFFER_DISCOUNT_TYPE_CUT.equals(amountInfo.getDiscountType())) { // 减免
					subDiscount = amountInfo.getDiscount();
				}

				feeInfo.setDiscount(subDiscount.toString());
			}

			BdSubOrder subOrder = new BdSubOrder();
			subOrder.setFdId(feeInfo.getFdId());
			subOrder.setOdId(feeInfo.getOdId());
			subOrder.setOrderNo(orderInfo.getOrderNo());
			subOrder.setFeeAmount(feeInfo.getAmount());
			subOrder.setOfferAmount(feeInfo.getDiscount());
			subOrder.setPayeeId(payeeId);

			BigDecimal subPayable = AmountUtil.str2Amount(feeInfo.getAmount())
					.subtract(AmountUtil.str2Amount(feeInfo.getDiscount()));
			if (subPayable.compareTo(BigDecimal.ZERO) < 0) {
				subPayable = BigDecimal.ZERO;
			}
			subOrder.setPayable(subPayable.setScale(2, BigDecimal.ROUND_DOWN).toString());
			subOrder.setSubOrderStatus(FinanceConstants.ORDER_STATUS_UNPAID);
			subOrder.setStdId(orderInfo.getStdId());

			subOrder.setItemCode(feeInfo.getItemCode());
			subOrder.setStdName(learnInfo.getStdName());
			subOrder.setFdId(learnInfo.getFeeId());// TODO ?什么鬼东西
			subOrder.setOdId(learnInfo.getOfferId());// TODO ?什么鬼东西
			subOrder.setSubOrderNo(IDGenerator.generatorId());
			subOrderMapper.createSubOrder(subOrder);

			amount = amount.add(AmountUtil.str2Amount(feeInfo.getAmount()));
			discount = discount.add(AmountUtil.str2Amount(feeInfo.getDiscount()));
			payable = payable.add(subPayable);
		}
		log.info("-------------------------- initStudentOrder(2)：学员子订单创建成功");

		// TODO 更新订单总金额
		orderInfo.setAmount(amount.setScale(2, BigDecimal.ROUND_DOWN).toString());
		orderInfo.setDiscount(discount.setScale(2, BigDecimal.ROUND_DOWN).toString());
		orderInfo.setPayable(payable.setScale(2, BigDecimal.ROUND_DOWN).toString());
		orderMapper.updateOrder(orderInfo);
		log.info("-------------------------- initStudentOrder(3)：学员订单金额更新成功");
	}

	@Autowired
	private BdStdPayFeeMapper payMapper;

	@Autowired
	private BdCouponMapper couponMapper;

	// 奖金、考前确认优惠类型
	private final static String[] offerScholarship = { "2", "3", "5", "6", "7", "8", "9", "10", "11", "12", "13", "23",
			"24" };

	// 优惠年级
	private final static String[] offerGrade = { "2018", "2016", "2017" };

	// 普通全额类型
	private final static String[] normalScholarship = { "1" };

	// 国开一百Y1优惠券
	private final static String gkCouponId = "1002";

	// 成教一百Y0优惠券
	private final static String cjCouponId = "1001";

	// 成教一百九十九Y1优惠券
	private final static String gkNCouponId = "1003";

	/**
	 * 学员转报直接退款滞留账户
	 * 
	 * @param learnInfo
	 */
	@SuppressWarnings("unchecked")
	public void studentOrderDirectRefund(Map<String, String> learnInfo) {

		String learnId = learnInfo.get("learnId");
		String stdId = learnInfo.get("stdId");
		String grade = learnInfo.get("grade");
		String scholarship = learnInfo.get("scholarship");
		String targetGrade = learnInfo.get("targetGrade");

		// 已缴纳辅导费用
		String tutorAmount = payMapper.selectPaidTutorAmount(learnId);
		BigDecimal amount = BigDecimal.ZERO;
		if (StringUtil.hasValue(tutorAmount)) {
			amount = AmountUtil.str2Amount(tutorAmount);
		}

		// 优惠时间判断
		boolean offerFlag = false;

		try {
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
			Date endTime = df.parse("2019-02-29 00:00");
			long end = endTime.getTime();
			long now = date.getTime();
			if (now < end) {
				offerFlag = true;
			}

		} catch (ParseException e) {
			log.debug("-------------------- 时间转换错误：" + e.getMessage());
		}

		// 删除2018通用优惠券
		if ("2018".equals(grade)) {
			String[] coupons = new String[] { "7", "67", "68" };
			couponMapper.deleteCouponByStdId(stdId, coupons);
		}

		if (StringUtil.hasValue(offerGrade, grade) && amount.compareTo(BigDecimal.ZERO) > 0) { // 符合优惠年级
			if ("2019".equals(targetGrade)) { // 转报2019操作

				if (StringUtil.hasValue(offerScholarship, scholarship)) { // 奖学金、考前冲刺

					// 退费金额设为0
					amount = BigDecimal.ZERO;

				} else if (StringUtil.hasValue(normalScholarship, scholarship)) { // 普通全额
					if (offerFlag) {

						// 赠送Y0一百优惠券
						BdStdCoupon stdCoupon = new BdStdCoupon();
						stdCoupon.setCouponId(cjCouponId);
						stdCoupon.setStdId(stdId);
						stdCoupon.setScId(IDGenerator.generatorId());
						couponMapper.insertStdCoupon(stdCoupon);
						amount = BigDecimalUtil.substract(amount, new BigDecimal(("100")));
					}
				}
			} else if ("201809".equals(targetGrade)) { // 转报201803 操作
				if (StringUtil.hasValue(offerScholarship, scholarship)) { // 奖学金、考前冲刺

					// 退费金额设为0
					amount = BigDecimal.ZERO;

					if (offerFlag) { // 赠送Y1一百优惠券
						BdStdCoupon stdCoupon = new BdStdCoupon();
						stdCoupon.setCouponId(gkCouponId);
						stdCoupon.setStdId(stdId);
						stdCoupon.setScId(IDGenerator.generatorId());
						couponMapper.insertStdCoupon(stdCoupon);
					}
				}
			}

		} else if (offerFlag && "2019".equals(grade) && ("201803".equals(targetGrade) || "201809".equals(targetGrade))
				&& amount.compareTo(new BigDecimal("199")) >= 0) {
			// 赠送Y1一百九十九优惠券
			BdStdCoupon stdCoupon = new BdStdCoupon();
			stdCoupon.setCouponId(gkNCouponId);
			stdCoupon.setStdId(stdId);
			stdCoupon.setScId(IDGenerator.generatorId());
			couponMapper.insertStdCoupon(stdCoupon);
			// amount = BigDecimalUtil.substract(amount, new
			// BigDecimal(("199")));
			amount = BigDecimal.ZERO;
		}

		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			amount = BigDecimal.ZERO;
		}

		log.debug("学员id：" + learnInfo + "------------------ 退款总金额为：" + amount);

		BaseUser user = SessionUtil.getUser();

		BdOrderRefund refund = new BdOrderRefund();
		if (null != user) {
			refund.setEmpId(user.getEmpId());
			refund.setUpdateUser(user.getRealName());
			refund.setUpdateUserId(user.getUserId());
		}
		refund.setLearnId(learnId);
		refund.setRefundAmount(amount.toString());
		refund.setStdId(stdId);

		// 插入订单退款信息

		if (amount.compareTo(BigDecimal.ZERO) > 0) {

			refund.setRefundId(IDGenerator.generatorId());
			orderMapper.insertRefundInfo(refund);
			// 设置转账对象
			Body body = new Body();
			body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
			body.put("stdId", stdId);
			body.put("amount", refund.getRefundAmount());
			body.put("action", FinanceConstants.ACC_ACTION_IN);
			body.put("excDesc", String.format("学员由%s级转报后退款", grade));// TODO
																		// 描述补充
																		// 2017/7/28
			// to 倪宇鹏
			if (null != user) {
				body.put("updateUser", user.getRealName());
				body.put("updateUserId", user.getUserId());
				body.put("createUser", user.getRealName());
				body.put("createUserId", user.getUserId());
			}
			body.put("mappingId", refund.getOrderNo());

			accountApi.trans(body);
		}
		// 修改订单状态
		orderMapper.finishiRefund(learnId);

	}

	/**
	 * 学员退学退费状态更改
	 * 
	 * @param learnId
	 * @param checkType
	 * @return
	 */
	public String studentOrderRefund(String learnId,String stdId, String checkType) {

		int count = orderMapper.selectCountOrderRefunde(learnId, checkType);
		if (count > 0) {
			return null;
		}
		BaseUser user = SessionUtil.getUser();

		BdOrderRefund refund = new BdOrderRefund();
		refund.setEmpId(user.getEmpId());
		refund.setLearnId(learnId);
		refund.setUpdateUser(user.getRealName());
		refund.setUpdateUserId(user.getUserId());
		refund.setCheckType(checkType);
		refund.setStdId(stdId);
		orderMapper.refundingOrder(refund.getLearnId());
		refund.setRefundId(IDGenerator.generatorId());
		orderMapper.insertRefundInfo(refund);

		return refund.getRefundId();
	}

	/**
	 * 修改退费金额
	 * 
	 * @param learnId
	 * @param items
	 */
	public void updateOrderRefundAmount(String learnId, List<BdStdPayInfoResponse> items) {
		String amount = "0.00";

		if (items != null && items.size() > 0) {

			for (BdStdPayInfoResponse item : items) {
				BigDecimal refundAmount = AmountUtil.str2Amount(item.getRefundAmount());
				if (refundAmount.compareTo(BigDecimal.ZERO) < 0) {
					throw new BusinessException("E000064"); // 金额必须大于0
				}
				amount = BigDecimalUtil.add(amount, item.getRefundAmount());
				item.setRefundAmount(refundAmount.toString());
			}
		}
		log.debug("------------------ 退款总金额为：" + amount);

		orderMapper.updateOrderRefundAmount(learnId, items, amount);
	}

	/**
	 * 退款驳回
	 * 
	 * @param learnId
	 */
	public void rejectRefund(String learnId) {
		orderMapper.rejectRefund(learnId);
	}

	/**
	 * 完成退款
	 * 
	 * @param learnId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void finishRefund(String learnId) {
		// 先获取到订单数据，再去修改订单状态，方便后续操作
		List<BdSubOrder> order = subOrderMapper.getSubOrders(learnId);
		// 修改订单状态
		orderMapper.finishiRefund(learnId);

		BdLearnInfo learnInfo = stdPayFeeMapper.selectLearnInfoByLearnId(learnId);
		// 退款划账
		List<BdOrderRefund> refunds = orderMapper.selectRefundInfo(learnId);
		BdOrderRefund refund = null;
		if (refunds != null && refunds.size() > 0) {
			refund = refunds.get(0);
		}

		if (null == refund) {
			return;
		}
		orderMapper.finishStudentRefund(refund.getRefundId());

		if (AmountUtil.str2Amount(refund.getRefundAmount()).compareTo(BigDecimal.ZERO) > 0) {
			// 设置转账对象
			Body body = new Body();
			body.put("accType", FinanceConstants.ACC_TYPE_NORMAL);
			body.put("stdId", refund.getStdId());
			body.put("amount", refund.getRefundAmount());
			body.put("action", FinanceConstants.ACC_ACTION_IN);
			// TODO
			// 描述补充
			// 2017/7/28
			// to
			// 倪宇鹏
			body.put("excDesc", String.format("%s级退学退款", learnInfo.getGrade()));
			body.put("mappingId", refund.getOrderNo());
			accountApi.trans(body);
		}

	}

	/**
	 * 退学退回智米 只按照新系统的赠送记录来退回 查找订单记录和学生流水记录，整合mappingId，通过MappingId查找赠送流水，再退智米
	 * 
	 * @param learnId
	 */
	public void giveBackZhimi(String learnId) {
		// 根据学业ID找到用户ID
		Map<String, String> stdRelation = studentRecruitMapper.getUserRelationByLearnId(learnId);
		// 获取退费信息
		List<BdStdPayInfoResponse> bdStdPayInfoList = payMapper.selectPayableInfo(learnId,
				FinanceConstants.ORDER_STATUS_REFUND);
		List<Body> transList = new ArrayList<>();
		String strItemName = "";
		for (BdStdPayInfoResponse payInfo : bdStdPayInfoList) {
			if (!"".equals(payInfo.getItemYear()) && payInfo.getItemYear() != null) {
				strItemName += payInfo.getItemName() + ",";
			}
		}
		String[] itemName = "".equals(strItemName) ? null
				: strItemName.substring(0, strItemName.length() - 1).split(",");
		// 获取自己和上级的缴费赠送智米记录
		List<Body> thisTransList = payMapper.selectStudentSerialByLearnId(learnId);
		Body condition = flowMapper.getCondition(learnId);// 获取个人信息
		// 判断存不存在记录，存在记录就增加部分信息
		if (thisTransList != null && thisTransList.size() > 0) {
			thisTransList.forEach(v -> {
				// 判断记录是不是自己的，是自己的就填写自己的备注，否则就填写上级对应的备注
				if (v.getString("userId").equals(stdRelation.get("user_id"))) {
					v.put("excDesc", "退" + StringUtil.join(itemName, ",") + "费用扣减智米");
				} else {
					v.put("excDesc", "下线学员【姓名：" + condition.get("stdName") + "，手机：" + condition.get("mobile") + "，身份证："
							+ condition.get("idCard") + "】退学扣减智米");
				}
				v.put("action", FinanceConstants.ACC_ACTION_OUT);
			});
			transList.addAll(thisTransList);
		}
		// 如果存在记录，就发送调用
		if (transList.size() > 0) {
			atsApi.transMore(transList);
		}
	}

	/**
	 * 个人缴费智米退回匹配
	 * 
	 * 不再使用
	 * 
	 * @param learnId
	 * @param itemCodes
	 * @param itemYears
	 * @param payable
	 * @return
	 */
	// @Deprecated
	// @Transactional(isolation = Isolation.READ_UNCOMMITTED)
	// public CommunicationMap chargeAwardPersonalBack(String learnId, String[]
	// itemCodes, String[] itemYears,
	// String payable) {
	// List<Map<String, String>> relation = flowMapper.getRelation(learnId);
	// if (relation != null && relation.size() > 1) {
	// log.debug("-------------------------- 学业[" + learnId + "]查询到多个用户关系
	// 不做扣减处理");
	// return null;
	// }
	//
	// MpCondition condition = flowMapper.getCondition(learnId);
	// if (condition.get("userId") != null) {
	// String stdId = condition.getString("stdId");
	//
	// List<Map<String, String>> learnList = flowMapper.getHistoryLearn(stdId,
	// learnId);
	// condition.put("learnList", learnList);
	// condition.put("lSize", learnList != null ? learnList.size() : 0);
	// condition.put("itemCodes", itemCodes);
	// condition.put("itemYears", itemYears);
	// condition.put("payable", payable);
	// // 个人缴费赠送流程
	// Flow iChargeFlow = MpContext.getiChargeFlow();
	// MpResult iResult = null;
	// if (iChargeFlow != null)
	// iResult = iChargeFlow.match(condition);
	// if (iResult != null && iResult.hasValue()) {
	// iResult.getTarget().put("stdName", condition.get("stdName"));
	// iResult.getTarget().put("mobile", condition.get("mobile"));
	// iResult.getTarget().put("idCard", condition.get("idCard"));
	// return iResult.getTarget();
	// } else {
	// // 如果没有匹配到规则自定结果返回
	// CommunicationMap communicationMap = new CommunicationMap();
	// /*
	// * communicationMap.put("userId", condition.get("userId"));
	// * communicationMap.put("stdId", condition.get("stdId"));
	// * communicationMap.put("accType",
	// * FinanceConstants.ACC_TYPE_ZHIMI);
	// * communicationMap.put("action",
	// * FinanceConstants.ACC_ACTION_OUT);
	// * communicationMap.put("amount", new
	// * BigDecimal(payable).setScale(2, BigDecimal.ROUND_DOWN));
	// * communicationMap.put("ruleType",
	// * FinanceConstants.AWARD_RT_MARKTING);
	// * communicationMap.put("triggerUserId", null);
	// */
	// communicationMap.put("stdName", condition.get("stdName"));
	// communicationMap.put("mobile", condition.get("mobile"));
	// communicationMap.put("idCard", condition.get("idCard"));
	// return communicationMap;
	// }
	// }
	// return null;
	// }

	/**
	 * 上线智米退回匹配
	 * 
	 * 不再使用
	 * 
	 * @param learnId
	 * @param itemCodes
	 * @param itemYears
	 * @param payable
	 * @return
	 */
	// @Deprecated
	// @Transactional(isolation = Isolation.READ_UNCOMMITTED)
	// public CommunicationMap chargeAwardBack(String learnId, String[]
	// itemCodes, String[] itemYears, String payable) {
	// List<Map<String, String>> relation = flowMapper.getRelation(learnId);
	// if (relation != null && relation.size() > 1) {
	// log.debug("-------------------------- 学业[" + learnId + "]查询到多个用户关系
	// 不做扣减处理");
	// return null;
	// }
	//
	// MpCondition condition = flowMapper.getCondition(learnId);
	// if (condition.get("pId") != null) {
	// String stdId = condition.getString("stdId");
	// String pId = condition.getString("pId");
	//
	// List<Map<String, String>> learnList = flowMapper.getHistoryLearn(stdId,
	// learnId);
	// condition.put("learnList", learnList);
	// condition.put("lSize", learnList != null ? learnList.size() : 0);
	// condition.put("itemCodes", itemCodes);
	// condition.put("itemYears", itemYears);
	// condition.put("payable", payable);
	//
	// // 上线缴费赠送流程
	// Flow chargeFlow = MpContext.getChargeFlow();
	// MpResult result = null;
	// if (chargeFlow != null)
	// result = chargeFlow.match(condition);
	//
	// if (result != null && result.hasValue()) {
	// CommunicationMap award = result.getTarget();
	// if (award != null && award.size() > 0 && StringUtil.hasValue(pId)) {
	// return award;
	// }
	// }
	// }
	//
	// return null;
	// }

	// 处理2017学员缴费订单
	@SuppressWarnings("unchecked")
	public void getAccount(String stdId, String amount, String orderNo) {
		String grade = "";
		BdStudentOrder order = orderMapper.selectOrderByOrderNo(orderNo);
		grade = learnAnnexMapper.selectGradeByLearnId(order.getLearnId());
		// 设置转账对象
		Body body = new Body();
		body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
		body.put("stdId", stdId);
		body.put("amount", amount);
		body.put("action", FinanceConstants.ACC_ACTION_IN);
		body.put("excDesc", "缴" + grade + "级费用金额不足,退款到滞留账户");
		body.put("mappingId", orderNo);

		accountApi.trans(body);
	}
}
