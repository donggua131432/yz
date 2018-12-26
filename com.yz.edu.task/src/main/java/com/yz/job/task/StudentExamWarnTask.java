package com.yz.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yz.job.common.AbstractSimpleTask; 
import com.yz.job.common.YzJob;
import com.yz.job.constants.JobConstants;
import com.yz.job.service.BdMsgScheduleService;
import com.yz.task.YzTaskConstants;
/**
 * 考试提醒通知
 * @author lx
 * @date 2018年4月18日 上午10:58:12
 */
@Component(value = "studentExamWarnTask")
@YzJob(taskDesc = YzTaskConstants.YZ_MSG_STUDENT_EXAM_WARN_DESC, cron = "0 30 10 1/1 * ? ", shardingTotalCount = 1,
log =true,logFormat = JobConstants.JOB_EXAM_WARN_LOGFORMAT)
public class StudentExamWarnTask extends AbstractSimpleTask {

	@Autowired
	private BdMsgScheduleService msgScheduleService;
	
	@Override
	public void executeOther() {
		msgScheduleService.examMsgSchedule();
	}
}
