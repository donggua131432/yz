package com.yz.edu.trace.constant;

/**
 * 
 * @desc trace调用链的常量
 * @author lingdain
 *
 */
public interface TraceConstant {

	public static final int TRACE_MYSQL = 1; // mysql

	public static final int TRACE_REDIS = 2; // redis

	public static final int TRACE_RPC = 3; // RPC 调用
	
	public static final int TRACE_HTTP = 4; // HTTP 调用
	
	public static final int TRACE_MQ= 5;  // MQ 调用

	public static final String TRACE_INFO_TOPIC = "com.yz.trace.info";
	
	public static final String TRACE_INFO_TOPIC_DESC = "traceInfo调用链任务";
	
 	public static final String TRACE_SPAN_TOPIC_DESC = "traceSpan调用链任务";

	public static final String TRACE_SPAN_TOPIC = "com.yz.trace.span";
	
	public static final int TRACE_SPAN_ASYNC = 1; 
	
	public static final int TRACE_SPAN_SYNC = 0; 
 
	
}
