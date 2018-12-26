package com.yz.job.task;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.constants.WechatMsgConstants;
import com.yz.job.common.AbstractSimpleTask; 
import com.yz.job.common.YzJob;
import com.yz.job.model.GsSalesNotify;
import com.yz.job.model.GwWechatMsgTemplate;
import com.yz.job.dao.GsSalesNotifyMapper;
import com.yz.job.service.WechatSendMessageService;
import com.yz.model.MsgRemindVo;
import com.yz.model.WechatMsgVo;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;

@Component(value = "jdCollectionNoticeTask")
@YzJob( taskDesc = YzTaskConstants.YZ_JD_COLLECTION_TASK_DESC, cron = "0/30 * * * * ?", shardingTotalCount = 1 )
/**
 * 
 * @desc 通知消息 
 * @author Administrator
 *
 */
public class JdCollectionNoticeTask extends AbstractSimpleTask{

	@Autowired
	private GsSalesNotifyMapper gsSalesNotifyMapper;
	
	@Autowired
	private WechatSendMessageService wechatSendMessageService;
	
	@Override
	public void executeOther() {
		  long now = new Date().getTime();
		  Set<String>msgPubSet = getRedisService().zrangeByScoreWithScores(YzTaskConstants.YZ_JD_COLLECTION_TASK_SET, 0,now ,0 ,1);
		  if(msgPubSet!=null&&!msgPubSet.isEmpty())
		  {
			  MsgRemindVo remindVo =  msgPubSet.stream().map(v->{
				   getRedisService().zrem(YzTaskConstants.YZ_JD_COLLECTION_TASK_SET, v);
				   return JsonUtil.str2Object(v, MsgRemindVo.class);
			  }).findFirst().get();
			  //获取数据
			  List<GsSalesNotify> notifyList = gsSalesNotifyMapper.getAllNotifyBySalesId(remindVo.getSalesId(),remindVo.getPlanCount());
			  logger.info("jdCollectionNoticeTask:{}",JsonUtil.object2String(notifyList));
			  if(null != notifyList && notifyList.size() >0){
				 //模板提前取出来,防止循环的时候每次都取
				  GwWechatMsgTemplate template = wechatSendMessageService.getWechatMsgTemplate(WechatMsgConstants.TEMPLATE_MSG_EXCHANGE_WARN);
				  for(GsSalesNotify notify : notifyList){
					if (remindVo.getSalesType().equals("1")) { // 兑换
						WechatMsgVo msgVo = new WechatMsgVo();
						msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_EXCHANGE_WARN);
						msgVo.setTouser(notify.getOpenId());
						msgVo.addData("activeName", remindVo.getSalesName()+"的竞拍");
						msgVo.addData("theme", "商品竞拍");
						msgVo.addData("startTime", remindVo.getStartTime());
						msgVo.setIfUseTemplateUlr(true);
						
						 wechatSendMessageService.sendWechatMsg(msgVo,template);
					} else if (remindVo.getSalesType().equals("2")) { // 抽奖
						
						WechatMsgVo msgVo = new WechatMsgVo();
						msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_WINNING_WARN);
						msgVo.setTouser(notify.getOpenId());
						msgVo.addData("activeName", remindVo.getSalesName()+"的抽奖");
						msgVo.addData("theme", "商品抽奖");
						msgVo.addData("startTime", remindVo.getStartTime());
						msgVo.setIfUseTemplateUlr(true);
						
						wechatSendMessageService.sendWechatMsg(msgVo,template);
						
					} else if (remindVo.getSalesType().equals("3")) { // 竞拍
						WechatMsgVo msgVo = new WechatMsgVo();
						msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_AUCTION_WARN);
						msgVo.setTouser(notify.getOpenId());
						msgVo.addData("activeName", remindVo.getSalesName()+"的竞拍");
						msgVo.addData("theme", "商品竞拍");
						msgVo.addData("startTime", remindVo.getStartTime());
						msgVo.setIfUseTemplateUlr(true);
						
						wechatSendMessageService.sendWechatMsg(msgVo,template);
					}
				  }
			  }
			 
			  return;
		  }
		  sleep(100);
	}
}
