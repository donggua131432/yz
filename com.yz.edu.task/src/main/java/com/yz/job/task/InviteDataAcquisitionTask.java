package com.yz.job.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.job.common.AbstractSimpleTask; 
import com.yz.job.common.YzJob;
import com.yz.job.service.InviteDataAcquisitionService;
import com.yz.model.InviteDataAcquisitionEvent;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;

@Component(value = "inviteDataAcquisitionTask")
@YzJob(queueName = YzTaskConstants.YZ_REG_INVITE_DATE_ACQUISITION_EVENT, taskDesc = YzTaskConstants.YZ_REG_INVITE_DATE_ACQUISITION_DESC, cron = "0/30 * * * * ?", shardingTotalCount = 3, step=10, targetCls = InviteDataAcquisitionEvent.class, log = true)
public class InviteDataAcquisitionTask extends AbstractSimpleTask<InviteDataAcquisitionEvent>  {
	
	@Autowired
	private InviteDataAcquisitionService inviteDataAcquisitionService;
	
	@Override
	public void executeRedis(List<InviteDataAcquisitionEvent> event) {
		logger.info("InviteDataAcquisitionTask.param:{}", JsonUtil.object2String(event));
		inviteDataAcquisitionService.saveDataAcquisition(event);
	}
}
