package com.yz.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.job.common.AbstractSimpleTask; 
import com.yz.job.common.YzJob;
import com.yz.job.service.EmpExpenseService;
import com.yz.task.YzTaskConstants;

/**
 * 初始化员工  月度报销记录
 * @author lx
 * @date 2018年4月18日 上午11:31:17
 */
@Component(value = "empExpenseTask")
@YzJob(taskDesc = YzTaskConstants.YZ_EMP_EXPENSE_TASK_DESC, cron = "0 0 1 1/1 * ? ", shardingTotalCount = 1)
public class EmpExpenseTask extends AbstractSimpleTask{
	
	@Autowired
	private EmpExpenseService empExpenseService;
	@Override
	public void executeOther() {
		empExpenseService.initEmpExpense();
	}

}
