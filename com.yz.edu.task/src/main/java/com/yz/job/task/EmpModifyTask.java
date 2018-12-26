package com.yz.job.task;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.model.OaEmployeeJobInfo;
import com.yz.job.service.OaEmpModifyService;
import com.yz.task.YzTaskConstants;
import com.yz.util.JsonUtil;

@Component(value = "empModifyTask")
@YzJob(taskDesc = YzTaskConstants.YZ_EMP_MODIFY_TASK, cron = "0/5 * * * * ?", shardingTotalCount = 1)
/**
 * 
 * @desc 员工岗位信息修改task
 * @author Administrator
 *
 */
public class EmpModifyTask extends AbstractSimpleTask {

	@Autowired
	private OaEmpModifyService empModifyService;

	@Override
	public void executeOther() {
		// 从redis获取信息
		long now = new Date().getTime();
		Set<String> modifySet = getRedisService().zrangeByScoreWithScores(YzTaskConstants.YZ_EMP_MODIFY_TASK, 0, now, 0,
				1);
		if (modifySet != null && !modifySet.isEmpty()) {
			OaEmployeeJobInfo jobInfo = modifySet.stream().map(v -> {
				getRedisService().zrem(YzTaskConstants.YZ_EMP_MODIFY_TASK, v);
				return JsonUtil.str2Object(v, OaEmployeeJobInfo.class);
			}).findFirst().get();

			empModifyService.empJobUpdate(jobInfo);
			
			return;
		}
		sleep(100);
	}
}
