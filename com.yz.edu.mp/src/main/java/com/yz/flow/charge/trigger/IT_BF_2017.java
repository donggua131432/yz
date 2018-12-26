package com.yz.flow.charge.trigger;

import com.yz.flow.charge.clt.IC_AF_201703;
import com.yz.flow.charge.clt.IC_BF_2017;
import com.yz.markting.AbstractTrigger;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;

/**
 * @描述: 学员缴费赠送智米
 * @作者: DuKai
 * @创建时间: 2017/12/15 15:53
 * @版本号: V1.0
 *
 * 2015,2016,2017级学员缴费科目为第一年及以后的 按照缴费金额1:3赠送智米
 * 201703级以后的学员缴费科目为第一年及以后的 按照缴费金额1:1赠送智米
 * 自身受益
 */
public class IT_BF_2017 extends AbstractTrigger {

    @Override
    public Calculator getCalculator(MpCondition condition) {
        int lSize = condition.getInt("lSize");
        if (lSize > 0) return null;// 历届学员不做赠送
        String grade = condition.getString("grade");
        Object iys = condition.get("itemYears");
        String[] itemYears = iys == null ? null : (String[]) iys;

        if("2014".equals(grade) || "2015".equals(grade) ||  "2016".equals(grade) || "2017".equals(grade)){
            if (itemYears != null) {
                for (String itemYear : itemYears) {
                    if ("1".equals(itemYear) || "2".equals(itemYear) || "3".equals(itemYear) || "4".equals(itemYear)) {
                        return new IC_BF_2017();
                    }
                }
            }
        }else{
            if (itemYears != null) {
                for (String itemYear : itemYears) {
                    if ("1".equals(itemYear) || "2".equals(itemYear) || "3".equals(itemYear) || "4".equals(itemYear)) {
                        return new IC_AF_201703();
                    }
                }
            }
        }
        return null;
    }
}
