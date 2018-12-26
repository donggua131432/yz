package com.yz.job.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.conf.YzSysConfig;
import com.yz.job.dao.EmpExpenseMapper;
import com.yz.util.JsonUtil;

@Service
public class EmpExpenseService {

	private final static Logger log = LoggerFactory.getLogger(EmpExpenseService.class);

	@Autowired
	private EmpExpenseMapper mapper;

	@Autowired
	private YzSysConfig yzSysConfig;
	
	public void initEmpExpense() {

		String year = yzSysConfig.getRecruitYear();
		
		log.debug("------------------------------- 月度报销记录员工刷新开始,当前年度：" + year);

		List<String> empIds = mapper.selectAllEmpNoContains(year);
		
		log.debug("------------------------------- 未生成记录员工ID：" + JsonUtil.object2String(empIds));
		
		if (null != empIds && empIds.size() > 0) {
			mapper.insertExpenses(empIds, year);
		}

	}
}
