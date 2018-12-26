package com.yz.flow.charge.trigger;

import java.util.Date;

import com.yz.flow.charge.clt.CC_AF_2017_11_2;
import com.yz.markting.AbstractTrigger;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.util.DateUtil;
/**
 * 于2017-11-02之后2018-03-01之前邀约进入平台的用户
 * @author C
 *
 */
public class CT_AF_2017_11_2 extends AbstractTrigger {

	@Override
	public Calculator getCalculator(MpCondition condition) {
		Date createTime = condition.getDate("createTime");
		Date time = DateUtil.convertDateStrToDate("20171102", "yyyyMMdd");
		Date endTime = DateUtil.convertDateStrToDate("20190401", "yyyyMMdd");
		
		int lSize = condition.getInt("lSize");
		if (lSize > 0) return null; //历届学员不做赠送
		if (createTime != null && createTime.after(time) && createTime.before(endTime)) {
			return new CC_AF_2017_11_2();
		}

		return null;
	}

}
