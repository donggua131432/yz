package com.yz.job.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.model.GwWechatMsgTemplate;
import com.yz.job.service.WechatSendMessageService; 
import com.yz.model.WechatMsgVo;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil; 


@Component(value="yzSendWechatMsgTask")
@YzJob( step=5,targetCls =WechatMsgVo.class,
		taskDesc=YzTaskConstants.YZ_WECHAT_MSG_TASK_DESC,cron="0/3 * * * * ?",shardingTotalCount =4,queueName = YzTaskConstants.YZ_WECHAT_MSG_TASK )
public class SendWechatMsgTask extends AbstractSimpleTask<WechatMsgVo>{

	@Autowired
	private WechatSendMessageService wechatSendMessageService;
	
	@Override
	public void executeRedis(List<WechatMsgVo> msgList) 
	{ 
		logger.info("SendWechatMsgTask.param:{}",JsonUtil.object2String(msgList));
		msgList.stream().forEach(msg->{
			//模板提前取出来,防止循环的时候每次都取
			GwWechatMsgTemplate template = wechatSendMessageService.getWechatMsgTemplate(msg.getTemplateId());
		    wechatSendMessageService.sendWechatMsg(msg,template);
		}); 
	}
}
