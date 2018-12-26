package com.yz.flow.charge.trigger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yz.constants.FinanceConstants;
import com.yz.constants.StudentConstants;
import com.yz.flow.charge.clt.CC_AF_2017_06_CJBM_JXJ;
import com.yz.flow.charge.clt.CC_AF_2017_06_CJBM_PTQE;
import com.yz.flow.charge.clt.CC_AF_2017_06_CJBM_YY;
import com.yz.flow.charge.clt.CC_AF_2017_06_CJZC_JXJ;
import com.yz.flow.charge.clt.CC_AF_2017_06_CJZC_PTQE;
import com.yz.flow.charge.clt.CC_AF_2017_06_CJZC_YY;
import com.yz.flow.charge.clt.CC_AF_2017_06_GKBM;
import com.yz.markting.*;
import com.yz.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 粉丝B有上线，并且粉丝B报读了如下类型
 * 
 * @Param ① 成教全额，报名送10000，注册送50000
 * @Param ② 成教奖学金，报名送5000，注册送50000
 * @Param ③ 成教惠州圆梦，报名送5000，注册送50000
 * @Param ⑦ 国开，报名送10000
 * 
 * @author C
 *
 */
public class CT_AF_2017_06 extends AbstractTrigger {

	private static final Logger log = LoggerFactory.getLogger(CT_AF_2017_06.class);

	//圆梦计划
	private List<String> yyScholarships = new ArrayList<String>();
	//奖学金
	private List<String> jxjScholarships = new ArrayList<String>();

	{
		//圆梦计划
		yyScholarships.add("17"); //惠州圆梦
		yyScholarships.add("15"); //东莞圆梦
		yyScholarships.add("16"); //东莞圆梦2
		//奖学金
		jxjScholarships.add("2"); //全额奖学金
		jxjScholarships.add("3"); //东莞奖学金
		jxjScholarships.add("5"); //阳江奖学金
		jxjScholarships.add("6"); //河源奖学金
		jxjScholarships.add("7"); //湛江奖学金
		jxjScholarships.add("8"); //肇庆奖学金
		jxjScholarships.add("9"); //韶关奖学金
		jxjScholarships.add("10");//惠州奖学金
		jxjScholarships.add("12");//梅州奖学金
		jxjScholarships.add("13");//全省奖学金-普
		jxjScholarships.add("21");//全省奖学金-全
		jxjScholarships.add("23");//圆梦奖学金

	}

	@Override
	public Calculator getCalculator(MpCondition condition) {
		Date createTime = condition.getDate("createTime");
		log.debug("-------------------------匹配注册时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
		String scholarship = condition.getString("scholarship");
		String recruitType = condition.getString("recruitType");
		int lSize = condition.getInt("lSize");
		
		if(lSize > 0) return null; //历届学员不做赠送

		Object ics = condition.get("itemCodes");
		Object iys = condition.get("itemYears");
		String[] itemCodes = ics == null ? null : (String[]) ics;
		String[] itemYears = iys == null ? null : (String[]) iys;

		Date time = DateUtil.convertDateStrToDate("20170531", "yyyyMMdd");
		Date endTime = DateUtil.convertDateStrToDate("20171101", "yyyyMMdd");

		if (createTime != null && createTime.after(time) && createTime.before(endTime)) {
			if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
				Calculators calculators = new Calculators();

				if (itemCodes != null) {
					for (String itemCode : itemCodes) {
						if (FinanceConstants.FEE_ITEM_CODE_Y0.equals(itemCode)) {
							if (StudentConstants.SCHOLARSHIP_NORMARL.equals(scholarship)) {
								log.debug("-------------------------普通全额报名-赠送上线智米匹配规则：CC_AF_2017_06_CJBM_PTQE");
								calculators.addCalculator(new CC_AF_2017_06_CJBM_PTQE());
							} else if (yyScholarships.contains(scholarship)) {
								log.debug("-------------------------圆梦计划报名-赠送上线智米匹配规则：CC_AF_2017_06_CJBM_YY");
								calculators.addCalculator(new CC_AF_2017_06_CJBM_YY());
							} else if (jxjScholarships.contains(scholarship)) {
								log.debug("-------------------------奖学金报名-赠送上线智米匹配规则：CC_AF_2017_06_CJBM_JXJ");
								calculators.addCalculator(new CC_AF_2017_06_CJBM_JXJ());
							}
							break;
						}
					}
				}

				if (itemYears != null) {
					for (String itemYear : itemYears) {
						if ("1".equals(itemYear)) {
							if (StudentConstants.SCHOLARSHIP_NORMARL.equals(scholarship)) {
								log.debug("-------------------------普通全额注册-赠送上线智米匹配规则：CC_AF_2017_06_CJZC_PTQE");
								calculators.addCalculator(new CC_AF_2017_06_CJZC_PTQE());
							} else if (yyScholarships.contains(scholarship)) {
								log.debug("-------------------------圆梦计划注册-赠送上线智米匹配规则：CC_AF_2017_06_CJZC_YY");
								calculators.addCalculator(new CC_AF_2017_06_CJZC_YY());
							} else if (jxjScholarships.contains(scholarship)) {
								log.debug("-------------------------奖学金注册-赠送上线智米匹配规则：CC_AF_2017_06_CJZC_JXJ");
								calculators.addCalculator(new CC_AF_2017_06_CJZC_JXJ());
							}
							break;
						}
					}
				}

				return calculators;
			} else if (StudentConstants.RECRUIT_TYPE_GK.equals(recruitType)) {
				if (itemYears != null) {
					for (String itemYear : itemYears) {
						if ("1".equals(itemYear)) {
							log.debug("-------------------------国开报名-赠送上线智米匹配规则：CC_AF_2017_06_GKBM");
							return new CC_AF_2017_06_GKBM();
						}
					}
				}
			}
		}
		return null;
	}

}
