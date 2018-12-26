package com.yz.core.security.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yz.constants.GlobalConstants;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.constants.SecurityConstant;
import com.yz.core.security.context.RuleContext;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.edu.trace.TraceTransfer;
import com.yz.exception.BusinessException;
import com.yz.exception.CustomException;
import com.yz.exception.ExceptionCode;
import com.yz.exception.IRpcException;
import com.yz.exception.IRuntimeException;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.BmsFunc;
import com.yz.model.common.PubInfo;
import com.yz.model.common.ReturnInfo;
import com.yz.service.SysErrorMessageService;
import com.yz.trace.sender.TraceSender;
import com.yz.util.OSUtil;
import com.yz.util.StringUtil;

@Aspect
@Component
public class ControllerAspect {

	private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

	@Autowired
	private SysErrorMessageService sysErrorMessageService;

	private String groupId;
	private String saveToken;

	@Value("${oss.file.browser://yzims.oss-cn-shenzhen.aliyuncs.com/}")
	private String file_browser;

	// Controller层切点
	@Pointcut("execution(* com.yz.controller..*.*(..))")
	public void controllerAspect() {
	}

	@Pointcut("execution(* com.yz.core.security.controller.IIndexController.*(..))")
	public void loginAspect() {
	}

	@Around("loginAspect()")
	public Object doLoginAspect(ProceedingJoinPoint pjp) throws Throwable {
		return doAround(pjp);
	}

	@Around("controllerAspect()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		boolean isJson = isJson(pjp);
		StopWatch watch = new StopWatch();
		watch.start();
		TraceTransfer transfer = TraceTransfer.getTrace("bms");
		try {
			transfer.setServiceName(RequestUtil.getRequestUrl());
			transfer.setRemark(transfer.getServiceName());
			transfer.getTraceSpan("bms", pjp.getSignature().getName(), OSUtil.getIp());
			Rule rule = (Rule) getMethodAnnotation(Rule.class, pjp);
			if (rule != null) {
				if (rule.checkLogin()) {
					String sessionId = RequestUtil.getSessionId();
					logger.debug("------------------------- 当前sessionId ： " + sessionId);
					BaseUser user = SessionUtil.getUser();
					if (user != null) {
						logger.debug("------------------------- 当前用户 ： " + user.getRealName() + "[" + user.getUserId()
								+ "]");
						String key = SessionUtil.getKey(user.getUserId());
						if (!StringUtil.hasValue(key)) {
							throw new BusinessException("E000034");// 登录超时或尚未登录
						} else if (!sessionId.equals(key)) {
							throw new BusinessException("E000035");// 异地登录，被挤下线
						}
					} else {
						throw new BusinessException("E000034");// 登录超时或尚未登录
					}

					SessionUtil.freshTime(sessionId, user.getUserId());// 更新session生存时间

					if (rule.checkRule()) {
						boolean allow = false;
						List<BmsFunc> functionList = user.getFuncs();

						if (functionList == null || functionList.isEmpty()) {
							throw new BusinessException("E000036");// 尚未配置权限
						}
						String[] methodRules = rule.value();
						if (methodRules != null && methodRules.length > 0) {
							for (BmsFunc function : functionList) {
								String ruleCode = function.getFuncCode();

								for (String mr : methodRules) {
									if (mr.equals(ruleCode)) {
										RuleContext.setFunctionInfo(function);
										allow = true;
										break;
									}
								}
							}
						}

						if (!allow) {
							throw new BusinessException("E000037");// 权限不足 无法访问
						}

						Token token = (Token) getMethodAnnotation(Token.class, pjp);

						if (token != null) {

							String groupId = this.groupId = token.groupId();

							switch (token.action()) {
							case Save:
								String tokenId = StringUtil.UUID();
								SessionUtil.pushToken(groupId, tokenId);
								RequestUtil.setAttribute(SecurityConstant.WEB_TOKEN, tokenId);
								break;
							case Remove:
								String saveToken = this.saveToken = SessionUtil.pullToken(groupId);
								String webToken = (String) RequestUtil.getParameter(SecurityConstant.WEB_TOKEN);

								if (MediaType.valueOf(RequestUtil.getRequest().getContentType())
										.isCompatibleWith(MediaType.MULTIPART_FORM_DATA)) {
									if (pjp.getArgs() == null || pjp.getArgs().length == 0) {
										throw new CustomException("请在第一个参数上面写上HttpServletRequest 谢谢合作");
									}
									Object arg = pjp.getArgs()[0];
									MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) arg;
									if (StringUtil.isEmpty(webToken)) {
										webToken = mRequest.getParameter(SecurityConstant.WEB_TOKEN);
									}
								}

								if (!StringUtil.hasValue(saveToken) || !saveToken.equals(webToken)) {
									throw new BusinessException("E000038");// 请勿重复提交
								}
								break;
							}
						}
					}

					Object[] args = pjp.getArgs();
					if (args != null) {
						for (Object o : args) {
							if (o instanceof PubInfo) {
								PubInfo p = (PubInfo) o;
								p.setCreateUser(user.getRealName());
								p.setCreateUserId(user.getUserId());
								p.setUpdateUser(user.getRealName());
								p.setUpdateUserId(user.getUserId());
							}
						}
					}
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
			transfer.setIsError(1);
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
				transfer.setErrorCode(rtInfo.getCode());
				SessionUtil.pushToken(groupId, saveToken);
				return rtInfo;
			} else {
				transfer.setErrorCode(ex.getMessage());
				throw ex;
			}
		} finally {
			long destination = watch.getTime();
			transfer.setDestination(destination);
			if(!StringUtil.contains(RequestUtil.getRequestUrl(), "toLogin"))
			{
				TraceSender.sendTrace(transfer);
			}
			TraceTransfer.getTrace().remove();
		}
	}

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
			appErrorMsg = sysErrorMessageService.getErrorMsg(errorCode);
			systemErrorMsg = sysErrorMessageService.getSysErrorMsg(errorCode);
			if (appErrorMsg == null) {
				return processLocalException(ex);
			} else {
				// systemErrorMsg = msgInfo.getSysMsg();
				// appErrorMsg = msgInfo.getAppMsg();
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
								systemErrorMsg = systemErrorMsg.replace(oldChar, newChar);
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

		return new ReturnInfo(errorCode, systemErrorMsg);
	}

}
