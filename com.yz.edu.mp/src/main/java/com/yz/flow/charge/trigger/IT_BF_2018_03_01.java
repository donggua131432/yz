package com.yz.flow.charge.trigger;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yz.constants.StudentConstants;
import com.yz.flow.charge.clt.IC_BF_2018_03_01;
import com.yz.markting.AbstractTrigger;
import com.yz.markting.Calculator;
import com.yz.markting.MpCondition;
import com.yz.util.DateUtil;

/**
 * 2015级 [大专学员]在规定时间[在线]申请报读[2018级（不含国开）] 缴纳Y1的时间在2018年3月1日前的赠送学员智米50000
 * 自身受益
 * 
 * @author C
 *
 */
public class IT_BF_2018_03_01 extends AbstractTrigger {

	@Override
	public Calculator getCalculator(MpCondition condition) {
		if(true)//流程未确定 ， 先不开放
			return null;
		
		List<Object> learnList = null;
		Object o = condition.get("learnList");
		if (o != null)
			learnList = (List<Object>) o;

		boolean isDaZhuan = false; // 是否大专学员

		boolean isRight = false;// 是否正常状态下的学员

		boolean isAllow = false;// 是否允许参与活动

		String[] pfsnLevels = { "5", "3" };// 大专学历

		String[] stages = { StudentConstants.STD_STAGE_STUDYING, StudentConstants.STD_STAGE_FINISH };

		if (!learnList.isEmpty()) {

			for (Object l : learnList) {

				if (l == null)
					continue;

				Map<String, String> m = (Map<String, String>) l;
				String grade = m.get("grade");
				String stdStage = m.get("stdStage");
				String recruitType = m.get("recruitType");
				String pfsnLevel = m.get("pfsnLevel");

				if ("2015".equals(grade)) {

					if (!StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType))
						continue;

					for (String s : stages) {
						if (s.equals(stdStage)) {
							isRight = true;
							break;
						}
					}

					if (!isRight)
						continue;

					for (String pl : pfsnLevels) {
						if (pl.equals(pfsnLevel)) {
							isDaZhuan = true;
							break;
						}
					}

					if (!isDaZhuan)
						continue;

					isAllow = true;
					break;
				}
			}

			if (isAllow) {
				String grade = condition.getString("grade");

				boolean isOnline = "1".equals(condition.getString("isOnline"));// 是否在线报名
				if (isOnline && "2018".equals(grade)) {//2018级学生

					Object iys = condition.get("itemYears");

					String[] itemYears = iys == null ? null : (String[]) iys;

					if (itemYears != null) {
						for (String itemYear : itemYears) {
							if ("1".equals(itemYear)) {
								Date time = DateUtil.convertDateStrToDate("20180301", "yyyyMMdd");
								if (time.after(new Date())) {
									return new IC_BF_2018_03_01();
								}
							}
						}
					}
				}
			}

		}

		return null;
	}

}
