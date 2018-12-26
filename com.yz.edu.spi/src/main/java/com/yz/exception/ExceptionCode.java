package com.yz.exception;

import java.util.HashMap;
import java.util.Map;

public enum ExceptionCode {
	
	SYSYTEM_ERROR("E000001"),
	OTHER_ERROR("E000002"),
	DB_ERROR("E000003"),
	CACHE_ERROR("E000004"),
	AUTH_ERROR("E000005"),
	PAY_ERROR("EPAY0001"),
	PAGE_NOT_FOUND("HTTP404"),
	FORBIDDEN("HTTP403"),
	BAD_GATE_WAY("HTTP502"),
	SERVER_ERROR("HTTP500"),
	TIME_OUT("HTTP504"),
	UPLOAD_ERROR("UE00001"),
	UNDEFINED_ERROR("UNDEFINED"),
	CUSTOM_ERROR("CUSTOMER");
	
	private String code;
	private ExceptionCode(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public static final Map<String, String> ERROR_MESSAGE = new HashMap<String, String>();
	
	static {
		ERROR_MESSAGE.put(SYSYTEM_ERROR.getCode(), "系统错误");
		ERROR_MESSAGE.put(OTHER_ERROR.getCode(), "未知错误");
		ERROR_MESSAGE.put(DB_ERROR.getCode(), "数据操作错误");
		ERROR_MESSAGE.put(PAY_ERROR.getCode(), "支付异常");
		ERROR_MESSAGE.put(CACHE_ERROR.getCode(), "缓存操作错误");
		ERROR_MESSAGE.put(PAGE_NOT_FOUND.getCode(), "接口不存在");
		ERROR_MESSAGE.put(FORBIDDEN.getCode(), "访问被拒绝");
		ERROR_MESSAGE.put(BAD_GATE_WAY.getCode(), "服务器无响应");
		ERROR_MESSAGE.put(SERVER_ERROR.getCode(), "系统错误");
		ERROR_MESSAGE.put(TIME_OUT.getCode(), "访问超时");
		ERROR_MESSAGE.put(UPLOAD_ERROR.getCode(), "文件上传失败");
		ERROR_MESSAGE.put(UNDEFINED_ERROR.getCode(), "异常未定义");
		ERROR_MESSAGE.put(CUSTOM_ERROR.getCode(), "自定义异常");
		ERROR_MESSAGE.put(AUTH_ERROR.getCode(), "权限不足");
	}
}
