package com.yz.job.handler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;
import com.yz.model.AtsAwardRule;
import com.google.common.collect.Sets;
import com.yz.template.handler.YzBuzHandler;
import net.sf.json.JSONArray;

@Component(value = "userReChargeBuzHandler")
public class UserReChargeBuzHandler implements YzBuzHandler {

	private boolean has = true;

	private Set<YzBuzHandler> handlers;

	public UserReChargeBuzHandler() {
		handlers = Sets.newHashSet(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean match(Map<String, Object> param) {
		has = true;
		// 不存在规则ID，不进行比较
		if (!param.containsKey("ruleCode"))
			return has;
		AtsAwardRule rule = getRule(param.get("ruleCode").toString());
		if (rule == null)
			return false;
		if (rule.getCustomizeAttr() == null)
			return has;
		rule.getCustomizeAttr().forEach((k, v) -> {
			if (!param.containsKey(k)) {
				if (!k.equals("multiple") && !k.equals("isParent")) {
					has = false;
				}
				return;
			}
			// 规则包含itemCode，就进行itemCode比对，如果事件的itemCodes不存在规则需要的itemCode，就返回
			// 规则包含itemYear，就进行itemYear比对，如果事件的itemYears不存在规则需要的itemYear，就返回
			if ((k.equals("itemCode") || k.equals("itemYear")) && !TextUtils.isEmpty(v)) {
				if (v.split(",").length > 1) {
					JSONArray jsonArray = (JSONArray) param.get(k);
					List<Object> list = Arrays.asList(jsonArray.toArray());
					List<Object> customizelist = Arrays.asList(v.split(","));
					list.forEach(c -> {
						if (customizelist.indexOf(c) < 0) {
							has = false;
						}
					});
				} else {
					JSONArray jsonArray = (JSONArray) param.get(k);
					List<Object> list = Arrays.asList(jsonArray.toArray());
					if (!param.containsKey(k) || list.indexOf(v) < 0) {
						has = false;
					}
				}
				return;
			}
			// 判断规则是否包含奖励类型
			// "{"recruitType":"1","scholarship":"1","itemCode":"Y0"}
			// {"lSize":0,"recruitType":"1","itemCode":["Y0"],"scholarship":"23"}
			if ((k.equals("scholarship") || k.equals("grade")) && !TextUtils.isEmpty(v)) {
				List<Object> list = Arrays.asList(v.split(","));
				if (!param.containsKey(k) || list.indexOf(param.get(k)) < 0) {
					has = false;
				}
				return;
			}
			// 如果规则包含的value都是和事件包含的value一致的，就不需要额外的写判断语句
			if (!TextUtils.isEmpty(v)) {
				if (!param.containsKey(k) || !v.equals(String.valueOf(param.get(k)))) {
					has = false;
				}
				return;
			}
		});
		return has;
	}

	@Override
	public Set<YzBuzHandler> handlers() {
		return handlers;
	}
}
