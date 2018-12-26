package com.yz.core.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.yz.cache.handler.BaseRedisCacheHandler;
import com.yz.core.constants.AppConstants;
import com.yz.model.zhimi.ZhimiAwardInfo;
import com.yz.redis.RedisService;
import com.yz.service.zhimi.ZhimiAwardService;
import com.yz.util.JsonUtil;

@Component(value = AppConstants.ZHIMI_AWARD_CACHE_HANDLER)
public class ZhimiAwardCacheHandler extends BaseRedisCacheHandler{

	@Autowired
	private ZhimiAwardService zhimiAwardService;
	@Override
	public void setCache(String redisName, String key, Object result, int cacheExpire) {
		//根据key取出同组的数据放入缓存
		List<ZhimiAwardInfo> list = zhimiAwardService.getAwardListInfo(key);
		List<String>rules =  list.stream().map(v->{
			Map<String,Object> award = JsonUtil.object2Map(v);
			award.remove("attrList");
			award.remove("items");
			award.remove("createUserId");
			award.remove("updateUserId");
			award.remove("createUser");
			award.remove("updateUser");
			Map<String,Object>data = Maps.newHashMap();
			v.getAttrList().stream().forEach(data::putAll);
			award.put("customizeAttr", data);
			return JsonUtil.object2String(award);}).collect(Collectors.toList());
		String[]arr = new String [rules.size()];
		rules.toArray(arr);
		RedisService.getRedisService().del(key);
		RedisService.getRedisService().sadd(key, arr);
	}

	@Override
	public Object getCache(String redisName, String key, Class<?> cls) {
		return null;
	}

}
