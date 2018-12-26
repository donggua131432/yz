package com.yz.edu.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yz.job.model.RechargeAwardRule;
import com.yz.template.YzBuzResolverEngine;
import com.yz.util.JsonUtil;
/**
 * 
 * 
 * @author Administrator
 *
 */
public class YzBuzResolverEngineTest {
	public static void main(String[] args) {
		Map<String,Object>param = Maps.newHashMap();
		List data = Lists.newArrayList();
		Map<String, Object> row = Maps.newHashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date payDateTime = null;
		try {
			row.put("startTime", sdf.parse("2017-11-02 00:00:00"));
			row.put("endTime", sdf.parse("2019-04-01 00:00:00"));
			param.put("createTime", sdf.parse("2018-05-01 00:00:00"));
//			param.put("payDateTime", sdf.parse("2017-08-17 00:00:00"));
//			payDateTime = sdf.parse("2017-08-17 00:00:00");
		} catch (Exception e) {

		}
		row.put("ruleCode", "recharge_1");
		row.put("ruleDesc", "邀请2017年11月2号以后录入的被邀约人，缴费后(历届学员不做赠送)");
		row.put("zhimiCount", "0");
		row.put("expCount", "0");
		row.put("ruleGroup", "recharge");
		row.put("isMutex", "1"); 
		row.put("isRepeat", "1");
		row.put("sort", "1");
		row.put("ruleType", "1");
		Map<String, String> attr = Maps.newHashMap();
//		attr.put("payDateTime", "2017-08-17 00:00:00");
		attr.put("lSize", "0");
		attr.put("isParent", "1");
//		attr.put("itemCode", "Y1");
		attr.put("multiple", "5");
		row.put("customizeAttr", attr);
		row.put("customizeAttrString", JsonUtil.object2String(attr));
		data.add(row);
		
		param.put("rules", data);
		param.put("userId", "11111");
		param.put("pId", "2222");
//		param.put("event",data);
		param.put("event",JsonUtil.object2String(attr));
		param.put("payable", "5000");
//		param.put("recruitType", "1");
//		param.put("itemCodes", "Y1,Y2,Y0");
//		param.put("itemYears", "1,2,3");
//		param.put("scholarship", "1");
		param.put("lSize", "0");
//		param.put("grade", "2017");
		List<RechargeAwardRule> rules = YzBuzResolverEngine.getInstance()
				.resolverList("user_recharge", param, RechargeAwardRule.class);
		System.out.println(JsonUtil.object2String(rules) + JsonUtil.object2String(param));
	}
}
