package com.yz.job.common;

 

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yz.constants.CommonConstants;
import com.yz.job.constants.JobConstants;
import com.yz.util.StringUtil;

/**
 * @author Administrator
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YzJob {

    public String taskDesc();//任务描述

    public String redisName() default JobConstants.REDIS_NAME_DEFAULT;

    public String queueName() default StringUtil.EMPTY; //队列名称

    public String queueType() default JobConstants.JOB_QUEUE_TYPE_LIST;// redis 连接源名称

    public String jobType() default JobConstants.JOB_TYPE_SIMPLE;//任务调度类型 task 

    public Class<?> targetCls() default Object.class;

    public String convert() default CommonConstants.COMMON_CONVERT;//数据转化器

    public String cron() default "0/5 * * * * ?"; //表达式 
    
    public String parameter()default StringUtil.EMPTY; //运行参数
    
    public int shardingTotalCount() default 3; //任务的总分片数
    
    public boolean runNow() default true; // 是否立即执行 
     
    public int step() default 1; // 消费的步长  step表示单笔消费 
    
    public boolean log() default false; // 是否记录日志 
    
    public String logFormat() default StringUtil.EMPTY; // 日志格式
    
    public int dbIndex() default 0 ; // 默认redis连接数据库 
}
