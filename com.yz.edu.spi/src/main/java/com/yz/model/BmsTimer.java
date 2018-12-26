package com.yz.model;

import java.io.Serializable;
import java.util.Date;

import com.yz.model.communi.Body;

@Deprecated
public class BmsTimer implements Serializable{
	private static final long serialVersionUID = 2118685152497349401L;
	
	public static final String STATUS_RUNNING = "1";  
	public static final String STATUS_NOT_RUNNING = "0";  
	public static final String CONCURRENT_IS = "1";  
	public static final String CONCURRENT_NOT = "0";  
	
	private String id;
    
    private String jobName;
    private String jobGroup;
    private String jobStatus;
    private String cronExpression;
    private Body body;
    private String description;
    private String beanClass;
    private String isConcurrent;
    private String methodName;
    
    private String param;

    private String updateUserId;

    private String updateUser;

    private Date updateTime;

    private String beanType;
    private String version;
    private Date cronTime;    //表达式对应的时间
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.trim();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup == null ? null : jobGroup.trim();
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus == null ? null : jobStatus.trim();
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass == null ? null : beanClass.trim();
    }

    public String getIsConcurrent() {
        return isConcurrent;
    }

    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent == null ? null : isConcurrent.trim();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName == null ? null : methodName.trim();
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBeanType()
	{
		return beanType;
	}

	public void setBeanType(String beanType)
	{
		this.beanType = beanType;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public static String getStatusRunning() {
		return STATUS_RUNNING;
	}

	public static String getStatusNotRunning() {
		return STATUS_NOT_RUNNING;
	}

	public static String getConcurrentIs() {
		return CONCURRENT_IS;
	}

	public static String getConcurrentNot() {
		return CONCURRENT_NOT;
	}

	public Date getCronTime()
	{
		return cronTime;
	}

	public void setCronTime(Date cronTime)
	{
		this.cronTime = cronTime;
	}
}