package com.yz.job.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.JobEventListener;
import com.dangdang.ddframe.job.event.JobEventListenerConfigurationException;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.yz.dubbo.YzDubboBean;
import com.yz.job.common.YzJob;
import com.yz.job.common.YzTask;
import com.yz.job.common.YzTaskContext;
import com.yz.job.constants.JobConstants;
import com.yz.redis.YzRedisBean;
import com.yz.redis.YzRedisFactory;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Administrator
 */
@Component(value = "taskExecutor")
public class TaskExecutor {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JobEventListener rdbListener;
	
	private JobEventConfiguration configuration = new JobEventConfiguration() {

		@Override
		public JobEventListener createJobEventListener() throws JobEventListenerConfigurationException {
			return rdbListener;
		}

		@Override
		public String getIdentity() {
			return rdbListener.getIdentity();
		}
	};

	@Bean(name = "yzDubboBean")
	public static YzDubboBean initDubboBean() {
		return new YzDubboBean();
	}

	@Bean(name = "yzRedisBean")
	public YzRedisBean getRedisBean() {
		return new YzRedisBean();
	}

	@Bean(initMethod = "init")
	public Object initRedisFactory(@Autowired YzRedisBean bean) {
		YzRedisFactory.getInstance().init(bean);
		return null;
	}

	@Bean(name = "jobRegistry")
	public ZookeeperRegistryCenter regCenter(@Autowired YzDubboBean dubboBean) {
		String serverList = dubboBean.getRegistryAddress();
		String namespace = dubboBean.getApplication();
		ZookeeperRegistryCenter registry = new ZookeeperRegistryCenter(
				new ZookeeperConfiguration(serverList, namespace));
		registry.init();
		return registry;
	}

	@Autowired
	private ZookeeperRegistryCenter jobRegistry;

	/**
	 * 启动task任务
	 */
	public void startTask(Map<YzTask, YzJob> tasks) {
		JobCoreConfiguration conf = null;
		JobTypeConfiguration jobConf = null;
		Iterator<Entry<YzTask, YzJob>> iter = tasks.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<YzTask, YzJob> entry = iter.next();
			YzJob yzJob = entry.getValue();
			YzTask task = entry.getKey();
			String jobParam = JsonUtil.object2String(yzJob);
			conf = createJobCoreConfiguration(task.getClass().getName(), yzJob, jobParam);
			jobConf = StringUtil.equalsIgnoreCase(yzJob.jobType(), JobConstants.JOB_TYPE_DATAFLOW)?
					new DataflowJobConfiguration(conf, task.getClass().getCanonicalName(),true):
					new SimpleJobConfiguration(conf, task.getClass().getCanonicalName());
			SpringJobScheduler scheduler = new SpringJobScheduler(task, jobRegistry,
					LiteJobConfiguration.newBuilder(jobConf).reconcileIntervalMinutes(1).overwrite(true).build(),
					configuration);
			logger.info("{}:startTask", yzJob.taskDesc());
			scheduler.init();
			YzTaskContext.getTaskContext().addYzJobInfo(task.getClass().getName(), jobParam);
		}
	}

	/**
	 * 
	 * 
	 * @param name
	 * @param mhsJob
	 * @return
	 */
	private JobCoreConfiguration createJobCoreConfiguration(String name, YzJob yzJob, String jobParam) {
		JobCoreConfiguration conf = JobCoreConfiguration.newBuilder(name, yzJob.cron(), yzJob.shardingTotalCount())
				.failover(true).misfire(true).shardingItemParameters(yzJob.parameter()).jobParameter(jobParam)
				.description(yzJob.taskDesc()).build();
		return conf;
	}
}
