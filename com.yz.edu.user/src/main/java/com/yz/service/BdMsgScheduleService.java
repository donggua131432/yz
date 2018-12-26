package com.yz.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference; 
import com.yz.dao.BdsMsgScheduleMapper;
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
@Transactional
public class BdMsgScheduleService {

	@Autowired
	private BdsMsgScheduleMapper msgMapper;

	@Autowired
	private UsInfoService infoService;

	/**
	 * 定时推送考试提醒信息
	 */
	public void examMsgSchedule() {

		String date = DateUtil.dateTimeAddOrReduceDays(DateUtil.getNowDateAndTime(), 1);
		// String date = "2017-12-13 00:00:00";
		List<Map<String, String>> examPlans = msgMapper.selectExamPlansByDate(date);

		if (null == examPlans || examPlans.size() <= 0) {
			return;
		}

		for (Map<String, String> plan : examPlans) {

			List<Map<String, String>> stds = msgMapper.selectStdExamInfo(plan.get("pyId"));

			if (null == stds || stds.size() <= 0) {
				continue;
			}

			for (Map<String, String> std : stds) {
				String userId = std.get("userId");
				if (!StringUtil.hasValue(userId)) {
					continue;
				}
				Object o = infoService.getOpenIdByUserId(userId);
				if (null == o) {
					continue;
				}
				String openId = o.toString();
				std.put("openId", openId);
				std.put("startTime", plan.get("startTime"));
				std.put("endTime", plan.get("endTime"));
				std.put("address", plan.get("address"));
				std.put("epName", plan.get("epName"));
				std.put("provinceName", plan.get("provinceName"));
				std.put("cityName", plan.get("cityName"));
				std.put("districtName", plan.get("districtName"));

			}
			//TODO 临时添加的
			//wechatApi.sendExamWarn(stds);

		}
	}

}
