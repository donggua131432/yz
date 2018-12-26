package com.yz.job.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dangdang.ddframe.job.executor.AbstractElasticJobExecutor;
import com.yz.constants.EducationConstants;
import com.yz.constants.StudentConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.job.common.YzJobInfo;
import com.yz.job.common.YzTaskContext;
import com.yz.job.dao.BdsCourseMapper;
import com.yz.job.model.GwWechatMsgTemplate;
import com.yz.model.WechatMsgVo;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 上课提醒
 * @author lx
 * @date 2018年4月12日 上午9:48:31
 */
@Service
public class ClassRemindService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WechatSendMessageService wechatSendMessageService;
	
	@Autowired
	private BdsCourseMapper courseMapper;
	
	/**
	 * 发送上课提醒通知
	 * @param startTimePre
	 * @param endTimePre
	 */
	public void sendClassRemindNotice(String remindTime,String startTimePre, String endTimePre){
		// 查找学科课程
		List<Map<String, String>> courseInfoXK = courseMapper.selectCourse(EducationConstants.COURSE_TYPE_XK,startTimePre, endTimePre);
		sendCourseInfoMsg(remindTime,courseInfoXK, StudentConstants.STD_STAGE_STUDYING, EducationConstants.COURSE_TYPE_XK);
		// 辅导课程
		List<Map<String, String>> courseInfoFD = courseMapper.selectCourse(EducationConstants.COURSE_TYPE_FD,startTimePre, endTimePre);
		sendCourseInfoMsg(remindTime,courseInfoFD, StudentConstants.STD_STAGE_HELPING, EducationConstants.COURSE_TYPE_FD);
	}
	/**
	 * {@link AbstractElasticJobExecutor}
	 * 推送消息
	 * @param courseInfo
	 * @param stdStage
	 * @param courseType
	 */
	private void sendCourseInfoMsg(String remindTime,List<Map<String, String>> courseInfo, String stdStage, String courseType) {
		if (null != courseInfo && courseInfo.size() > 0) {

			logger.info("class course info {}",JsonUtil.object2String(courseInfo));

			for (Map<String, String> map : courseInfo) {
				String courseId = map.get("courseId");
				String courseName = map.get("courseName");
				StringBuffer classTime = new StringBuffer();

				String date = map.get("cpDate");
				String startTime = map.get("startTime");
				String endTime = map.get("endTime");

				classTime.append(date).append(" ").append(startTime).append(" 至 ").append(endTime);

				String address = "1、登录网络上课平台;2、请看课程表";
				String firstWord = "温馨提示：准备上课啦！请按时登录网络上课平台喔。";
				String cpType = map.get("cpType");
				if(StringUtil.isNotBlank(cpType)){
					if("6".equals(cpType) || "7".equals(cpType)){  //直播,面授+直播
						 address = "①手机端：点击页面下方：学员服务---远智学堂---我的课程表，进入课程直播。 ②网页版：远智教育官网http://www.yzou.cn/---远智学堂---登录---我的课表，进入课程直播。（账号：身份证号码 密码：身份证后六位） 。";
						 firstWord = "温馨提示：准备上课啦！请同学们按指定上课时间认真听课喔。";
					}
				}
				

				/**
				 * 根据课程id与学员阶段查询userId
				 */
				List<String> userIds = new ArrayList<String>();

				if (EducationConstants.COURSE_TYPE_FD.equals(courseType)) {
					userIds = courseMapper.selectUserIdByFDCourseId(courseId, stdStage);
				} else if(EducationConstants.COURSE_TYPE_XK.equals(courseType)) {
					userIds = courseMapper.selectUserIdByXKCourseId(courseId, stdStage);
				}
				
				// 查询所有需发送的openId
				if (null != userIds && userIds.size() > 0) {
					List<String> openIds = courseMapper.getOpenIdsByUserIds(userIds);

					if (null != openIds && openIds.size() > 0) {
						// 微信发送推送
						// 模板提前取出来,防止循环的时候每次都取
						GwWechatMsgTemplate template = wechatSendMessageService
								.getWechatMsgTemplate(WechatMsgConstants.TEMPLATE_MSG_CLASS_WARN);
						for (String str : openIds) {
							WechatMsgVo msgVo = new WechatMsgVo();
							msgVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_CLASS_WARN);
							msgVo.setTouser(str);
							msgVo.addData("firstWord", firstWord);
							msgVo.addData("courseName", courseName);
							msgVo.addData("classTime", classTime.toString());
							msgVo.addData("address", address);

							wechatSendMessageService.sendWechatMsg(msgVo, template);
						}

						// 增加task日志
						YzJobInfo info = YzTaskContext.getTaskContext().getContext(YzJobInfo.class);
						YzTaskContext.getTaskContext().addEventDetail(courseName, String.format(info.getLogFormat(),
								classTime.toString(), remindTime, courseName, openIds.size()));
					}
				}
			}

		}
	}
}
