package com.yz.job.common;

/**
 * 
 * @author lingdian
 *
 */
public class YzJobInfo implements java.io.Serializable{

    private String taskDesc;//任务描述

    public String redisName;

    public String queueName; //队列名称

    public String queueType;// redis 连接源名称

    public String jobType;//任务调度类型 task 

    public Class<?> targetCls;

    public String convert;//数据转化器
    
    private int step; // 消费的步长  step表示单笔消费 
    
    private boolean log; // 是否记录日志 
    
    private String logFormat; // 日志格式
    
    private int dbIndex ; //数据库下标 
    
    public void setDbIndex(int dbIndex) {
		this.dbIndex = dbIndex;
	}
    
    public int getDbIndex() {
		return dbIndex;
	}
     
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public String getLogFormat() {
		return logFormat;
	}

	public void setLogFormat(String logFormat) {
		this.logFormat = logFormat;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getRedisName() {
		return redisName;
	}

	public void setRedisName(String redisName) {
		this.redisName = redisName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getQueueType() {
		return queueType;
	}

	public void setQueueType(String queueType) {
		this.queueType = queueType;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Class<?> getTargetCls() {
		return targetCls;
	}

	public void setTargetCls(Class<?> targetCls) {
		this.targetCls = targetCls;
	}

	public String getConvert() {
		return convert;
	}

	public void setConvert(String convert) {
		this.convert = convert;
	}
 
}
