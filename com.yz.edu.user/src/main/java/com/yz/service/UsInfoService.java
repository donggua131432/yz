package com.yz.service;

import java.util.*;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yz.edu.paging.common.PageHelper;
import com.yz.constants.FinanceConstants;
import com.yz.constants.GlobalConstants;
import com.yz.dao.UsBaseInfoMapper;
import com.yz.dao.UsGiveStatusMapper;
import com.yz.model.AtsAccountSerial;
import com.yz.model.AtsAwardRecord;
import com.yz.model.AtsAwardRule;
import com.yz.model.UsBaseInfo;
import com.yz.model.UsGiveStatus;
import com.yz.util.Assert;
import com.yz.util.CodeUtil;

@Service
@Transactional
public class UsInfoService {
	private static final Logger log = LoggerFactory.getLogger(UsInfoService.class);

	@Autowired
	private UsBaseInfoMapper baseInfoMapper;

	@Autowired
	private UsGiveStatusMapper giveStatusMapper;

	//@Reference(version = "1.0")
	//private AtsAccountApi accountApi;
	@Autowired
	private AccountService accountService;
	
	@Autowired
    private AwardRuleService awardRuleService;
	
	@SuppressWarnings("unchecked")
	public Map<String, String> complete(UsBaseInfo baseInfo, UsGiveStatus giveStatus) {

		Map<String, String> result = new HashMap<String, String>();

		baseInfoMapper.updateByPrimaryKeySelective(baseInfo);

		int count = giveStatusMapper.countBy(giveStatus);
		if (count == 0) {
			giveStatus.setIsGive(GlobalConstants.TRUE);
			giveStatus.setGiveTime(new Date());
			giveStatusMapper.insertSelective(giveStatus);

//			Body body = new Body();
//
//			body.put("userId", giveStatus.getUserId());
//			body.put("ruleCode", "complete.other.info");
//			body.put("ruleType", FinanceConstants.AWARD_RT_NORMAL);
//			body.put("excDesc", "用户完善" + giveStatus.getFieldDesc() + "信息");
//			body.put("mappingId", giveStatus.getGiveId());
//			body.put("triggerUserId", giveStatus.getUserId());

//			Map<String, String> amountInfo = accountApi.award(body);
			
			String ruleCode = "complete.other.info";
			String userId = giveStatus.getUserId();
			String stdId = null;
			String empId = null;
			String triggerUserId = giveStatus.getUserId();
			String excDesc = "用户完善" + giveStatus.getFieldDesc() + "信息";
			String mappingId =  giveStatus.getGiveId();
			String ruleType = FinanceConstants.AWARD_RT_NORMAL;
			

			Assert.hasText(userId, "用户ID不能为空");
			Assert.hasText(excDesc, "动账描述不能为空");
			Assert.hasText(ruleCode, "奖励规则编码不能为空");

			Map<String, String> amountInfo = new HashMap<String, String>();
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
			
			result.put("isFisrt", GlobalConstants.TRUE);
			result.put("amount", amountInfo.get("amount"));
		} else if (count > 0) {
			result.put("isFisrt", GlobalConstants.FALSE);
			result.put("amount", "0");
		}

		return result;
	}

	/**
	 * 获取附属信息
	 * 
	 * @param userId
	 * @return
	 */
	public Object getOtherInfo(String userId) {

		Map<String, String> otherInfo = baseInfoMapper.getOtherInfo(userId);

		return otherInfo;
	}

	/**
	 * 获取粉丝列表
	 * 
	 * @param userId
	 * @return
	 */
	public Object getFans(String userId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize,false);
		List<Map<String, String>> list = baseInfoMapper.getMyFans(userId);

		if (list != null) {
			for (Map<String, String> param : list) {
				param.put("nickname", CodeUtil.base64Decode2String(param.get("nickname")));
			}
		}

		return list;
	}

	/**
	 * 通过userId获取openId
	 * 
	 * @param userId
	 * @return
	 */
	public String getOpenIdByUserId(String userId) {
		return baseInfoMapper.getOpenIdByUserId(userId);
	}

	/**
	 * 批量获取openIds
	 * @param userIds	用户id集合
	 * @return
	 */
	public List<String> getOpenIdsByUserIds(List<String> userIds) {
		return baseInfoMapper.getOpenIdsByUserIds(userIds);
	}

    /**
     * @desc undo use cache 
     * @return
     */
	public Object getNewRegList() {  
		return baseInfoMapper.selectNewRegList();
	}
}
