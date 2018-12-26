package com.yz.constants;

public class MessageConstants {

	/** 消息推送状态  1-待提交 */
	public static final String MESSAGE_STATUS_UNSUBMIT = "1";
	/** 消息推送状态  2-待审核 */
	public static final String MESSAGE_STATUS_UNCHECK = "2";
	/** 消息推送状态  3-待同步*/
	public static final String MESSAGE_STATUS_UNSEND = "3";
	/** 消息推送状态  4-已同步 */
	public static final String MESSAGE_STATUS_SENDED = "4";
	/** 消息推送状态  5-驳回 */
	public static final String MESSAGE_STATUS_REJECT = "5";
	
	/** 消息发送渠道  1-短信 */
	public static final String MESSAGE_CHANNEL_MSG = "1";
	/** 消息发送渠道  2-微信 */
	public static final String MESSAGE_CHANNEL_WECHAT = "2";
	/** 消息发送渠道  3-钉钉 */
	public static final String MESSAGE_CHANNEL_DINGDING= "3";
	/** 消息发送渠道  4-邮箱 */
	public static final String MESSAGE_CHANNEL_EMAIL = "4";
	
	/** 消息发送状态  1-成功 */
	public static final String MESSAGE_SEND_STATUS_SUCCESS = "1";
	/** 消息发送状态   2-失败 */
	public static final String MESSAGE_SEND_STATUS_FAILED = "2";
	
	/** 消息类型   1-学员通知 */
	public static final String MESSAGE_TYPE_STUDENT = "inform";
	
	
	/** 消息替换字符   [NAME]-学员姓名 */
	public static final String MESSAGE_STATEMENT_NAME = "[NAME]";
	
	/** 消息-学员是否选中  1-是 */
	public static final String MESSAGE_STD_CHECKED = "1";
	/** 消息-学员是否选中   0-否 */
	public static final String MESSAGE_STD_UNCHECK = "2";
	
	/** 员工离职消息类型   1-微信 */
	public static final String DIMISSION_MSG_TYPE_WECHAT = "1";
	/** 员工离职消息类型   2-短信 */
	public static final String DIMISSION_MSG_TYPE_MESSAGE = "2";
	
	
}
