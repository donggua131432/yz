package com.yz.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.job.common.AbstractSimpleTask; 
import com.yz.job.common.YzJob;
import com.yz.job.service.UserPaySuccesService;
import com.yz.model.UserPaySuccessEvent; 
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;


@Component(value = "userPaySuccessTask")
@YzJob(
		queueName = YzTaskConstants.YZ_USER_PAYSUCCESS_EVENT, taskDesc = YzTaskConstants.YZ_USER_PAYSUCCESS_EVENT_DESC,
        cron = "0/5 * * * * ?", shardingTotalCount = 5, targetCls = UserPaySuccessEvent.class, log = true)
public class UserPaySuccessTask extends AbstractSimpleTask<UserPaySuccessEvent>  {

	@Autowired
	private UserPaySuccesService userPaySuccesService;
	
	@Override
	public void executeRedis(UserPaySuccessEvent event) {
	   logger.info("UserPaySuccessTask.event:{}",JsonUtil.object2String(event));
	   userPaySuccesService.paySuccess(event);
	}
}
