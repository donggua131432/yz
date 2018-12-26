package com.yz.edu.trace;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yz.util.JsonUtil;
import com.yz.util.OSUtil;
import com.yz.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lingdian
 * @desc trace 调用监控
 */
@SuppressWarnings("serial")
public class TraceTransfer implements java.io.Serializable {

	private transient static final Logger logger = LoggerFactory.getLogger(TraceTransfer.class);

	private transient static ThreadLocal<TraceTransfer> trace = ThreadLocal.withInitial(TraceTransfer::new);

	private transient TraceSpan currentSpan = null; // 当前调用方法

	private Map<String, TraceSpan> traceSpans = null; // 方法级别的监控数据

	private String traceId; // 调用链ID 要求全局唯一

	private String appName; // appName

	private String serviceName; // 服务名称

	private String remark; // 调用备注

	private Date date; // 调用时间

	private String addr; // 地址

	private long destination; // 耗时

	private int isError; // 是否异常 0 否 1 是

	private String errorCode; // 错误代码

	private int index = 0;

	/**
	 * @desc 构造函数 初始化一些基本参数信息
	 */
	private TraceTransfer() {
		this.traceId = StringUtil.UUID();
		this.date = new Date();
		this.addr = OSUtil.getIp(); // 服务器地址
		this.traceSpans = Maps.newHashMap();
	}

	public Map<String, TraceSpan> getTraceSpans() {
		return traceSpans;
	}

	/**
	 * @desc
	 */
	public static TraceTransfer getTrace() {
		return trace.get();
	}

	/**
	 * @desc
	 */
	public static TraceTransfer getTrace(String appName) {
		trace.get().setAppName(appName);
		return trace.get();
	}

	public int getIsError() {
		return isError;
	}

	public void setIsError(int isError) {
		this.isError = isError;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public long getDestination() {
		return destination;
	}

	public void setDestination(long destination) {
		this.destination = destination;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppName() {
		return appName;
	}

	/**
	 * @desc 获取调用链上的全部的TraceSpan
	 */
	public List<TraceSpan> getTraceSpan() {
		return Lists.newArrayList(traceSpans.values());
	}

	/**
	 * @desc 根据方法名获取当前的TraceSpan
	 */
	public TraceSpan getTraceSpan(String key) {
		TraceSpan span = this.traceSpans.get(key);
		if (span == null) {
			return this.currentSpan;
		}
		this.currentSpan = span;
		return span;
	}

	/**
	 * @desc 根据方法名获取当前的TraceSpan
	 */
	public void setParentTraceSpanCurrent(TraceSpan span) {
		String pid = span.getPid();
		if (StringUtil.isNotBlank(pid)) {
			traceSpans.values().forEach(v -> {
				if (StringUtil.equalsIgnoreCase(v.getTid(), pid)) {
					currentSpan = v;
					logger.info("切换span。success");
				}
			});
		}
	}

	/**
	 * @desc 添加调用块到调用链
	 */
	public void addTraceSpan(TraceSpan span) {
		span.setSeq(++index);
		if (this.currentSpan != null) {
			span.setPid(currentSpan.getTid());
			span.setTraceId(currentSpan.getTraceId());
			currentSpan.addTraceSpan(span);
		}
		this.currentSpan = span;
		this.traceSpans.put(span.getMethodName(), span);
		logger.debug("TraceSpan.addSpan{}", JsonUtil.object2String(span));
	}

	/**
	 * @desc 切换CurrentSpan
	 */
	public void setCurrentSpan(String tid) {
		this.traceSpans.entrySet().parallelStream().forEach(v -> {
			if (StringUtil.equalsIgnoreCase(tid, v.getValue().getTid())) {
				this.currentSpan = v.getValue();
			}
		});
		logger.debug("setCurrentSpan{}", tid);
	}
	
	/**
	 * @desc 切换CurrentSpan
	 */
	public void setCurrentSpan(TraceSpan span) {
		setCurrentSpan(span.getPid());
		logger.debug("setCurrentSpan{}", JsonUtil.object2String(span));
	}

	/**
	 * @desc 添加资源调用埋点 直接将annotation添加至span中
	 */
	public void addAnnotation(TraceAnnotation annotation) {
		if (currentSpan != null) {
			currentSpan.addTraceAnnotation(annotation);
			logger.debug("TraceSpan.addAnnotation{}", JsonUtil.object2String(annotation));
		}
	}

	/**
	 * @desc 清除trace的上下文环境
	 */
	public void remove() {
		trace.remove();
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @desc 从当前的上下文环境获取traceId（默认方法为同步调用）
	 * @param applicationName
	 * @param methodName
	 * @param host
	 * @return
	 */
	public TraceSpan getTraceSpan(String applicationName, String methodName, String host) {
		return this.getTraceSpan(applicationName, methodName, host, 0);
	}

	/**
	 * @desc 从当前的上下文环境获取traceId（默认方法为同步调用）
	 * @param applicationName
	 * @param methodName
	 * @param host
	 * @param async
	 *            方法是否同步调用 0 同步调用 1 异步调用
	 * @return
	 */
	public TraceSpan getTraceSpan(String applicationName, String methodName, String host, int async) {
		return this.getTraceSpan(traceId, applicationName, methodName, host, async);
	}

	/**
	 * 
	 * @param applicationName
	 * @param methodName
	 * @param host
	 * @return
	 */
	public TraceSpan getTraceSpan(String traceId, String applicationName, String methodName, String host, int async) {
		TraceSpan span = new TraceSpan();
		span.setAppName(applicationName);
		span.setTraceId(traceId);
		span.setMethodName(methodName);
		span.setHost(host);
		span.setDate(new Date());
		span.setAsync(async);
		this.addTraceSpan(span);
		return span;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
