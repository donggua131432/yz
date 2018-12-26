package com.yz.trace.filter;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.support.RpcUtils;
import com.yz.edu.trace.TraceAnnotation;
import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.trace.sender.TraceSender;

@Activate(group = { "provider", "consumer" })
/**
 * 
 * @desc dubbo采集点
 * @step consumer 消费端在记录Span，以及记录相应的Annotation
 * @step provider 服务提供者span记录另一个进程的span以及相应的Annotations
 * @author lingdian
 *
 */
public class RpcTraceFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(RpcTraceFilter.class);

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		StopWatch watch = new StopWatch();
		watch.start();
		TraceTransfer transfer = TraceTransfer.getTrace();
		RpcContext context = RpcContext.getContext(); // 构建RPC调用的上下文环境
		boolean isAsync = RpcUtils.isAsync(invoker.getUrl(), invocation);
		boolean isConsumerSide = context.isConsumerSide(); // 是否是服务端
		TraceSpan span = getSpanInfo(transfer, invocation, context, isConsumerSide);
		span.setAsync(isAsync ? 1 : 0);
		try {
			Result result = invoker.invoke(invocation); // 执行具体的方法体
			if (result.hasException()) {
				setSpanEx(span, result.getException().getMessage());
			}
			return result;
		} catch (RpcException ex) {
			setSpanEx(span, ex.getMessage());
			logger.error(ex.getMessage(), ex);
			throw ex;
		} finally {
			long duration = watch.getTime();
			transfer.setParentTraceSpanCurrent(span);
			addAnnotation(invocation, context, span, duration, isConsumerSide);
		}
	}

	/**
	 * @desc 设置错误信息
	 * @param span
	 * @param error
	 */
	private void setSpanEx(TraceSpan span, String error) {
		span.setIsError(1);
		span.setErrorCode(error);
	}

	/**
	 * 
	 * @param invocation
	 * @param context
	 * @param span
	 * @param duration
	 * @param isConsumerSide
	 */
	private void addAnnotation(Invocation invocation, RpcContext context, TraceSpan span, long duration,
			boolean isConsumerSide) {
		if (isConsumerSide) // 消费端埋点，添加相应的Annotation埋点
		{
			addConsumerAnnotation(invocation, context, span, duration);
		} else // 消费提供者调用直接将数据推送到监控平台处理
		{
			span.setDestination(duration);
			TraceSender.sendSpan(span);
			TraceTransfer.getTrace().remove();
		}
	}

	/**
	 * @desc 在服务消费端添加消费埋点
	 * @param invocation
	 * @param context
	 * @param span
	 * @param duration
	 */
	public void addConsumerAnnotation(Invocation invocation, RpcContext context, TraceSpan span, long duration) {
		if (null != span) {
			String methodName = invocation.getMethodName();
			String interfaceName = context.getUrl().getServiceInterface();
			String content = "consumer:" + interfaceName + "." + methodName + ";";
			TraceAnnotation traceAnnotation = new TraceAnnotation();
			traceAnnotation.setResouceType(TraceConstant.TRACE_RPC);
			traceAnnotation.setDestination(duration);
			traceAnnotation.setOperation(content);
			traceAnnotation.setResouceURI(context.getUrl().getHost() + ":" + context.getUrl().getPort());
			span.setDestination(duration);
			span.addTraceAnnotation(traceAnnotation);
		}
	}

	/**
	 * 
	 * @param invocation
	 *            方法体调用的封装器
	 * @param context
	 *            RPC RPC调用上下文
	 * @param isConsumerSide
	 *            是否是消费端
	 * @return
	 */
	private TraceSpan getSpanInfo(TraceTransfer transfer, Invocation invocation, RpcContext context,
			boolean isConsumerSide) {
		// 从RPC环境获取traceId
		String methodName = context.getMethodName();
		String interfaceName = context.getUrl().getParameter("interface");
		String applicationName = context.getUrl().getParameter("application");
		String host = context.getUrl().getHost();
		TraceSpan span = transfer.getTraceSpan(interfaceName + "." + methodName); // 获取当前方法的调用span
		if (isConsumerSide) { // 如果是消费端
			// 以及spanId 往下传递
			// 将traceId
			if (span == null) // 并且调用方法块为空
			{
				span = transfer.getTraceSpan(applicationName, methodName, host);
				((RpcInvocation) invocation).setAttachment("traceId", span.getTraceId());
			} else {
				TraceSpan nowSpan = transfer.getTraceSpan(span.getTraceId(), applicationName, methodName, host,
						TraceConstant.TRACE_SPAN_SYNC);
				nowSpan.setPid(span.getTid());
				((RpcInvocation) invocation).setAttachment("traceId", span.getTraceId());
				return nowSpan;
			}
		} else {
			String traceId = invocation.getAttachment("traceId");
			applicationName = "provider:" + applicationName;
			span = transfer.getTraceSpan(traceId, applicationName, methodName, host, 0);
		}
		return span;
	}

}
