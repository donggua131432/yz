package com.yz.template.handler;

import java.util.List;
import java.util.Map;

import com.yz.model.AtsAwardRule;
import com.yz.template.YzBuzEngineContext;
import com.yz.util.StringUtil;


/**
 * 
 * @desc 业务规则处理
 * 
 * @author Administrator
 *
 */
public interface YzBuzHandler extends YzBuzHandlerChain{

	/***
	 * 
	 * @desc 处理类  返回boolean 
	 * @param param
	 * @return
	 */
	public boolean match(Map<String,Object>param);
	
	
	default public AtsAwardRule getRule(String ruleCode) {
		List<AtsAwardRule> rules = (List<AtsAwardRule>)YzBuzEngineContext.getInstance().getContextAttr("rules");
		return rules.stream().filter(v->StringUtil.equalsIgnoreCase(ruleCode, v.getRuleCode())).findFirst().get();
	}
}
