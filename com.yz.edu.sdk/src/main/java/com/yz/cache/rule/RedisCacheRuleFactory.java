package com.yz.cache.rule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.util.ApplicationContextUtil;
 

/**
 * @desc RedisCacheFactory thread safe
 * @author Administrator
 *
 */
public class RedisCacheRuleFactory {

	private Logger logger = LoggerFactory.getLogger(RedisCacheRuleFactory.class);
	
	private Map<String,RedisCacheRule>cacheRuleMap = new ConcurrentHashMap<>();

	private static class RedisCacheRuleFactoryHolder {
		private static RedisCacheRuleFactory _instance = new RedisCacheRuleFactory();
	}

	private RedisCacheRuleFactory() {

	}

	public static RedisCacheRuleFactory getInstance() {
		return RedisCacheRuleFactoryHolder._instance;
	}
	
	/**
	 * 
	 * @param ruleName
	 * @return
	 */
	public RedisCacheRule getRedisCacheRule(String ruleName)
	{
		if(cacheRuleMap.containsKey(ruleName))
		{
			return cacheRuleMap.get(ruleName);
		}
		RedisCacheRule rule = createCacheRule(ruleName);
		cacheRuleMap.put(ruleName, rule);
		return rule;
	}
	
	/**
	 * 
	 * @param ruleName
	 * @return
	 */
	private RedisCacheRule createCacheRule(String ruleName)
	{
		RedisCacheRule rule =  ApplicationContextUtil.getBeanIgnoreEx(ruleName);
		logger.info("ruleName:{},ruleInfo:{}",ruleName,rule);
		return rule;
	}
	
	
	
}
