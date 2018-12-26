package com.yz.job.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.constants.JobConstants;
import com.yz.job.service.SmsService;
import com.yz.model.SendSmsVo;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;

@Component(value = "yzSendSmsTask")
@YzJob(step=5,targetCls = SendSmsVo.class, taskDesc = YzTaskConstants.YZ_SMS_SEND_TASK_DESC, 
cron = "0/5 * * * * ?", shardingTotalCount = 3, queueName = YzTaskConstants.YZ_SMS_SEND_TASK,
log = true,logFormat = JobConstants.JOB_SEND_SMS_LOGFORMAT)
public class SendSmsTask extends AbstractSimpleTask<SendSmsVo> {

	@Autowired
	private SmsService smsService;
	
	@Override
	public void executeRedis(List<SendSmsVo> smsList) { 
		 logger.info("sendSmsTask:param:{}",JsonUtil.object2String(smsList));
		 smsList.stream().forEach(smsService::sendSms);
	}
}
