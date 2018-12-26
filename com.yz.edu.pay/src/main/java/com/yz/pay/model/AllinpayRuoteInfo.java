package com.yz.pay.model;

import java.util.LinkedHashMap; 

public class AllinpayRuoteInfo {

	private LinkedHashMap<String, String> INFO = new LinkedHashMap<String, String>();

	private LinkedHashMap<String, String> TRANS = new LinkedHashMap<String, String>();

	/* ========================== info start ============================= */
	/**
	 * 交易代码
	 * @param TRX_CODE
	 */
	public void set_TRX_CODE(String TRX_CODE) {
		INFO.put("TRX_CODE", TRX_CODE);
	}
	/**版本*/
	public void set_VERSION(String VERSION) {
		INFO.put("VERSION", VERSION);
	}
	/**数据格式*/
	public void set_DATA_TYPE(String DATA_TYPE) {
		INFO.put("DATA_TYPE", DATA_TYPE);
	}
	/**处理级别*/
	public void set_LEVEL(String LEVEL) {
		INFO.put("LEVEL", LEVEL);
	}
	/**用户名*/
	public void set_USER_NAME(String USER_NAME) {
		INFO.put("USER_NAME", USER_NAME);
	}
	/**用户密码*/
	public void set_USER_PASS(String USER_PASS) {
		INFO.put("USER_PASS", USER_PASS);
	}
	/** 交易流水号 */
	public void set_REQ_SN(String REQ_SN) {
		INFO.put("REQ_SN", REQ_SN);
	}
	/**签名信息*/
	public void set_SIGNED_MSG(String SIGNED_MSG) {
		INFO.put("SIGNED_MSG", SIGNED_MSG);
	}
    /* ============================ info end trans start ============================== */
	/**业务代码*/
	public void set_BUSINESS_CODE(String BUSINESS_CODE) {
		TRANS.put("BUSINESS_CODE", BUSINESS_CODE);
	}
	/**商户代码*/
	public void set_MERCHANT_ID(String MERCHANT_ID) {
		TRANS.put("MERCHANT_ID", MERCHANT_ID);
	}
	/**提交时间*/
	public void set_SUBMIT_TIME(String SUBMIT_TIME) {
		TRANS.put("SUBMIT_TIME", SUBMIT_TIME);
	}
	/**账号：银行卡或存折号码*/
	public void set_ACCOUNT_NO(String ACCOUNT_NO) {
		TRANS.put("ACCOUNT_NO", ACCOUNT_NO);
	}
	/**账号名：银行卡或存折上的所有人姓名*/
	public void set_ACCOUNT_NAME(String ACCOUNT_NAME) {
		TRANS.put("ACCOUNT_NAME", ACCOUNT_NAME);
	}
	/** 账号属性: 0-私人，1-公司 */
	public void set_ACCOUNT_PROP(String ACCOUNT_PROP) {
		TRANS.put("ACCOUNT_PROP", ACCOUNT_PROP);
	}
	/** 金额 单位为分 */
	public void set_AMOUNT(String AMOUNT) {
		TRANS.put("AMOUNT", AMOUNT);
	}
	
	public LinkedHashMap<String, String> getInfo() {
		return INFO;
	}
	
	public LinkedHashMap<String, String> getTrans() {
		return TRANS;
	}
	
}
