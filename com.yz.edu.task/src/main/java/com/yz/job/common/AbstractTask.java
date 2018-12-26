package com.yz.job.common;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.google.common.collect.Lists;
import com.yz.convert.YzDataConvert;
import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.model.BaseEvent;
import com.yz.redis.RedisService;
import com.yz.serializ.FstSerializer;
import com.yz.trace.sender.TraceSender;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.OSUtil;
import com.yz.util.StringUtil;

/**
 * 
 * @author Administrator
 *
 * @param <T>
 */
public abstract class AbstractTask<T extends BaseEvent> implements YzTask {

	public final static YzTaskContext taskContext = YzTaskContext.getTaskContext();

	public Logger logger = LoggerFactory.getLogger(YzTask.class);

	public static FstSerializer fstSerialize = FstSerializer.getInstance();

	public RedisService redisService;

	@Value(value = "${dubbo.application:task}")
	private String appName;

	/**
	 *
	 *
	 * @return
	 */
	public RedisService getRedisService() {
		return this.getRedisService(null);
	}

	/**
	 * @desc 获取redisService thread safe
	 */
	public RedisService getRedisService(String redisName) {
		if (redisService == null) {
			synchronized (AbstractSimpleTask.class) {
				if (redisService == null) {
					if (StringUtil.isBlank(redisName)) {
						redisService = RedisService.getRedisService();
					} else if (StringUtil.isNotBlank(redisName)) {
						redisService = RedisService.getRedisService(redisName);
					}
				}
				logger.info("getRedisName:{}.success!", redisName);
			}
		}
		return redisService;
	}

	/**
	 *
	 * @param sleep
	 */
	public void sleep(long sleep) {
		try {
			if (sleep <= 0) {
				sleep = 100;
			}
			Thread.sleep(sleep); // 避免出现cpu空转 sleep 0.1s
		} catch (InterruptedException e) {
			logger.error("sleep.error,msg:{}", ExceptionUtil.getStackTrace(e));
		}
	}

	/**
	 * 
	 * @param shardingContext
	 */
	public void setJobInfoContext(ShardingContext shardingContext, YzJobInfo job) {
		YzTaskContext.getTaskContext().setContextAttr(JOB_EVENT_MARK, shardingContext.getEventId());
		YzTaskContext.getTaskContext().setContextOnce(job);
	}

	/**
	 * @desc 获取job上下文配置信息
	 * @param shardingContext
	 * @return
	 */
	public YzJobInfo getYzJobInfo(ShardingContext shardingContext) {
		YzJobInfo job = taskContext.getYzJobInfo(shardingContext.getJobName());
		setJobInfoContext(shardingContext, job);
		return job;
	}

	/**
	 * @desc simpleJobExecutor
	 * @param shardingContext
	 */
	public void execute(ShardingContext shardingContext) {
		TraceTransfer traceTransfer = TraceTransfer.getTrace();
		TraceSpan span = null;
		try {
			List<T> data = fetchData(shardingContext);
			if (data != null && !data.isEmpty()) {
				Optional<T> optional = data.parallelStream().filter(v -> {
					if (v instanceof BaseEvent) {
						return ((BaseEvent) v).isTrace();
					}
					return false;
				}).findFirst();
				if (optional.isPresent()) {
					YzJobInfo job = this.getYzJobInfo(shardingContext);
					BaseEvent event = optional.get();
					span = traceTransfer.getTraceSpan(event.getTraceId(), appName, job.getQueueName(), OSUtil.getIp(),
							TraceConstant.TRACE_SPAN_ASYNC);
					span.setRemark(job.getTaskDesc());
				}
				processData(shardingContext, data);
			}
		} catch (Exception ex) {
			logger.error("executeTask.error:{}", ExceptionUtil.getStackTrace(ex));
		} finally {
			YzTaskContext.getTaskContext().clear(); 
			TraceSender.sendSpan(span);
			traceTransfer.remove();
		}
	}

	/**
	 * 获取待处理数据.
	 *
	 * @param shardingContext
	 *            分片上下文
	 * @return 待处理的数据集合
	 */
	public List<T> fetchData(ShardingContext shardingContext) {
		YzJobInfo job = this.getYzJobInfo(shardingContext);
		String queueName = job.getQueueName();
		if (StringUtil.isBlank(queueName))// 队列名称
		{
			this.executeOther();
			logger.debug("execTask:{},queueName is null,invoke executeOther", job.getTaskDesc());
			return null;
		}
		String queueType = job.getQueueType();
		List<String> data = Lists.newArrayListWithCapacity(job.getStep());
		if (StringUtil.equals(queueType, JOB_QUEUE_TYPE_LIST)) {
			data.addAll(getRedisService(job.getRedisName()).lpop(job.getQueueName(), job.getStep(), job.getDbIndex()));
		} else if (StringUtil.equals(queueType, JOB_QUEUE_TYPE_SET)) {
			data.addAll(getRedisService(job.getRedisName()).spop(job.getQueueName(), job.getStep(), job.getDbIndex()));
		}
		if (data == null || data.isEmpty()) {
			logger.debug("{}.FetchRedisData:{} not found!", job.getTaskDesc(), job.getQueueName());
			this.sleep(100);
			return null;
		}
		YzDataConvert convert = ApplicationContextUtil.getBean(job.getConvert());
		return data.stream().map(v -> (T) convert.convert(v, job.getTargetCls())).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param shardingContext
	 * @param data
	 */
	public void processData(ShardingContext shardingContext, List<T> data) {
		YzJobInfo job = this.getYzJobInfo(shardingContext);
		if (job.getStep() == 1) {
			executeRedis(data.get(0));
			return;
		}
		executeRedis(data);
	}

	/**
	 * @desc 批量多条消费
	 * @param data
	 */
	public void executeRedis(List<T> data) {

	}

	/**
	 * @desc 单条消费
	 * @param data
	 */
	public void executeRedis(T data) {

	}

	/**
	 * @desc 其他方式消费
	 * 
	 */
	public void executeOther() {

	}

}
