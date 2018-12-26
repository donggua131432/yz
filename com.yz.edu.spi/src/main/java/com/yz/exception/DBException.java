package com.yz.exception;

public class DBException extends IRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2024764604915305502L;
	
	private static final String NAME = "数据库操作异常";


	public DBException(String errMessage, Throwable e) {
		super(NAME, ExceptionCode.DB_ERROR.getCode(), errMessage, e);
	}
	
	public DBException(String errMessage) {
		super(NAME, ExceptionCode.DB_ERROR.getCode(), errMessage, null);
	}
	
	public DBException() {
		super(NAME, ExceptionCode.DB_ERROR.getCode());
	}
}
