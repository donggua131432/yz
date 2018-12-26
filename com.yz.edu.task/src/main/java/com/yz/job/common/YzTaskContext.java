package com.yz.job.common;

import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.job.event.type.JobExecutionEvent;

import com.google.common.collect.Maps;
import com.yz.job.constants.JobConstants;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import com.google.common.collect.Lists;

/**
 * 
 * @desc TaskContext上下文环境
 * @author Administrator
 *
 */
public class YzTaskContext {

	private static Logger logger = LoggerFactory.getLogger(YzTaskContext.class);

	private ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(Maps::newHashMap);

	private LRUMap<String, JobExecutionEvent> taskEventMap = new LRUMap<String, JobExecutionEvent>(20000);
	/**
	 * @desc key jobName
	 * @desc value YzJobInfo task配置信息
	 */
	private Map<String, YzJobInfo> jobInfoMap = new ConcurrentHashMap<>();

	private static class TaskContextHolder {
		private static YzTaskContext _instance = new YzTaskContext();

	}

	private YzTaskContext() {

	}

	public static YzTaskContext getTaskContext() {
		return TaskContextHolder._instance;
	}

	/**
	 * 
	 * @param jobName
	 * @param param
	 * @return
	 */
	public YzJobInfo addYzJobInfo(String jobName, String param) {
		if (jobInfoMap.containsKey(jobName)) {
			return jobInfoMap.get(jobName);
		}
		YzJobInfo info = JsonUtil.str2Object(param, YzJobInfo.class);
		jobInfoMap.put(jobName, info);
		return info;
	}

	/**
	 * 
	 * @param jobName
	 * @return
	 */
	public YzJobInfo getYzJobInfo(String jobName) {
		if (jobInfoMap.containsKey(jobName)) {
			return jobInfoMap.get(jobName);
		}
		logger.error("not found {}.config ", jobName);
		return null;
	}
	
	/**
	 * 
	 * @param obj
	 */
	public void setContextOnce(Object obj) {
		if (obj != null) {
			String keyName = obj.getClass().getName();
			Map<String, Object> data = context.get();
			if (!data.containsKey(keyName)) { 
				data.put(keyName, obj);
			}
		}
	}

	/**
	 * 
	 * @param obj
	 */
	@SuppressWarnings("rawtypes")
	public void setContext(Object obj) {
		if (obj != null) {
			String keyName = obj.getClass().getName();
			Map<String, Object> data = context.get();
			if (data.containsKey(keyName)) {
				Object src = data.get(keyName);
				List list = null;
				if (src instanceof List) {
					list = (List) src;
					list.add(obj);
				} else {
					list = Lists.newArrayList(obj, src);
				}
				data.put(keyName, list);
			} else {
				data.put(keyName, obj);
			}
		}
	}

	/**
	 * @param key
	 * @param obj
	 */
	public void setContextAttr(String key, Object obj) {
		if (obj != null) {
			context.get().put(key, obj);
		}
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 *            默认值
	 * @return
	 */
	public Object getContextAttr(String key, Object obj) {
		if (StringUtil.isNotBlank(key)) {
			return context.get().get(key);
		}
		return obj;
	}

	/**
	 * 
	 * @param key
	 * @param obj
	 *            默认值
	 * @return
	 */
	public Object getContextAttr(String key) {
		return this.getContextAttr(key, null);
	}

	/**
	 * 
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getContext(Class<?> cls) {
		if (cls != null) {
			return (T) context.get().get(cls.getName());
		}
		logger.error("cls is null,check config again!");
		return null;
	}

	/**
	 * 
	 * @param cls
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getContextList(Class<?> cls) {
		if (cls != null) {
			Object obj = context.get().get(cls.getName());
			if (obj != null) {
				if (obj instanceof List) {
					return (List<T>) obj;
				}
				List<T> list = Lists.newArrayList((T) obj);
				return list;
			}
		}
		logger.error("cls is null,check config again!");
		return null;
	}

	/**
	 * 
	 * @param event
	 */
	public void setJobExecutionEvent(JobExecutionEvent event) {
		this.taskEventMap.put(event.getId(), event);
	}

	/**
	 * 
	 * @param keywords
	 * @param remark
	 */
	public void addEventDetail(String keywords, Object remark) {
		String eventId = (String) getContextAttr(JobConstants.JOB_EVENT_MARK);
		// 获取eventId
		if (StringUtil.isNotBlank(eventId)) {
			JobExecutionEvent event = this.taskEventMap.get(eventId);
			logger.info("eventId:{} event:{}!", eventId, JsonUtil.object2String(event));
			if (event != null) {
				event.addEventDetail(keywords, remark);
			}
			return;
		}
		logger.error("eventId is null,please check config !");
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public JobExecutionEvent getJobExecutionEvent(JobExecutionEvent event) {
		return this.taskEventMap.get(event.getId());
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public void removeJobExecutionEvent(JobExecutionEvent event) {
		this.taskEventMap.remove(event.getId());
	}

	/**
	 * @desc 清楚上下文环境
	 * 
	 */
	public void clear() {
		context.remove();
	}
}
