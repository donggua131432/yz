package com.yz.pay.constants;

public class PayConstants {

	public static final String UNIONPAY_TRADE_BASE = "unionpay.base.api";
	public static final String WECHAT_TRADE_BASE = "wechat.base.api";
	public static final String ALIPAY_TRADE_BASE = "alipay.base.api";
	public static final String PAY_NOTIFY_ACTION = "trans.notify.url";
	
	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final String SIGN_ALGORITHMS = "SHA1withRSA";
	
	public static final String ALGORITHM = "RSA";
	
	public static final String DEFAULT_MERCHANT_NAME = "default";
	
	public static final String GBK_ENCODING = "GBK";
	
	public static final String CONTENT_TYPE = "Content-type";
	
	public static final String APP_XML_TYPE = "application/xml;charset=utf-8";
	
	public static final String APP_FORM_TYPE = "application/x-www-form-urlencoded;charset=";
	/** 远智教育微信证书ID */
	public static final String YZ_EDUCATION = "yz";
	/** 远智教育微信证书ID */
	public static final String YZ_EDUCATION_TEST = "yztest";
	/** 远智教育微信红包证书ID */
	public static final String YZ_EDUCATION_RED_POCKET = "redpocket";
	
	/** 远智教育通联JSAPI证书ID */
	public static final String YZ_ALLINPAY_JSAPI = "allin_jsapi";
	/** 远智教育通联NATIVE证书ID */
	public static final String YZ_NATIVE_JSAPI = "allin_native";
	/** 通联支付 账户分发 */
	public static final String YZ_ALLNPAY_ROUTE = "allin_route";
	
	public static final String WECHAT_CERTS = "wechat_certs";
	public static final String WECHAT_RED_POCKET_CERTS = "wechat_red_pocket_certs";
	
	/** 远智教育通联学费缴纳证书 */
	public static final String ALLINPAY_JSAPI_CERTS = "allinpay_jsapi_certs";
	/** 远智教育通联智米充值证书 */
	public static final String ALLINPAY_NATIVE_CERTS = "allinpay_native_certs";	
	
	public static final String ALLINPAY_ROUTE_CERTS = "allinpay_route_certs";	
	
	/** 支付方式 1-微信支付 */
	public static final String PAYMENT_TYPE_WECHAT = "1";
	
	/** 通联支付方式   W02-微信JSAPI支付 */
	public static final String ALLINPAY_PAYMENT_TYPE_WECHAT_JSAPI = "W02";
	/** 通联支付方式   W01-微信NATIVE支付 */
	public static final String ALLINPAY_PAYMENT_TYPE_WECHAT_NATIVE = "W01";
	
}
