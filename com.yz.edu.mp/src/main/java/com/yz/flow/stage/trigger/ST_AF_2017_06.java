package com.yz.flow.stage.trigger;

import java.util.Date;

import com.yz.constants.StudentConstants;
import com.yz.flow.stage.clt.SC_AF_2017_06;
import com.yz.markting.AbstractTrigger;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.util.DateUtil;

/**
 * 被邀约人报读了如下类型的
 * 
 * @Param ① 国开，注册送50000
 * @author C
 *
 */
public class ST_AF_2017_06 extends AbstractTrigger {

	@Override
	public Calculator getCalculator(MpCondition condition) {
		Date createTime = condition.getDate("createTime");
		String recruitType = condition.getString("recruitType");
		
		String stdStage = condition.getString("stdStage");

		Date time =  DateUtil.convertDateStrToDate("20170531", "yyyyMMdd");
		Date endTime = DateUtil.convertDateStrToDate("20171102", "yyyyMMdd");
		
		int lSize = condition.getInt("lSize");

		if (lSize > 0) return null; //历届学员不做赠送

		if (createTime != null && createTime.after(time) && createTime.before(endTime)) {
			if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
				if (StudentConstants.STD_STAGE_STUDYING.equals(stdStage)) {
					return new SC_AF_2017_06();
				}
			}
		}
		return null;
	}

}
