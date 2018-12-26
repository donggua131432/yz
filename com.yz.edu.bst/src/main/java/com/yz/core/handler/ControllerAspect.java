package com.yz.core.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.yz.service.SysErrorMessageService;
import com.yz.service.SysParameterService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.yz.constants.GlobalConstants;
import com.yz.core.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.core.util.SessionUtil;
import com.yz.exception.BusinessException;
import com.yz.exception.ExceptionCode;
import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.model.system.SysErrorMessage;
import com.yz.util.ExceptionUtil;
import com.yz.util.StringUtil;
import com.yz.vo.LoginUser;
import com.yz.vo.ReturnInfo;

@Aspect
@Component
public class ControllerAspect {

	private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

	@Autowired
	SysErrorMessageService errorMessageService;
  
	@Value("${oss.file.browser://yzims.oss-cn-shenzhen.aliyuncs.com/}")
	private String file_browser;

	// Controller层切点
	@Pointcut("execution(* com.yz.controller..*.*(..))")
	public void controllerAspect() {
	}

	
	@Around("controllerAspect()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		boolean isJson = isJson(pjp);

		try {
			Rule rule = (Rule) getMethodAnnotation(Rule.class, pjp);

			if (rule != null) {
				if (rule.checkLogin()) {
					String sessionId = RequestUtil.getSessionId();
					logger.debug("------------------------- 当前sessionId ： " + sessionId);
					LoginUser user = SessionUtil.getUser();
					if (user != null) {
						logger.debug("------------------------- 当前用户 ： " + user.getUsername() + "[" + user.getStdId()
								+ "]");
						String key = SessionUtil.getKey(user.getStdId());
						if (!StringUtil.hasValue(key)) {
							throw new BusinessException("E000034");// 登录超时或尚未登录
						} else if (!sessionId.equals(key)) {
							throw new BusinessException("E000035");// 异地登录，被挤下线
						}
					} else {
						throw new BusinessException("E000034");// 登录超时或尚未登录
					}

					SessionUtil.freshTime(sessionId, user.getStdId());// 更新session生存时间

				}
			}
			Object result = pjp.proceed();

			if (isJson) {
				return new ReturnInfo(GlobalConstants.SUCCESS_CODE, "", result);
			} else {
				RequestUtil.setAttribute("_file_browser", file_browser);
			}
			return result;
		} catch (Exception ex) {

			if (isJson) {
				ReturnInfo rtInfo = null;
				if (ex instanceof IRpcException) {
					IRpcException rpcException = (IRpcException) ex;
					rtInfo = processRpcException(rpcException.getTarget());
				} else if (ex instanceof IRuntimeException) {
					rtInfo = processRpcException(ex);
				} else {
					rtInfo = processLocalException(ex);
				}
				return rtInfo;
			} else {
				if (ex instanceof BusinessException) {
					BusinessException be = (BusinessException) ex;
					if(be.getErrCode().equals("E000034")||be.getErrCode().equals("E000035")) {
						return "toLogin";
					}else {
						throw ex;
					}
				}else {
					throw ex;
				}
				
			}
		}
	}

	

	@AfterThrowing(pointcut = "controllerAspect()", throwing = "ex")
	public void afterThrow(JoinPoint joinPoint, Exception ex) {
//		SysLogInfoWithBLOBs log = LogContext.getCurrentLogInfo();
//		if (log != null) {
//			LogContext.clear();
//			log.setExceptionDetail(ExceptionUtil.getStackTrace(ex));
//			log.setLogStatus("2");// 失败
//			BmsFunc function = RuleContext.getFunctionInfo();
//			if (function != null) {
//				log.setFncName(function.getFuncName());
//			}
//
//			logService.insert(log);
//		}
//		
		System.out.println(ExceptionUtil.getStackTrace(ex));
	}


	@AfterReturning("controllerAspect()")
	public void afterReturning() {
//		SysLogInfoWithBLOBs log = LogContext.getCurrentLogInfo();
//		if (log != null) {
//			LogContext.clear();
//			BmsFunc function = RuleContext.getFunctionInfo();
//			if (function != null) {
//				log.setFncName(function.getFuncName());
//			}
//			log.setLogStatus("1");// 成功
//			logService.insert(log);
//		}
	}

//	/**
//	 * log对象赋值
//	 * 
//	 * @param pjp
//	 */
//	private void initLogParam(ProceedingJoinPoint pjp) {
//		// 获取输入参数
//		Map<?, ?> inputParamMap = RequestUtil.getParamMap();
//		// 获取请求地址
//		String requestPath = RequestUtil.getRequestUrl();
//
//		SysLogInfoWithBLOBs log = new SysLogInfoWithBLOBs();
//
//		log.setRequestPath(requestPath);
//
//		log.setInParam(JsonUtil.object2String(inputParamMap));
//
//		log.setMethod(pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName() + "()");
//
//		log.setLogStatus("1");// 成功
//
//		String ip = RequestUtil.getIp();
//
//		log.setIp(ip);
//		log.setMac(RequestUtil.getMac(ip));
//		log.setExceptionDetail(null);
//
//		BaseUser user = SessionUtil.getUser();
//
//		if (null != user) {
//
//			String userId = user.getUserId();
//			log.setUserId(userId);
//			log.setUserName(user.getRealName());
//		}
//
//		LogContext.setCurrentLogInfo(log);
//	}

	private boolean isJson(ProceedingJoinPoint pjp) throws Throwable {
		ResponseBody responseBody = (ResponseBody) getAnnotation(ResponseBody.class, pjp);
		RestController restController = (RestController) getClassAnnotation(RestController.class, pjp);

		return responseBody != null || restController != null;
	}

	/**
	 * 获取方法注解
	 * 
	 * @param an
	 * @param pjp
	 * @return
	 */
	private Annotation getMethodAnnotation(Class<? extends Annotation> an, ProceedingJoinPoint pjp) {
		// 获取执行的方法
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();

		return method.getAnnotation(an);
	}

	/**
	 * 
	 * @param an
	 * @param pjp
	 * @return
	 */
	private Annotation getClassAnnotation(Class<? extends Annotation> an, ProceedingJoinPoint pjp) {
		// 获取执行的方法
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();

		return method.getDeclaringClass().getAnnotation(an);
	}

	private Annotation getAnnotation(Class<? extends Annotation> an, ProceedingJoinPoint pjp) {
		Annotation a = getMethodAnnotation(an, pjp);
		return a == null ? getClassAnnotation(an, pjp) : a;
	}

	/**
	 * 处理本地异常
	 * 
	 * @param ex
	 * @return
	 */
	private ReturnInfo processLocalException(Exception ex) {
		String systemErrorMsg = ex == null ? "未知异常" : ex.getMessage();

		logger.error(systemErrorMsg, ex);

		String appErrorMsg = systemErrorMsg;// ExceptionCode.ERROR_MESSAGE.get(ExceptionCode.SYSYTEM_ERROR);开发用

		return new ReturnInfo(ExceptionCode.SYSYTEM_ERROR.getCode(), appErrorMsg);
	}

	/**
	 * 处理远程调用异常
	 * 
	 * @param rpcException
	 * @return
	 */
	private ReturnInfo processRpcException(Exception ex) {
		String errorCode = null;
		String systemErrorMsg = null;
		String appErrorMsg = null;

		if (ex instanceof BusinessException) {
			BusinessException be = (BusinessException) ex;
			errorCode = be.getErrCode();
			SysErrorMessage msgInfo = errorMessageService.getErrorMsg(errorCode);
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
			systemErrorMsg = appErrorMsg = me.getErrMessage();
			errorCode = ExceptionCode.SYSYTEM_ERROR.getCode();
		} else {
			return processLocalException(ex);
		}

		logger.error(systemErrorMsg, ex);

		return new ReturnInfo(errorCode, appErrorMsg);
	}

}
