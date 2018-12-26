package com.yz.edu.model;

import java.util.Date;

import com.yz.edu.trace.constant.TraceConstant;

/**
 * 
 * @desc resouceType {@link TraceConstant}
 * @author Administrator
 *
 */
public class AnnotationAlarm implements java.io.Serializable {

	private Double avgDestination; // 平均耗时

	private Long maxDestination; // 最大耗时

	private Integer resouceType; // 资源类型

	private Long date; // 触发时间

	private int count; // 调用次数

	public AnnotationAlarm(Double avgDestination, Long maxDestination, Integer resouceType, Long count) {
		this.avgDestination = avgDestination;
		this.maxDestination = maxDestination;
		this.resouceType = resouceType;
		this.count = count.intValue();
		this.date = new Date().getTime();
	}
	
	 
	public int getCount() {
		return count;
	}
 

	public void setCount(int count) {
		this.count = count;
	}



	public void setDate(Long date) {
		this.date = date;
	}

	public Long getDate() {
		return date;
	}

	public Double getAvgDestination() {
		return avgDestination;
	}

	public void setAvgDestination(Double avgDestination) {
		this.avgDestination = avgDestination;
	}

	public Long getMaxDestination() {
		return maxDestination;
	}

	public void setMaxDestination(Long maxDestination) {
		this.maxDestination = maxDestination;
	}

	public void setResouceType(Integer resouceType) {
		this.resouceType = resouceType;
	}

	public int getResouceType() {
		return resouceType;
	}

	public void setResouceType(int resouceType) {
		this.resouceType = resouceType;
	}
}
