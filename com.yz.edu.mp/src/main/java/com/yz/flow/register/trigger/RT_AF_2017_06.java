package com.yz.flow.register.trigger;

import java.util.Date;

import com.yz.flow.register.clt.RC_AF_2017_06;
import com.yz.markting.AbstractTrigger;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.util.DateUtil;
/**
 * 在2017年6月后注册成为平台用户
 * @author C
 *
 */
public class RT_AF_2017_06 extends AbstractTrigger {

	@Override
	public Calculator getCalculator(MpCondition condition) {
		Date createTime = condition.getDate("createTime");
		
		Date time = DateUtil.convertDateStrToDate("20170531", "yyyyMMdd");
		
		if(createTime != null && createTime.after(time)) {
			return new RC_AF_2017_06();
		}
		
		return null;
	}

}
