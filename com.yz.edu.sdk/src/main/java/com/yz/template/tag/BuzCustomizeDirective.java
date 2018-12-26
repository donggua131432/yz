package com.yz.template.tag;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.yz.template.handler.YzBuzHandler;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;
import com.yz.util.MapUtil;
import com.yz.util.ReflectionUtils;
import com.yz.util.StringUtil;

import freemarker.core.Environment;
import freemarker.core.TemplateElement;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

/**
 * 
 * @author Administrator
 *
 */
public class BuzCustomizeDirective implements TemplateDirectiveModel {

	private static Logger logger = LoggerFactory.getLogger(BuzCustomizeDirective.class);

	@Override
	public void execute(Environment env, Map map, TemplateModel[] template, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String handler = StringUtil.obj2String(map.get("handler"));
		String param = StringUtil.obj2String(map.get("param"));
		logger.info("handler:{},param:{}", handler, param);
		Map<String, Object> paramMap = Maps.newHashMap();
		JSONObject jb = JSONObject.fromObject(param);
		paramMap=(Map<String, Object>)jb;
		YzBuzHandler buzHandler = ApplicationContextUtil.getBeanIgnoreEx(handler);
		boolean bol = buzHandler.doHandler(paramMap); // 如果不满足条件 直接删除该标签
		if (bol)
			try {
				ReflectionUtils.invokeMethod(env, "visit", new Class[] { TemplateElement[].class },
						new Object[] { ReflectionUtils.getFieldValue(body, "childBuffer") });
			} catch (InvocationTargetException e) {
				logger.error("execute.error:{}", ExceptionUtil.getStackTrace(e));
			}
	}
}
