package com.yz.service.transfer;

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
import com.yz.edu.paging.common.PageHelper;
import com.yz.api.AtsAccountApi;

import com.yz.constants.CheckConstants;
import com.yz.constants.FinanceConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.TransferConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.educational.BdStudentSendMapper;
import com.yz.dao.finance.BdOrderMapper;
import com.yz.dao.recruit.StudentAllMapper;
import com.yz.dao.transfer.BdStudentModifyMapper;
import com.yz.dao.us.UsFollowMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.WechatMsgVo;
import com.yz.model.admin.BaseUser;
import com.yz.model.communi.Body;
import com.yz.model.finance.stdfee.BdPayInfo;
import com.yz.model.finance.stdfee.BdPayableInfoResponse;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.recruit.BdFeeInfo;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.system.SysDict;
import com.yz.model.transfer.BdCheckRecord;
import com.yz.model.transfer.BdStudentModify;
import com.yz.model.transfer.StudentModifyMap;
import com.yz.redis.RedisService;
import com.yz.service.educational.BdStudentSendService;
import com.yz.service.finance.BdStdPayFeeService;
import com.yz.service.finance.BdTuitionService;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.service.system.SysDictService;
import com.yz.task.YzTaskConstants;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdStudentModifyService {

	private static final Logger log = LoggerFactory.getLogger(BdStudentModifyService.class);

	@Autowired
	private SysDictService sysDictService;

	@Autowired
	private BdStudentOutService studentOutService;
	@Autowired
	private BdCheckRecordService checkRecordService;
	@Autowired
	private BdStudentModifyMapper studentModifyMapper;

	@Reference(version = "1.0")
	private AtsAccountApi atdApi;

	@Autowired
	private BdStudentSendService sendService;

	@Autowired
	private StudentRecruitService recruitService;

	@Autowired
	private BdTuitionService tuitionService;

	@Autowired
	private BdStdPayFeeService payService;

	@Autowired
	private StudentAllMapper allMapper;

	@Autowired
	private UsFollowMapper usMapper;
	
	@Autowired
	private BdStudentSendMapper studentSendMapper;

	public void passModifyBatch(String[] modifyIds) {
		for (String modifyId : modifyIds) {
			passModify(modifyId, CheckConstants.PASS_CHECK_2, null);
		}
	}

	public void passModify(String modifyId, String checkStatus, String reason) {

		// 判断是否已审核
		int count = studentModifyMapper.selectModifyFinishCount(modifyId);

		if (count > 0) {
			return;
		}

		BdStudentModify bdStudentModify = findStudentModifyById(modifyId);

		// 设置转账对象
		Body body = new Body();

		BdCheckRecord checkRecord = new BdCheckRecord();
		checkRecord.setMappingId(bdStudentModify.getModifyId());
		checkRecord.setCheckOrder(bdStudentModify.getCheckOrder());
		checkRecord.setCheckStatus(checkStatus);
		bdStudentModify.setIsComplete("1");

		// 是否修改辅导书
		boolean flag = false;

		// 审核是否通过
		if (CheckConstants.PASS_CHECK_2.equals(checkStatus)) {
			bdStudentModify.setExt1("审核状态:审核通过");
			if (StringUtil.hasValue(bdStudentModify.getNewPfsnId())
					|| StringUtil.hasValue(bdStudentModify.getNewScholarship())
					|| StringUtil.hasValue(bdStudentModify.getNewTaId())
					|| StringUtil.hasValue(bdStudentModify.getNewUnvsId())) {

				if (!StringUtil.hasValue(bdStudentModify.getNewPfsnId())) {
					bdStudentModify.setNewPfsnId(bdStudentModify.getPfsnId());
				}
				if (!StringUtil.hasValue(bdStudentModify.getNewScholarship())) {
					bdStudentModify.setNewScholarship(bdStudentModify.getScholarship());
				}

				if (!StringUtil.hasValue(bdStudentModify.getNewTaId())) {
					bdStudentModify.setNewTaId(bdStudentModify.getTaId());
				}

				if (!StringUtil.hasValue(bdStudentModify.getNewUnvsId())) {
					bdStudentModify.setNewUnvsId(bdStudentModify.getUnvsId());
				}

				// 如修改专业，则进入辅导书操作
				if (!bdStudentModify.getNewPfsnId().equals(bdStudentModify.getPfsnId())) {
					flag = true;
				}

				// 修改后收费标准ID
				String feeId = studentModifyMapper.selectFeeStandard(bdStudentModify.getNewPfsnId(),
						bdStudentModify.getNewTaId(), bdStudentModify.getNewScholarship());

				if (!StringUtil.hasValue(feeId)) {
					String[] name = new String[] { bdStudentModify.getStdName() };
					throw new BusinessException("E000077", name); // 无收费标准
				}

				// 当前收费标准ID
				String nowFeeId = studentModifyMapper.selectNowFeeId(bdStudentModify.getLearnId());
				if (!StringUtil.hasValue(nowFeeId)) {
					throw new BusinessException("E000089"); // 现收费标准不存在
				}
				// 如果收费标准变更

				String offerId = studentModifyMapper.selectOfferId(bdStudentModify.getNewPfsnId(),
						bdStudentModify.getNewTaId(), bdStudentModify.getNewScholarship());

				// 已缴金额
				BigDecimal paidAmount = BigDecimal.ZERO;

				String amount = studentModifyMapper.selectPaidAmountByLearnId(bdStudentModify.getLearnId());
				if (StringUtil.hasValue(amount)) {
					paidAmount = AmountUtil.str2Amount(amount);
				}

				// 获取当前辅导费金额
				/*
				 * BigDecimal tutorAmountNow = AmountUtil
				 * .str2Amount(studentModifyMapper.selectTutorAmountNow(
				 * bdStudentModify.getLearnId()));
				 */

				/*
				 * if (StringUtil.hasValue(offerId)) { BigDecimal offerAmount =
				 * AmountUtil.str2Amount(studentModifyMapper.
				 * selectOfferTutorAmount(offerId)); amount =
				 * BigDecimalUtil.substract(tutorAmountNow,
				 * BigDecimalUtil.add(tutorAmount, offerAmount)); } else {
				 * amount = BigDecimalUtil.substract(tutorAmountNow,
				 * tutorAmount); }
				 */

				// 修改学业阶段为意向学员，废弃订单，重新初始化
				studentModifyMapper.updateStdStageByLearnId(bdStudentModify.getLearnId());

				BdLearnInfo learnInfo = new BdLearnInfo();
				BaseUser user = SessionUtil.getUser();
				learnInfo.setCreateUser(user.getRealName());
				learnInfo.setCreateUserId(user.getUserId());
				learnInfo.setFeeId(feeId);
				learnInfo.setMobile(bdStudentModify.getMobile());
				learnInfo.setStdName(bdStudentModify.getStdName());
				learnInfo.setStdId(bdStudentModify.getStdId());
				learnInfo.setUpdateUser(user.getRealName());
				learnInfo.setUpdateUserId(user.getUserId());
				learnInfo.setPfsnId(bdStudentModify.getNewPfsnId());
				learnInfo.setUnvsId(bdStudentModify.getNewUnvsId());
				learnInfo.setTaId(bdStudentModify.getTaId());
				learnInfo.setFeeId(feeId);
				learnInfo.setOfferId(offerId);
				learnInfo.setRecruitType(bdStudentModify.getRecruitType());
				learnInfo.setStdStage(StudentConstants.STD_STAGE_PURPOSE);
				learnInfo.setLearnId(bdStudentModify.getLearnId());

				// 费用是否有变化
				boolean isNotChange = modifyInitOrder(learnInfo);

				// 学员阶段改回来
				studentModifyMapper.updateStdStageBack(bdStudentModify.getLearnId(), bdStudentModify.getStdStage());

				/*
				 * String sendId =
				 * sendMapper.selectFdSendId(bdStudentModify.getLearnId());
				 * 
				 * // 是否存在发书记录 if (StringUtil.hasValue(sendId)) { // 查询考试科目
				 * String[] testSub =
				 * sendMapper.selectTestSubByLearnId(bdStudentModify.getLearnId(
				 * )); int subCount = sendMapper.selectTestBookCount(testSub);
				 * // 删除旧的书籍 sendMapper.deleteTextBookSend(sendId); if (subCount
				 * > 0) { sendMapper.insertBdTextBookSendFD(sendId, testSub); }
				 * else { // 考试科目不存在，删除发书记录 sendMapper.deleteBookSend(sendId); }
				 * }
				 */

				// 如果订单发生了变化
				if (!isNotChange) {

					if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
						// 设置转账对象
						body.put("accType", FinanceConstants.ACC_TYPE_DEMURRAGE);
						body.put("stdId", bdStudentModify.getStdId());
						body.put("amount", paidAmount);
						body.put("action", FinanceConstants.ACC_ACTION_IN);
						body.put("excDesc", "新生信息修改，"+bdStudentModify.getGrade()+"年级已缴费用退至滞留账户");
						body.put("mappingId", modifyId);
						atdApi.trans(body);
					}

					if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
						autoPayTheFees(paidAmount, bdStudentModify);
					}
				}

				bdStudentModify.setFeeId(feeId);
				bdStudentModify.setOfferId(offerId);
			}

			// 通过则修改数据bd_student_enroll bd_learn_info bd_student_info
			updateBdStudentEnroll(bdStudentModify);// 根据学业id
			updateBdLearnInfo(bdStudentModify);// 根据学业id
			updateBdStudentInfo(bdStudentModify);// 根据学员id

			if (flag) {

				// 找出变更专业后不一致的考试科目
				List<String> unSameList = studentModifyMapper
						.selectUnSameTestSubjectByPfsnId(bdStudentModify.getPfsnId(), bdStudentModify.getNewPfsnId());
				// 考试科目不一直，重新发放辅导书
				if (unSameList.size() > 0) {
					
					// 删除未发记录
					List<String> sendIds = studentSendMapper.selectFdSendIds(bdStudentModify.getLearnId());
					for (String sendId : sendIds) {
						studentSendMapper.deleteBookSend(sendId);
						studentSendMapper.deleteTextBookSend(sendId);
					}

					// 重置辅导书发放
					sendService.sendBook(bdStudentModify.getLearnId(), null, true);
				}
			}
		} else {
			checkRecord.setReason(reason);
			bdStudentModify.setExt1("审核状态:审核被驳回，驳回原因：" + reason);
		}
		updateStudentModify(bdStudentModify);

		checkRecordService.updateBdCheckRecord(checkRecord);

	}

	@Autowired
	private BdOrderMapper orderMapper;

	private boolean checkTestSubjectChange(String oldPfsnId, String newPfsnId) {
		boolean flag = false;

		return flag;
	}

	private boolean modifyInitOrder(BdLearnInfo learnInfo) {

		boolean flag = checkInitOrder(learnInfo);

		// 检查是否需要刷新订单
		if (!flag) {
			studentModifyMapper.destroyOrderByLearnId(learnInfo.getLearnId());
			recruitService.initStudentOrder(learnInfo);

		}

		return flag;
	}

	// 成教异动校验费用科目
	private final static String[] CJ_CHECK_ITEM_CODE = new String[] { "Y0" };

	// 国开异动校验费用科目
	private final static String[] GK_CHECK_ITEM_CODE = new String[] { "Y1", "Y2", "Y3", "Y4", "S1", "S2", "S3", "S4",
			"W1", "W2", "W3", "W4" };

	private boolean checkInitOrder(BdLearnInfo learnInfo) {
		// 成教 对比Y0辅导费用
		if (StudentConstants.RECRUIT_TYPE_CJ.equals(learnInfo.getRecruitType())) {
			return checkCondition(learnInfo, CJ_CHECK_ITEM_CODE);
		} else if (StudentConstants.RECRUIT_TYPE_GK.equals(learnInfo.getRecruitType())) {
			String[] itemCodes = orderMapper.selectItemCodeByLearnId(learnInfo.getLearnId());
			return checkCondition(learnInfo, itemCodes);
		}

		return false;
	}

	private boolean checkCondition(BdLearnInfo learnInfo, String[] itemCodes) {

		for (String itemCode : itemCodes) {

			BdFeeInfo amountInfo = orderMapper.getFeeInfo(learnInfo.getFeeId(), learnInfo.getOfferId(), itemCode);
			if (null == amountInfo) {
				return false;
			}

			if (!StringUtil.hasValue(amountInfo.getAmount())) {
				return false;
			}

			// 现在应缴额
			String nowAmount = orderMapper.selectItemCodePayable(learnInfo.getLearnId(), itemCode);

			String feeAmount = null;

			// 收费标准应缴额
			if (!StringUtil.hasValue(amountInfo.getDiscount())) {
				feeAmount = amountInfo.getAmount();
			} else {
				feeAmount = BigDecimalUtil.substract(amountInfo.getAmount(), amountInfo.getDiscount());
				if (AmountUtil.str2Amount(feeAmount).compareTo(BigDecimal.ZERO) < 0) {
					feeAmount = "0.00";
				}
			}

			// 如果费用不一直 返回false
			if (AmountUtil.str2Amount(nowAmount).compareTo(AmountUtil.str2Amount(feeAmount)) != 0) {
				return false;
			}

		}

		return true;

	}

	/**
	 * 自动缴费
	 * 
	 * @param paidAmount
	 * @param bdStudentModify
	 * @param delayAmount
	 */
	public void autoPayTheFees(BigDecimal paidAmount, BdStudentModify bdStudentModify) {

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
				paidAmount = BigDecimal.ZERO;
			}

		} else if (StudentConstants.RECRUIT_TYPE_GK.equals(bdStudentModify.getRecruitType())
				&& paidAmount.compareTo(BigDecimal.ZERO) > 0) { // 国开 比对所有科目
			BigDecimal firstPayable = BigDecimal.ZERO;
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
				} else {
					paidAmount = BigDecimal.ZERO;
				}
			}

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
					} else {
						paidAmount = BigDecimal.ZERO;
					}
				}
			}

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
						paidAmount = BigDecimal.ZERO;
					}
				}
			}
		}
		if (null != itemCodes && itemCodes.size() > 0) {
			payInfo.setItemCodes(itemCodes.toArray(new String[itemCodes.size()]));
			payInfo.setLearnId(bdStudentModify.getLearnId());
			payInfo.setZmDeduction("0.00");
			payInfo.setPaidAmount(payableAmount.toString());
			payInfo.setPaymentType(FinanceConstants.PAYMENT_TYPE_DELAY);
			payInfo.setAccDeduction("0.00");
			if (null != years && years.size() > 0) {
				payInfo.setYears(years.toArray(new String[years.size()]));
			}
			payInfo.setGiveZhimi(false);
			tuitionService.payTuition(payInfo, false, true);
		} else {

			try {
				// 发送微信推送，重新缴费
				String openId = usMapper.selectOpenIdByStdId(bdStudentModify.getStdId());
				String title = bdStudentModify.getStdName() + " 您好，您有一个重要通知，内容如下：";
				String msgName = "信息变更成功通知";
				String code = "YZ";
				String content = "由于您变更了院校专业信息，请在远智学堂进行重新缴费，原缴纳金额已退至您的可用账户，可在缴费时使用，谢谢。";

				WechatMsgVo msgVo = new WechatMsgVo();
				msgVo.setTouser(openId);
				msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_STUDENT_INFORM);
				msgVo.addData("title", title);
				msgVo.addData("msgName", msgName);
				msgVo.addData("code", code);
				msgVo.addData("content", content);

				RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(msgVo));
				// wechatApi.sendStudentInform(openId, title, code, content,
				// msgName, null);
			} catch (Exception e) {
				log.error("新生信息异动缴费推送异常：" + JsonUtil.object2String(e));
			}
		}
	}

	public List<Map<String, String>> findStudentModify(StudentModifyMap studentModifyMap, int start, int length) {
		PageHelper.offsetPage(start, length);
		BaseUser user = SessionUtil.getUser();
		if (user.getJtList().contains("YXGLZXZL")) { // 营销管理中心助理
			user.setUserLevel("14");
		}
		return studentModifyMapper.findStudentModify(studentModifyMap, user);
	}

	public BdStudentModify findStudentModifyById(String modifyId) {
		// TODO Auto-generated method stub
		return studentModifyMapper.findStudentModifyById(modifyId);
	}

	public List<Map<String, String>> findStudentInfo(String sName, String[] stauts, int page, int rows) {
		PageHelper.startPage(page, rows);
		BaseUser user = SessionUtil.getUser();
		if (user.getJtList().contains("YXGLZXZL")) { // 营销管理中心助理
			user.setUserLevel("14");
		}
		return studentModifyMapper.findStudentInfo(sName, stauts, user);
	}

	public Map<String, String> findStudentEnrollInfo(String learnId) {
		// TODO Auto-generated method stub
		return studentModifyMapper.findStudentEnrollInfo(learnId);
	}

	@Autowired
	private BdStudentChangeService changeService;

	public void insertStudentModify(BdStudentModify studentModify) {
		checkParam(studentModify);
		checkRepeat(studentModify);

		// 检查流水金额是否大于订单金额
		changeService.checkStudentSerial(studentModify.getLearnId());

		if (StringUtil.hasValue(studentModify.getNewScholarship())) {

			// TODO 临时加入优惠分组 后续可以从页面带过来
			String scholarship = studentModify.getScholarship();
			SysDict dict = sysDictService.getDict("scholarship." + scholarship);
			String sg = dict.getExt1();// 优惠分组
			studentModify.setSg(sg);

			String newScholarship = studentModify.getNewScholarship();
			SysDict newDict = sysDictService.getDict("scholarship." + newScholarship);
			String newSg = newDict.getExt1();// 优惠分组
			studentModify.setNewSg(newSg);
		}
		studentModify.setModifyId(IDGenerator.generatorId());
		studentModifyMapper.insertSelective(studentModify);
		// 添加审核记录
		// 获取审核权重
		List<Map<String, String>> checkWeights = null;
		checkWeights = studentOutService.getCheckWeight(TransferConstants.CHECK_TYPE_NEW_MODIFY);
		for (Map<String, String> map : checkWeights) {
			// 初始化审核记录
			BdCheckRecord checkRecord = new BdCheckRecord();
			checkRecord.setMappingId(studentModify.getModifyId());
			checkRecord.setCheckOrder(map.get("checkOrder"));
			checkRecord.setCheckType(map.get("checkType"));
			checkRecord.setJtId(map.get("jtId"));
			checkRecord.setCheckStatus("1");
			checkRecordService.addBdCheckRecord(checkRecord);
		}
	}

	/**
	 * 添加学员变更记录
	 * 
	 * @param studentModify
	 */
	public void addStudentModifyRecord(BdStudentModify studentModify) {
		BaseUser user = SessionUtil.getUser();
		studentModify.setCreateUser(user.getUsername());
		studentModify.setCreateUserId(user.getUserId());
		if (StringUtil.isEmpty(studentModify.getStdId())) {
			studentModify.setStdId(allMapper.selectStdIdByLearnId(studentModify.getLearnId()));
		}
		if (StringUtil.hasValue(studentModify.getNewScholarship())) {
			// TODO 临时加入优惠分组 后续可以从页面带过来
			String scholarship = studentModify.getScholarship();
			SysDict dict = sysDictService.getDict("scholarship." + scholarship);
			String sg = dict.getExt1();// 优惠分组
			studentModify.setSg(sg);

			String newScholarship = studentModify.getNewScholarship();
			SysDict newDict = sysDictService.getDict("scholarship." + newScholarship);
			String newSg = newDict.getExt1();// 优惠分组
			studentModify.setNewSg(newSg);
		}
		studentModify.setModifyId(IDGenerator.generatorId());
		studentModifyMapper.insertSelective(studentModify);
	}

	/**
	 * 添加学员变更记录
	 * 
	 * @param learnId
	 * @param edittext
	 */
	public void addStudentModifyRecord(String learnId, String edittext) {
		BdStudentModify stumodify = new BdStudentModify();
		stumodify.setLearnId(learnId);
		stumodify.setCreateTime(DateUtil.getNowDateAndTime());
		BaseUser user = SessionUtil.getUser();
		stumodify.setCreateUser(user.getUsername());
		stumodify.setCreateUserId(user.getUserId());
		stumodify.setStdId(allMapper.selectStdIdByLearnId(learnId));
		stumodify.setModifyType(TransferConstants.MODIFY_TYPE_CHANGE_normalopt_5);
		stumodify.setExt1(edittext);
		stumodify.setIsComplete("1");
		stumodify.setModifyId(IDGenerator.generatorId());
		studentModifyMapper.insertSelective(stumodify);
	}

	private void checkRepeat(BdStudentModify studentModify) {

		boolean repeatFlag = true;

		if (StringUtil.hasValue(studentModify.getNewStdName())
				&& studentModify.getStdName().equals(studentModify.getNewStdName())) {
			throw new BusinessException("E000079", new String[] { "姓名" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewIdCard())
				&& studentModify.getIdCard().equals(studentModify.getNewIdCard())) {
			throw new BusinessException("E000079", new String[] { "身份证" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewSex())
				&& studentModify.getSex().equals(studentModify.getNewSex())) {
			throw new BusinessException("E000079", new String[] { "性别" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewNation())
				&& studentModify.getNation().equals(studentModify.getNewNation())) {
			throw new BusinessException("E000079", new String[] { "性别" }); // 未做任何修改
		}
		if (StringUtil.hasValue(studentModify.getNewUnvsId())) {

			if (!studentModify.getUnvsId().equals(studentModify.getNewUnvsId())) {
				repeatFlag = false;
			}
			if (StringUtil.hasValue(studentModify.getNewPfsnId())
					&& !studentModify.getPfsnId().equals(studentModify.getNewPfsnId())) {
				repeatFlag = false;
			}
			if (StringUtil.hasValue(studentModify.getNewScholarship())
					&& !studentModify.getScholarship().equals(studentModify.getNewScholarship())) {
				repeatFlag = false;
			}
			if (StringUtil.hasValue(studentModify.getNewTaId())
					&& !studentModify.getTaId().equals(studentModify.getNewTaId())) {
				repeatFlag = false;
			}

			if (repeatFlag) {
				throw new BusinessException("E000079", new String[] { "报读信息" }); // 未做任何修改
			}
		}

	}

	private void checkParam(BdStudentModify studentModify) {
		boolean blankFlag = false;
		if (StringUtil.hasValue(studentModify.getNewStdName())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewIdCard())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewSex())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewNation())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewUnvsId())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewPfsnId())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewScholarship())) {
			blankFlag = true;
		}
		if (StringUtil.hasValue(studentModify.getNewTaId())) {
			blankFlag = true;
		}
		if (!blankFlag) {
			throw new BusinessException("E000078"); // 提交内容不能为空
		}
	}

	public void deleteStudentModify(String[] idArray) {
		// TODO Auto-generated method stub
		studentModifyMapper.deleteStudentModify(idArray);
	}

	public void updateStudentModify(BdStudentModify studentModify) {
		// TODO Auto-generated method stub
		studentModifyMapper.updateByPrimaryKeySelective(studentModify);
	}

	public List<Map<String, String>> findStudentAuditModify(StudentModifyMap studentModifyMap) {
		// TODO Auto-generated method stub
		return studentModifyMapper.findStudentAuditModify(studentModifyMap);
	}

	public void updateBdStudentEnroll(BdStudentModify bdStudentModify) {
		// TODO Auto-generated method stub
		studentModifyMapper.updateBdStudentEnroll(bdStudentModify);
	}

	public void updateBdLearnInfo(BdStudentModify bdStudentModify) {
		// TODO Auto-generated method stub
		studentModifyMapper.updateBdLearnInfo(bdStudentModify);
	}

	public void updateBdStudentInfo(BdStudentModify bdStudentModify) {
		// TODO Auto-generated method stub
		studentModifyMapper.updateBdStudentInfo(bdStudentModify);
	}

}
