package com.yz.flow.charge.clt;

import com.yz.constants.FinanceConstants;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.markting.MpResult;
import com.yz.model.CommunicationMap;

/**
 * @描述: 参与考前冲刺活动
 * @作者: DuKai
 * @创建时间: 2017/12/29 15:48
 * @版本号: V1.0
 *
 * @Param 被推荐学员在2018-03-01前注册学籍缴纳第一年学费时，推荐人可获得50000智米
 */
public class CC_BF_2018_03_01_CJZC_PTKQCC implements Calculator {
    @Override
    public MpResult output(MpCondition condition) {
        String pId = condition.getString("pId");
        String userId = condition.getString("userId");
        String mappingId = condition.getString("mappingId");

        CommunicationMap map = new CommunicationMap();
        map.put("userId", pId);
        map.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
        map.put("action", FinanceConstants.ACC_ACTION_IN);
        map.put("excDesc", "邀请在2017年08月21日之后2017年09月10日之前录入的被邀约人，报读成教考前冲刺并缴纳第一年学费");
        map.put("amount", "50000");
        map.put("ruleType", FinanceConstants.AWARD_RT_MARKTING);
        map.put("triggerUserId", userId);
        map.put("mappingId", mappingId);

        String ruleCode = this.getClass().getSimpleName();
        map.put("ruleCode", ruleCode);

        return new MpResult(map);
    }
}
