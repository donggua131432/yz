package com.yz.job.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GwConstants;
import com.yz.generator.IDGenerator;
import com.yz.job.common.YzTaskContext;
import com.yz.job.dao.UserRegisterMapper;
import com.yz.job.model.AtsAccount;
import com.yz.job.model.AtsAccountSerial;
import com.yz.job.model.AtsAwardRecord;
import com.yz.job.model.RechargeAwardRule;
import com.yz.model.UserReChargeEvent;
import com.yz.template.YzBuzResolverEngine;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 充值赠送服务
 * 
 * @ClassName: UserReChargeService
 * @Description: 处理用户充值，推荐人能获得的智米奖励
 * @author zhanggh
 * @date 2018年5月5日
 *
 */
@Service
public class UserReChargeService {
	private static Logger logger = LoggerFactory.getLogger(UserReChargeService.class);

	@Autowired
	private UserRegisterMapper userRegisterMapper;

	/**
	 * 充值赠送操作
	 * 
	 * @param event
	 */
	public void reCharge(UserReChargeEvent event) {
		if (event != null) {
			award(event);
		}
	}

	/**
	 * 赠送智米
	 * 
	 * @param event
	 */
	private void award(UserReChargeEvent event) {
		// 过滤规则
		List<RechargeAwardRule> rules = getRules(event);
		if (rules != null && !rules.isEmpty() && rules.size() > 0) {
			Set<RechargeAwardRule> enableRule = Sets.newHashSet();
			RechargeAwardRule rule = rules.stream().sorted().findFirst().get();
			boolean mutex = rule.getIsMutex() == 0; // 奖励规则互斥
			if (mutex) {
				enableRule.add(rule);
			} else {
				enableRule.addAll(rules);
			}
			ArrayList<String> awardDesc = new ArrayList<>();
			Map<String, List<RechargeAwardRule>> data = enableRule.parallelStream()
					.filter(v -> StringUtil.isNotBlank(v.getUserId()))
					.collect(Collectors.groupingBy(RechargeAwardRule::getUserId));
			// 累加
			data.entrySet().stream().forEach(v -> {
				awardDesc.clear();
				long amount = v.getValue().stream().mapToLong(r -> {
					AtsAwardRecord record = new AtsAwardRecord();
					record.setAwardId(IDGenerator.generatorId());
					record.setExpCount(r.getExpCount());
					record.setRuleCode(r.getRuleCode());
					record.setAwardDesc(r.getRuleDesc());
					record.setUserId(r.getUserId());
					record.setTriggerUserId(event.getUserId());
					record.setRuleType(r.getRuleType());
					record.setZhimiCount(r.getZhimiCount());
					record.setMappingId(event.getMappingId());
					awardDesc.add(r.getRuleDesc() + "、");
					this.userRegisterMapper.saveAwardRecord(record);
					return NumberUtils.toLong(r.getZhimiCount());
				}).sum();
				if (amount > 0) {
					// v.getKey() 获取的是赠送的userId
					String awardDescString = StringUtil.join(awardDesc, ",");
					addAtsAccountSerial(v.getKey(), amount, event.getMappingId(),
							awardDescString.substring(0, awardDescString.length() - 1));
					YzTaskContext.getTaskContext().addEventDetail(event.getUserId(),
							"触发" + awardDescString.substring(0, awardDescString.length() - 1) + "赠送规则,"
									+ event.getUserId() + "赠送智米" + String.valueOf(amount));
				}
			});
		}
		logger.error("not found handler rule !");
	}

	/**
	 * 获取符合的规则
	 * 
	 * @param event
	 * @return
	 */
	public List<RechargeAwardRule> getRules(UserReChargeEvent event) {
		List<RechargeAwardRule> rules = new ArrayList<>();
		Map<String, Object> param = Maps.newHashMap();
		param.put("lSize", event.getlSize());// 是否是往届学员 0：本届学员 大于0:往届学员
		param.put("itemCode", JsonUtil.object2String(event.getItemCode()));// 学年ID组Y0,Y1,Y2,Y3
		param.put("itemYear", JsonUtil.object2String(event.getItemYear()));// 学年1,2,3,4
		param.put("scholarship", event.getScholarship());// 优惠类型 1：全额奖学金
		param.put("recruitType", event.getRecruitType());// 招生类型 1：成教 2：国开
		param.put("grade", event.getGrade());// 年级 2015级 2016级
		param.put("payDateTime", event.getPayDateTime());
		param.put("createTime", event.getCreateTime() != null ? event.getCreateTime() : event.getCreateDate());
		param.put("payable", event.getPayable());
		param.put("userId", event.getUserId());
		param.put("pId", event.getpId());
		param.put("stdStage", TextUtils.isEmpty(event.getStdStage())?"":event.getStdStage());
		// 过滤规则
		rules = YzBuzResolverEngine.getInstance().resolverList(GwConstants.USER_RECHARGE_BUZ_ENGINE, param,
				RechargeAwardRule.class);
		return rules;
	}

	/**
	 * @description 更新用户账户信息 ，并记录智米帐变流水
	 * @param userId
	 * @param amount
	 */
	private void addAtsAccountSerial(String userId, Long amount, String mappingId, String ruleDesc) {
		AtsAccount account = userRegisterMapper.getAccountInfo(userId, FinanceConstants.ACC_TYPE_ZHIMI);
		long accAmount = new BigDecimal(account.getAccAmount()).longValue();
		AtsAccountSerial serial = new AtsAccountSerial();
		serial.setAccId(account.getAccId());
		serial.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
		serial.setAction(FinanceConstants.ACC_ACTION_IN);
		serial.setAmount(String.valueOf(amount));
		serial.setUserId(userId);
		serial.setExcDesc(ruleDesc + "，获得" + amount + "智米");
		serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_SUCCESS);
		serial.setBeforeAmount(account.getAccAmount());
		serial.setAfterAmount(String.valueOf(accAmount + amount));
		serial.setAccSerialNo(IDGenerator.generatorId());
		serial.setMappingId(mappingId);
		userRegisterMapper.saveSerial(serial);
		account.setAccAmount(String.valueOf(amount));
		userRegisterMapper.updateAccount(account);
	}
}
