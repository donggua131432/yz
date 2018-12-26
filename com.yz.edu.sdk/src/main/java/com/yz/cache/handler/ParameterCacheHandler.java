package com.yz.cache.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yz.constants.CommonConstants;
import com.yz.redis.RedisService;
import com.yz.util.ClassUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;  

@Component(value =  CommonConstants.PARAMETER_CACHE_HANDLER)
public class ParameterCacheHandler extends BaseRedisCacheHandler {
 
	@Override
	public void setCache(String redisName, String key, Object result,int cacheExpire) {
		String cachePrefix="yz-common-parameter";
		RedisService.getRedisService(redisName).hsetex(
				cachePrefix, 
				key, 
				JsonUtil.object2String(result),
				cacheExpire);
		logger.info("setCache:{}", key);
	}

	@Override
	public Object getCache(String redisName, String key, Class<?> cls) {
		String cachePrefix="yz-common-parameter";
		String str = RedisService.getRedisService(redisName).hget(cachePrefix, key);
		if (StringUtil.isBlank(str)) {
			return null;
		}
		if (cls == null) {
			Object obj = JSON.parse(str);
			logger.info("getCache:{}", key);
			return obj;
		} else {
			Object obj = JSON.parse(str);
			if (obj instanceof JSONObject) {
				if (cls.isInstance(Map.class)) {
					return obj;
				}
				return JSON.toJavaObject((JSONObject) obj, cls);
			} else if (obj instanceof JSONArray) {
				List result = new ArrayList<>();
				JSONArray arr = (JSONArray) obj;
				Iterator<Object> iter = arr.iterator();
				while (iter.hasNext()) {
					Object next = iter.next();
					if (ClassUtil.isBaseDataType(next.getClass())) {
						result.add(next);
					} else if (next instanceof JSONObject) {
						result.add(JSON.toJavaObject((JSONObject) next, cls));
					}
				}
				return result;
			}
			logger.error("data.error:{}.please check again!", obj);
		}
		return null;
	}
	

}
