package com.yz.exception;

public class IRpcException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7834197997660978216L;

	private Exception target;

	public IRpcException(Exception target) {
		super(target);
		this.target = target;
	}

	public IRpcException(String errorMessage, Exception target) {
		super(errorMessage, target);
		this.target = target;
	}

	/**
	 * @return the target
	 */
	public Exception getTarget() {
		return target;
	}
}
