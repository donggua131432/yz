package com.yz.job.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yz.constants.WechatMsgConstants;
import com.yz.job.common.YzJobInfo;
import com.yz.job.common.YzTaskContext;
import com.yz.job.dao.BdsStudentExamWarnMapper;
import com.yz.job.model.GwWechatMsgTemplate;
import com.yz.model.WechatMsgVo;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;

/**
 * 定时发送推送消息 Description:
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2018年1月4日.
 *
 */
@Service
public class BdMsgScheduleService {

	@Autowired
	private BdsStudentExamWarnMapper msgMapper;
	
	@Autowired
	private WechatSendMessageService wechatSendMessageService;
	
	/**
	 * 定时推送考试提醒信息
	 */
	public void examMsgSchedule() {

		String date = DateUtil.dateTimeAddOrReduceDays(DateUtil.getNowDateAndTime(), 1);
		List<Map<String, String>> examPlans = msgMapper.selectExamPlansByDate(date);

		if (null == examPlans || examPlans.size() <= 0) {
			return;
		}
		
		//模板提前取出来,防止循环的时候每次都取
		GwWechatMsgTemplate template = wechatSendMessageService.getWechatMsgTemplate(WechatMsgConstants.TEMPLATE_MSG_EXAM_WARN);
		for (Map<String, String> plan : examPlans) {

			List<Map<String, String>> stds = msgMapper.selectStdExamInfo(plan.get("pyId"));
			//抽取userId
			if(null != stds && stds.size() >0){
				stds.stream().forEach(std -> {
					String openId = std.get("openId");
					if(StringUtil.isNotBlank(openId)){  //有openId的时候推送
						WechatMsgVo msgVo = new WechatMsgVo();
						msgVo.setTouser(openId);
						msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_EXAM_WARN);
						msgVo.addData("stdName", std.get("stdName"));
						String startTime =  plan.get("startTime");
						if(null !=startTime && startTime.contains("AM")){
							startTime = startTime.replaceAll("AM", "上午");
						}else{
							startTime = startTime.replaceAll("PM", "下午");
						}
						String endTime = plan.get("endTime");
						msgVo.addData("startAndEndTime", startTime + "-" + endTime);
						msgVo.addData("epName", plan.get("epName"));
						StringBuffer sb = new StringBuffer();
						sb.append(std.get("stdName")).append(":同学您好，请于").append(startTime + "-" + endTime).append(",到:")
								.append(plan.get("epName")).append("(").append(plan.get("provinceName")).append(plan.get("cityName"))
								.append(plan.get("districtName")).append(plan.get("address")).append(")").append(std.get("placeName"))
								.append(":座位[").append(std.get("esNum")).append("]准时参加考试。请提前下载好复习资料进行复习。祝您考试顺利！");
						msgVo.addData("remark", sb.toString());
						
						
						wechatSendMessageService.sendWechatMsg(msgVo,template);
					}
				});
				//增加日志
				YzJobInfo info = YzTaskContext.getTaskContext().getContext(YzJobInfo.class);
				YzTaskContext.getTaskContext().addEventDetail(plan.get("pyId"), String.format(info.getLogFormat(),
						plan.get("epName"), plan.get("startTime"), plan.get("endTime"), stds.size()));
			}
		}
	}

}
