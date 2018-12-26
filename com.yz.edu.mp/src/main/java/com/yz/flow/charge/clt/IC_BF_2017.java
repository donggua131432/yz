package com.yz.flow.charge.clt;

import com.yz.constants.FinanceConstants;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.markting.MpResult;
import com.yz.model.CommunicationMap;
import com.yz.util.StringUtil;

import java.math.BigDecimal;

/**
 * @描述: 2017年度以前的缴费赠送智米
 * @作者: DuKai
 * @创建时间: 2017/12/14 18:38
 * @版本号: V1.0
 *
 * @Param ① 2015,2016,2017级在读学员缴费科目为第二年及以后的 按照缴费金额1:3赠送智米
 */
public class IC_BF_2017 implements Calculator {

    @Override
    public MpResult output(MpCondition condition) {
        String userId = condition.getString("userId");
        String stdId = condition.getString("stdId");
        String payable = condition.getString("payable");
        String mappingId = condition.getString("mappingId");
        String grade = condition.getString("grade");

        if (StringUtil.hasValue(userId) || StringUtil.hasValue(stdId)) {
            CommunicationMap map = new CommunicationMap();
            BigDecimal a = new BigDecimal(payable);
            a = a.multiply(new BigDecimal(3));

            map.put("userId", userId);
            map.put("stdId", stdId);
            map.put("accType", FinanceConstants.ACC_TYPE_ZHIMI);
            map.put("action", FinanceConstants.ACC_ACTION_IN);
            map.put("excDesc", grade+"级学员缴费，按照缴费金额1:3赠送智米");
            map.put("amount", a.setScale(2, BigDecimal.ROUND_DOWN));
            map.put("ruleType", FinanceConstants.AWARD_RT_MARKTING);
            map.put("triggerUserId", null);
            map.put("mappingId", mappingId);

            String ruleCode = this.getClass().getSimpleName();
            map.put("ruleCode", ruleCode);

            return new MpResult(map);
        }
        return null;
    }
}
