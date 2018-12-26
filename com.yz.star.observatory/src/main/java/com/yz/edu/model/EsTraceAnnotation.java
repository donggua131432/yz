package com.yz.edu.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.trace.TraceAnnotation;
import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;

/**
 * 
 * @desc 监控某个资源 redis mysql rpc http
 * @author Administrator
 *
 */
public class EsTraceAnnotation extends BaseEsTraceObj {

	private int resouceType; // 资源类型

	private String resouceURI; // 资源链接地址

	private long destination; // 耗时

	private Date date; // 调用时间

	private String operation; // 操作 以及相关的参数

	private String traceId; // traceId

	private String spanId; // spanId

	private int sort; // sort

	public EsTraceAnnotation() {

	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getSort() {
		return sort;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getSpanId() {
		return spanId;
	}

	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public int getResouceType() {
		return resouceType;
	}

	public void setResouceType(int resouceType) {
		this.resouceType = resouceType;
	}

	public long getDestination() {
		return destination;
	}

	public void setDestination(long destination) {
		this.destination = destination;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public void setResouceURI(String resouceURI) {
		this.resouceURI = resouceURI;
	}

	public String getResouceURI() {
		return resouceURI;
	}

	public static EsTraceAnnotation wrap(TraceAnnotation annotation) {
		EsTraceAnnotation ann = new EsTraceAnnotation();
		ann.setDate(annotation.getDate());
		ann.setDestination(annotation.getDestination());
		ann.setOperation(annotation.getOperation());
		ann.setResouceType(annotation.getResouceType());
		ann.setResouceURI(annotation.getResouceURI());
		ann.setSort(annotation.getSort());
		ann.setSpanId(annotation.getSpanId());
		ann.setTraceId(annotation.getTraceId());
		return ann;
	}

	/**
	 * 
	 * @param span
	 * @return
	 */
	public static List<EsTraceAnnotation> wrap(TraceSpan span) {
		List<TraceSpan> spans = Lists.newArrayList(span);
		wrap(span, spans);
		List<EsTraceAnnotation> annotations = Lists.newArrayList();
		spans.parallelStream().forEach(v -> {
			annotations.addAll(v.getAnnotations().parallelStream().filter(Objects::nonNull).map(EsTraceAnnotation::wrap)
					.collect(Collectors.toList()));
		});
		return annotations;
	}

	/**
	 * @desc
	 * @param info
	 * @return
	 */
	public static List<EsTraceAnnotation> wrap(TraceTransfer info) {
		List<TraceSpan> spans = Lists.newArrayList();
		info.getTraceSpan().parallelStream().filter(Objects::nonNull).forEach(span -> {
			spans.add(span);
			wrap(span, spans);
		});
		List<EsTraceAnnotation> annotations = Lists.newArrayList();
		spans.parallelStream().forEach(v -> {
			annotations.addAll(v.getAnnotations().parallelStream().filter(Objects::nonNull).map(EsTraceAnnotation::wrap)
					.collect(Collectors.toList()));
		});
		return annotations;
	}

	/**
	 * 
	 * @param span
	 * @param spans
	 */
	public static void wrap(TraceSpan span, List<TraceSpan> spans) {
		List<TraceSpan> datas = span.getSpans();
		if (datas != null && !datas.isEmpty()) {
			datas.parallelStream().forEach(v -> {
				spans.add(v);
				wrap(v, spans); // 递归调用
			});
		}
	}

	@Override
	public String indexName() {
		return ObservatoryStarConstant.OBSERVATORY_STAR_ANNOTATION;
	}

	@Override
	public String getId() {
		return new StringBuilder(spanId).append("-").append(sort).toString();
	}
}
