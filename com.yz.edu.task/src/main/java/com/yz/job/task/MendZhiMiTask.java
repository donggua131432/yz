package com.yz.job.task;

import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.service.MendZhiMiService;
import com.yz.task.YzTaskConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @描述: 转报智米补送，专本连读智米补送  只补送一次
 * @作者: DuKai
 * @创建时间: 2018/5/28 16:15
 * @版本号: V1.0
 */

@Component(value = "mendZhiMiTask")
@YzJob(taskDesc = YzTaskConstants.YZ_MEND_ZHIMI_TASK, cron = "0 0/10 * * * ? *", shardingTotalCount = 1)
public class MendZhiMiTask extends AbstractSimpleTask {
    @Autowired
    private MendZhiMiService mendZhiMiService;

    @Override
    public void executeOther() {
        System.out.println("开始执行智米补送任务！");
        //专本连读智米补送
        mendZhiMiService.evenReadMend();
        //转报智米补送
        mendZhiMiService.referralMend();
    }

}
