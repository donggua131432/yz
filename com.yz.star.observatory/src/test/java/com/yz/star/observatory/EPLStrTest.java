package com.yz.star.observatory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.google.common.collect.Lists;
import com.yz.edu.service.TraceSearchService;
import com.yz.util.JsonUtil;
import com.yz.util.StringUtil;


public class EPLStrTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {
		List infos =  JsonUtil.str2List(FileUtils.readFileToString(new File("d://info.txt"),"utf-8"), Map.class);
		List spans = JsonUtil.str2List(FileUtils.readFileToString(new File("d://span.txt"),"utf-8"), Map.class);
		List annotations = JsonUtil.str2List(FileUtils.readFileToString(new File("d://annotations.txt"),"utf-8"),
				Map.class);
		TraceSearchService searchService = new TraceSearchService();
		searchService.addAnnsToSpan((List<Map<String,Object>> )annotations, (List<Map<String,Object>> )spans); // 将annotations根据spanId分组并存入span
		System.out.println("spans=>" + JsonUtil.object2String(spans));
		spans = searchService.handlerSpanData(spans); // 规整spans数据，子父级
		System.out.println("spans=>" + JsonUtil.object2String(spans));
		searchService.addSpansToInfo((List<Map<String,Object>>)infos, (List<Map<String,Object>> )spans);// 将spans数据加入traceInfo
		System.out.println("main=>" + JsonUtil.object2String(infos));
		
	}
	
	
	 

	public static void testSearch() throws Exception {
		String str = StringUtil.EMPTY;

		String str1 = "1234";

		List<String> result = Lists.newArrayList(str, str1);
		for (int i = 0; i < 10; i++) {
			String uuid = StringUtil.UUID();
			result.add(uuid);
			System.out.println("uuid->" + uuid + ";index->" + i);
			Thread.sleep(1);
		}

		result.parallelStream().sorted((y, x) -> {
			return x.compareTo(y);
		});
		System.out.println(str.compareTo(str1));
		System.out.println(JsonUtil.object2String(result));

		RestClient restClient = RestClient.builder(new HttpHost("es.yzwill.cn", 80, "http")).build();
		HttpEntity entity = new NStringEntity(
				"{ \"query\": { \"term\": {\"traceId\":\"38090EE05815494FA06B789F5632A6D6\"}}}",
				ContentType.APPLICATION_JSON);
		Response response = restClient.performRequest("POST", "es-trace-span/_search", Collections.EMPTY_MAP, entity);
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent(), "utf-8"));
		StringBuilder sb = new StringBuilder();
		String text;
		while ((text = bufferedReader.readLine()) != null) {
			sb.append(text);
		}
		System.out.println(sb);
	}

}
