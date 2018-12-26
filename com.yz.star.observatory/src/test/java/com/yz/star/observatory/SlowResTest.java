package com.yz.star.observatory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.model.EsTraceAnnotation;
import com.yz.template.YzBuzResolverEngine;
import java.util.*;

/**
 * 
 * @desc freemark 测试 
 * @author Administrator
 *
 */
public class SlowResTest {

	
	private static YzBuzResolverEngine templateEngine = YzBuzResolverEngine.getInstance();
	
	
	public static void main(String[] args) {
		Map<String,Object>data =Maps.newHashMap();
		EsTraceAnnotation annotation = new EsTraceAnnotation();
		annotation.setDate(new Date());
		annotation.setResouceType(1);
		annotation.setDestination(1000l);
		annotation.setOperation("hget");
		annotation.setTraceId("1");
		annotation.setSpanId("1");
		annotation.setSort(1);
		annotation.setResouceURI("localhost");
		data.put("annotations", Lists.newArrayList(annotation));
		templateEngine.resolveTemplate(ObservatoryStarConstant.SLOW_TEMPLATE, data);
	}
}
