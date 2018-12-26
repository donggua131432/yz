package com.yz.executor;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.rpc.service.GenericException;
import com.yz.cache.handler.RedisCacheHandler;
import com.yz.cache.rule.RedisCacheRule;
import com.yz.cache.rule.RedisCacheRuleFactory;
import com.yz.edu.trace.TraceTransfer;
import com.yz.exception.CustomException;
import com.yz.exception.IRpcException;
import com.yz.model.YzServiceInfo;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;
import com.yz.trace.sender.TraceSender;
import com.yz.util.ApplicationContextUtil;
import com.yz.util.ExceptionUtil;
import com.yz.util.OSUtil;

/**
 * @author Administrator
 */
public abstract class CacheInvokeInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(CacheInvokeInterceptor.class);

	public abstract Object action() throws Exception;

	/**
	 * @desc 任务执行
	 * @param interfaceInfo
	 * @param header
	 * @param body
	 * @return
	 */
	public Object invoke(YzServiceInfo interfaceInfo, Header header, Body body) throws Exception {
		if (interfaceInfo != null) {
			StopWatch watch = new StopWatch();
			watch.start();
			TraceTransfer transfer = TraceTransfer.getTrace("bcc");
			try {
				Object result = null;
				transfer.setServiceName(interfaceInfo.getName());
				transfer.setRemark(interfaceInfo.getMethodRemark());
				if (interfaceInfo.isLocal()) {
					transfer.getTraceSpan("bcc", interfaceInfo.getInterfaceMethod(), OSUtil.getIp());
				}
				// 接口是否启用缓存 cacheKey,cacheRule must not null
				if (interfaceInfo.isUseCache()) {
					String key = getCacheKey(interfaceInfo, header, body);
					Object obj = getCache(interfaceInfo, key);
					if (obj != null) {
						return obj;
					}
					obj = action();
					if (obj != null) {
						setCache(interfaceInfo, key, obj);
					}
					result = obj;
				} else {
					logger.info("interfaceInfo is not enable cache!");
					result = action();
				}
				return result;
			} catch (Exception ex) {
				transfer.setIsError(1);
				if(ex instanceof GenericException) {
					GenericException generic = (GenericException) ex;
					transfer.setErrorCode(generic.getExceptionMessage());
				}else {
					transfer.setErrorCode(ex.getMessage());
				}
				throw ex;
			} finally {
				long destination = watch.getTime();
				transfer.setDestination(destination);
				TraceSender.sendTrace(transfer);
				TraceTransfer.getTrace().remove();
			}
		}
		logger.error("interfaceInfo is null");
		throw new CustomException("interfaceInfo is null");
	}

	/**
	 * setCache
	 */
	private void setCache(YzServiceInfo interfaceInfo, String key, Object result) {
		String cacheHandler = interfaceInfo.getCacheHandler();
		RedisCacheHandler redisCacheHandler = ApplicationContextUtil.getBean(cacheHandler);
		redisCacheHandler.setCache(interfaceInfo.getRedisName(), key, result, interfaceInfo.getCacheExpire());
	}

	/**
	 *
	 * @param interfaceInfo
	 * @param header
	 * @param body
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getCacheKey(YzServiceInfo interfaceInfo, Header header, Body body) {
		RedisCacheRule cacheRule = RedisCacheRuleFactory.getInstance().getRedisCacheRule(interfaceInfo.getCacheRule());
		if (null == body)
			body = new Body();
		body.put("appType", header.getAppType());
		body.put("empId", header.getEmpId());
		body.put("stdId", header.getStdId());
		body.put("userId", header.getUserId());
		body.put("learnId", header.getLearnId());
		body.put("openId", header.getOpenId());
		String key = cacheRule.getCacheName(interfaceInfo.getCacheKey(), body);
		return key;
	}

	/**
	 * 直接返回给调用端使用 ，无需额外转化为相应的pojo实体类
	 */
	private Object getCache(YzServiceInfo interfaceInfo, String key) {
		String cacheHandler = interfaceInfo.getCacheHandler();
		RedisCacheHandler redisCacheHandler = ApplicationContextUtil.getBean(cacheHandler);
		Object obj = redisCacheHandler.getCache(interfaceInfo.getRedisName(), key, null);
		return obj;
	}

}
