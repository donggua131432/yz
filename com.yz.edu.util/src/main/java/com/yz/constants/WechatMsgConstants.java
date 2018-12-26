package com.yz.constants;

public class WechatMsgConstants {
	/**
	 * 文本消息
	 */
	public static final String MESSAGE_TEXT = "text";
	/**
	 * 图片消息
	 */
	public static final String MESSAtGE_IMAGE = "image";
	/**
	 * 图文消息
	 */
	public static final String MESSAGE_NEWS = "news";
	/**
	 * 语音消息
	 */
	public static final String MESSAGE_VOICE = "voice";
	/**
	 * 视频消息
	 */
	public static final String MESSAGE_VIDEO = "video";
	/**
	 * 小视频消息
	 */
	public static final String MESSAGE_SHORTVIDEO = "shortvideo";
	/**
	 * 地理位置消息
	 */
	public static final String MESSAGE_LOCATION = "location";
	/**
	 * 链接消息
	 */
	public static final String MESSAGE_LINK = "link";
	/**
	 * 事件推送消息
	 */
	public static final String MESSAGE_EVENT = "event";
	/**
	 * 事件推送消息中,事件类型，subscribe(订阅)
	 */
	public static final String MESSAGE_EVENT_SUBSCRIBE = "subscribe";
	/**
	 * 事件推送消息中,事件类型，unsubscribe(取消订阅)
	 */
	public static final String MESSAGE_EVENT_UNSUBSCRIBE = "unsubscribe";
	/**
	 * 事件推送消息中,上报地理位置事件
	 */
	public static final String MESSAGE_EVENT_LOCATION_UP = "LOCATION";
	/**
	 * 事件推送消息中,自定义菜单事件,点击菜单拉取消息时的事件推送
	 */
	public static final String MESSAGE_EVENT_CLICK = "CLICK";
	/**
	 * 事件推送消息中,自定义菜单事件,点击菜单跳转链接时的事件推送
	 */
	public static final String MESSAGE_EVENT_VIEW = "VIEW";
	
	
	/**
	 * 微信模板消息-中奖通知
	 */
	public static final String TEMPLATE_MSG_WINNING = "winning";
	/**
	 * 微信模板消息-购买通知
	 */
	public static final String TEMPLATE_MSG_TUITION = "tuition";
	/**
	 * 微信模板消息-任务通知
	 */
	public static final String TEMPLATE_MSG_TASK = "task";
	/**
	 * 微信模板消息-签到通知
	 */
	public static final String TEMPLATE_MSG_SIGN = "sign";
	/**
	 * 微信模板消息-充值通知
	 */
	public static final String TEMPLATE_MSG_RECHARGE = "recharge";
	/**
	 * 微信模板消息-竞拍结果提醒
	 */
	public static final String TEMPLATE_MSG_AUCTION = "auction";
	/**
	 * 微信模板消息-竞拍开始提醒
	 */
	public static final String TEMPLATE_MSG_AUCTION_WARN = "auction_warn";
	/**
	 * 微信模板消息-抽奖开始提醒
	 */
	public static final String TEMPLATE_MSG_WINNING_WARN = "winning_warn";
	/**
	 * 微信模板消息-兑换开始提醒
	 */
	public static final String TEMPLATE_MSG_EXCHANGE_WARN = "exchange_warn";
	/**
	 * 微信模板消息-学员通知
	 */
	public static final String TEMPLATE_MSG_STUDENT_INFORM = "inform";
	/**
	 * 微信模板消息-学员注册提醒
	 */
	public static final String TEMPLATE_MSG_STUDENT_REGISTER = "register";
	
	/**
	 * 微信模板消息-学员上课提醒
	 */
	public static final String TEMPLATE_MSG_CLASS_WARN = "class_warn";
	
	/**
	 * 微信模板消息-学员提现申请失败通知
	 */
	public static final String TEMPLATE_MSG_WITHDRAW_REJECT = "withdraw_reject";
	
	/**
	 * 微信模板消息-学员考试提醒
	 */
	public static final String TEMPLATE_MSG_EXAM_WARN = "exam_warn";
	
	/**
	 * 微信模板消息--地址驳回
	 */
	public static final String TEMPLATE_MSG_TASK_FAILED = "task_failed";
	
	
	/** 微信api请求结果 0-成功*/
	public static final String WECHAT_POST_STATUS_SUCCESS = "0";
	
	/** 学服任务未完成*/
	public static final String PUB_MSG_TYPE_TASK ="stuTask";
	/** 消息注册*/
	public static final String PUB_MSG_TYPE_NOTICE = "noticeTask";

	/** 学员分配*/
	public static final String PUB_MSG_STUDENT_ASSIGN = "student_assign";
	
	
}
