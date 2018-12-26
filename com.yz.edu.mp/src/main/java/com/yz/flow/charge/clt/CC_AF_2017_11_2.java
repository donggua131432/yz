package com.yz.flow.charge.clt;

import java.math.BigDecimal;

import com.yz.constants.FinanceConstants;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.markting.MpResult;
import com.yz.model.CommunicationMap;

public class CC_AF_2017_11_2 implements Calculator {

	@Override
	public MpResult output(MpCondition condition) {
		String pId = condition.getString("pId");
		String userId = condition.getString("userId");
		String payable = condition.getString("payable");
		String mappingId = condition.getString("mappingId");

		CommunicationMap map = new CommunicationMap();

		BigDecimal a = new BigDecimal(payable);

		a = a.multiply(new BigDecimal(5));

		map.put("userId", pId);
		map.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
		map.put("action", FinanceConstants.ACC_ACTION_IN);
		map.put("excDesc", "邀请2017年11月2号以后录入的被邀约人，缴费后");
		map.put("amount", a.setScale(2, BigDecimal.ROUND_DOWN));
		map.put("ruleType", FinanceConstants.AWARD_RT_MARKTING);
		map.put("triggerUserId", userId);
		map.put("mappingId", mappingId);

		String ruleCode = this.getClass().getSimpleName();
		map.put("ruleCode", ruleCode);

		return new MpResult(map);
	}

}
