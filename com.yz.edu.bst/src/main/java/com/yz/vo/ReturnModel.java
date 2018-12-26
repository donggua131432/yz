package com.yz.vo;

import java.io.Serializable;
import com.yz.enums.ReturnStatus;



/**
 * 返回类 模型
 * @author Administrator
 *
 */
public class ReturnModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ReturnStatus code=ReturnStatus.LoginError;
	private String msg = "";
	private Object basemodle =null;
	
	
	public ReturnModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReturnModel(ReturnStatus statu, String statusReson, Object baseModle) {
		super();
		code = statu;
		msg = statusReson;
		basemodle = baseModle;
	}
	public ReturnStatus getCode() {
		return code;
	}
	public void setCode(ReturnStatus code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getBasemodle() {
		return basemodle;
	}
	public void setBasemodle(Object basemodle) {
		this.basemodle = basemodle;
	}
	
	
}
