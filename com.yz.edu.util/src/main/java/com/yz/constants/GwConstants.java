package com.yz.constants;

/**
 * 第三方调用常量类
 * 
 * @author Administrator
 *
 */
public class GwConstants {

	/** 短信类型 - 文本短信 */
	public static final String SMS_TYPE_TEXT = "1";
	/** 短信类型 - 语音短信 */
	public static final String SMS_TYPE_VOICE = "2";

	/** 操作人姓名 */
	public static final Object OPERATOR_NAME = "operatorName";
	/** 操作人ID */
	public static final Object OPERATOR_ID = "operatorId";

	/** 短信发送状态-成功 */
	public static final String SEND_STATUS_SUCCESS = "1";
	/** 短信发送状态-失败 */
	public static final String SEND_STATUS_FAIL = "2";

	/** 微信回调地址系统参数名称 */
	public static final String WECHAT_NOTIFY_URL_PARAM_NAME = "wechat.notify.url";
	/** 通联回调地址系统参数名称 */
	public static final String ALLINPAY_NOTIFY_URL_PARAM_NAME = "allinpay.notify.url";

	/** 微信公众号accessToken超时时间- 毫秒 */
	public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 100;

	/** 微信获取accessToken grantType */
	public static final String WECHAT_GRANT_TYPE = "client_credential";

	/** 微信菜单类型 view-跳转类型 */
	public static final String WECHAT_MENU_TYPE_VIEW = "view";
	/** 微信菜单类型 click-点击 */
	public static final String WECHAT_MENU_TYPE_CLICK = "click";

	/** 微信支付日志 支付状态 1-处理中 */
	public static final String WECHAT_PAYMENT_LOG_STATUS_DEALING = "1";
	/** 微信支付日志 支付状态 2-完成 */
	public static final String WECHAT_PAYMENT_LOG_STATUS_FINISHED = "2";
	/** 微信支付日志 支付状态 3-失败 */
	public static final String WECHAT_PAYMENT_LOG_STATUS_FAILED = "3";

	/** 支付方式 wechat-微信 */
	public static final String PAY_TYPE_WECHAT = "wechat";
	/** 支付方式 allinpay-通联 */
	public static final String PAY_TYPE_ALLINPAY = "allinpay";
	
	/** 京东实物accessToken超时时间- 毫秒 23小时 */
	public static final long JD_ACCESS_TOKEN_EXPIRE_TIME = 1000 *828 * 100;
	
	public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

	public static String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESSTOKEN&type=jsapi";

	public static String SERVICE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

	public static String SERVICE_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
	
	public static String JD_SELECT_ORDER_URL="https://bizapi.jd.com/api/order/selectJdOrder";
	
	public static String USER_REGISTER_BUZ = "register"; // 注册事件引擎
	
	public static String USER_REGISTER_BUZ_ENGINE = "user_register";
	
	public static String USER_RECHARGE_BUZ = "charge"; // 注册充值赠送事件引擎
	
	public static String USER_RECHARGE_BUZ_ENGINE = "user_recharge";
}
