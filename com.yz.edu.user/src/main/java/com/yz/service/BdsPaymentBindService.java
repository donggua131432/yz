package com.yz.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.yz.constants.FinanceConstants;
import com.yz.dao.BdUnawardMapper;
import com.yz.dao.BdsStudentMapper;
import com.yz.dao.StudentMpFlowMapper;
import com.yz.model.CommunicationMap;
import com.yz.model.UserReChargeEvent;
import com.yz.model.communi.Body;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.trace.wrapper.TraceEventWrapper;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Transactional
@Service
public class BdsPaymentBindService {

	private static final Logger log = LoggerFactory.getLogger(BdsPaymentBindService.class);

	@Autowired
	private UsCertService certService;
	
	@Autowired
	private BdUnawardMapper unawardMapper;

	@Autowired
	private StudentMpFlowMapper flowMapper;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private BdsStudentMapper bdsStudentMapper;

	public void bindAndAward(String[] itemCodes, String[] itemYear,String serialMark, String orderNo, String amount, String learnId) {
		// 判断是否需要更新跟进关系
		if (needRefreshRelation(itemCodes)) {
			Body recruitMap = flowMapper.getRecruitMapBySerialMark(serialMark);
			if (recruitMap != null && recruitMap.size() > 0) {
				String stdId = recruitMap.getString("stdId");
				String empStatus = recruitMap.getString("empStatus");
				boolean isIn = StringUtil.hasValue(empStatus) && !"2".equals(empStatus);
				if (!isIn) {
					recruitMap.remove("empId");
					recruitMap.remove("dpId");
					recruitMap.remove("campusId");
				}

				List<Map<String, String>> relations = flowMapper.getRelationsByStd(stdId);
				boolean needRefresh = false;
				if (relations == null || relations.isEmpty()) {
					needRefresh = true;
				} else if (relations.size() == 1) {
					needRefresh = true;
					Map<String, String> rm = relations.get(0);
					recruitMap.putAll(rm);
				} else {
					log.error("----------------- 学员[" + stdId + "]对应多个用户关系 不做处理");
				}
				log.info("变更跟进关系{}",JsonUtil.object2String(recruitMap));
				if (needRefresh) {
					//Map<String, String> userMap = certApi.refreshFollow(recruitMap);
					Map<String, String> userMap = certService.refreshFollow(recruitMap);
					if (userMap != null && userMap.size() > 0) {
						String userId = userMap.get("userId");
						bdsStudentMapper.updateUserIdByStdId(userId, stdId);
						/*int count = flowMapper.countRelation(userMap);
						if (count > 0) {
							flowMapper.updateRelation(userMap);
						} else {
							if (StringUtil.hasValue(stdId)) {
								flowMapper.deleteRelation(stdId);
							}
							flowMapper.insertRelation(userMap);
						}*/
					}
				}
			}
		}

		try {
			// 智米赠送规则
//			List<CommunicationMap> mList = 
					chargeAward(learnId, itemCodes, itemYear, amount.toString(), orderNo);
			//atsApi.awardMore(mList);
//			accountService.awardMore(mList);
			// TODO 代码报错，可能导致智米已赠送，但事务回滚了
		} catch (Exception e) {
			log.error("-------------------流水批次号：[" + serialMark + "] 智米赠送失败", e);
		}
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
	private void chargeAward(String learnId, String[] itemCodes, String[] itemYears, String payable,
			String orderNo) {
		List<CommunicationMap> mList = new ArrayList<CommunicationMap>();

		List<Map<String, String>> relation = flowMapper.getRelation(learnId);

		if (relation != null && relation.size() > 1) {
			log.error("-------------------------- 学业[" + learnId + "]查询到多个用户关系 不做赠送处理");
			return ;
		}

		Body condition = flowMapper.getCondition(learnId);

		String stdId = condition.getString("stdId");

		String pId = condition.getString("pId");

		List<Map<String, String>> learnList = flowMapper.getHistoryLearn(stdId, learnId);

		//构建缴费赠送智米事件
		UserReChargeEvent event = new UserReChargeEvent();
		event.setPayable(payable);
		event.setMappingId(orderNo);
		event.setCreateTime(
				DateUtil.convertDateStrToDate(condition.getString("createTime", ""), DateUtil.YYYYMMDDHHMMSS_SPLIT));
		event.setPayDateTime(new Date());
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
		event.setGrade(condition.getString("grade", ""));
		event.setStdStage("");
		log.info("发送个人缴费上级赠送指令 lpush {} {}",YzTaskConstants.YZ_USER_RECHARGE_EVENT,JsonUtil.object2String(event));
		RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_RECHARGE_EVENT, JsonUtil.object2String(TraceEventWrapper.wrapper(event)));
//		log.debug("发送个人缴费本人赠送指令 lpush {} {}",YzTaskConstants.YZ_USER_RECHARGE_EVENT,JsonUtil.object2String(event));
//		RedisService.getRedisService().lpush(YzTaskConstants.YZ_USER_RECHARGE_EVENT, JsonUtil.object2String(event));
	}
}
