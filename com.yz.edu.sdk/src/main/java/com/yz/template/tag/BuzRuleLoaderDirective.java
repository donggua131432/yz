package com.yz.template.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map; 

import com.yz.model.AtsAwardRule;
import com.yz.service.SysRuleService;
import com.yz.template.YzBuzEngineContext;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.StringUtil;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 *
 * @desc 模板引擎库加载 
 * @author Administrator
 *
 */
public class BuzRuleLoaderDirective implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		String ruleCode = StringUtil.obj2String(params.get("ruleCode"));
		SysRuleService sysRuleService = ApplicationContextUtil.getBean(SysRuleService.class);
		List<AtsAwardRule> rules = sysRuleService.queryAwardRule(ruleCode); 
		env.setVariable("rules", getBeansWrapper().wrap(rules));
		YzBuzEngineContext.getInstance().setContextAttr("rules",rules);
	}

	/**
	 * 
	 * @return
	 */
	public static BeansWrapper getBeansWrapper() {
		BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_25).build();
		return beansWrapper;
	}

}
