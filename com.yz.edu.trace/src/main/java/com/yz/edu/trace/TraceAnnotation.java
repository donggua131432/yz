package com.yz.edu.trace;

import java.util.Date;

/**
 * 
 * @desc 监控某个资源  redis mysql rpc
 * @author Administrator
 *
 */
public class TraceAnnotation implements java.io.Serializable{

	private int resouceType ; // 资源类型 
	
	private String resouceURI; // 资源链接地址
	
	private long destination ; //耗时 
	
	private Date date ; // 调用时间 
	
	private String operation ; // 操作 以及相关的参数 
	
	private String traceId; // traceId
	
	private String spanId;  // spanId
	
	private int sort ; // sort 
	
	public TraceAnnotation(int resouceType, String resouceURI, long destination, String operation) {
		this();
		this.resouceType = resouceType;
		this.resouceURI = resouceURI;
		this.destination = destination;
		this.operation = operation;
	}

	public TraceAnnotation()
	{
		setDate(new Date());
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
}
