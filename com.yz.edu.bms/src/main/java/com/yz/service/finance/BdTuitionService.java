package com.yz.service.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.AtsAccountApi;
import com.yz.api.GsLotteryApi;
import com.yz.api.UsCertApi;
import com.yz.api.UsInfoApi;
import com.yz.api.UsTaskCardApi;
import com.yz.conf.YzSysConfig;
import com.yz.constants.CheckConstants;
import com.yz.constants.EducationConstants;
import com.yz.constants.FeeConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.OrderConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.educational.BdStudentSendMapper;
import com.yz.dao.enroll.BdStdEnrollMapper;
import com.yz.dao.finance.BdFeeItemMapper;
import com.yz.dao.finance.BdStdPayFeeMapper;
import com.yz.dao.finance.StudentMpFlowMapper;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.CommunicationMap;
import com.yz.model.UserReChargeEvent;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.communi.Body;
import com.yz.model.educational.BdStudentSend;
import com.yz.model.finance.BdOrder;
import com.yz.model.finance.coupon.BdCoupon;
import com.yz.model.finance.coupon.BdCouponDetail;
import com.yz.model.finance.fee.FeeItemForm;
import com.yz.model.finance.feeitem.BdFeeItem;
import com.yz.model.finance.stdfee.BdPayInfo;
import com.yz.model.finance.stdfee.BdSerialSurplus;
import com.yz.model.finance.stdfee.BdStudentSerial;
import com.yz.model.finance.stdfee.BdSubSerial;
import com.yz.model.oa.OaCampusInfo;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.transfer.BdStudentModify;
import com.yz.redis.RedisService;
import com.yz.service.transfer.BdStudentModifyService;
import com.yz.task.YzTaskConstants;
import com.yz.trace.wrapper.TraceEventWrapper;
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
 * @Create Date: 2017年11月4日.
 *
 */
@Service
@Transactional
public class BdTuitionService {

	private static final Logger log = LoggerFactory.getLogger(BdTuitionService.class);

	@Autowired
	private BdStdPayFeeMapper payMapper;

	@Reference(version = "1.0")
	private AtsAccountApi atsApi;

	@Reference(version = "1.0")
	private GsLotteryApi lotteryApi;

	@Reference(version = "1.0")
	private UsInfoApi usApi;

	@Reference(version = "1.0")
	private UsCertApi certApi;

	@Reference(version = "1.0")
	private UsTaskCardApi usTaskCardApi;

	@Autowired
	private StudentMpFlowMapper flowMapper;

	@Autowired
	private BdReptService reptService;

	@Autowired
	private BdStudentModifyService modifyService;

	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private StudentAllMapper studentAllMapper;

	@Autowired
	private BdStudentSendMapper studentSendMapper;

	@Autowired
	private StudentAllMapper stdAllMapper;

	@Autowired
	private BdStdEnrollMapper stdEnrollMapper;
	
	@Autowired
	private BdFeeItemMapper itemMapper;

	/**
	 * 后台缴费接口
	 * 
	 * @param payInfo
	 * @param isPrint
	 *            是否需要打印收据
	 * @param isAuto
	 *            是否异动自动缴费
	 * @return
	 */
	public Map<String, String> payTuition(BdPayInfo payInfo, boolean isPrint, boolean isAuto) {

		/*
		 * if (StringUtil.hasValue(payInfo.getCouponId())) { throw new
		 * BusinessException("E000092"); // 优惠券暂时无法使用 }
		 */

		Map<String, String> result = new HashMap<String, String>();

		// 学业ID
		String learnId = payInfo.getLearnId();

		// 订单信息
		BdOrder order = payMapper.selectOrder(learnId);

		// 应付金额
		List<FeeItemForm> items = payMapper.selectAmountByItems(learnId, payInfo.getItemCodes());

		// 计算应付金额
		BigDecimal allAmount = BigDecimal.ZERO;
		for (FeeItemForm amt : items) {
			BigDecimal amtAmount = AmountUtil.str2Amount(amt.getAmount());
			allAmount = BigDecimalUtil.add(allAmount, amtAmount);
			if (FeeConstants.FEE_ITEM_TYPE_COACH.equals(amt.getItemType())
					&& AmountUtil.str2Amount(payInfo.getZmDeduction()).compareTo(BigDecimal.ZERO) > 0) {
				throw new BusinessException("E60025"); // 智米不能抵扣辅导费用
			}
		}

		String grade = "";
		BdLearnInfo learnInfo = stdEnrollMapper.selectLearnInfoByLearnId(learnId);
		if (learnInfo != null) {
			grade = learnInfo.getGrade();
		}

		BaseUser user = SessionUtil.getUser();
		OaCampusInfo campus = payMapper.selectCampusInfoByEmpId(user.getEmpId());
		String financeCode = null;

		if (null != campus) {
			financeCode = campus.getFinanceNo();
		}
		// 所有流水统一标识
		String serialMark = IDGenerator.generatororderNo(financeCode);

		// 总共应缴
		BigDecimal allPayable = BigDecimal.ZERO;

		// 用户滞留账户抵扣金额
		BigDecimal accDeduction = AmountUtil.str2Amount(payInfo.getAccDeduction());

		if (accDeduction.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException("E60013"); // 现金抵扣不能小于0
		}

		// 智米抵扣比例
		BigDecimal zmScale = AmountUtil.str2Amount(yzSysConfig.getZhiMiScale());

		// 用户智米抵扣金额
		BigDecimal zmDeduction = BigDecimalUtil.divide(AmountUtil.str2Amount(payInfo.getZmDeduction()), zmScale);

		if (zmDeduction.compareTo(BigDecimal.ZERO) < 0) {
			throw new BusinessException("E60014"); // 智米抵扣不能小于0
		}

		// 动账集合
		List<Body> transList = new ArrayList<Body>();

		List<BdCoupon> coupons = new ArrayList<BdCoupon>();

		// 优惠券优惠金额
		// BigDecimal couponAmount = BigDecimal.ZERO;

		if (null != payInfo.getCoupons() && !payInfo.getCoupons().isEmpty()) {

			// 优惠券信息
			for (BdCoupon bdCoupon : payInfo.getCoupons()) {
				Map<String, String> std = payMapper.selectStdUserIdByLearnId(learnId);
				String userId = null;
				if (StringUtil.hasValue(std.get("userId"))) {
					userId = std.get("userId");
				}
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

			if (payInfo.getCoupons().isEmpty()) {
				throw new BusinessException("E60021"); // 无效优惠券
			}

			/*
			 * for (BdCoupon bdCoupon : coupons) { couponAmount =
			 * BigDecimalUtil.add(AmountUtil.str2Amount(bdCoupon.getAmount()),
			 * couponAmount); }
			 */
		}

		List<BdStudentSerial> serials = new ArrayList<BdStudentSerial>();

		for (FeeItemForm item : items) {
			BdStudentSerial serial = new BdStudentSerial();
			serial.setItemCode(item.getItemCode());
			serial.setItemAmount(item.getAmount());
			serial.setItemType(item.getItemType());
			serial.setZmCouponNum(item.getZmCouponNum());
			serial.setDelayNum(item.getDelayNum());
			serial.setSerialMark(serialMark);
			serial.setEmpId(user.getUserId());
			serial.setEmpName(user.getRealName());
			serial.setUpdateUser(user.getRealName());
			serial.setUpdateUserId(user.getUserId());
			serial.setCreateUser(user.getRealName());
			serial.setCreateUserId(user.getUserId());
			serial.setRemark(payInfo.getRemark());
			serials.add(serial);

		}

		// 根据智米优惠券优惠顺序排序
		Collections.sort(serials, new Comparator<BdStudentSerial>() {
			public int compare(BdStudentSerial arg0, BdStudentSerial arg1) {
				// 定义如何比较
				return Integer.valueOf(arg0.getZmCouponNum()) - Integer.valueOf(arg1.getZmCouponNum());
			}
		});

		/**
		 * 智米、优惠券抵扣
		 */
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

			if (null != payInfo.getCoupons() && !payInfo.getCoupons().isEmpty()) {

				for (BdCoupon coupon : payInfo.getCoupons()) {

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

			}

			// 组合支付
			if (couponCount.compareTo(BigDecimal.ZERO) > 0) { // 优惠券抵扣接大于0 为组合支付
				isAssembly = FinanceConstants.PAYMENT_IS_ASSEMBLY_YES;
			}

			sum = BigDecimalUtil.substract(sum, couponCount); // 应付金额减去优惠金额
			if (sum.compareTo(BigDecimal.ZERO) <= 0) { // 如果优惠券抵扣所有金额
				sum = BigDecimal.ZERO; // 应付金额设为0

			}

			BigDecimal zmSubDeduct = BigDecimal.ZERO;
			// 如果应缴大于0，智米抵扣大于0
			if (sum.compareTo(BigDecimal.ZERO) > 0 && zmDeduction.compareTo(BigDecimal.ZERO) > 0) {

				/**
				 * 智米抵扣
				 */
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

			}

			// 流水赋值
			serial = getSerial(serial, order, sum.toString(), couponCount.toString(), isAssembly, payInfo,
					OrderConstants.SERIAL_STATUS_PROCESS);
			// 子流水赋值
			BdSubSerial cSerial = getCouponSerial(couponCount, coupons, order, OrderConstants.SERIAL_STATUS_UNCHECK);
			if (null != cSerial) {
				subSerials.add(cSerial);
			}

			// 智米流水
			BdSubSerial zmSerial = getZMSerial(zmSubDeduct, serial, order.getStdId(), zmScale.toString());
			if (null != zmSerial) {
				subSerials.add(zmSerial);
			}

			serial.setSubSerials(subSerials);

		}

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

				BdSubSerial accSerial = getAccSerial(accSubDeduct, serial, order.getStdId());

				if (null == subSerials) {
					subSerials = new ArrayList<BdSubSerial>();
				}

				if (null != accSerial) {
					subSerials.add(accSerial);
				}

			}
			if (sum.compareTo(BigDecimal.ZERO) > 0
					|| AmountUtil.str2Amount(serial.getItemAmount()).compareTo(BigDecimal.ZERO) == 0) {
				BdSubSerial cashSerial = getCashSerial(sum, serial, order.getStdId(), payInfo.getPaymentType());
				if (null != cashSerial) {
					subSerials.add(cashSerial);
				}

			}
			serial.setSubSerials(subSerials);

			allPayable = BigDecimalUtil.add(allPayable, sum);

		}

		BdSerialSurplus surplus = new BdSerialSurplus();
		if (zmDeduction.compareTo(BigDecimal.ZERO) > 0) {

			surplus.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
			surplus.setAmount(zmDeduction.toString());
			surplus.setSerialMark(serialMark);
			surplus.setUnit(FinanceConstants.ACC_UNIT_ZHIMI);

			surplus.setSurplusId(IDGenerator.generatorId());
			payMapper.insertSerialSurplus(surplus);

		}

		// 全部抵扣 完成订单
		for (BdStudentSerial serial : serials) {

			// 记录付款前滞留账户余额
			String beforeAmount = null;
			Map<String, String> beforeMap = atsApi.getAccount(null, order.getStdId(), null,
					FinanceConstants.ACC_TYPE_DEMURRAGE);
			if (null != beforeMap) {
				beforeAmount = beforeMap.get("accAmount");
			}

			serial.setDemurrageBefore(beforeAmount);
			serial.setPayTime(DateUtil.getNowDateAndTime());
			serial.setSerialStatus(OrderConstants.SERIAL_STATUS_UNCHECK);
			for (BdSubSerial subSerial : serial.getSubSerials()) {
				subSerial.setSubSerialStatus(OrderConstants.SERIAL_STATUS_UNCHECK);

				if (FinanceConstants.PAYMENT_TYPE_ZM.equals(subSerial.getPaymentType())) {
					// 动账集合添加
					transList.add(amountTrans(order.getStdId(), subSerial.getAmount(), FinanceConstants.ACC_ACTION_OUT,
							serial.getSerialNo(), String.format("缴纳%s级学费，智米账户抵扣", grade),
							FinanceConstants.ACC_TYPE_ZHIMI));
					continue;
				} else if (FinanceConstants.PAYMENT_TYPE_COUPON.equals(subSerial.getPaymentType())) {
					// 修改优惠券为已使用
					for (BdCoupon coupon : payInfo.getCoupons()) {

						payMapper.updateCouponUsed(coupon.getScId(), order.getStdId());
					}
					continue;
				} else if (FinanceConstants.PAYMENT_TYPE_DELAY.equals(subSerial.getPaymentType())) {
					// 动账集合添加
					transList.add(amountTrans(order.getStdId(), subSerial.getAmount(), FinanceConstants.ACC_ACTION_OUT,
							serial.getSerialNo(), String.format("缴纳%s级学费，滞留账户抵扣", grade),
							FinanceConstants.ACC_TYPE_DEMURRAGE));
					continue;
				}

			}

		}
		
		if (null != surplus) {
			if (FinanceConstants.ACC_TYPE_ZHIMI.equals(surplus.getAccType())) {
				// 动账集合添加
				transList.add(amountTrans(order.getStdId(), surplus.getAmount(), FinanceConstants.ACC_ACTION_OUT,
						serialMark, String.format("缴纳%s级学费，智米账户抵扣", grade), FinanceConstants.ACC_TYPE_ZHIMI));
			}
			surplus.setStatus(FinanceConstants.STUDENT_SERIAL_STATUS_FINISHED);
			payMapper.updateSerialSurplus(surplus);
		}

		// 插入流水
		insertSerial(serials);

		// 修改子订单状态
		payMapper.updateSubOrderPaid(learnId, payInfo.getItemCodes());

		if (isPrint) { // 需要打印收据，插入收据管理信息
			result.put("stdId", order.getStdId());
			result.put("learnId", payInfo.getLearnId());
			result.put("serialMark", serialMark);
			result.put("stdName", order.getStdName());
			String reptId = reptService.insertStudentRept(result);
			result.put("reptId", reptId);
		}

		// 动账
		if (transList.size() > 0) {
			atsApi.transMore(transList);
		}

		//// 记录学员缴费记录
		StringBuffer logText = new StringBuffer();
		try {

			logText.append("学员缴费处理，订单号:" + order.getOrderNo()).append("<br/>");
			for (BdStudentSerial serial : serials) {
				// 记录付款完成滞留账户余额
				String afterAmount = null;
				Map<String, String> afterMap = atsApi.getAccount(null, order.getStdId(), null,
						FinanceConstants.ACC_TYPE_DEMURRAGE);
				if (null != afterMap) {
					afterAmount = afterMap.get("accAmount");
				}
				serial.setDemurrageAfter(afterAmount);
				payMapper.updateAfterAmount(serial);

				String[] itemNames = payMapper.selectItemNameBySerialNo(serial.getSerialNo());
				String item = "";
				for (String itemName : itemNames) {
					item = item + itemName + ",";
				}
				if (StringUtil.hasValue(item)) {
					item = item.substring(0, item.length() - 1);
				}
				logText.append(serial.getItemCode() + ":" + item + ", 实缴金额：" + serial.getPayable());
				if (StringUtil.hasValue(serial.getDeduction()) && Double.valueOf(serial.getDeduction()) > 0) {
					logText.append("抵扣：" + serial.getDeduction());
				}
				logText.append("<br/>");
			}

			String[] itemNames = payMapper.selectItemNameBySerialMark(serialMark);

			if (!isAuto) {
				/**
				 * 发送微信推送
				 */
				sendWechatMsg(order.getStdName(), allPayable.toString(), itemNames, learnId);
				List<String> itemCodes =  payMapper.selectItemCodesBySerialMark(serialMark);
				
				// 判断是否是辅导教材,如果是成教Y0辅导费,插入发书记录信息 20180510版
				if (null != learnInfo) {
					String stdStage = learnInfo.getStdStage();
					String recruitType = learnInfo.getRecruitType();
					if (StudentConstants.STD_STAGE_PURPOSE.equals(stdStage)
							&& StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
						for (String itemCode : itemCodes) {
							BdFeeItem item = itemMapper.selectItemInfoById(itemCode);
							if (FeeConstants.FEE_ITEM_TYPE_COACH.equals(item.getItemType())) {
								initBookSend(learnId, true);
								// 修改学业辅导费审核时间
								break;
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.debug("--------------------------------- 学员缴费消息推送失败：" + e.getMessage());
		}

		result.put("resCode", "SUCCESS");

		// 判断是否需要更新跟进关系
		if (needRefreshRelation(payInfo.getItemCodes())) {
			log.debug("缴费后刷新跟进关系----------");
			Body recruitMap = flowMapper.getRecruitMapBySerialMark(serialMark);
			if (recruitMap != null && recruitMap.size() > 0) {
				String stdId = recruitMap.getString("stdId");
				String empStatus = recruitMap.getString("empStatus");
				// 如果招生老师离职
				boolean isIn = StringUtil.hasValue(empStatus) && !"2".equals(empStatus);
				if (!isIn) {
					recruitMap.remove("empId");
					recruitMap.remove("dpId");
					recruitMap.remove("campusId");
				}
				// 查询学员与用户关系
				List<Map<String, String>> relations = flowMapper.getRelationsByStd(stdId);
				boolean needRefresh = false;
				if (relations == null || relations.isEmpty()) {
					needRefresh = true;
					log.error("----------------- 学员[" + stdId + "]还没有用户关系绑定");
				} else if (relations.size() == 1) {
					needRefresh = true;
					Map<String, String> rm = relations.get(0);
					recruitMap.putAll(rm);
				} else {
					log.error("----------------- 学员[" + stdId + "]对应多个用户关系 不做处理");
				}

				if (needRefresh) {
					Map<String, String> userMap = certApi.refreshFollow(recruitMap);

					if (userMap != null && userMap.size() > 0) {
						String userId = userMap.get("userId");
						BdStudentBaseInfo baseInfo = new BdStudentBaseInfo();
						baseInfo.setStdId(stdId);
						baseInfo.setUserId(userId);
						studentAllMapper.updateBaseInfo(baseInfo);
						/*
						 * int count = flowMapper.countRelation(userMap); if
						 * (count > 0) { flowMapper.updateRelation(userMap); }
						 * else { if (StringUtil.hasValue(stdId)) {
						 * flowMapper.deleteRelation(stdId); }
						 * flowMapper.insertRelation(userMap); }
						 */
					}
				}
			}
		}

		// 判断是否赠送智米
		if (payInfo.getGiveZhimi()) {
			try {
				// 智米赠送规则
				// List<CommunicationMap> mList =
				chargeAward(learnId, payInfo.getItemCodes(), payInfo.getYears(), allPayable.toString(),
						order.getOrderNo());
				// atsApi.awardMore(mList);
				// TODO 代码报错，可能导致智米已赠送，但事务回滚了
			} catch (Exception e) {
				log.error("-------------------订单：[" + order.getOrderNo() + "] 智米赠送失败", e);
			}
		}

		try {
			// 添加退学 学员变更记录
			BdStudentModify studentModify = new BdStudentModify();
			studentModify.setLearnId(learnId);
			studentModify.setStdId(order.getStdId());
			studentModify.setExt1(logText.toString());
			studentModify.setIsComplete("1");
			studentModify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_normalopt_5);
			modifyService.addStudentModifyRecord(studentModify);
		} catch (Exception e) {
			log.error("-------------------学员变更记录添加失败", e);
		}
		String stdUserId = payMapper.selectUserId(order.getStdId());
		if (!isAuto) {
			// 赠送抽奖机会
			// gainTicket(items, learnId, stdUserId);
		}

		//记录任务卡进度
		usTaskCardApi.addUsTaskCardDetail(stdUserId,payInfo.getItemCodes(),learnId);
		return null;

	}

	private void gainTicket(List<FeeItemForm> items, String learnId, String stdUserId) {
		// 是否赠送抽奖机会
		boolean hasGainTicket = false;

		for (FeeItemForm item : items) {
			BdLearnInfo learnInfo = payMapper.selectLearnInfoByLearnId(learnId);
			if (null != learnInfo) {
				String recruitType = learnInfo.getRecruitType();
				if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
					if ("Y0".equals(item.getItemCode())) {
						hasGainTicket = true;
					}
				} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
					if ("Y1".equals(item.getItemCode())) {
						hasGainTicket = true;
					}
				}
			}
		}

		if (hasGainTicket) {
			// 赠送抽奖机会
			lotteryApi.gainLoterryTicket(stdUserId);
		}
	}

	/**
	 * 发送微信推送
	 * 
	 * @param stdName
	 * @param amount
	 * @param itemNames
	 * @param learnId
	 */

	private void sendWechatMsg(String stdName, String amount, String[] itemNames, String learnId) {
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
			/*
			 * if (StringUtil.hasValue(map.get("tutorUserId"))) { Body body =
			 * new Body(); body.put("userId", map.get("tutorUserId")); Object
			 * obj = usApi.getOpenIdByUserId(body); if (null != obj) {
			 * tutorOpenId = (String) obj; } }
			 */
			// 发送模板通知
			String grade = payMapper.selectGradeByLearnId(learnId);
			if ("201809".equals(grade) || "201803".equals(grade) || "201703".equals(grade)) {
				for (int i = 0; i < itemNames.length; i++) {
					// item.replace("年", "学期");
					itemNames[i] = itemNames[i].replaceAll("年", "学期");
				}
			}
			// 推送微信公众号信息
			sendTuitionMsg(stdName, amount, null, itemNames, learnId, openId, tutorOpenId, recruitOpenId);
		}

	}

	private void insertSerial(List<BdStudentSerial> serials) {
		for (BdStudentSerial serial : serials) {
			serial.setSerialNo(IDGenerator.generatororderNo(serial.getFinanceCode()));
			for (BdSubSerial s : serial.getSubSerials()) {
				s.setSubSerialNo(IDGenerator.generatorId());
			}
			payMapper.insertSerialBatch(serial);
		}
	}

	@SuppressWarnings("unchecked")
	private Body amountTrans(String stdId, String accAmount, String accAction, String serialNo, String excDesc,
			String accType) {

		// 设置转账对象
		Body body = new Body();
		body.put("accType", accType);
		body.put("stdId", stdId);
		body.put("amount", accAmount);
		body.put("action", accAction);
		body.put("excDesc", excDesc);
		body.put("mappingId", serialNo);

		return body;
	}

	private BdStudentSerial getSerial(BdStudentSerial serial, BdOrder order, String itemPayable, String couponCount,
			String isAssembly, BdPayInfo payInfo, String serialStatus) {

		BaseUser user = SessionUtil.getUser();
		OaCampusInfo campus = payMapper.selectCampusInfoByEmpId(user.getEmpId());

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
		serial.setPayeeId(order.getPayeeId());
		serial.setIsAssembly(isAssembly);

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

				cSerial.setCouponId(cSerial.getCouponId() + "," + coupon.getCouponId());
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

	private BdSubSerial getCashSerial(BigDecimal sum, BdStudentSerial serial, String stdId, String paymentType) {
		return getSubSerial(sum, serial.getSerialNo(), OrderConstants.SERIAL_STATUS_PROCESS, paymentType, null,
				FinanceConstants.MONEY_UNIT_RMB, null);
	}

	/**
	 * 智米赠送
	 * 
	 * @param learnId
	 * @param itemCodes
	 * @param itemYears
	 * @param payable
	 * @param orderNo
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	void chargeAward(String learnId, String[] itemCodes, String[] itemYears, String payable, String orderNo) {
		List<CommunicationMap> mList = new ArrayList<CommunicationMap>();

		List<Map<String, String>> relation = flowMapper.getRelation(learnId);

		if (relation != null && relation.size() > 1) {
			log.info("-------------------------- 学业[" + learnId + "]查询到多个用户关系 不做赠送处理");
			return;
		}

		Body condition = flowMapper.getCondition(learnId);

		String stdId = condition.getString("stdId");

		String pId = condition.getString("pId");

		List<Map<String, String>> learnList = flowMapper.getHistoryLearn(stdId, learnId);

		// 构建缴费赠送智米事件
		UserReChargeEvent event = new UserReChargeEvent();
		event.setPayable(payable);
		event.setMappingId(orderNo);
		event.setCreateTime(
				DateUtil.convertDateStrToDate(condition.getString("createTime", ""), DateUtil.YYYYMMDDHHMMSS_SPLIT));
		event.setPayDateTime(new Date());
		// map.put("learnList", learnList);
		event.setlSize(String.valueOf(learnList != null ? learnList.size() : 0));
		if (itemCodes != null && itemCodes.length > 0) {
			event.setItemCode(Arrays.asList(itemCodes));
		}
		if (itemYears != null && itemYears.length > 0) {
			event.setItemYear(Arrays.asList(itemYears));
		}
		if (condition.containsKey("scholarship"))
			event.setScholarship(condition.getString("scholarship"));
		if (condition.containsKey("recruitType"))
			event.setRecruitType(condition.getString("recruitType"));
		// 上线缴费赠送流程 + 个人缴费赠送流程
		event.setUserId(condition.getString("userId", ""));
		event.setpId(condition.getString("pId", ""));
		event.setGrade(condition.getString("grade",""));
		event.setStdStage("");
		log.info("发送个人缴费上级赠送指令 lpush {} {}", YzTaskConstants.YZ_USER_RECHARGE_EVENT, JsonUtil.object2String(event));
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_RECHARGE_EVENT, JsonUtil.object2String(TraceEventWrapper.wrapper(event)));
	}

	/**
	 * 判断是否需要更新用户关进关系
	 * 
	 * @param itemCodes
	 * @return
	 */
	private boolean needRefreshRelation(String[] itemCodes) {
		if (itemCodes != null) {
			for (String itemCode : itemCodes) {
				if (FinanceConstants.FEE_ITEM_CODE_Y1.equals(itemCode)
						|| FinanceConstants.FEE_ITEM_CODE_Y0.equals(itemCode)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 自动缴费----刷订单专用
	 * 
	 * @param payInfo
	 * @return
	 */
	public Map<String, String> selfPayTuition(BdPayInfo payInfo) {
		Map<String, String> result = new HashMap<String, String>();

		String learnId = payInfo.getLearnId();

		// 修改子订单状态
		payMapper.updateSubOrderPaid(learnId, payInfo.getItemCodes());

		result.put("resCode", "SUCCESS");

		return result;

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

			// 根据地址是否存在来决定地址是否审核

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
			studentSend.setSendId(IDGenerator.generatorId());
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