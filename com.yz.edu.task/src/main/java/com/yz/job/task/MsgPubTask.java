package com.yz.job.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.constants.MessageConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.dao.GwReceiverMapper;
import com.yz.job.dao.OaTaskInfoMapper;
import com.yz.job.model.GwReceiver;
import com.yz.job.model.GwWechatMsgTemplate;
import com.yz.job.model.OaStudentTask;
import com.yz.job.service.WechatSendMessageService;
import com.yz.model.MsgPubVo;
import com.yz.model.WechatMsgVo;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;


@Component(value = "msgPubTask")
@YzJob( taskDesc = YzTaskConstants.YZ_MSG_PUB_TASK_DESC, cron = "0/5 * * * * ?", shardingTotalCount = 1 )
/**
 * 
 * @desc 通知消息 
 * @author Administrator
 *
 */
public class MsgPubTask extends AbstractSimpleTask{
	
	@Autowired
	private OaTaskInfoMapper taskInfoMapper;
	
	@Autowired
	private WechatSendMessageService wechatSendMsgService;
	
	@Autowired
	private GwReceiverMapper receiverMapper;
	
	@Override
	public void executeOther() {
	      //从redis获取信息
		  long now = new Date().getTime();
		  Set<String>msgPubSet = getRedisService().zrangeByScoreWithScores(YzTaskConstants.YZ_MSG_PUB_TASK_SET	, 0,now ,0 ,1);
		  if(msgPubSet!=null&&!msgPubSet.isEmpty())
		  {
			  MsgPubVo msgVo =  msgPubSet.stream().map(v->
			   {
				   getRedisService().zrem(YzTaskConstants.YZ_MSG_PUB_TASK_SET, v);
				   logger.info(":{}-pushMsg:{}",YzTaskConstants.YZ_MSG_PUB_TASK_DESC,v);
				   return JsonUtil.str2Object(v, MsgPubVo.class);
			   }).findFirst().get();
			  if(msgVo.getMsgType().equals(WechatMsgConstants.PUB_MSG_TYPE_TASK)){
				  //学服任务未完成的提醒
				  List<OaStudentTask> stuTaskList = taskInfoMapper.getTaskUnfinishedStudent(msgVo.getMsgId());
				  if(null != stuTaskList&& stuTaskList.size()>0){
					  //模板提前取出来,防止循环的时候每次都取
					  GwWechatMsgTemplate template = wechatSendMsgService.getWechatMsgTemplate(WechatMsgConstants.TEMPLATE_MSG_STUDENT_INFORM);
					  for (OaStudentTask task : stuTaskList) {
							if(StringUtil.hasValue(task.getOpenId())){
								//发送消息
								WechatMsgVo wechatVo = new WechatMsgVo();
								wechatVo.setTouser(task.getOpenId());
								wechatVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_STUDENT_INFORM);
								wechatVo.addData("title", msgVo.getMsgTitle());
								wechatVo.addData("msgName", msgVo.getMsgTitle());
								wechatVo.addData("code", msgVo.getMsgCode());
								wechatVo.addData("content", msgVo.getMsgContent());
								wechatVo.setExt1(msgVo.getUrl()+task.getLearnId());
								wechatVo.setIfUseTemplateUlr(false);
								
								wechatSendMsgService.sendWechatMsg(wechatVo,template);
							}
						}
				  }
			  }else if(msgVo.getMsgType().equals(WechatMsgConstants.PUB_MSG_TYPE_NOTICE)){
				  //消息注册 发送微信公众号
				  List<GwReceiver> list = receiverMapper.selectRecerverByMtpId(msgVo.getMsgId());
				  if(null !=list && list.size() >0){
					  //模板提前取出来,防止循环的时候每次都取
					  GwWechatMsgTemplate template = wechatSendMsgService.getWechatMsgTemplate(WechatMsgConstants.TEMPLATE_MSG_STUDENT_INFORM);
					  for (GwReceiver rec : list) {
						  if (MessageConstants.MESSAGE_CHANNEL_WECHAT.equals(msgVo.getExt1())) {
							  	// 发送微信消息
							if (StringUtil.isNotBlank(rec.getOpenId()) && StringUtil.isNotBlank(rec.getName())) {
								WechatMsgVo wechatVo = new WechatMsgVo();
								wechatVo.setTouser(rec.getOpenId());
								wechatVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_STUDENT_INFORM);
								wechatVo.addData("title", msgVo.getMsgTitle()
										.replace(MessageConstants.MESSAGE_STATEMENT_NAME, rec.getName()));
								wechatVo.addData("msgName", msgVo.getMsgName());
								wechatVo.addData("code", msgVo.getMsgCode());
								wechatVo.addData("content", msgVo.getMsgContent());
								wechatVo.setExt1(msgVo.getUrl());
								wechatVo.setIfUseTemplateUlr(false);
								
								//回传参数
								Map<String, String> postBackData = new HashMap<>();
								postBackData.put("mtpId", rec.getMtpId());
								postBackData.put("receiverId", rec.getReceiverId());
								postBackData.put("receiverName", rec.getName());
								postBackData.put("sender", rec.getSender());
								wechatVo.setPostBackData(postBackData);
						
								wechatSendMsgService.sendWechatMsg(wechatVo, template);
							}
								
							} else if (MessageConstants.MESSAGE_CHANNEL_MSG.equals(msgVo.getExt1())) {
								// TODO 发送短信通知
							} else if (MessageConstants.MESSAGE_CHANNEL_DINGDING.equals(msgVo.getExt1())) {
								// TODO 发送钉钉通知
							} else if (MessageConstants.MESSAGE_CHANNEL_EMAIL.equals(msgVo.getExt1())) {
								// TODO 发送邮箱通知
							}
						}
					    //修改同步状态
					  receiverMapper.updateMtpStatus(msgVo.getMsgId(), MessageConstants.MESSAGE_STATUS_SENDED);
				  }
			  }
			  return;
		  }
		  sleep(100);
	}
}
