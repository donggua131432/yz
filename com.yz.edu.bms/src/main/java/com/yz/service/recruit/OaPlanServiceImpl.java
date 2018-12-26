package com.yz.service.recruit;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.recruit.OaPlanMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.recruit.OaPlan;

@Service
@Transactional
public class OaPlanServiceImpl  {

	@Autowired
	private OaPlanMapper planMapper;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPageInfo queryPlanByPage(int start, int length, String planType, OaPlan plan) {
		PageHelper.offsetPage(start, length);
		List<HashMap<String, String>> plans = planMapper.selectPlanByType(plan, planType);
		return new IPageInfo((Page) plans);
	}

	public OaPlan queryPlanById(String planId) {
		return planMapper.selectByPrimaryKey(planId);
	}

	public void updatePlan(OaPlan plan) {
		planMapper.updateByPrimaryKeySelective(plan);
	}

	public void addPlan(OaPlan plan) {
		plan.setPlanId(IDGenerator.generatorId());
		planMapper.insert(plan);
	}

	public void deletePlanPlans(String[] planIds) {
		planMapper.deletePlans(planIds);
	}

	public void deletePlan(String planId) {
		planMapper.deleteByPrimaryKey(planId);
	}

}
