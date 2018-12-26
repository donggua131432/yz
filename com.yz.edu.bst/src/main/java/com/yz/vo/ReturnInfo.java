package com.yz.vo;

import java.io.Serializable;

public class ReturnInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3988381038549725335L;

	public ReturnInfo() {
		super();
	}

	public ReturnInfo(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public ReturnInfo(String code, String msg, Object body) {
		this(code, msg);
		this.body = body;
	}

	/**
	 * 错误编码
	 */
	private String code = "";
	/**
	 *  返回对象
	 */
	private Object body = null;
	/**
	 * 错误信息
	 */
	private String msg = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
