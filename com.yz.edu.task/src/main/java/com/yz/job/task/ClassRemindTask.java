package com.yz.job.task;

import java.text.ParseException; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
import com.yz.job.common.AbstractSimpleTask; 
import com.yz.job.common.YzJob;
import com.yz.job.constants.JobConstants;
import com.yz.job.service.ClassRemindService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;

/**
 * 
 * @desc 上课提醒 每天 7点,12点,17点
 * @author Administrator
 *
 */
@Component(value = "classRemindTask")
@YzJob(taskDesc = YzTaskConstants.YZ_MSG_CLASS_REMIND_DESC, cron = "0 00 7,12,17 * * ?", shardingTotalCount = 1,
log =true,logFormat = JobConstants.JOB_CLASS_REMIND_LOGFORMAT)
public class ClassRemindTask extends AbstractSimpleTask {

	@Autowired
	private ClassRemindService classRemindService;

	@Override
	public void executeOther() {
		try {
			if (DateUtil.getCurrentTime() >=DateUtil.getLong("6:00") && DateUtil.getCurrentTime() <= DateUtil.getLong("11:00")) { // 上午上课
				String startTimePre = "1970-01-01 08:00";
				String endTimePre = "1970-01-01 12:00";
				logger.info("推送早上上课通知............");
				classRemindService.sendClassRemindNotice("早上",startTimePre, endTimePre);
			} else if (DateUtil.getCurrentTime() >DateUtil.getLong("11:00") && DateUtil.getCurrentTime() <= DateUtil.getLong("16:00")) { // 下午上课
				String startTimePre = "1970-01-01 12:00";
				String endTimePre = "1970-01-01 18:00";
				logger.info("推送下午上课通知............");
				classRemindService.sendClassRemindNotice("下午",startTimePre, endTimePre);
			} else if (DateUtil.getCurrentTime() >=DateUtil.getLong("16:00") && DateUtil.getCurrentTime() <= DateUtil.getLong("18:00")) { // 晚上上课
				String startTimePre = "1970-01-01 18:00";
				String endTimePre = "1970-01-01 22:00";
				logger.info("推送晚上上课通知............");
				classRemindService.sendClassRemindNotice("晚上",startTimePre, endTimePre);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

 
}
