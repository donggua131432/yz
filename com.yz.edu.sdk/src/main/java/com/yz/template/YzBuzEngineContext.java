package com.yz.template;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yz.util.StringUtil;

/**
 * 
 * @desc 模板上下文环境
 * @author Administrator
 *
 */
public class YzBuzEngineContext {

	private Logger logger = LoggerFactory.getLogger(YzBuzEngineContext.class);

	private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(Maps::newHashMap);

	private static class YzBuzEngineContextHolder {
		private static YzBuzEngineContext _instance = new YzBuzEngineContext();
	}

	private YzBuzEngineContext() {
	}

	public static YzBuzEngineContext getInstance() {
		return YzBuzEngineContextHolder._instance;
	}

	/**
	 * 
	 * @param obj
	 */
	public void setContent(Object obj) {
		if (obj != null) {
			String keyName = obj.getClass().getName();
			Map<String, Object> data = context.get();
			if (data.containsKey(keyName)) {
				Object src = data.get(keyName);
				List list = null;
				if (src instanceof List) {
					list = (List) src;
					list.add(obj);
				} else {
					list = Lists.newArrayList(obj, src);
				}
				data.put(keyName, list);
			} else {
				data.put(keyName, obj);
			}
		}

	}

	/**
	 * @param key
	 * @param obj
	 */
	public void setContextAttr(String key, Object obj) {
		if (obj != null) {
			context.get().put(key, obj);
		}
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 *            默认值
	 * @return
	 */
	public Object getContextAttr(String key, Object obj) {
		if (StringUtil.isNotBlank(key)) {
			return context.get().get(key);
		}
		return obj;
	}
	
	
	/**
	 * 
	 * @param param
	 */
	public void addContextParam(Map<String,Object>param) {
	      if(param!=null&&!param.isEmpty())
	      {
	    	  this.context.get().putAll(param);
	      }
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 *            默认值
	 * @return
	 */
	public Object getContextAttr(String key) {
		return this.getContextAttr(key, null);
	}

	/**
	 * 
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getContext(Class<?> cls) {
		if (cls != null) {
			return (T) context.get().get(cls.getName());
		}
		logger.error("cls is null,check config again!");
		return null;
	}
	
	
	/**
	 * 
	 */
	public void clear() {
		context.remove();
	} 
}
