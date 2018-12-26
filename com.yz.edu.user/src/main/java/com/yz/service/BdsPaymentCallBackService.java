package com.yz.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yz.api.GsLotteryApi;
import com.yz.api.UsInfoApi;
import com.yz.constants.CheckConstants;
import com.yz.constants.EducationConstants;
import com.yz.constants.FeeConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.OrderConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.dao.BdCutOffMapper;
import com.yz.dao.BdFeeItemMapper;
import com.yz.dao.BdStudentSendMapper;
import com.yz.dao.BdsLearnMapper;
import com.yz.dao.BdsPaymentMapper;
import com.yz.dao.StudentAllMapper;
import com.yz.dao.StudentMpFlowMapper;
import com.yz.model.AtsAccount;
import com.yz.model.BdSerialSurplus;
import com.yz.model.BdStudentBaseInfo;
import com.yz.model.WechatMsgVo;
import com.yz.model.communi.Body;
import com.yz.model.educational.BdStudentSend;
import com.yz.model.payment.BdFeeItem;
import com.yz.model.payment.BdStudentSerial;
import com.yz.model.payment.BdSubSerial;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.AmountUtil;
import com.yz.util.Assert;
import com.yz.util.BigDecimalUtil;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdsPaymentCallBackService {

	private static final Logger log = LoggerFactory.getLogger(BdsPaymentCallBackService.class);

	@Autowired
	private BdsPaymentMapper payMapper;

	@Autowired
	private BdsLearnMapper learnMapper;

	@Autowired
	private AccountService accountService;

	@Autowired
	private StudentMpFlowMapper flowMapper;

	@Autowired
	private BdCutOffMapper ctMapper;

	@Reference(version = "1.0")
	private GsLotteryApi lotteryApi;

	@Autowired
	private BdFeeItemMapper itemMapper;

	@Autowired
	private BdStudentSendMapper studentSendMapper;

	@Autowired
	private StudentAllMapper stdAllMapper;

	@Autowired
	private UsTaskCardService usTaskCardService;

	public boolean paySuccess(String serialMark, String outSerialNo, String payType, String amount) {

		log.error("--------------------------支付成功流程开始，批次号为：" + serialMark);

		int exist = payMapper.selectExistFinishedSerial(serialMark);
		// 获取订单号
		String orderNo = payMapper.selectOrderNoBySerialNo(serialMark);
		if (exist > 0) { // 重复操作
			log.error("--------------------------在线支付重复回调，批次号：" + serialMark);
			return true;
		}

		// 转换为 单位；元
		amount = BigDecimalUtil.divide(amount, "100");

		List<BdStudentSerial> serials = payMapper.selectSerialDetail(serialMark);

		String learnId = serials.get(0).getLearnId();
		String stdId = serials.get(0).getStdId();

		List<String> itemCodes = payMapper.selectItemCodesBySerialMark(serialMark);

		Map<String, String> object = learnMapper.selectLearnInfoByLearnId(learnId);
		String grade = object.get("grade");

		// 现金支付金额
		BigDecimal serialAmount = AmountUtil.str2Amount(payMapper.selectPayableAmountBySerialMark(serialMark));

		if (AmountUtil.str2Amount(amount).compareTo(serialAmount) != 0) {
			log.error("--------------------------在线支付回调金额与付款金额不匹配，流水号：" + serialMark);
			return false;
		}
		// 是否成功
		boolean successFlag = true;

		// 动账集合
		List<Body> transList = new ArrayList<Body>();

		String userId = payMapper.selectUserId(serials.get(0).getStdId());

		log.error("--------------------------抵扣信息比对：" + JsonUtil.object2String(serials));

		String[] couponId = null;

		// 跳出循环标识
		boolean breakFlag = false;

		for (BdStudentSerial serial : serials) {
			// 是否智米抵扣
			boolean zmFlag = false;

			// 是否优惠券抵扣
			boolean couponFlag = false;

			String zmAmount = "0";
			log.info("订单流水信息:"+successFlag);
			if (!breakFlag) {

				for (BdSubSerial s : serial.getSubSerials()) {
					log.info("支付方式:"+s.getPaymentType());
					if (FinanceConstants.PAYMENT_TYPE_COUPON.equals(s.getPaymentType())) { // 优惠券使用
						
						int couponCount = payMapper.selectAbleCouponByLearnId(stdId,userId,learnId).size();

						if (couponCount <= 0) {
							log.debug("-----------------------------无效优惠券，serialNo：" + serialMark + "，学员ID："
									+ serial.getStdId() + "优惠券ID：" + s.getCouponId());
							// 订单无效

							transList.clear();
							// 退还缴费金额到滞留账户,修改流水状态
							transList.add(refundAmount(serialMark, serial.getStdId(), amount, outSerialNo));
							successFlag = false;
							// 跳出循环
							breakFlag = true;
							break;
						}

						couponId = s.getScId().split(",");
						couponFlag = true;
						continue;
					}

					if (FinanceConstants.PAYMENT_TYPE_ZM.equals(s.getPaymentType())) {

						BigDecimal balance = getAccountByUserId(userId, FinanceConstants.ACC_TYPE_ZHIMI);
						if (balance.compareTo(AmountUtil.str2Amount(s.getAmount())) < 0) {
							// 退还缴费金额到现金账户,修改流水状态
							/*
							 * if (couponFlag) { // 退还优惠券
							 * payMapper.updateCouponUsed(couponId,
							 * serial.getStdId(), FinanceConstants.COUPON_UNUSE,
							 * userId); }
							 */

							log.debug("------------------------------智米余额不足,serialNo：" + serialMark + "，学员ID："
									+ serial.getStdId());
							transList.clear();
							transList.add(refundAmount(serialMark, serial.getStdId(), amount, outSerialNo));
							successFlag = false;
							// 跳出循环
							breakFlag = true;
							break;
						}
						transList.add(amountZmTrans(userId, s.getAmount(), FinanceConstants.ACC_ACTION_OUT, serialMark,
								"学员缴费,智米抵扣", FinanceConstants.ACC_TYPE_ZHIMI));
						zmFlag = true;
						zmAmount = s.getAmount();
						continue;
					}

					if (FinanceConstants.PAYMENT_TYPE_DELAY.equals(s.getPaymentType())) {
						BigDecimal balance = getAccount(serial.getStdId(), FinanceConstants.ACC_TYPE_DEMURRAGE);
						if (balance.compareTo(AmountUtil.str2Amount(s.getAmount())) < 0) {
							// 退还缴费金额到滞留账户,修改流水状态

							/*
							 * if (couponFlag) { // 退还优惠券
							 * payMapper.updateCouponUsed(couponId,
							 * serial.getStdId(), FinanceConstants.COUPON_UNUSE,
							 * userId); }
							 */

							/*
							 * if (zmFlag) { // 如果智米扣费，退还智米
							 * transList.add(amountZmTrans(userId, zmAmount,
							 * FinanceConstants.ACC_ACTION_IN, serialMark,
							 * "学员缴费失败，退还智米", FinanceConstants.ACC_TYPE_ZHIMI));
							 * }
							 */
							transList.clear();

							transList.add(refundAmount(serialMark, serial.getStdId(), amount, outSerialNo));
							log.debug("------------------------------滞留账户余额不足,serialNo：" + serialMark + "，学员ID："
									+ serial.getStdId());

							successFlag = false;
							// 跳出循环
							breakFlag = true;
							break;
						}
						transList.add(amountTrans(serial.getStdId(), s.getAmount(), FinanceConstants.ACC_ACTION_OUT,
								serialMark, "学员缴" + grade + "级费用，滞留账户抵扣", FinanceConstants.ACC_TYPE_DEMURRAGE));

						continue;
					}
				}
			}

			String payable = serial.getPayable();
			log.info("支付金额："+payable);
			if (StringUtil.hasValue(payable)) {
				BigDecimal b = new BigDecimal(payable);
				if (b.compareTo(BigDecimal.ZERO) > 0) {
					ctMapper.setNeedCutOff(serial.getSerialNo(), FinanceConstants.CT_STATUS_NEED);
				}
			}
		}
		log.info("是否支付成功:"+successFlag);
		if (successFlag) {

			List<BdSerialSurplus> surplus = payMapper.selectSurplusBySerialMark(serialMark);
			if (null != surplus && surplus.size() > 0) {

				for (BdSerialSurplus sur : surplus) {
					if (FinanceConstants.ACC_TYPE_ZHIMI.equals(sur.getAccType())) {
						amountZmTrans(userId, sur.getAmount(), FinanceConstants.ACC_ACTION_OUT, serialMark,
								"学员缴" + grade + "级费用,智米抵扣", FinanceConstants.ACC_TYPE_ZHIMI);
					}
				}

			}
			
			if(null != couponId && couponId.length > 0){
				
				for (String scId : couponId) {
					// 修改优惠券为已使用
					payMapper.updateCouponUsed(scId,FinanceConstants.COUPON_USED);

				}
			}


			log.info("--------------------------支付成功，修改状态");

			// 修改流水状态
			payMapper.updateSerialStatus(serialMark, OrderConstants.SERIAL_STATUS_UNCHECK, outSerialNo);
			// 修改订单状态
			payMapper.updateSubOrderStatus(learnId, itemCodes, FinanceConstants.ORDER_STATUS_PAID);

			log.info("------------------------ 动账数据：" + JsonUtil.object2String(transList));
			if (transList.size() > 0)
				// atsApi.transMore(transList);
				accountService.transMore(transList);
			/*
			 * } else { atsApi.transAndAwardMore(transList, mList); }
			 */

			try {
				log.info("缴费成功--------开始发送微信推送-----" + JsonUtil.object2String(serials.get(0)));

				String stdUserId = learnMapper.selectUserIdByLearnId(serials.get(0).getLearnId());
				String[] itemNames = payMapper.selectItemNameBySerialMark(serialMark);

				// 微信推送
				sendWechatMsg(serials.get(0), amount, itemNames, stdUserId);
				// 抽奖券赠送
				gainTicket(itemCodes, learnId, stdUserId);

				// 判断是否是辅导教材,如果是成教Y0辅导费,插入发书记录信息 20180510版
				Map<String, String> learnInfo = learnMapper.selectLearnInfoByLearnId(learnId);
				if (null != learnInfo) {
					String stdStage = learnInfo.get("stdStage");
					String recruitType = learnInfo.get("recruitType");
					if (StudentConstants.STD_STAGE_PURPOSE.equals(stdStage)
							&& StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
						for (String itemCode : itemCodes) {
							BdFeeItem item = itemMapper.selectItemInfoById(itemCode);
							if (FeeConstants.FEE_ITEM_TYPE_COACH.equals(item.getItemType())) {
								//payMapper.updateStdStage(learnId, StudentConstants.STD_STAGE_HELPING);
								initBookSend(learnId, true);
								// 修改学业辅导费审核时间
								//payMapper.updateTutionTime(learnId);	在收费审核改
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				log.info("-------------------------  微信缴费通知失败：serialMark = " + serialMark);
			}

		} else {

			log.info("------------------------------- 缴费批次: " + serialMark + "支付失败，退还滞留账户");

			if (transList.size() > 0)
				// atsApi.transMore(transList); // 支付失败，退款至滞留账户等
				accountService.transMore(transList);// 支付失败，退款至滞留账户等
		}

		log.info("------------------------------- 缴费批次: " + serialMark + "支付成功");

		String[] itemYears = payMapper.selectYearsBySerialMark(serialMark);

		String[] s_itemCodes = new String[itemCodes.size()];

		itemCodes.toArray(s_itemCodes);

		bindService.bindAndAward(s_itemCodes, itemYears, serialMark, orderNo, amount, learnId);

		// 任务卡活动
		usTaskCardService.addUsTaskCardDetail(userId, s_itemCodes, learnId);

		return true;
	}

	@Autowired
	private BdsPaymentBindService bindService;

	@Reference(version = "1.0")
	private UsInfoApi usApi;

	private void sendWechatMsg(BdStudentSerial serial, String amount, String[] itemNames, String stdUserId) {
		try {
			Map<String, String> map = learnMapper.selectTutorAndRecruitUserId(serial.getLearnId());

			log.info("学业:" + serial.getLearnId() + "对应的用户userId:" + stdUserId);
			// 赠送抽奖机会
			// lotteryApi.gainLoterryTicket(stdUserId);
			// log.info("用户赠送抽奖机会success.............");

			String recruitOpenId = null;
			String tutorOpenId = null;
			String stdOpenId = null;
			if (null != map) {
				if (StringUtil.hasValue(map.get("recruitUserId"))) {
					Body body = new Body();
					body.put("userId", map.get("recruitUserId"));
					Object obj = usApi.getOpenIdByUserId(body);
					if (null != obj) {
						recruitOpenId = (String) obj;
					}
				}
				log.debug("缴费通知----招生老师openId:" + recruitOpenId);
				/*
				 * if (StringUtil.hasValue(map.get("tutorUserId"))) { Body body
				 * = new Body(); body.put("userId", map.get("tutorUserId"));
				 * Object obj = usApi.getOpenIdByUserId(body); if (null != obj)
				 * { tutorOpenId = (String) obj; } }
				 */
			}
			Body body = new Body();
			body.put("userId", stdUserId);
			Object obj = usApi.getOpenIdByUserId(body);
			if (null != obj) {
				stdOpenId = (String) obj;
			}
			log.debug("缴费通知----学员openId:" + stdOpenId);
			String grade = payMapper.selectGradeByLearnId(serial.getLearnId());
			if ("201803".equals(grade) || "201703".equals(grade) || "201809".equals(grade)) {
				for (int i = 0; i < itemNames.length; i++) {
					itemNames[i] = itemNames[i].replaceAll("年", "学期");
				}
			}
			log.debug("缴费通知信息推送ing---------");
			// 发送模板通知
			sendTuitionMsg(serial.getStdName(), amount, serial.getSerialNo(), itemNames, serial.getLearnId(), stdOpenId,
					tutorOpenId, recruitOpenId);
		} catch (Exception e) {
			log.error("------------------------------ 学员缴费推送消息失败：" + e.getMessage());
		}

	}

	private void gainTicket(List<String> itemCodes, String learnId, String stdUserId) {
		// 是否赠送抽奖机会
		boolean hasGainTicket = false;

		for (String itemCode : itemCodes) {
			Map<String, String> learnMap = learnMapper.selectLearnInfoByLearnId(learnId);
			if (null != learnMap) {
				String recruitType = learnMap.get("recruitType");
				if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
					if ("Y0".equals(itemCode)) {
						hasGainTicket = true;
					}
				} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
					if ("Y1".equals(itemCode)) {
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

	public boolean payFailed(String serialMark, String outSerialNo) {
		int exist = payMapper.selectExistFinishedSerial(serialMark);

		if (exist > 0) { // 重复操作
			log.error("--------------------------在线支付重复回调，流水号：" + serialMark);
			return true;
		}

		// 修改流水状态
		payMapper.updateSerialStatus(serialMark, OrderConstants.SERIAL_STATUS_FAILED, outSerialNo);

		return false;
	}

	// private List<CommunicationMap> chargeAward(String learnId, String[]
	// itemCodes, String[] itemYears) {
	// List<CommunicationMap> mList = new ArrayList<CommunicationMap>();
	// MpCondition condition = flowMapper.getCondition(learnId);
	//
	// String stdId = condition.getString("stdId");
	//
	// List<Map<String, String>> learnList = flowMapper.getHistoryLearn(stdId,
	// learnId);
	//
	// condition.put("learnList", learnList);
	// condition.put("createTime", new Date());
	// condition.put("itemCodes", itemCodes);
	// condition.put("itemYears", itemYears);
	// // 个人缴费赠送流程
	// Flow iChargeFlow = MpContext.getiChargeFlow();
	// // 上线缴费赠送流程
	// Flow chargeFlow = MpContext.getChargeFlow();
	//
	// MpResult iResult = null;
	// MpResult result = null;
	//
	// if (iChargeFlow != null) {
	// iResult = iChargeFlow.match(condition);
	// }
	//
	// if (chargeFlow != null) {
	// result = chargeFlow.match(condition);
	// }
	//
	// if (iResult != null && iResult.hasValue()) {
	// mList.add(iResult.getTarget());
	// }
	//
	// if (result != null && result.hasValue()) {
	// mList.add(result.getTarget());
	// }
	//
	// return mList;
	// }

	private Body refundAmount(String serialNo, String stdId, String amount, String outSerialNo) {
		// 修改流水状态失败
		payMapper.updateSerialStatus(serialNo, OrderConstants.SERIAL_STATUS_FAILED, outSerialNo);
		return amountTrans(stdId, amount, FinanceConstants.ACC_ACTION_IN, serialNo, "学员缴费失败，缴费金额退还滞留账户",
				FinanceConstants.ACC_TYPE_DEMURRAGE);
	}

	/**
	 * 学员账户操作
	 * 
	 * @param stdId
	 * @param accAmount
	 * @param accAction
	 * @param serialNo
	 * @param excDesc
	 * @param accType
	 */
	@SuppressWarnings("unchecked")
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

	private BigDecimal getAccount(String stdId, String accType) {
		// Map<String, String> acount = atsApi.getAccount(null, stdId, null,
		// accType);
		Assert.isTrue(StringUtil.hasValue(stdId), "学员ID不能为空");
		Assert.hasText(accType, "账户类型不能为空");

		AtsAccount account = new AtsAccount();
		account.setStdId(stdId);
		account.setUserId(null);
		account.setEmpId(null);
		account.setAccType(accType);
		Map<String, String> acount = accountService.getAccount(account);

		String amount = acount.get("accAmount");
		return AmountUtil.str2Amount(amount);
	}

	private BigDecimal getAccountByUserId(String userId, String accType) {
		// Map<String, String> acount = atsApi.getAccount(userId, null, null,
		// accType);

		Assert.isTrue(StringUtil.hasValue(userId), "用户ID不能为空");
		Assert.hasText(accType, "账户类型不能为空");

		AtsAccount account = new AtsAccount();
		account.setStdId(null);
		account.setUserId(userId);
		account.setEmpId(null);
		account.setAccType(accType);
		Map<String, String> acount = accountService.getAccount(account);

		String amount = acount.get("accAmount");
		return AmountUtil.str2Amount(amount);
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
