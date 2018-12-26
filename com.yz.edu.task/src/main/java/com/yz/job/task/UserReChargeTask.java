package com.yz.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.service.UserReChargeService;
import com.yz.model.UserReChargeEvent;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;

/**
 * 
 * @description 用户充值缴费流程
 * @author Administrator
 *
 */
@Component(value = "userReChargeTask")
@YzJob(queueName = YzTaskConstants.YZ_USER_RECHARGE_EVENT, taskDesc = YzTaskConstants.YZ_USER_RECHARGE_EVENT_DESC, cron = "0/5 * * * * ?", shardingTotalCount = 3, targetCls = UserReChargeEvent.class, log = true)
public class UserReChargeTask extends AbstractSimpleTask<UserReChargeEvent> {

	@Autowired
	private UserReChargeService userReChargeService;

	@Override
	public void executeRedis(UserReChargeEvent event) {
		logger.info("UserReChargeTask.param:{}", JsonUtil.object2String(event));
		userReChargeService.reCharge(event);
	}
}
