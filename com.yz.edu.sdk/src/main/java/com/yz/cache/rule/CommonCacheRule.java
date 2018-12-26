package com.yz.cache.rule;

import com.yz.util.RegUtils;
import com.yz.util.StringUtil;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value = com.yz.constants.CommonConstants.COMMON_CACHE_RULE)
/**
 * @desc 通用的commonCacheRule
 * @author Administrator
 *
 */
public class CommonCacheRule implements RedisCacheRule {

    private static final Logger logger = LoggerFactory.getLogger(CommonCacheRule.class);

    @Override
    public String getCacheName(String cacheKey,Map<String,Object> paramMap) {
        String result = RegUtils.findAndReplace("\\$\\{(.+?)\\}", cacheKey, paramName -> {
            if (paramMap.containsKey("onlyOne")) {
                return StringUtil.obj2String(paramMap.get("value"));
            }
            String value = paramName.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("\\}", "").trim();
            if (paramMap.containsKey(value)) {
                return StringUtil.obj2String(paramMap.get(value));
            }
            return StringUtil.EMPTY;
        });
        logger.info("cacheKey:{},result:{}",cacheKey,result);
        return result;
    }
}
