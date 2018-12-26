package com.yz.edu.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.model.BaseEsTraceObj;
import com.yz.edu.model.EsTraceAnnotation;
import com.yz.edu.model.EsTraceSpan;
import com.yz.edu.mq.MessageHandler;
import com.yz.edu.mq.Processor;
import com.yz.edu.trace.TraceSpan;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.mq.Event;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;

@Service(value = "traceSpanService")
@Processor(topic = TraceConstant.TRACE_SPAN_TOPIC)
public class TraceSpanService extends TraceInfoService implements MessageHandler<Event>, ObservatoryStarConstant {

	@Override
	public void execute(List<Event> events) throws Exception {
		List<BaseEsTraceObj> traceDatas = Lists.newArrayList();
		try {
			events.parallelStream().filter(Objects::nonNull).forEach(v -> {
				TraceSpan span = (TraceSpan) v.getEvent();
				traceDatas.addAll(EsTraceSpan.wrap(span));
				traceDatas.addAll(EsTraceAnnotation.wrap(span));
			});
			saveTrace(traceDatas);
		} catch (Exception ex) {
			logger.error("execute.error{},param:{}", ExceptionUtil.getStackTrace(ex),
					JsonUtil.object2String(traceDatas));
			throw ex; 
		}
	}

}
