package com.yz.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.poi.poifs.storage.ListManagedBlock;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yz.model.AtsAwardRule;
import com.yz.template.YzBuzEngineContext;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service(value = "yzSysRuleService")
public class SysRuleService extends BaseService {

	private static final String RULE_SQL = "select * from goods.ats_award_rule rule left join goods.ats_customize_attr attr on rule.rule_code = attr.ref_handler and attr.ref_type='awardRule' where rule.is_allow =1 and rule.rule_group=:ruleCode order by rule.sort desc ";

	@PostConstruct
	public void initService() {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	/**
	 * 
	 * @param ruleCode
	 * @return
	 */
	public List<AtsAwardRule> queryAwardRule(String ruleCode) {
		Map<String, String> param = Maps.newHashMap();
		param.put("ruleCode", ruleCode);  
		List<AtsAwardRule> rules = this.jdbcTemplate.query(RULE_SQL, param, new AtsAwardRuleRowMapper());
		return rules.stream().collect(Collectors.groupingBy(AtsAwardRule::getRuleCode)).entrySet()
				.parallelStream().map(v -> {
					List<AtsAwardRule> ruleCodeList = v.getValue();
					AtsAwardRule rule = ruleCodeList.get(0);
					ruleCodeList.stream().forEach(temp -> rule.addCustomizeAttrs(temp.getCustomizeAttr()));
					Iterator<Entry<String, String>> iter = rule.getCustomizeAttr().entrySet().iterator();
					while (iter.hasNext()) {
						Entry<String, String> entry = iter.next();
						if (StringUtil.isBlank(StringUtil.obj2String(entry.getValue()))) {
							iter.remove();
						}
					}
					return rule;
				}).collect(Collectors.toList()); 
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private class AtsAwardRuleRowMapper implements RowMapper<AtsAwardRule> {

		@Override
		public AtsAwardRule mapRow(ResultSet rs, int rowNum) throws SQLException {
			AtsAwardRule rule = new AtsAwardRule();
			rule.setEndTime(new Date(rs.getTimestamp("end_time").getTime()));
			rule.setStartTime(new Date(rs.getTimestamp("start_time").getTime()));
			rule.setExpCount(rs.getString("exp_count"));
			rule.setIsAllow(rs.getString("is_allow"));
			rule.setIsMutex(rs.getInt("is_mutex"));
			rule.setSort(rs.getInt("sort"));
			rule.setRuleCode(rs.getString("rule_code"));
			rule.setRuleType(rs.getString("rule_type"));
			rule.setRuleGroup(rs.getString("rule_group"));
			rule.setRuleDesc(rs.getString("rule_desc"));
			rule.setIsRepeat(rs.getInt("is_repeat"));
			rule.setIsAllow(rs.getString("is_allow"));
			rule.setZhimiCount(rs.getString("zhimi_count"));
			rule.addCustomizeAttr(rs.getString("attr_name"), rs.getString("attr_value"));
			return rule;
		}

	}
}
