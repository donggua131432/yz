package com.yz.edu.model;

import java.util.Date;

import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.trace.TraceTransfer;

/**
 * 
 * @desc
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class EsTraceInfo extends BaseEsTraceObj {

	private String traceId; // 调用链ID 要求全局唯一

	private String appName; // appName

	private String serviceName; // 服务名称

	private String remark; // 调用备注

	private Date date; // 调用时间

	private String addr; // 地址

	private long destination; // 耗时

	private int isError; // 是否异常 0 否 1 是

	private String errorCode; // 错误代码

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

	public static EsTraceInfo wrap(TraceTransfer traceInfo) {
		EsTraceInfo info = new EsTraceInfo();
		info.setAddr(traceInfo.getAddr());
		info.setAppName(traceInfo.getAppName());
		info.setDestination(traceInfo.getDestination());
		info.setErrorCode(traceInfo.getErrorCode());
		info.setIsError(traceInfo.getIsError());
		info.setRemark(traceInfo.getRemark());
		info.setTraceId(traceInfo.getTraceId());
		info.setServiceName(traceInfo.getServiceName());
		info.setDate(traceInfo.getDate());
		return info;
	}

	
	@Override
	public String indexName() {
		return ObservatoryStarConstant.OBSERVATORY_STAR_INFO;
	}

	@Override
	public String getId() {
		return this.traceId;
	}
    
}
