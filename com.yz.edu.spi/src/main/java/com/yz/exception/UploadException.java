package com.yz.exception;

public class UploadException extends IRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3101978542423406932L;

	private static final String NAME = "文件上传异常";

	public UploadException(String errMessage, Throwable e) {
		super(NAME, ExceptionCode.UPLOAD_ERROR.getCode(), errMessage, e);
	}

	public UploadException(String errMessage) {
		super(NAME, ExceptionCode.UPLOAD_ERROR.getCode(), errMessage, null);
	}

	public UploadException() {
		super(NAME, ExceptionCode.UPLOAD_ERROR.getCode());
	}
}
