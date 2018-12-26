package com.yz.model;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class BaseEvent implements java.io.Serializable {

	private Date createDate; // 创建时间

	private Long id; // id

	private String traceId; // traceId
	
	private boolean isTrace = true ; // 是否收集Trace记录 

	public BaseEvent() {
		this.createDate = new Date();
		this.id = this.createDate.getTime();
	} 

	public boolean isTrace() {
		return isTrace;
	}



	public void setTrace(boolean isTrace) {
		this.isTrace = isTrace;
	}



	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
