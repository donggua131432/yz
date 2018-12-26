package com.yz.dao;

import java.util.List;

import com.yz.model.AtsAwardRule;

public interface AtsAwardRuleMapper {

    AtsAwardRule getRule(String ruleCode);

	List<AtsAwardRule> getAll();
}