package com.yz.flow.charge.clt;

import com.yz.constants.FinanceConstants;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.markting.MpResult;
import com.yz.model.CommunicationMap;

/**
 * 2017年6月以后 报读国家开放大学 并缴纳第一年费用
 * 
 * @author C
 *
 */
public class CC_AF_2017_06_GKBM implements Calculator {

	@Override
	public MpResult output(MpCondition condition) {
		String zhimi = "5000";

		String pId = condition.getString("pId");
		String userId = condition.getString("userId");
		String mappingId = condition.getString("mappingId");

		CommunicationMap map = new CommunicationMap();

		map.put("userId", pId);
		map.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
		map.put("action", FinanceConstants.ACC_ACTION_IN);
		map.put("excDesc", "邀约2017年6月以后录入的被邀约人，报读国家开放大学 并缴纳第一年费用");
		map.put("amount", zhimi);
		map.put("ruleType", FinanceConstants.AWARD_RT_MARKTING);
		map.put("triggerUserId", userId);
		map.put("mappingId", mappingId);

		String ruleCode = this.getClass().getSimpleName();
		map.put("ruleCode", ruleCode);

		return new MpResult(map);
	}

}
