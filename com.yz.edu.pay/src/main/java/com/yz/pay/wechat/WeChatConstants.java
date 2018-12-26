package com.yz.pay.wechat;

public class WeChatConstants {
	/**设备号*/
	public static final String param_device_info = "device_info";
	/** 应用ID */
	public static final String param_appid = "appid";
	/** 商户号 */
	public static final String param_mch_id = "mch_id";
	/**随机字符串*/
	public static final String param_nonce_str = "nonce_str";
	/**签名*/
	public static final String param_sign = "sign";
	/**商品描述*/
	public static final String param_body = "body";
	/**商品详情*/
	public static final String param_detail = "detail";
	/**附加数据*/
	public static final String param_attach = "attach";
	/**商户订单号*/
	public static final String param_out_trade_no = "out_trade_no";
	/**货币类型*/
	public static final String param_fee_type = "fee_type";
	/**总金额*/
	public static final String param_total_fee = "total_fee";
	/**终端IP*/
	public static final String param_spbill_create_ip = "spbill_create_ip";
	/**交易起始时间*/
	public static final String param_time_start = "time_start";
	/**交易结束时间*/
	public static final String param_time_expire = "time_expire";
	/**商品标记*/
	public static final String param_goods_tag = "goods_tag";
	/**通知地址*/
	public static final String param_notify_url = "notify_url";
	/**交易类型*/
	public static final String param_trade_type = "trade_type";
	/**指定支付方式*/
	public static final String param_limit_pay = "limit_pay";
	/**用户标识*/
	public static final String param_open_id = "openid";
	/** 通讯返回码 */
	public static final String resp_result_code = "result_code";
	/** 返回信息 */
	public static final String resp_return_msg = "return_msg";
	/** 预支付交易会话ID */
	public static final String resp_prepay_id = "prepay_id";
	/** 商户ID*/
	public static final String param_partnerid = "partnerid";
	/** 预支付交易会话ID */
	public static final String param_prepayid = "prepayid";
	/** 扩展字段 */
	public static final String param_package = "package";
	/** 时间戳 */
	public static final String param_timestamp = "timestamp";
	/** 加密方法 */
	public static final String param_sign_type = "signType";
	/** 扫码支付*/
	public static final String param_product_id = "product_id";
	/** 支付二维码*/
	public static final String param_code_url = "code_url";
	
	//=========================================================================
	//微信红包参数
	/** 微信红包公众号APPID */
	public static final String param_wxappid = "wxappid";
	/** 微信红包商户订单号 */
	public static final String param_mch_billno = "mch_billno";
	/** 微信红包红包发送者名称 */
	public static final String param_send_name = "send_name";
	/** 微信红包红包接收者OPENID */
	public static final String param_re_openid = "re_openid";
	/** 微信红包红包金额 */
	public static final String param_total_amount = "total_amount";
	/** 微信红包数量 */
	public static final String param_total_num = "total_num";
	/** 微信红包祝福语*/
	public static final String param_wishing = "wishing";
	/** 微信红包发送者IP*/
	public static final String param_client_ip = "client_ip";
	/** 微信红包活动名称*/
	public static final String param_act_name = "act_name";
	/** 微信红包活动类型*/
	public static final String param_scene_id = "scene_id";
	
	
	//=========================================================================
	
	/** 设备信息*/
	public static final String WEB_SERVER = "WEB";
	/** 人民币*/
	public static final String value_rmb = "CNY";
	/** 通讯成功*/
	public static final String SUCCESS = "SUCCESS";
	/** 通讯失败*/
	public static final String FAILED = "FAILED";
	/** 固定扩展字段 */
	public static final String PACKAGE = "Sign=WXPay";
	
	public static final String TRADE_TYPE = "JSAPI";
	
	
	/** 付款状态 1-处理中 */
	public static final String PAY_STATUS_DEALING = "1";
	/** 付款状态2-完成 */
	public static final String PAY_STATUS_FINISHED = "2";
	/** 付款状态 3-失败*/
	public static final String PAY_STATUS_FAILED = "3";
	
	/** 微信api请求结果 0-成功*/
	public static final String WECHAT_POST_STATUS_SUCCESS = "0";
	
	public static final String param_pay_sign = "paySign";
}
