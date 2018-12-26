package com.yz.edu.sdk.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yz.template.YzBuzResolverEngine;

/**
 * 
 * 
 * @author Administrator
 *
 */
public class YzBuzResolverEngineTest {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String,Object>param = Maps.newHashMap();
		List data = Lists.newArrayList();
		Date startDate = new Date();
		Date endDate = new Date();
		
		Map<String,Object>row= Maps.newHashMap();
		row.put("startDate", startDate);
		row.put("endDate", endDate);
		row.put("zhimiCount",100);
		row.put("expCount",100);
		row.put("ruleCode",100);
		data.add(row);
		
		param.put("rules", data);
		YzBuzResolverEngine.getInstance().resolver("user_register", param, null);
	}
}
