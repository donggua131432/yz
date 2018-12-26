package com.yz.core.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.rpc.RpcException;
import com.yz.exception.IRpcException;
import com.yz.exception.SystemException;

@Aspect
@Component
public class ExceptionPointCut {
	
	private static final Logger log = LoggerFactory.getLogger(ExceptionPointCut.class);

	@Pointcut("execution(* com.yz.service.*.*(..))")
	public void execService() {
	}

	@Around("execService()")
	public Object introcepter(ProceedingJoinPoint pjp) throws Throwable {
		try {
			return pjp.proceed();
		} catch (RpcException e) {
			log.error(e.getMessage(), e);
			throw new IRpcException(e.getMessage(), new SystemException("接口未发布", e));
		} catch(IRpcException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new IRpcException(e.getMessage(), e);
		}
	}
}
