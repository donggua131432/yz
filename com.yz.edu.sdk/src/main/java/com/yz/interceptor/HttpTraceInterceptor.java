package com.yz.interceptor;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.edu.trace.TraceAnnotation;
import com.yz.edu.trace.TraceTransfer;
import com.yz.edu.trace.constant.TraceConstant;
import com.yz.http.interceptor.HttpInterceptor;
import com.yz.util.JsonUtil;

/**
 * 
 * 
 * @author Administrator
 *
 */
public interface HttpTraceInterceptor {

	public static Logger logger = LoggerFactory.getLogger(HttpTraceInterceptor.class);

	public static final String TRACEANNOTATION = "HttpTraceInterceptor.TraceAnnotation";

	public static final HttpInterceptor TRACE_INTERCEPTOR = new HttpInterceptor() {

		@Override
		public void process(HttpRequest httpRequest, HttpContext context) throws HttpException, IOException {
			HttpRequestWrapper request = (HttpRequestWrapper) httpRequest;
			URI uri = request.getURI();
			TraceAnnotation annotation = new TraceAnnotation();
			annotation.setResouceType(TraceConstant.TRACE_HTTP);
			annotation.setResouceURI(request.getOriginal().toString());
			annotation.setOperation("query:" + uri.getQuery() + ";path" + uri.getPath());
			context.setAttribute(TRACEANNOTATION, annotation);
			logger.info("request.info:{}", JsonUtil.object2Map(request.getRequestLine()));
		}

		@Override
		public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
			TraceAnnotation annotation = (TraceAnnotation) context.getAttribute(TRACEANNOTATION);
			StopWatch sw = (StopWatch) context.getAttribute("sw");
			annotation.setDestination(sw.getTime());
			context.removeAttribute("sw");
			context.removeAttribute(TRACEANNOTATION);
			TraceTransfer.getTrace().addAnnotation(annotation); // traceInfo
			logger.info("response.info:{};costTime:{}", response.getStatusLine(),sw.getTime());
		}
	};
}
