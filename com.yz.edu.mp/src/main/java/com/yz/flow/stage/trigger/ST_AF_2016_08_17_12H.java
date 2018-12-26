package com.yz.flow.stage.trigger;

import java.util.Date;

import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.flow.stage.clt.SC_AF_2016_08_17_12H_GKZC;
import com.yz.markting.AbstractTrigger;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.util.DateUtil;

/**
 * 被推荐人报读了如下类型的
 * 
 * @Param ① 成教全额，报名送10000，注册送50000
 * @Param ② 成教圆梦，报名送0，注册送50000
 * @Param ③ 成教奖学金，报名送5000，注册送50000
 * @Param ⑥ 国开，缴纳了第一年费用送10000，注册在读送50000
 * 
 * @author C
 *
 */
public class ST_AF_2016_08_17_12H extends AbstractTrigger {

	@Override
	public Calculator getCalculator(MpCondition condition) {
		Date createTime = condition.getDate("createTime");
		String recruitType = condition.getString("recruitType");
		//String pIsMb = condition.getString("pIsMb");
		
		String stdStage = condition.getString("stdStage");

		Date time = DateUtil.convertDateStrToDate("20160817120000", "yyyyMMddHHmmss");
		
		int lSize = condition.getInt("lSize");

		if (lSize > 0) // 历届学员不做赠送
			return null;
		//if (createTime != null && createTime.after(time) && GlobalConstants.TRUE.equals(pIsMb)) {
		if (createTime != null && createTime.after(time)) {
			if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
				if (StudentConstants.STD_STAGE_STUDYING.equals(stdStage)) {
					return new SC_AF_2016_08_17_12H_GKZC();
				}
			}
		}
		return null;
	}

}
