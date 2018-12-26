package com.yz.exception;

/**
 * 业务异常 - 业务层(service)使用
 * @author Administrator
 *
 */
public class BusinessException extends IRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 362170708888091067L;
	
	private static final String NAME = "业务异常";
	
	private String[] values;

	public BusinessException(String errCode, String errMessage, Throwable e, String[] values) {
		super(NAME, errCode, errMessage, e);
		this.values = values;
	}
	
	public BusinessException(String errCode, String errMessage, String[] values) {
		this(errCode, errMessage, null, values);
	}
	
	public BusinessException(String errCode, String[] values) {
		this(errCode, null, values);
	}
	
	public BusinessException(String errCode) {
		super(NAME, errCode);	
	}

	public String[] getValues() {
		return values;
	}

}
