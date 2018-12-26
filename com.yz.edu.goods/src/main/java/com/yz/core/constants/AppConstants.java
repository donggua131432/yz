package com.yz.core.constants;
/**
 * 第三方调用常量类
 * @author Administrator
 *
 */
public class AppConstants {
	
	/** 短信类型 - 文本短信 */
	public static final String SMS_TYPE_TEXT = "1";
	/** 短信类型 - 语音短信 */
	public static final String SMS_TYPE_VOICE = "2";
	
	/** 操作人姓名 */
	public static final Object OPERATOR_NAME = "operatorName";
	/** 操作人ID */
	public static final Object OPERATOR_ID = "operatorId";
	
	/** 短信发送状态-成功*/
	public static final String SEND_STATUS_SUCCESS = "1";
	/** 短信发送状态-失败*/
	public static final String SEND_STATUS_FAIL = "2";
	
	/**活动类型-兑换*/
	public static final String SALES_TYPE_EXCHNAGE = "1";
	/**活动类型-抽奖*/
	public static final String SALES_TYPE_LOTTERY = "2";
	/**竞拍*/
	public static final String SALES_TYPE_AUCTION = "3";
	
	/** 奖品类型 1-实物*/
	public static final String LOTTERY_PRIZE_TYPE_KIND = "1";
	/** 奖品类型 2-虚拟产品*/
	public static final String LOTTERY_PRIZE_TYPE_VIRTUAL = "2";
	
	/** 奖品发送状态 1-未发送*/
	public static final String LOTTERY_USER_PRIZE_STATUS_UNSEND = "1";
	/** 奖品发送状态 2-邮寄中*/
	public static final String LOTTERY_USER_PRIZE_STATUS_SENDING = "2";
	/** 奖品发送状态 3-已收货*/
	public static final String LOTTERY_USER_PRIZE_STATUS_RECEIVED = "3";
	/** 奖品发送状态 4-虚拟奖品已发放*/
	public static final String LOTTERY_USER_PRIZE_STATUS_SENDED = "4";
	
	/**京东下单,不预占库存 0 预占(可通过接口取消),1 不预占*/
	public static final String SUBMIT_STATE = "submit.state";
	/**京东开票 纳税号*/
	public static final String REG_CODE = "reg.code";
	/**京东开票 收增票人电话*/
	public static final String INVOICE_PHONE = "invoice.phone";
	
	/**京东发票类型:1 普通发票 2 增值税发票 3 电子发票*/
	public static final String INVOICE_TYPE = "invoice.type";
}
