package com.yz.controller;

import java.io.IOException; 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.constants.AppConstants;
import com.yz.core.handler.CheckHandlerChain;
import com.yz.core.util.RequestUtil; 
import com.yz.core.util.ValidCodeUtil;
import com.yz.exception.BusinessException;
import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.executor.YzServiceExecutor;
import com.yz.model.SysErrorMessage;
import com.yz.model.YzServiceInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.model.communi.Request;
import com.yz.model.communi.Response;
import com.yz.redis.RedisService;
import com.yz.service.SysErrorMessageService;
import com.yz.util.Assert;
import com.yz.util.ExceptionUtil;
import com.yz.util.StringUtil;

@Controller
public class ProxyController {

	private static final Logger logger = LoggerFactory.getLogger(ProxyController.class);

	@Autowired
	private YzServiceExecutor serviceExecutor;
	
	@Autowired
	private CheckHandlerChain handlerChain;
	
	@Autowired
	private SysErrorMessageService sysErrorService;

	public ProxyController() {
	}

	/**
	 * 接口路由方法 调用接口制定的系统
	 * 
	 * @param interfaceName
	 * @param request
	 * @param servletRequest
	 * @return
	 * @throws MxdRuntimeException
	 */
	@RequestMapping(value = "{sysBelong}/{interfaceName}/{interfaceVersion}/")
	@ResponseBody
	public Object route(@PathVariable String sysBelong, @PathVariable String interfaceName,
			@PathVariable String interfaceVersion, @RequestBody Request request, HttpServletRequest servletRequest)
			throws Throwable {

		Header header = request.getHeader();
		Body body = request.getBody();

		Assert.hasText(sysBelong, "所属系统");
		Assert.hasText(interfaceName, "接口名称不能为空");
		Assert.hasText(interfaceVersion, "接口版本号不能为空不能为空");

		logger.debug("---------------------请求接口 ： " + sysBelong + "-" + interfaceName + "-" + interfaceVersion);
		YzServiceInfo interfaceInfo = serviceExecutor.getInterfaceInfo(sysBelong,interfaceName, interfaceVersion);
		if (interfaceInfo == null) {
			return new Response( "E00006","["+sysBelong +"."+ interfaceName + "." + interfaceVersion + "]" + " 接口尚未发布");
		}

		// 检测
		handlerChain.check(interfaceInfo, request, servletRequest);

		Object result = null;
		try {
			result = serviceExecutor.execute(interfaceInfo, header, body);
		} catch (Throwable e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			throw e;
		}
		return new Response(result);
	}

	/**
	 * 验证码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping("/captcha")
	public void getCaptcha(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String mobile = RequestUtil.getString("mobile");
		String captcha = ValidCodeUtil.createValidCode(response).toLowerCase();

		if (StringUtil.hasValue(mobile)) {
			RedisService.getRedisService().set(mobile, captcha);
		} else {
			RedisService.getRedisService().set(captcha, String.valueOf(System.currentTimeMillis()));
		}
	}

	/**
	 * 统一异常处理
	 * 
	 * @param req
	 * @param resp
	 * @param handler
	 * @param ex
	 * @return
	 */
	@ExceptionHandler
	@ResponseBody
	public Object systemExceptionHandler(HttpServletRequest req, HttpServletResponse resp, Object handler,
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
	private Response processLocalException(Exception ex) {
		String errorCode = null;
		String systemErrorMsg = ex == null ? "未知异常" : ex.getMessage();
		SysErrorMessage msgInfo = sysErrorService.getErrorMsg(errorCode = AppConstants.GLOBAL_EXCEPTION_CODE);
		String appErrorMsg = msgInfo.getAppMsg();
		logger.error(systemErrorMsg, ex);
		return new Response(errorCode, appErrorMsg);
	}

	/**
	 * 处理远程调用异常
	 * 
	 * @param rpcException
	 * @return
	 */
	private Response processRpcException(Exception ex) {
		String errorCode = null;
		String systemErrorMsg = null;
		String appErrorMsg = null;
		SysErrorMessage msgInfo = null;

		if (ex instanceof BusinessException) {
			BusinessException be = (BusinessException) ex;
			errorCode = be.getErrCode();
		    msgInfo = sysErrorService.getErrorMsg(errorCode);
			if (msgInfo == null) {
				return processLocalException(ex);
			} else {
				systemErrorMsg = msgInfo.getSysMsg();
				appErrorMsg = msgInfo.getAppMsg();
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
			errorCode = me.getErrCode();
			msgInfo = sysErrorService.getErrorMsg(errorCode);
			appErrorMsg = msgInfo.getAppMsg();
		} else {
			return processLocalException(ex);
		}

		logger.error(systemErrorMsg, ex);

		return new Response(errorCode, appErrorMsg);
	}

}
