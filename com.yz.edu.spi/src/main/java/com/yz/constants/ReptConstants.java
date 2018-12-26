package com.yz.constants;

public class ReptConstants {
	
	/** 顺丰订单号 */
	public static final String SF_EXPRESS_MAILNO = "mailno";
	/** 顺丰收件地区编码 */
	public static final String SF_EXPRESS_DESTCODE = "destcode";
	
	/** 收据寄件状态 1-待下单 */
	public static final String REPT_STATUS_UNORDER = "1";
	/** 收据寄件状态 2-待邮寄 */
	public static final String REPT_SEND_STATUS_UNSEND = "2";
	/** 收据寄件状态 3-已邮寄 */
	public static final String REPT_SEND_STATUS_SENDED = "3";
	/** 收据寄件状态 4-现场打印 */
	public static final String REPT_SEND_STATUS_SITE = "4";
	/** 收据寄件状态 5-电子收据 */
	public static final String REPT_SEND_STATUS_PICTURE = "5";
	/** 收据寄件状态 6-快递费待支付 */
	public static final String REPT_SEND_STATUS_UNPAID = "6";
	/** 收据寄件状态 7-自取 */
	public static final String REPT_SEND_STATUS_SELF_PICK = "7";
	/** 收据寄件状态 8-已自取 */
	public static final String REPT_SEND_STATUS_SELF_PICKED = "8";
	
	/** 顺丰响应状态 OK-成功 */
	public static final String SF_RESPONSE_SUCCESS = "OK";
	/** 顺丰响应状态 ERR-错误 */
	public static final String SF_RESPONSE_ERR = "ERR";
	/** 顺丰响应状态 head-请求头 */
	public static final String SF_RESPONSE_HEAD = "Head";
	
	/** 顺丰无响应*/
	public static final String SF_RESPONSE_ERRMSG_NULL = "顺丰无响应";
	/** 顺丰下单成功*/
	public static final String SF_RESPONSE_SUCCESS_MSG = "顺丰下单成功";
	
	/** 京东下单成功*/
	public static final String JD_RESPONSE_SUCCESS_MSG = "京东下单成功";
	
	/** 寄件部门 1-智米中心   */
	public static final String SF_ORDER_SENDER_ZHIMI = "1";
	/** 寄件部门 2-财务部   */
	public static final String SF_ORDER_SENDER_FINANCE = "2";
	/** 寄件部门 3-教学部发书 */
	public static final String SF_ORDER_SENDER_SENDBOOK = "3";
	
	
	/** 收据申请类型  1-电子收据 */
	public static final String REPT_TYPE_PICTURE = "1";
	/** 收据申请类型  2-快递 */
	public static final String REPT_TYPE_EXPRESS = "2";
	/** 收据申请类型  3-自取 */
	public static final String REPT_TYPE_SELF_PICK = "3";
	/** 收据申请类型  4-现场打印 */
	public static final String REPT_TYPE_SITE = "4";
	
	
	/** 收据快递费用系统参数名称 */
	public static final String REPT_EXPRESS_AMOUNT = "yz.rept.amount";
	

}
