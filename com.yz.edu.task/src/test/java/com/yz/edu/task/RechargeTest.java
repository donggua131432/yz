package com.yz.edu.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Sets;
import com.yz.job.app.MyTaskApplication;
import com.yz.job.model.RechargeAwardRule;
import com.yz.job.service.UserReChargeService;
import com.yz.model.UserReChargeEvent;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

/**
 * 规则匹配单元测试
 * 
 * @ClassName: RechargeTest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhanggh
 * @date 2018年5月29日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyTaskApplication.class)
public class RechargeTest {

	@Autowired
	private UserReChargeService service;

	@Test
	public void testUserReCharge() {
//		String jsonString = "";
//		UserReChargeEvent event = (UserReChargeEvent) JsonUtil.str2Object(jsonString);
		UserReChargeEvent event = new UserReChargeEvent();
		event.setPayable("1995");
		event.setMappingId("YZ20180522124847164650");
		event.setCreateTime(DateUtil.convertDateStrToDate("2018-05-09 19:26:08", DateUtil.YYYYMMDDHHMMSS_SPLIT));
		event.setPayDateTime(DateUtil.convertDateStrToDate("2018-05-22 12:48:47", DateUtil.YYYYMMDDHHMMSS_SPLIT));
		event.setlSize("0");
		event.setItemCode(Arrays.asList(new String[]{"Y1","S1","W1"}));
			event.setItemYear(Arrays.asList(new String[]{"1"}));
			event.setScholarship("1");
			event.setRecruitType("2");
		// 上线缴费赠送流程 + 个人缴费赠送流程
		event.setUserId("152665040362678131");
		event.setpId("152661046785363233");
		event.setGrade("201809");
		event.setStdStage("");
		if (event != null) {
			// json字符串
//			System.out.println(jsonString);
			// 获取符合条件的规则
			List<RechargeAwardRule> rules = service.getRules(event);
			Set<RechargeAwardRule> enableRule = Sets.newHashSet();
			RechargeAwardRule rule = rules.stream().sorted().findFirst().get();
			boolean mutex = rule.getIsMutex() == 0; // 奖励规则互斥
			if (mutex) {
				enableRule.add(rule);
			} else {
				enableRule.addAll(rules);
			}
			ArrayList<String> awardDesc = new ArrayList<>();
			Map<String, List<RechargeAwardRule>> data = enableRule.parallelStream()
					.filter(v -> StringUtil.isNotBlank(v.getUserId()))
					.collect(Collectors.groupingBy(RechargeAwardRule::getUserId));
			if (data != null && data.size() > 0) {
				data.entrySet().stream().forEach(v -> {
					System.out.println(v.getKey() + " : " + JsonUtil.object2String(v.getValue()));
				});
			}
		}
	}

	@Test
	public void test() {
		UserReChargeEvent event = new UserReChargeEvent();
		event.setPayable("1995");
		event.setMappingId("YZ20180522124847164650");
		event.setCreateTime(DateUtil.convertDateStrToDate("2018-05-09 19:26:08", DateUtil.YYYYMMDDHHMMSS_SPLIT));
		event.setPayDateTime(DateUtil.convertDateStrToDate("2018-05-22 12:48:47", DateUtil.YYYYMMDDHHMMSS_SPLIT));
		event.setlSize("0");
		event.setItemCode(Arrays.asList(new String[]{"Y1","S1","W1"}));
			event.setItemYear(Arrays.asList(new String[]{"1"}));
			event.setScholarship("1");
			event.setRecruitType("2");
		// 上线缴费赠送流程 + 个人缴费赠送流程
		event.setUserId("152665040362678131");
		event.setpId("152661046785363233");
		event.setGrade("201809");
		System.out.println(
				"发送个人缴费上级赠送指令 lpush " + YzTaskConstants.YZ_USER_RECHARGE_EVENT + "  " + JsonUtil.object2String(event));
	}
}
