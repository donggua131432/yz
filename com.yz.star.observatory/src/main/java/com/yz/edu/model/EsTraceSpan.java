package com.yz.edu.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;

@SuppressWarnings("serial")
public class EsTraceSpan extends BaseEsTraceObj {

	private String tid; // 唯一标志

	private String pid; // 上级spanId

	private String traceId; // 调用链路ID

	private String appName; // 应用名称

	private String methodName; // 类名+方法名称 全路径

	private Date date; // 调用时间

	private long destination; // 耗时

	private int seq; // 排序使用

	private String host; // 主机地址

	private String remark; // 方法体备注

	private int async = 0; // 是否异步 0 否 1 是
	
	private int isError = 0 ; // 是否异常 0 否 1 是

	private String errorCode; // 错误代码

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getDestination() {
		return destination;
	}

	public void setDestination(long destination) {
		this.destination = destination;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getAsync() {
		return async;
	}

	public void setAsync(int async) {
		this.async = async;
	}

	/**
	 * 
	 * @param traceInfo
	 * @return
	 */
	public static List<EsTraceSpan> wrap(TraceTransfer traceInfo) {
		List<EsTraceSpan> result = Lists.newArrayList();
		traceInfo.getTraceSpan().parallelStream().filter(Objects::nonNull).forEach(v -> {
			result.addAll(wrap(v));
		});
		return result;
	}

	/**
	 * @desc
	 * @param span
	 * @return
	 */
	public static List<EsTraceSpan> wrap(TraceSpan span) {
		List<EsTraceSpan> spans = Lists.newArrayList(warpTraceSpan(span));
		List<TraceSpan> datas = span.getSpans();
		if (datas != null && !datas.isEmpty()) {
			datas.parallelStream().forEach(v -> {
				warpToList(v, spans);
			});
		}
		return spans;
	}

	/**
	 * 
	 * @param span
	 * @return
	 */
	private static void warpToList(TraceSpan span, List<EsTraceSpan> spans) {
		EsTraceSpan esTraceSpan = warpTraceSpan(span);
		spans.add(esTraceSpan);
		List<TraceSpan> datas = span.getSpans();
		if (datas != null && !datas.isEmpty()) {
			datas.parallelStream().forEach(v -> {
				warpToList(v, spans);
			});
		}
	}

	/**
	 * 
	 * @param span
	 * @return
	 */
	private static EsTraceSpan warpTraceSpan(TraceSpan span) {
		EsTraceSpan esTraceSpan = new EsTraceSpan();
		esTraceSpan.setAppName(span.getAppName());
		esTraceSpan.setAsync(span.getAsync());
		esTraceSpan.setTid(span.getTid());
		esTraceSpan.setHost(span.getHost());
		esTraceSpan.setTraceId(span.getTraceId());
		esTraceSpan.setSeq(span.getSeq());
		esTraceSpan.setRemark(span.getRemark());
		esTraceSpan.setDate(span.getDate());
		esTraceSpan.setDestination(span.getDestination());
		esTraceSpan.setMethodName(span.getMethodName());
		esTraceSpan.setPid(span.getPid());
		esTraceSpan.setIsError(span.getIsError());
		esTraceSpan.setErrorCode(span.getErrorCode());
		return esTraceSpan;
	}

	@Override
	public String indexName() {
		return ObservatoryStarConstant.OBSERVATORY_STAR_SPAN;
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
	
	@Override
	public String getId() {
		return this.tid;
	}

}
