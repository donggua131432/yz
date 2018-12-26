package com.yz.constants;

/**
 * 常量-财务模块 
 * @author Administrator
 *
 */
public class FinanceConstants {
	/** 账户类型-现金账户 */
	public static final String ACC_TYPE_NORMAL = "1";
	/** 账户类型-智米 */
	public static final String ACC_TYPE_ZHIMI = "2";
	/** 账户类型-滞留账户 */
	public static final String ACC_TYPE_DEMURRAGE = "3";
	
	/** 账户状态-正常 */
	public static final String ACC_STATUS_NORMAL = "1";
	/** 账户状态-冻结 */
	public static final String ACC_STATUS_FREEZE = "2";
	/** 账户状态-锁定 */
	public static final String ACC_STATUS_LOCK = "3";
	
	/** 公用财务代码  */
	public static final String DEFAULT_FINANCE_CODE = "YZ";
	
	/** 订单状态 - 待支付*/
	public static final String ORDER_STATUS_UNPAID = "1";
	/** 订单状态 - 已支付*/
	public static final String ORDER_STATUS_PAID = "2";
	/** 订单状态 - 已退款*/
	public static final String ORDER_STATUS_REFUND = "3";
	/** 订单状态 - 已废弃*/
	public static final String ORDER_STATUS_WASTE = "4";
	/** 订单状态 - 退款中 */
	public static final String ORDER_STATUS_REFUNDING = "5";
	/** 订单状态 - 付款中*/
	public static final String ORDER_STATUS_PAYING = "6";
	
	/** 账户操作类型 - 进账 */
	public static final String ACC_ACTION_IN = "1";
	/** 账户操作类型 - 出账 */
	public static final String ACC_ACTION_OUT = "2";
	
	/** 账户流水状态 - 处理中 */
	public static final String ACC_SERAIL_STATUS_PROCESSING = "1";
	/** 账户流水状态 - 成功 */
	public static final String ACC_SERAIL_STATUS_SUCCESS = "2";
	/** 账户流水状态 - 失败 */
	public static final String ACC_SERAIL_STATUS_FAILED = "3";
	
	/** 财务科目- Y0 辅导费 */
	public static final String FEE_ITEM_CODE_Y0 = "Y0";
	/** 财务科目- Y1 第一年学费 */
	public static final String FEE_ITEM_CODE_Y1 = "Y1";
	/** 财务科目- Y2 第二年学费 */
	public static final String FEE_ITEM_CODE_Y2 = "Y2";
	/** 财务科目- Y3 第三年学费 */
	public static final String FEE_ITEM_CODE_Y3 = "Y3";
	/** 财务科目- S1 第一年书费 */
	public static final String FEE_ITEM_CODE_S1 = "S1";
	/** 财务科目- S2 第二年书费 */
	public static final String FEE_ITEM_CODE_S2 = "S2";
	/** 财务科目- S3 第三年书费 */
	public static final String FEE_ITEM_CODE_S3 = "S3";
	/** 财务科目- W1 第一年网络费 */
	public static final String FEE_ITEM_CODE_W1 = "W1";
	/** 财务科目- W2 第二年网络费 */
	public static final String FEE_ITEM_CODE_W2 = "W2";
	
	/** 优惠券是否启用- 1-启用*/
	public static final String COUPON_ALLOW = "1";
	/** 优惠券是否启用 - 0-禁用*/
	public static final String COUPON_NOT_ALLOW = "0";
	
	/** 优惠券是否使用- 1-已使用*/
	public static final String COUPON_USED = "1";
	/** 优惠券是否使用 - 0-未使用*/
	public static final String COUPON_UNUSE = "0";
	
	/** 缴费年度 - 1-第一年*/
	public static final String FEE_ITEM_YEAR_FIRST = "1";
	/** 缴费年度 - 2-第二年*/
	public static final String FEE_ITEM_YEAR_SECOND = "2";
	/** 缴费年度 - 3-第三年*/
	public static final String FEE_ITEM_YEAR_THIRD = "3";
	
	
	/** 缴费流水状态 - 1-处理中*/
	public static final String STUDENT_SERIAL_STATUS_PROCESS = "1";
	/** 缴费流水状态 - 2-已完成*/
	public static final String STUDENT_SERIAL_STATUS_FINISHED = "2";
	/** 缴费流水状态 - 3-待审核*/
	public static final String STUDENT_SERIAL_STATUS_UNCHECK = "3";
	
	/** 账户金额单位-人民币*/
	public static final String  ACC_UNIT_RMB = "2";
	/** 账户金额单位-智米*/
	public static final String  ACC_UNIT_ZHIMI = "1";
	
	/** 付款方式- 1-现金支付*/
	public static final String  PAYMENT_TYPE_CASH = "1";
	/** 付款方式- 2-刷卡*/
	public static final String  PAYMENT_TYPE_CARD = "2";
	/** 付款方式- 3-组合支付*/
	public static final String  PAYMENT_TYPE_GROUP = "3";
	/** 付款方式- 4-微信支付*/
	public static final String  PAYMENT_TYPE_WECHAT = "4";
	/** 付款方式- 5-余额支付*/
	public static final String  PAYMENT_TYPE_BALANCE = "5";
	/** 付款方式- 6-滞留账户*/
	public static final String  PAYMENT_TYPE_DELAY = "6";
	/** 付款方式- 7-智米抵扣*/
	public static final String  PAYMENT_TYPE_ZM = "7";
	/** 付款方式- 8-优惠券抵扣*/
	public static final String  PAYMENT_TYPE_COUPON = "8";
	/** 付款方式- 9-现金账户抵扣*/
	public static final String  PAYMENT_TYPE_CASHACCOUNT = "9";
	
	/** 货币单位- 1-智米*/
	public static final String MONEY_UNIT_ZM = "1";
	/** 货币单位- 2-人民币*/
	public static final String MONEY_UNIT_RMB = "2";
	
	/** 是否组合支付- 1-是*/
	public static final String PAYMENT_IS_ASSEMBLY_YES = "1";
	/** 是否组合支付- 0-否*/
	public static final String PAYMENT_IS_ASSEMBLY_NO = "0";
	
	/** 在线支付处理类型  1-学费缴纳 */
	public static final String PAYMENT_DEAL_TYPE_TUITION = "1";
	/** 在线支付处理类型  2-智米充值 */
	public static final String PAYMENT_DEAL_TYPE_ZM_RECHARGE = "2";
	/** 在线支付处理类型  3-企业红包 */
	public static final String PAYMENT_DEAL_TYPE_RED_PACKET = "3";
	/** 在线支付处理类型  4-财务收据缴费 */
	public static final String PAYMENT_DEAL_TYPE_REPT = "4";
	
	/** 学员提现最大金额 */
	public static final String STUDENT_WITHDRAW_MAX_COUNT = "4999";
	/** 学员提现最最小金额 */
	public static final String STUDENT_WITHDRAW_MIN_COUNT = "50";
	
	/** 奖励类型 - 普通奖励 */
	public static final String AWARD_RT_NORMAL = "1";
	
	/** 奖励类型 - 营销活动奖励 */
	public static final String AWARD_RT_MARKTING = "2";
	
	
	/** 支付交易类型tradeType  1-普通支付 */
	public static final String TRADE_TYPE_JSAPI = "JSAPI";
	/** 支付交易类型tradeType  2-二维码支付 */
	public static final String TRADE_TYPE_NATIVE = "NATIVE";
	
	/** 银行卡类型 - 私人 */
	public static final String CARD_TYPE_PRI = "0";
	/** 银行卡类型 - 公司 */
	public static final String CARD_TYPE_PUB = "1";
	/** 银行卡类型 - 信用卡 */
	public static final String CARD_TYPE_CREDIT = "2";
	
	/** 结算状态 - 待结算 */
	public static final String CT_STATUS_NEED = "1";
	/** 结算状态 - 待结算 */
	public static final String CT_STATUS_FINISH = "2";
	/** 结算状态 - 不需结算 */
	public static final String CT_STATUS_WITHOUT = "3";
	
	/** 优惠类型 - 1-减免 */
	public static final String OFFER_DISCOUNT_TYPE_CUT = "1";
	/** 优惠类型 - 2-全免 */
	public static final String OFFER_DISCOUNT_TYPE_FREE = "2";
	/** 优惠类型 - 3-折扣 */
	public static final String OFFER_DISCOUNT_TYPE_DISCOUNT = "3";
	
} 
