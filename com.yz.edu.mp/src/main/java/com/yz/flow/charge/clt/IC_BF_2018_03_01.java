package com.yz.flow.charge.clt;

import com.yz.constants.FinanceConstants;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.markting.MpResult;
import com.yz.model.CommunicationMap;
import com.yz.util.StringUtil;

public class IC_BF_2018_03_01 implements Calculator {

	@Override
	public MpResult output(MpCondition condition) {
		String zhimi = "50000";

		String userId = condition.getString("userId");

		String stdId = condition.getString("stdId");

		if (StringUtil.hasValue(userId) || StringUtil.hasValue(stdId)) {

			CommunicationMap map = new CommunicationMap();

			map.put("userId", userId);
			map.put("stdId", stdId);
			map.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
			map.put("action", FinanceConstants.ACC_ACTION_IN);
			map.put("excDesc", "通过线上报名于2018年3月1号之前缴纳第一年学费的2015级大专学员");
			map.put("amount", zhimi);
			map.put("ruleType", FinanceConstants.AWARD_RT_MARKTING);
			String ruleCode = this.getClass().getSimpleName();
			map.put("ruleCode", ruleCode);
			map.put("triggerUserId", null);
			return new MpResult(map);
		}

		return null;
	}

}
