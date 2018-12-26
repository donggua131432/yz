package com.yz.model.communi;

import java.io.Serializable;

public class Response implements Serializable, Communication {

	/**
	 * 
	 */
	private static final long serialVersionUID = 915221070217844669L;

	private Object body;

	private String code = "00";
	private String message = "success";

	public Response() {
	}

	public Response(String errorCode, String errorMessage) {
		this.code = errorCode;
		this.message = errorMessage;
	}

	public Response(Object body) {
		
		if (body != null)
			this.body = body;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
