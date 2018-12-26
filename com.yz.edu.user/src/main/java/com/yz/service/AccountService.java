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

import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants; 
import com.yz.dao.AtsAccountChangeMapper;
import com.yz.dao.AtsAccountMapper;
import com.yz.dao.AtsAccountSerialMapper;
import com.yz.dao.AtsAwardRecordMapper;
import com.yz.exception.BusinessException;
import com.yz.model.AtsAccount;
import com.yz.model.AtsAccountChange;
import com.yz.model.AtsAccountSerial;
import com.yz.model.AtsAwardRecord;
import com.yz.model.AtsAwardRule;
import com.yz.model.CommunicationMap;
import com.yz.model.communi.Body;
import com.yz.util.AmountUtil;
import com.yz.util.Assert;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class AccountService {
	
	@Autowired
	private AwardRuleService awardRuleService;

	private static final Logger log = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private AtsAccountMapper accountMapper;

	@Autowired
	private AtsAwardRecordMapper recordMapper;

	@Autowired
	private AtsAccountSerialMapper serialMapper;

	public Map<String, String> getAccount(AtsAccount account) {

		AtsAccount accountInfo = accountMapper.getAccount(account);

		Map<String, String> result = new HashMap<String, String>();
		if (accountInfo != null) {
			result.put("accId", accountInfo.getAccId());
			result.put("accStatus", accountInfo.getAccStatus());
			result.put("accAmount", accountInfo.getAccAmount());
			result.put("canDeposit", accountInfo.getCanDeposit());
			result.put("thawTime", accountInfo.getThawTime());
			result.put("frezTime", accountInfo.getFrezTime());
			result.put("userId", accountInfo.getUserId());
			result.put("empId", accountInfo.getEmpId());
			result.put("stdId", accountInfo.getStdId());
		}

		return result;
	}

	public void initAccount(List<AtsAccount> accountList) {

		for (AtsAccount account : accountList) {

			AtsAccount tmp = accountMapper.getAccount(account);
			if (tmp != null) {
				tmp.setStdId(account.getStdId());
				tmp.setEmpId(account.getEmpId());
				accountMapper.updateAccount(tmp);
			} else {
				accountMapper.insertSelective(account);
			}
		}
		log.debug("------------------------------- 初始化账户完成");
	}

	public AtsAccountSerial trans(AtsAccountSerial serial) {
		log.info("------------------------------- 账户交易开始 :::::" + JsonUtil.object2String(serial));

		AtsAccount account = accountMapper.getAccountForUpdate(serial);

		try {

			if (account == null) {
				serial.setReason("E40001  ：账户不存在");
				throw new BusinessException("E40001");
			}

			serial.setAccId(account.getAccId());
			serial.setBeforeAmount(account.getAccAmount());

			if (FinanceConstants.ACC_STATUS_FREEZE.equals(account.getAccStatus())) {
				serial.setReason("E40002  ：账户被冻结，无法操作");
				throw new BusinessException("E40002");// 账户已冻结
			} else if (FinanceConstants.ACC_STATUS_LOCK.equals(account.getAccStatus())) {
				serial.setReason("E40003  ：账户锁定，无法操作");
				throw new BusinessException("E40003");// 账户已锁定
			}

			BigDecimal actionAmount = AmountUtil.str2Amount(serial.getAmount());

			if (actionAmount.compareTo(BigDecimal.ZERO) < 1) {
				serial.setReason("E40004  ：操作金额不能为负数");
				throw new BusinessException("E40004");// 操作金额不能为负数
			}

			BigDecimal accAmount = AmountUtil.str2Amount(account.getAccAmount());

			switch (serial.getAction()) {
			case FinanceConstants.ACC_ACTION_IN:
				accAmount = accAmount.add(actionAmount);
				break;
			case FinanceConstants.ACC_ACTION_OUT:
				//如果是智米账户余额可以为负数
				if (actionAmount.compareTo(accAmount) > 0 && !"2".equals(serial.getAccType())) {
					serial.setReason("E40005  ：账户余额不足");
					throw new BusinessException("E40005");// 余额不足
				}
				accAmount = accAmount.subtract(actionAmount);
				break;
			}

			account.setAccAmount(accAmount.setScale(2, BigDecimal.ROUND_DOWN).toString());
			accountMapper.updateAccount(account);

			serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_SUCCESS);
			serial.setAfterAmount(account.getAccAmount());
		} catch (Exception e) {
			serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_FAILED);
			throw e;
		} finally {
			insertSerial(serial);
		}

		return serial;
	}

	public void transMore(List<Body> transList) {
		if (transList != null) {
			for (Body body : transList) {
				String accId = body.getString("accId");
				String accType = body.getString("accType");
				String stdId = body.getString("stdId");
				String userId = body.getString("userId");
				String empId = body.getString("empId");
				String amount = body.getString("amount");
				String action = body.getString("action");
				String excDesc = body.getString("excDesc");
				String mappingId = body.getString("mappingId");

				if (StringUtil.isEmpty(accId)) {
					Assert.isTrue(
							StringUtil.hasValue(userId) || StringUtil.hasValue(stdId) || StringUtil.hasValue(empId),
							"用户、学员、员工ID至少有一个不为空");
				}
				Assert.hasText(accType, "账户类型不能为空");
				Assert.hasText(amount, "动账金额不能为空");
				Assert.hasText(action, "进出账类型不能为空");
				Assert.hasText(excDesc, "动账描述不能为空");

				AtsAccountSerial serial = new AtsAccountSerial();
				serial.setAccId(accId);
				serial.setAccType(accType);
				serial.setAction(action);
				serial.setAmount(amount);
				serial.setUserId(userId);
				serial.setEmpId(empId);
				serial.setStdId(stdId);
				serial.setExcDesc(excDesc);
				serial.setMappingId(mappingId);
				serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_PROCESSING);

				trans(serial);
			}
		}
	}

	public void insertSerial(AtsAccountSerial serial) {
		serialMapper.initSerial(serial);
	}

	public Object getAccoutList(String userId) {
		List<Map<String, String>> accountList = accountMapper.getAccountList(userId);
		return accountList;
	}

	public AtsAccountSerial award(AtsAwardRecord record) {
		String ruleCode = record.getRuleCode();
		String ruleType = record.getRuleType();

		String userId = record.getUserId();
		String stdId = record.getStdId();

		if (StringUtil.isEmpty(record.getUserId())) {
			log.error("----------------------- 智米赠送[ userId : " + userId + " | stdId : " + stdId + "]用户ID不能为空");
			return null;
		}

		if (StringUtil.isEmpty(record.getAwardDesc())) {
			log.error("----------------------- 智米赠送[ userId : " + userId + " | stdId : " + stdId + "]动账描述不能为空");
			return null;
		}

		if (StringUtil.isEmpty(record.getRuleCode())) {
			log.error("----------------------- 智米赠送[ userId : " + userId + " | stdId : " + stdId + "]奖励规则编码不能为空");
			return null;
		}
		
		if (!FinanceConstants.AWARD_RT_NORMAL.equals(ruleType)) {
			if ((!"CC_AF_2017_11_2".equals(ruleCode)) && (!"RC_AF_2017_06".equals(ruleCode))) { // 新增送规则和邀约注册可重复赠送
				int count = countBy(record);
				if (count > 0) {
					log.debug("----------------------- userId[" + userId + "] 智米赠送[" + ruleCode + "]重复赠送");
					return null;
				}
			}
			//只要下线缴费就按照1:5赠送给上线
			/*else if("CC_AF_2017_11_2".equals(ruleCode) && StringUtil.hasValue(record.getMappingId())){
				int count = recordMapper.newCountBy(record);
				if (count > 0) {
					log.debug("----------------------- userId[" + userId + "] - mappingId[" + record.getMappingId() + "] - 智米赠送[" + ruleCode + "]重复赠送");
					return null;
				}
			}*/
		}

		log.debug("------------------------------- 智米赠送开始 ::::: " + JsonUtil.object2String(record));
		recordMapper.insertSelective(record);

		AtsAccountSerial serial = new AtsAccountSerial();
		serial.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		serial.setAction(FinanceConstants.ACC_ACTION_IN);
		serial.setAmount(record.getZhimiCount());
		serial.setUserId(record.getUserId());
		serial.setStdId(record.getStdId());
		serial.setEmpId(record.getEmpId());
		serial.setExcDesc(record.getAwardDesc() + "，获得" + record.getZhimiCount() + "智米");
		serial.setMappingId(record.getMappingId());
		serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_PROCESSING);

		return trans(serial);
	}

	public Object getAccountDetail(String accId, String userId, String accType) {
		Map<String, String> param = new HashMap<String, String>();

		param.put("accId", accId);
		param.put("userId", userId);
		param.put("accType", accType);

		return accountMapper.getAccountDetail(param);
	}

	@Autowired
	private AtsAccountChangeMapper changeMapper;

	public void copyZhimi(String userId, String stdId) {
		log.debug("------------------------------- 智米账户合并开始 ::::: userId : " + userId + " | stdId : " + stdId);

		AtsAccount stdAcc = new AtsAccount();
		stdAcc.setStdId(stdId);
		stdAcc.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);

		stdAcc = accountMapper.getAccount(stdAcc);

		if (stdAcc != null) {
			String sUserId = stdAcc.getUserId();

			AtsAccountSerial userSerial = new AtsAccountSerial();
			userSerial.setUserId(userId);
			userSerial.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);

			AtsAccount userAcc = accountMapper.getAccountForUpdate(userSerial);

			if (userAcc != null) {

				String sAccId = stdAcc.getAccId();
				String uAccId = userAcc.getAccId();

				if (!sAccId.equals(uAccId)) {
					log.error("------------------------------- 智米账户变更！！！ ::::: userId : " + sUserId + " -----> "
							+ userId + " | stdId : " + stdId);
					AtsAccountChange atsChange = new AtsAccountChange();

					atsChange.setAccId(stdAcc.getAccId());
					atsChange.setOldUserId(sUserId);
					atsChange.setUserId(sUserId);
					atsChange.setOldStdId(stdAcc.getStdId());
					atsChange.setStdId(stdId);
					atsChange.setReason("用户绑定学员后进行智米账户合并");
					changeMapper.insertSelective(atsChange);

					String stdAmount = stdAcc.getAccAmount();
					String userAmount = userAcc.getAccAmount();

					BigDecimal amount = AmountUtil.str2Amount(userAmount).add(AmountUtil.str2Amount(stdAmount));

					userAcc.setStdId(stdId);
					userAcc.setAccAmount(amount.setScale(2, BigDecimal.ROUND_DOWN).toString());

					serialMapper.copy(stdAcc.getAccId(), userAcc.getAccId(), userId, stdId);
					accountMapper.deleteAccount(stdAcc.getAccId());

					// 添加学员ID到用户账户上
					accountMapper.updateStdAccount(userId, stdId);

					log.debug("------------------------------- 智米账户合并完成 ::::: userId : " + userId + " | stdId : "
							+ stdId);
				}
				accountMapper.updateAccount(userAcc);
			}
		}
	}

	public void awardMore(List<CommunicationMap> cmList) {
		for (CommunicationMap body : cmList) {
			String ruleCode = body.getString("ruleCode");
			String userId = body.getString("userId");
			String stdId = body.getString("stdId");
			String empId = body.getString("empId");
			String triggerUserId = body.getString("triggerUserId");
			String excDesc = body.getString("excDesc");
			String mappingId = body.getString("mappingId");
			String ruleType = body.getString("ruleType", FinanceConstants.AWARD_RT_NORMAL);
			if (FinanceConstants.AWARD_RT_NORMAL.equals(ruleType)) {
				AtsAwardRule ruleInfo = awardRuleService.getRule(ruleCode);

				if (GlobalConstants.TRUE.equals(ruleInfo.getIsAllow())) {
					String zhimiCount = ruleInfo.getZhimiCount();
					String expCount = ruleInfo.getExpCount();

					AtsAwardRecord record = new AtsAwardRecord();
					record.setZhimiCount(zhimiCount);
					record.setExpCount(expCount);
					record.setRuleCode(ruleCode);
					record.setMappingId(mappingId);
					record.setAwardDesc(excDesc);
					record.setUserId(userId);
					record.setStdId(stdId);
					record.setEmpId(empId);
					record.setTriggerUserId(triggerUserId);
					record.setRuleType(ruleType);

					award(record);

				}
			} else if (FinanceConstants.AWARD_RT_MARKTING.equals(ruleType)) {

				Assert.isTrue((StringUtil.hasValue(stdId) || StringUtil.hasValue(userId)), "用户ID、学员ID至少一个不能为空");

				String zhimiCount = body.getString("amount");

				AtsAwardRecord record = new AtsAwardRecord();
				record.setZhimiCount(zhimiCount);
				record.setRuleCode(ruleCode);
				record.setMappingId(mappingId);
				record.setAwardDesc(excDesc);
				record.setUserId(userId);
				record.setStdId(stdId);
				record.setEmpId(empId);
				record.setTriggerUserId(triggerUserId);
				record.setRuleType(ruleType);

				award(record);
			}
		}
	}

	public void transAndAwardMore(List<Body> transList, List<CommunicationMap> mList) {
		transMore(transList);
		awardMore(mList);
	}

	public int countBy(AtsAwardRecord record) {
		return recordMapper.countBy(record);
	}

	public void copyAndAward(Body body) {
		String ruleCode = body.getString("ruleCode");
		String mappingId = body.getString("mappingId");
		String excDesc = body.getString("excDesc");
		String userId = body.getString("userId");
		String stdId = body.getString("stdId");
		String triggerUserId = body.getString("triggerUserId");
		String ruleType = body.getString("ruleType");

		AtsAwardRule ruleInfo = awardRuleService.getRule(ruleCode);

		String zhimiCount = ruleInfo.getZhimiCount();

		AtsAwardRecord record = new AtsAwardRecord();
		record.setZhimiCount(zhimiCount);
		record.setExpCount("0");
		record.setRuleCode(ruleCode);
		record.setMappingId(mappingId);
		record.setAwardDesc(excDesc);
		record.setUserId(userId);
		record.setStdId(stdId);
		record.setTriggerUserId(triggerUserId);
		record.setRuleType(ruleType);
		record.setMappingId(mappingId);

		award(record);

		copyZhimi(userId, stdId);
	}



	public List<AtsAwardRecord> getZmRepairList(){
		return accountMapper.selectZmRepairList();
	}
}
