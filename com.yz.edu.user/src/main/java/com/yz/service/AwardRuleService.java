package com.yz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.dao.AtsAwardRuleMapper;
import com.yz.model.AtsAwardRule;

@Service
@Transactional
public class AwardRuleService {
	
	@Autowired
	private AtsAwardRuleMapper ruleMapper;

	public AtsAwardRule getRule(String ruleCode) {
		return ruleMapper.getRule(ruleCode);
	}

	public List<AtsAwardRule> getAll() {
		return ruleMapper.getAll();
	}


}
