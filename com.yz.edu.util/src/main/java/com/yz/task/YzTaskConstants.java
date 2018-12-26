package com.yz.task;

/**
 * 
 * @author Administrator
 *
 */
public interface YzTaskConstants {

	public String YZ_SMS_SEND_TASK = "com.yz.sms.sendtask";  // 发送sms短信的task
	
	public String YZ_SMS_SEND_TASK_DESC = "发送短信任务";  // 发送sms短信的task
	
	public String YZ_WECHAT_MSG_TASK  = "com.yz.wechatmsg.sendtask" ; // 推送微信消息task
	
	public String YZ_WECHAT_MSG_TASK_DESC  = "推送微信消息" ; // 推送微信消息task
	
	public String YZ_ACCESS_TOKEN_TASK = "获取微信公众号accessToken";  // 获取微信公众号accessToken
	
	public String YZ_MSG_PUB_TASK_SET = "com.yz.msg-pub.set";  // 通知消息
	
	public String YZ_MSG_PUB_TASK_DESC = "通知消息";  // 通知消息
	
	public String YZ_JD_COLLECTION_TASK_DESC = "京东收藏通知";  // 京东收藏通知
	
	public String YZ_JD_COLLECTION_TASK_SET = "com.yz.jd-collection.set";  // 京东收藏通知
	
	public String YZ_MSG_CLASS_REMIND_DESC = "上课提醒通知";        //上课提醒通知
	
	public String YZ_REFRESH_JDORDERTASK_DESC = "定时同步京东订单状态";        //定时刷新京东订单状态
	 
	public String YZ_USER_BINDMOBILE_EVENT = "com.yz.user.bindmobile.event";        //用户绑定手机事件
	public String YZ_USER_BINDMOBILE_EVENT_DESC = "用户绑定手机事件";        //用户绑定手机事件
	
	
	public String YZ_USER_REGISTER_EVENT = "com.yz.user.register.event";        //用户注册事件
	public String YZ_USER_REGISTER_EVENT_DESC = "用户注册事件";        //用户注册事件
	
	public String YZ_USER_RECHARGE_EVENT = "com.yz.user.recharge.event";        //用户缴费
	public String YZ_USER_RECHARGE_EVENT_DESC = "用户缴费";        //用户缴费
	
	public String YZ_USER_PAYSUCCESS_EVENT = "com.yz.user.pay.success.event";        //用户成功缴费
	public String YZ_USER_PAYSUCCESS_EVENT_DESC = "用户成功缴费";        //用户成功缴费
	
	public String YZ_REG_INVITE_DATE_ACQUISITION_EVENT = "com.yz.edu.invite.events"; //用户注册数据
	public String YZ_REG_INVITE_DATE_ACQUISITION_DESC = "用户注册数据采集";        //用户注册数据采集
	
	public String YZ_USER_BACK_EVENT = "com.yz.user.back.event";        //用户退费退智米
	public String YZ_USER_BACK_EVENT_DESC = "用户退费退智米";        //用户退费退智米
 
	public String YZ_MSG_STUDENT_EXAM_WARN_DESC = "考试提醒通知";   //考试提醒通知
	
	public String YZ_EMP_EXPENSE_TASK_DESC = "员工月度报销记录";    //员工月度报销记录 
	
	public String YZ_EMP_MODIFY_TASK = "员工岗位信息修改任务";  // 员工岗位信息修改任务

	public String YZ_MEND_ZHIMI_TASK = "智米补送任务";

	public String YZ_TASK_CARD_TASK = "任务卡状态自动修改任务";
}
