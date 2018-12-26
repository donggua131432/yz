package com.yz.exception;

public class FieldCheckException extends IRuntimeException {

	private static final String NAME = "字段校验异常";

	/**
	 * 
	 */
	private static final long serialVersionUID = -3782479028806993497L;
	
	public FieldCheckException(String message, Throwable e) {
		super(NAME, ExceptionCode.CUSTOM_ERROR.getCode(), message, e);
	}
	
	public FieldCheckException(String message) {
		this(message, null);
	}
}
