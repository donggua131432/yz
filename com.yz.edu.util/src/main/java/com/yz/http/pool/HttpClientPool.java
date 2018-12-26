package com.yz.http.pool;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yz.http.interceptor.HttpInterceptor;

/**
 * 
 * @author Administrator
 *
 */
public class HttpClientPool {

	private static Logger logger = LoggerFactory.getLogger(HttpClientPool.class);

	PoolingHttpClientConnectionManager cm = null;

	private static class HttpClientPoolHolder {
		private static HttpClientPool _instance = new HttpClientPool();
	}

	public static HttpClientPool getInstance() {
		return HttpClientPoolHolder._instance;
	}

	private HttpClientPool() {
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			logger.error("init.error:{}", e);
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
		cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
		Runtime.getRuntime().addShutdownHook(new Thread(cm::close));
	}

	/**
	 * 
	 * @param interceptor
	 * @return
	 */
	public CloseableHttpClient getHttpClient(HttpInterceptor interceptor) {
		if (interceptor == null) {
			return HttpClients.custom().setConnectionManager(cm).build();
		}
		CloseableHttpClient httpClient = HttpClients.custom().addInterceptorFirst((HttpRequestInterceptor) interceptor)
				.addInterceptorLast((HttpResponseInterceptor) interceptor).setConnectionManager(cm).build();
		return httpClient;
	}

	/**
	 * 
	 * @param interceptor
	 * @return
	 */
	public String invoke(HttpUriRequest request, HttpInterceptor interceptor) {
		StopWatch sw = new StopWatch();
		sw.start();
		CloseableHttpResponse response = null;
		InputStream in = null;
		try {
			HttpContext context = new BasicHttpContext();
			context.setAttribute("sw", sw);
			CloseableHttpClient client = this.getHttpClient(interceptor);
			response = client.execute(request, context);
			in = response.getEntity().getContent();
			return IOUtils.toString(in);
		} catch (Exception e) {
			logger.error("发送Http请求出现异常！", e);
		}
		// 使用finally块来关闭输入流
		finally {
			IOUtils.closeQuietly(in);
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("发送Http请求出现异常！", e);
				}
			}
		}
		return null;
	}
}
