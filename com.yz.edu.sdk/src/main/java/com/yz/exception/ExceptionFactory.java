package com.yz.exception;

import com.yz.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 */
public class ExceptionFactory {

	private Logger logger = LoggerFactory.getLogger(ExceptionFactory.class);

	private static class ExceptionFactoryHolder {
		private static ExceptionFactory _instance = new ExceptionFactory();
	}

	private ExceptionFactory() {
	}

	public static ExceptionFactory getInstance() {
		return ExceptionFactoryHolder._instance;
	}

	/**
	 *
	 * @param clsName
	 * @param message
	 * @return
	 */
	public IRuntimeException getRuntimeException(String clsName, String message) {
		switch (clsName) {
		case "com.yz.exception.IRpcException":
		case "com.yz.exception.BusinessException":
			return createBusinessException(message);
		case "com.yz.exception.CustomException":
			return createCustomException(message);
		case "java.lang.RuntimeException":
			return new IRuntimeException("sys",ExceptionCode.SYSYTEM_ERROR);
		default:
			logger.error("clsName not support !"); // 未支持异常，返回全局统一的错误代码
			return new IRuntimeException("unkonw",ExceptionCode.SYSYTEM_ERROR);
		}
	}

	/**
	 * @desc 
	 * \\:\\s*[a-zA-Z\\.0-9\\_\\-\\?\\*\\/]+\\s* a-zA-Z\\.0-9\\_\\-\\?\\*\\/
	 */
	private IRuntimeException createBusinessException(String message) {
		String errorCode = StringUtil.substringBetween(message, "[", "]");
		if (StringUtil.startsWith(errorCode, "E")) {
			return new BusinessException(errorCode);
		} else {
			return createCustomException(message);
		}
	}

	/**
	 * @desc 创建自定义客户异常 
	 * @param message
	 * @return
	 */
	private CustomException createCustomException(String message) {
		CustomException ex = new CustomException(StringUtil.substringAfter(message, ": "));
		return ex;
	}
}
