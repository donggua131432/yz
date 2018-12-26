package com.yz.trace.sender;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.mq.Event;
import com.yz.mq.MQProducer; 
import com.yz.util.JsonUtil;

/**
 * 
 * @desc trace的发送者
 * @author Administrator
 *
 */
public class TraceSender implements TraceConstant {

	private static final Logger logger = LoggerFactory.getLogger(TraceSender.class);

	private static MQProducer mqProducer = MQProducer.getMQProducer();

	private TraceSender() {
	}

	/**
	 * 
	 * @desc 将调用链监控数据推送到相应的Mq topic
	 * @param info
	 */
	public static void sendTrace(TraceTransfer info) {
		if (info == null) {
			logger.error("info is null!");
			return;
		}
		logger.debug("sendTrace.trace:{}", JsonUtil.object2String(info));
		int partition = Math.abs(info.getTraceId().hashCode() % 4);
		Event event = new Event(TRACE_INFO_TOPIC, info);
		mqProducer.send(event, partition);
	}

	/**
	 * 
	 * @desc 将调用链监控数据推送到相应的Mq topic
	 * @param span
	 */
	public static void sendSpan(TraceSpan span) {
		if (span == null)
			return;
		logger.debug("sendSpan.span:{}", JsonUtil.object2String(span));
		int partition = Math.abs(span.getTraceId().hashCode() % 4);
		span.setDestination((new Date().getTime() - span.getDate().getTime()) / 1000);
		Event event = new Event(TRACE_SPAN_TOPIC, span);
		mqProducer.send(event, partition);
	}
}
