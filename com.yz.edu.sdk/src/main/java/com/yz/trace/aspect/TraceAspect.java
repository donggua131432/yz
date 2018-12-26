package com.yz.trace.aspect;

import java.lang.reflect.Method;
import java.util.Date;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.annotation.YzTrace;
import com.yz.util.OSUtil;

@Aspect
/**
 * 
 * @desc service方法级别监控 自定义在相应方法体加上trace信息
 * @author lingdian
 *
 */
@Component
public class TraceAspect {

	private static final Logger logger = LoggerFactory.getLogger(TraceAspect.class);

	public TraceAspect() {

	}

	@Value("${dubbo.application:yzApp}")
	private String appName;

	@Pointcut(value = "@annotation(com.yz.edu.trace.annotation.YzTrace)")
	public void traceAspect() {
	}

	@Around("traceAspect()")
	public Object beginTrace(ProceedingJoinPoint pjp) throws Throwable {
		return invoke(pjp);
	}

	/**
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	private Object invoke(ProceedingJoinPoint pjp) throws Throwable {
		// 获取执行的方法
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		YzTrace trace = method.getAnnotation(YzTrace.class);
		if (trace != null) {
			logger.info("beginTrace.remark:{}", trace.remark());
			TraceSpan span = TraceTransfer.getTrace().getTraceSpan(appName, method.getName(), OSUtil.getIp());
			span.setRemark(trace.remark());
		}
		return pjp.proceed();
	}

	/**
	 * 
	 * @param jp
	 */
	private void afterInvoker(JoinPoint jp) {
		// 获取执行的方法
		String name = jp.getSignature().getName();
		TraceSpan span = TraceTransfer.getTrace().getTraceSpan(name);
		if (span != null) {
			logger.info("doTrace.remark:{}", span.getRemark());
			span.setDestination((new Date().getTime() - span.getDate().getTime()) / 1000);
			TraceTransfer.getTrace().setParentTraceSpanCurrent(span);
		}
	}

	@AfterReturning("traceAspect()")
	/**
	 * @desc 执行完毕相应的方法体
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public void afterTraceAspect(JoinPoint jp) throws Throwable {
		// 获取执行的方法
		afterInvoker(jp);
	}

	@AfterThrowing(pointcut = "traceAspect()", throwing = "e")
	public void doAfterThrowing(JoinPoint jp, Throwable e) {
		// 获取执行的方法
		String name = jp.getSignature().getName();
		TraceSpan span = TraceTransfer.getTrace().getTraceSpan(name);
		if (span != null) {
			span.setDestination((new Date().getTime() - span.getDate().getTime()) / 1000);
			span.setIsError(1);
			span.setErrorCode(e.getMessage());
			TraceTransfer.getTrace().setParentTraceSpanCurrent(span);
			logger.info("doTrace.remark:{}", span.getRemark());
		}
	}

}
