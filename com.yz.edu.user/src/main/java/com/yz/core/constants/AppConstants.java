package com.yz.core.constants;
/**
 * 第三方调用常量类
 * @author Administrator
 *
 */
public class AppConstants {
	/** 操作类型 - 添加 */
	public static final String EXC_TYPE_ADD = "1";
	/** 操作类型 - 修改*/
	public static final String EXC_TYPE_UPDATE = "2";
	/** 操作类型 - 删除 */
	public static final String EXC_TYPE_DELETE = "3";
	
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
	/** 添加地址最大数量 */
	public static final String ADDRESS_MAX_SIZE = "address.max.size";
	
	/** 资料完善 - 姓名 */
	public static final String COMPLETE_FIELD_NAME = "name";
	/** 资料完善 - 性别 */
	public static final String COMPLETE_FIELD_SEX = "sex";
	/** 资料完善 - 生日 */
	public static final String COMPLETE_FIELD_BIRTHDAY = "birthday";
	/** 资料完善 - 学历 */
	public static final String COMPLETE_FIELD_EDUCATION = "education";
	/** 资料完善 - 专业 */
	public static final String COMPLETE_FIELD_PROFESSION = "profession";
	/** 资料完善 - 毕业时间 */
	public static final String COMPLETE_FIELD_GRADUATETIME = "graduate_time";
	
	/** 完善资料 - 赠送智米数量 */
	public static final String COMPLETE_GIVEN = "complete.given";
	/** 用户签到 - 赠送智米数量 */
	public static final String SIGN_GIVEN = "sign.given";
	
	/** 微信授权- 作用域 - 拉取用户信息 */
	public static final String SCOPE_USER_INFO = "snsapi_userinfo";
	
	/** 微信授权- 作用域 - 获取用户OpenId */
	public static final String SCOPE_BASE = "snsapi_base";
	
	public static final String APP_NAME = "US";
	
	/** 短信发送开关 */
	public static final String SMS_IS_ON = "sms.is.on";
	
	/** 签到基数 */
	public static final String SIGN_BASE_NUMBER = "sign.base.number";
	
	/** 智米比例系统参数名 */
	public static final String SYS_PARAM_ZHIMI_SCALE = "yz.zhimi.scale";
	
	/** 系统参数 - 微信支付二维码有效时间 */
	public static final String WECHAT_QRCODE_VALIDTIME = "wechat.qrcode.validtime";
	/** 通联支付 代付通讯地址 */
	public static final String ALLINPAY_ROUTE_PAYMENT_URL = "allinpay.route.payment.url";

	/** 系统参数 - 微信支付 调起方式 通联，微信官方 */
	public static final String WECHAT_PAYMENT_INVOKE_TYPE = "wechat.pay.invoke.type";
}
