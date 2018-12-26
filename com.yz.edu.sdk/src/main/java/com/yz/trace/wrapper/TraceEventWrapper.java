package com.yz.trace.wrapper;

import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;
import com.yz.model.BaseEvent;

/**
 * 
 * @desc Task 发送接收Event的包装器
 * @author Administrator
 *
 */
public class TraceEventWrapper {

	/**
	 * @desc 发送Event
	 * @param event
	 * @return
	 */
	public static BaseEvent wrapper(BaseEvent event) {
		if (event.isTrace()) {
			TraceTransfer traceTransfer = TraceTransfer.getTrace();
			TraceSpan span = traceTransfer.getCurrentSpan(); 
			if(span==null)
			{
				event.setTraceId(traceTransfer.getTraceId());
			}else
			{
				event.setTraceId(span.getTraceId());
			} 
		}
		return event;
	}
}
