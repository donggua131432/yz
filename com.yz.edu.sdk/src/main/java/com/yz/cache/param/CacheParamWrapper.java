package com.yz.cache.param;

import com.google.common.collect.Maps;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.util.ClassUtil;
import com.yz.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 
 * @desc mybatis 参数封装工具类
 * @author Administrator
 *
 */
public class CacheParamWrapper {

	private static Logger logger = LoggerFactory.getLogger(CacheParamWrapper.class);

	/**
	 * 
	 * @return
	 */
	public static Map<String,Object> wrapMap(Object param) {
		Map<String,Object> paramMap = Maps.newHashMap();
		if (param instanceof Map) // map结构 直接添加
		{
			paramMap.putAll((Map) param);
		} else if (ClassUtil.isBaseDataType(param.getClass())) {
			paramMap.put("onlyOne", true);
			paramMap.put("value", param);
		} else {
			setMap(paramMap, param);
		}
		Page page = PageHelper.getPage();
		if (page != null) {
			paramMap.put("pageSize", page.getPageSize());
			paramMap.put("pageNum", page.getPageNum());
		}
		logger.info("param:{}>wrapMap,Map:{}", param, paramMap);
		return paramMap;
	}

	/**
	 * 
	 * @param mhsMap
	 * @param bean
	 */
	private static void setMap(Map<String,Object> paramMap, Object bean) {
		paramMap.putAll(JsonUtil.object2Map(bean));
	}
}
