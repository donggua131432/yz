package com.yz.job.register;

import org.apache.curator.shaded.com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.yz.job.common.YzJob;
import com.yz.job.common.YzTask;
 
import java.util.Map;
 
/**
 * @author Administrator
 */
@Component(value = "yzTaskRegister")
public class YzTaskRegister implements ApplicationListener<ContextRefreshedEvent> {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext parent = contextRefreshedEvent.getApplicationContext().getParent();
        if (parent == null) {
            ApplicationContext ac = contextRefreshedEvent.getApplicationContext();
            Map<String, Object> tasks = ac.getBeansWithAnnotation(YzJob.class);
            register(tasks);
        }
    }

    /**
     *
     * @param tasks
     */
    private void register(Map<String, Object> tasks) {
        if (tasks != null && !tasks.isEmpty()) {
            logger.info("registerTask:{}", tasks);
            YzTask obj = null;
            Map<YzTask,YzJob>jobMap = Maps.newHashMap();
            for (Object bean : tasks.values()) {
                YzJob job = bean.getClass().getAnnotation(YzJob.class);
                if (bean instanceof YzTask) {
                    obj = (YzTask) bean;
                    jobMap.put(obj, job);
                }
            }
            taskExecutor.startTask(jobMap);
            logger.info("taskExecutor.startTask...");
        }
    }

}
