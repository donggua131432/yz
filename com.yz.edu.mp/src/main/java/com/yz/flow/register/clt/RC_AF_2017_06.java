package com.yz.flow.register.clt;

import com.yz.constants.FinanceConstants;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.markting.MpResult;
import com.yz.model.CommunicationMap;

/**
 * 在2017年6月后注册成为平台用户 赠送100智米
 * 
 * @author C
 *
 */
public class RC_AF_2017_06 implements Calculator {

	@Override
	public MpResult output(MpCondition condition) {
		String zhimi = "100";

		String pId = condition.getString("pId");
		String userId = condition.getString("userId");

		CommunicationMap map = new CommunicationMap();

		map.put("userId", pId);
		map.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
		map.put("action", FinanceConstants.ACC_ACTION_IN);
		map.put("excDesc", "邀约用户在2017年6月后注册成为平台用户后");
		map.put("amount", zhimi);
		map.put("ruleType", FinanceConstants.AWARD_RT_MARKTING);
		map.put("triggerUserId", userId);

		String ruleCode = this.getClass().getSimpleName();
		map.put("ruleCode", ruleCode);

		return new MpResult(map);
		
	}

}
