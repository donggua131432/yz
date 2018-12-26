package com.yz.job.constants;

/**
 * @author Administrator
 */
public interface JobConstants {

    String JOB_TYPE_SIMPLE = "simple"; //简单任务触发 
    
    String JOB_TYPE_DATAFLOW = "dataFlow"; //dataFlow  
    
    String JOB_TYPE_MQ = "mq"; //mq消息订阅

    String JOB_TYPE_JOB = "job"; //跑批 

    String JOB_QUEUE_TYPE_LIST = "list";

    String JOB_QUEUE_TYPE_SET = "set";

    String JOB_QUEUE_TYPE_SORTSET = "sortSet";

    String REDIS_NAME_DEFAULT = "default";
    
    String JOB_EVENT_MARK = "eventId";
    
    String JOB_SEND_SMS_LOGFORMAT= "模板【%s】,发送结果【%s】";                              //短信发送记录
    
    String JOB_CLASS_REMIND_LOGFORMAT = "【%s】---【%s】推送课程【%s】上课提醒,共计推送【%s】人"; //上课提醒通知
    
    String JOB_EXAM_WARN_LOGFORMAT = "【%s】:考点,考试时间为【%s】--【%s】考试提醒推送,共计推送【%s】人"; //考试提醒                         

}
