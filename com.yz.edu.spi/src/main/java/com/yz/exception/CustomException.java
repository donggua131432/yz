package com.yz.exception;

public class CustomException extends IRuntimeException {
	
	private static final String NAME = "自定义异常";

	/**
	 * 
	 */
	private static final long serialVersionUID = -3782479028806993497L;
	
	public CustomException(String message, Throwable e) {
		super(NAME, ExceptionCode.CUSTOM_ERROR.getCode(), message, e);
	}
	
	public CustomException(String message) {
		this(message, null);
	}

}
