package com.yz.edu.service;

import java.util.List;
import java.util.Objects;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.edu.model.BaseEsTraceObj;
import com.yz.edu.model.EsTraceAnnotation;
import com.yz.edu.model.EsTraceInfo;
import com.yz.edu.model.EsTraceSpan;
import com.yz.edu.mq.MessageHandler;
import com.yz.edu.mq.Processor;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.mq.Event;
import com.yz.util.ExceptionUtil;
import com.yz.util.JsonUtil;

@Service(value = "traceInfoService")
@Processor(topic = TraceConstant.TRACE_INFO_TOPIC)
public class TraceInfoService implements MessageHandler<Event>, ObservatoryStarConstant {

	private static final Logger logger = LoggerFactory.getLogger(TraceInfoService.class);

	@Autowired
	private TransportClient client;

	@Override
	public void execute(List<Event> events) throws Exception {
		List<BaseEsTraceObj> traceData = Lists.newArrayList();
		try {
			events.parallelStream().filter(Objects::nonNull).forEach(v -> {
				TraceTransfer info = (TraceTransfer) v.getEvent();
				EsTraceInfo esTraceInfo = EsTraceInfo.wrap(info);
				traceData.add(esTraceInfo);
				traceData.addAll(EsTraceSpan.wrap(info));
				traceData.addAll(EsTraceAnnotation.wrap(info));
			});
			saveTrace(traceData);
		} catch (Exception ex) {
			logger.error("execute.error{},param:{}", ExceptionUtil.getStackTrace(ex),
					JsonUtil.object2String(traceData));
			throw ex;
		}
	}

	/**
	 * 
	 * @param esTraceInfos
	 */
	public void saveTrace(List<? extends BaseEsTraceObj> datas) {
		if (datas == null || datas.isEmpty()) {
			logger.error("saveTrace.error,datas is null");
			return;
		}
		try {
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			for (BaseEsTraceObj trace : datas) {
				IndexRequestBuilder bu = client.prepareIndex(trace.indexName(), trace.indexName());
				bu.setSource(trace.toXContentBuilder());
				bu.setId(trace.getId());
				bu.setTimeout("5m");
				if (bu != null)
					bulkRequest.add(bu);
				logger.info("addTobulkRequest.traceData:{},addTobulkRequest-2:{}", JsonUtil.object2String(trace),
						JsonUtil.object2String(bu));
			}

			if (bulkRequest.numberOfActions() > 0) {
				BulkResponse bulkResponse = bulkRequest.execute().actionGet();
				if (bulkResponse.hasFailures()) {
					logger.error("saveTrace.error:{}", bulkResponse.buildFailureMessage());
				}
			}
		} catch (Exception ex) {
			logger.error("execute.error{}", ExceptionUtil.getStackTrace(ex));
			throw ex;
		}
	}

}
