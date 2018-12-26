package com.yz.flow.charge.trigger;

import com.yz.constants.StudentConstants;
import com.yz.flow.charge.clt.CC_BF_2018_03_01_CJZC_PTKQCC;
import com.yz.markting.AbstractTrigger;
import com.yz.markting.Calculator;
import com.yz.markting.Calculators;
import com.yz.markting.MpCondition;
import com.yz.util.DateUtil;

import java.util.Date;

/**
 * @描述: 与2017-08-21之后2017-09-10之前邀约进入平台的用户  参与考前冲刺活动
 * @作者: DuKai
 * @创建时间: 2017/12/29 14:49
 * @版本号: V1.0
 *
 *
 * @Param 被推荐学员在2018-03-01前注册学籍缴纳第一年学费时，推荐人可获得50000智米
 */
public class CT_BF_2018_03_01  extends AbstractTrigger {

    @Override
    public Calculator getCalculator(MpCondition condition) {
        Date createTime = condition.getDate("createTime");
        String scholarship = condition.getString("scholarship");
        String recruitType = condition.getString("recruitType");
        int lSize = condition.getInt("lSize");

        if(lSize > 0) return null; //历届学员不做赠送

        Object iys = condition.get("itemYears");
        String[] itemYears = iys == null ? null : (String[]) iys;

        Date time = DateUtil.convertDateStrToDate("20170821000000", "yyyyMMddHHmmss");
        Date endTime = DateUtil.convertDateStrToDate("20170910235959", "yyyyMMddHHmmss");

        //缴费时间必须在20180301前
        Date payDateTime = DateUtil.convertDateStrToDate("20180301000000", "yyyyMMddHHmmss");
        Date currDateTime = new Date();

        if (currDateTime.before(payDateTime)){
            if (createTime != null && createTime.after(time) && createTime.before(endTime)) {
                if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
                    Calculators calculators = new Calculators();
                    if (itemYears != null) {
                        for (String itemYear : itemYears) {
                            if ("1".equals(itemYear)) {
                                if ("11".equals(scholarship)) {
                                    calculators.addCalculator(new CC_BF_2018_03_01_CJZC_PTKQCC());
                                }
                                break;
                            }
                        }
                    }
                    return calculators;
                }
            }
        }
        return null;
    }
}
