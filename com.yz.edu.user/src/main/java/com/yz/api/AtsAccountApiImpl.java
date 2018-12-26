package com.yz.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants; 
import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.model.AtsAccount;
import com.yz.model.AtsAccountSerial;
import com.yz.model.AtsAwardRecord;
import com.yz.model.AtsAwardRule;
import com.yz.model.CommunicationMap;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.service.AccountService;
import com.yz.service.AwardRuleService;
import com.yz.util.Assert;
import com.yz.util.StringUtil;

@Service(version = "1.0", timeout = 30000, retries = 0)
public class AtsAccountApiImpl implements AtsAccountApi {

	@Autowired
    private AwardRuleService awardRuleService;
	
	@Autowired
	private AccountService accountService;

	@Override
	public Map<String, String> getAccount(String userId, String stdId, String empId, String accType)
			throws IRuntimeException {

		Assert.isTrue(StringUtil.hasValue(userId) || StringUtil.hasValue(stdId) || StringUtil.hasValue(empId),
				"用户、学员、员工ID至少有一个不为空");
		Assert.hasText(accType, "账户类型不能为空");

		AtsAccount account = new AtsAccount();
		account.setStdId(stdId);
		account.setUserId(userId);
		account.setEmpId(empId);
		account.setAccType(accType);

		return accountService.getAccount(account);
	}

	@Override
	public void initAccount(String userId, String stdId, String empId, String[] accTypes) throws IRuntimeException {
		Assert.isTrue(StringUtil.hasValue(userId) || StringUtil.hasValue(stdId) || StringUtil.hasValue(empId),
				"用户、学员、员工ID至少有一个不为空");
		Assert.notNull(accTypes, "账户类型不能为空");

		List<AtsAccount> accountList = new ArrayList<AtsAccount>();

		for (String accType : accTypes) {
			AtsAccount account = new AtsAccount();

			switch (accType) {
			case FinanceConstants.ACC_TYPE_DEMURRAGE:
				account.setCanDeposit(GlobalConstants.FALSE);
				account.setIsVisiable(GlobalConstants.TRUE);
				account.setUnit(FinanceConstants.ACC_UNIT_RMB);
				break;
			case FinanceConstants.ACC_TYPE_NORMAL:
				account.setCanDeposit(GlobalConstants.TRUE);
				account.setIsVisiable(GlobalConstants.TRUE);
				account.setUnit(FinanceConstants.ACC_UNIT_RMB);
				break;
			case FinanceConstants.ACC_TYPE_ZHIMI:
				account.setCanDeposit(GlobalConstants.FALSE);
				account.setIsVisiable(GlobalConstants.TRUE);
				account.setUnit(FinanceConstants.ACC_UNIT_ZHIMI);
				break;
			}

			account.setEmpId(empId);
			account.setUserId(userId);
			account.setStdId(stdId);
			account.setAccType(accType);
			account.setAccStatus(FinanceConstants.ACC_STATUS_NORMAL);

			accountList.add(account);
		}

		accountService.initAccount(accountList);

	}

	@Override
	public Object getAccountList(Header header, Body body) throws IRuntimeException {
		return accountService.getAccoutList(header.getUserId());
	}

	@Override
	public void trans(Body body) throws IRuntimeException {
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
			Assert.isTrue(StringUtil.hasValue(userId) || StringUtil.hasValue(stdId) || StringUtil.hasValue(empId),
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

		accountService.trans(serial);
	}

	@Override
	public Map<String, String> award(CommunicationMap body) throws IRuntimeException {
		String ruleCode = body.getString("ruleCode");
		String userId = body.getString("userId");
		String stdId = body.getString("stdId");
		String empId = body.getString("empId");
		String triggerUserId = body.getString("triggerUserId");
		String excDesc = body.getString("excDesc");
		String mappingId = body.getString("mappingId");
		String ruleType = body.getString("ruleType", FinanceConstants.AWARD_RT_NORMAL);

		Assert.hasText(userId, "用户ID不能为空");
		Assert.hasText(excDesc, "动账描述不能为空");
		Assert.hasText(ruleCode, "奖励规则编码不能为空");

		Map<String, String> amountInfo = new HashMap<String, String>();

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

				AtsAccountSerial serial = accountService.award(record);
				if (serial != null) {
					amountInfo.put("amount", serial.getAmount());
					amountInfo.put("afterAmount", serial.getAfterAmount());
				}
			}
		} else if (FinanceConstants.AWARD_RT_MARKTING.equals(ruleType)) {
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

			AtsAccountSerial serial = accountService.award(record);
			if (serial != null) {
				amountInfo.put("amount", serial.getAmount());
				amountInfo.put("afterAmount", serial.getAfterAmount());
			}
		}

		return amountInfo;
	}

	@Override
	public Object getAccountDetail(Header header, Body body) throws IRpcException {
		String accType = body.getString("accType");
		String accId = body.getString("accId");
		String userId = header.getUserId();

		return accountService.getAccountDetail(accId, userId, accType);
	}

	@Override
	public void copyZhimi(String userId, String stdId) throws IRpcException {
		accountService.copyZhimi(userId, stdId);
	}

	@Override
	public void awardMore(List<CommunicationMap> cmList) {
		accountService.awardMore(cmList);
	}

	@Override
	public void transAndAwardMore(List<Body> transList, List<CommunicationMap> mList) {
		accountService.transAndAwardMore(transList, mList);
	}

	@Override
	public void transMore(List<Body> transList) {
		accountService.transMore(transList);
	}

	@Override
	public void copyAndAward(Body body) {
		accountService.copyAndAward(body);
	}

}
