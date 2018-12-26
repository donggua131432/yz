package com.yz.edu.trace;

 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;


/**
 * @author Administrator
 * @desc trace 监控span
 */
public class TraceSpan implements java.io.Serializable, Comparable<TraceSpan> {

    private static final Logger logger = LoggerFactory.getLogger(TraceSpan.class);

    private String tid; // 唯一标志

    private String pid; // 上级spanId

    private String traceId; // 调用链路ID

    private String appName; // 应用名称

    private String methodName; // 类名+方法名称 全路径

    private Date date; // 调用时间

    private long destination; // 耗时

    private int seq; // 排序使用

    private String host; //主机地址

    private String remark; // 方法体备注
    
    private int async = 0  ; // 是否异步 0 否 1 是 
    
	private int isError = 0 ; // 是否异常 0 否 1 是

	private String errorCode; // 错误代码

    private List<TraceAnnotation> annotations; // 资源埋点
    
    private List<TraceSpan>spans; // 下级 子span


    public TraceSpan() {
    	this.spans = new ArrayList<>();
        this.annotations = new ArrayList<>();
        this.tid = StringUtil.UUID();
        this.date = new Date();
    }

    
    public void setAsync(int async) {
		this.async = async;
	}
    
    public int getAsync() {
		return async;
	}
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void addTraceSpan(TraceSpan span)
    {
    	this.spans.add(span);
    	logger.debug("addTraceAnnotation.info:{}", JsonUtil.object2String(span));
    }
    
    
    public List<TraceSpan> getSpans() {
		return spans;
	}
    
    /**
     *
     *
     * @param traceAnnotation
     */
    public void addTraceAnnotation(TraceAnnotation traceAnnotation) {
    	traceAnnotation.setSpanId(this.tid);
    	traceAnnotation.setTraceId(this.traceId);
    	traceAnnotation.setSort(this.annotations.size()+1);
        this.annotations.add(traceAnnotation);
        logger.debug("addTraceAnnotation.info:{}", JsonUtil.object2String(traceAnnotation));
    }
 
    public List<TraceAnnotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<TraceAnnotation> annotations) {
		this.annotations = annotations;
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

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

    @Override
    public int compareTo(TraceSpan o) {
        return this.seq >= o.seq ? -1 : 1;
    }
}
