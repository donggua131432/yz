package com.yz.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
 
import com.yz.exception.BusinessException;
import com.yz.exception.ExceptionCode;
import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.service.SysErrorMessageService;
import com.yz.util.StringUtil;

/**
 * 
 * Description: 全局异常处理器
 * 
 * Author: 倪宇鹏 Version: 1.0 Create Date Time: 2017年5月5日 下午12:26:07.
 *
 */
@Component
public class ExceptionHandler implements HandlerExceptionResolver {
	
	@Autowired
	private SysErrorMessageService sysErrorMessageService;

	private final static Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ModelAndView mv = systemExceptionHandler(request, response, handler, ex);
		mv.setViewName("pub/error");
		return mv;
	}

	public ModelAndView systemExceptionHandler(HttpServletRequest req, HttpServletResponse resp, Object handler,
			Exception ex) {

		if (ex == null) {
			return processLocalException(ex);
		} else if (ex instanceof IRpcException) {
			IRpcException rpcException = (IRpcException) ex;
			return processRpcException(rpcException.getTarget());
		} else if (ex instanceof IRuntimeException) {
			return processRpcException(ex);
		} else {
			return processLocalException(ex);
		}
	}

	/**
	 * 处理本地异常
	 * 
	 * @param ex
	 * @return
	 */
	private ModelAndView processLocalException(Exception ex) {
		String systemErrorMsg = ex == null ? "未知异常" : ex.getMessage();

		String appErrorMsg = systemErrorMsg;// ExceptionCode.ERROR_MESSAGE.get(ExceptionCode.SYSYTEM_ERROR);开发用
		log.error(systemErrorMsg, ex);
		ModelAndView mav = new ModelAndView();

		mav.addObject("errorCode", ExceptionCode.SYSYTEM_ERROR.getCode());
		mav.addObject("errorMsg", appErrorMsg);

		return mav;
	}

	/**
	 * 处理远程调用异常
	 * 
	 * @param rpcException
	 * @return
	 */
	private ModelAndView processRpcException(Exception ex) {
		String errorCode = null;
		String systemErrorMsg = null;
		String appErrorMsg = null;

		ModelAndView mav = new ModelAndView();

		if (ex instanceof BusinessException) {
			BusinessException be = (BusinessException) ex;
			errorCode = be.getErrCode();
			appErrorMsg = sysErrorMessageService.getErrorMsg(errorCode);
			systemErrorMsg= sysErrorMessageService.getSysErrorMsg(errorCode);
			if (appErrorMsg == null||systemErrorMsg==null) {
				return processLocalException(ex);
			} else {
				//systemErrorMsg = msgInfo.getSysMsg();
				//appErrorMsg = msgInfo.getAppMsg();
				String[] values = be.getValues();
				if (values != null && values.length > 0) {
					for (int i = 0; i < values.length; i++) {
						String oldChar = "{" + i + "}";
						String newChar = values[i];
						if (StringUtil.hasValue(newChar)) {
							if (appErrorMsg.indexOf(oldChar) > -1) {
								appErrorMsg = appErrorMsg.replace(oldChar, newChar);
							}
							if (systemErrorMsg.indexOf(oldChar) > -1) {
								systemErrorMsg = systemErrorMsg.replaceAll(oldChar, newChar);
							}
						}
					}
				}
			}
		} else if (ex instanceof IRuntimeException) {
			IRuntimeException me = (IRuntimeException) ex;
			systemErrorMsg = appErrorMsg = me.getErrMessage();
			errorCode = ExceptionCode.SYSYTEM_ERROR.getCode();
		} else {
			return processLocalException(ex);
		}

		mav.addObject("errorCode", errorCode);
		mav.addObject("errorMsg", appErrorMsg);

		log.error(systemErrorMsg, ex);

		return mav;
	}

}
