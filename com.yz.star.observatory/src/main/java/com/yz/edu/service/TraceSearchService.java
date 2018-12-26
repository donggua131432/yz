package com.yz.edu.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.yz.edu.constant.ObservatoryStarConstant;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;

@Service(value = "traceSearchService")
@SuppressWarnings("unchecked")
public class TraceSearchService implements ObservatoryStarConstant {

	private Logger logger = LoggerFactory.getLogger(TraceSearchService.class);

	@Autowired
	private TransportClient transportClient;

	/**
	 * @desc 根据Trace查询TraceInfo,TraceSpan,TraceAnn
	 * @param traceId
	 * @return
	 */
	public Object search(String traceId) {
		return this.searchTraceInfo(traceId);
	}

	/**
	 * @desc
	 * @param traceId
	 * @return
	 */
	private List<Map<String, Object>> searchTraceInfo(String traceId) {
		List<Map<String, Object>> datas = Lists
				.newArrayList(this.queryTraceData(OBSERVATORY_STAR_INFO, OBSERVATORY_STAR_INFO, traceId));
		datas.addAll(this.queryTraceData(OBSERVATORY_STAR_SPAN, OBSERVATORY_STAR_SPAN, traceId));
		datas.addAll(this.queryTraceData(OBSERVATORY_STAR_ANNOTATION, OBSERVATORY_STAR_ANNOTATION, traceId));
		return handlerTraceData(datas);
	}

	/**
	 * 
	 * @param index
	 * @param type
	 * @param traceId
	 * @return
	 */
	private List<Map<String, Object>> queryTraceData(String index, String type, String traceId) {
		List<Map<String, Object>> result = Lists.newArrayList();
		MatchQueryBuilder mqb = QueryBuilders.matchQuery("traceId", traceId);
		SearchResponse response = transportClient.prepareSearch(index).setTypes(type)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(mqb)
				.setSize(50)
				.setExplain(true).execute().actionGet();
		SearchHits hits = response.getHits();
		hits.forEach(v -> {
			Map<String, Object> data = v.getSourceAsMap();
			data.put("index", index);
			result.add(data);
		});
		logger.info("searchTraceInfo.index:{},result:{}", index, JsonUtil.object2String(result));
		return result;
	}

	/**
	 * @desc
	 * @step 1.根据index进行分组 2.将annotations根据spanId 分组并存入span 3.规整spans数据，子父级
	 *       4.将spans数据加入traceInfo
	 * @param datas
	 */
	private List<Map<String, Object>> handlerTraceData(List<Map<String, Object>> datas) {
		Map<Object, List<Map<String, Object>>> indexMap = datas.parallelStream()
				.collect(Collectors.groupingBy(v -> v.get("index")));
		List<Map<String, Object>> infos = indexMap.get(OBSERVATORY_STAR_INFO);
		List<Map<String, Object>> spans = indexMap.get(OBSERVATORY_STAR_SPAN);
		List<Map<String, Object>> annotations = indexMap.get(OBSERVATORY_STAR_ANNOTATION);
		logger.info("handlerTraceData.infos:{},spans:{},annotations:{}", JsonUtil.object2String(infos), JsonUtil.object2String(spans), JsonUtil.object2String(annotations));
		addAnnsToSpan(annotations, spans); // 将annotations根据spanId分组并存入span
		spans = this.handlerSpanData(spans); // 规整spans数据，子父级
		addSpansToInfo(infos, spans);// 将spans数据加入traceInfo
		return infos;
	}

	/**
	 * 
	 * @param spans
	 * @return
	 */
	public List<Map<String, Object>> handlerSpanData(List<Map<String, Object>> spans) {
		if (spans == null || spans.isEmpty()) {
			logger.error("handlerSpanData.spans is null");
			return null;
		}
		List<Map<String, Object>> result = Lists.newArrayList();
		result.addAll(spans.parallelStream().filter(v -> StringUtil.isEmpty((String) v.get("pid")))
				.collect(Collectors.toList()));
		logger.info("handlerSpanData.data:{}", JsonUtil.object2String(result));
		spans.parallelStream().filter(v -> StringUtil.isNotBlank((String) v.get("pid")))
				.collect(Collectors.groupingBy(v -> v.get("pid"))).entrySet().parallelStream().sorted((y, x) -> {
					return x.getKey().toString().compareTo(y.getKey().toString());
				}).forEach(v -> {
					result.parallelStream().forEach(span -> {
						if (StringUtil.equalsIgnoreCase(span.get("tid").toString(), v.getKey().toString())) {
							span.put("spans", v.getValue());
						}
					});
				});
		return result;
	}

	/**
	 * 
	 * @param annotations
	 * @param spans
	 */
	public void addAnnsToSpan(List<Map<String, Object>> annotations, List<Map<String, Object>> spans) {
		if (spans == null || spans.isEmpty()) {
			logger.error("addAnnsToSpan.spans is null");
			return;
		}
		spans.parallelStream().forEach(span -> {
			annotations.parallelStream().forEach(v -> {
				if (StringUtil.equalsIgnoreCase(span.get("tid").toString(), v.get("spanId").toString())) {
					List<Map<String, Object>> ans = null;
					if (span.containsKey("annotations")) {
						ans = (List<Map<String, Object>>) span.get("annotations");
						ans.add(v);
					} else {
						ans = Lists.newArrayList(v);
					}
					span.put("annotations", ans);
				}
			});
		});
	}

	/**
	 * 
	 * @param infos
	 * @param traceSpans
	 */
	public void addSpansToInfo(List<Map<String, Object>> infos, List<Map<String, Object>> traceSpans) {
		if (infos == null || infos.isEmpty()) {
			logger.error("addSpansToInfo.infos is null");
			return;
		}
		infos.parallelStream().forEach(info -> {
			traceSpans.parallelStream().forEach(span -> {
				if (StringUtil.equalsIgnoreCase(span.get("traceId").toString(), info.get("traceId").toString())) {
					List<Map<String, Object>> spans = null;
					if (info.containsKey("spans")) {
						spans = (List<Map<String, Object>>) info.get("spans");
						spans.add(span);
					} else {
						spans = Lists.newArrayList(span);
					}
					info.put("spans", spans);
				}
			});
		});
	}

}
