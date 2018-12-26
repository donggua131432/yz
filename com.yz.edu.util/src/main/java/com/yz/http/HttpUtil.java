package com.yz.http;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.yz.http.interceptor.HttpInterceptor;
import com.yz.http.pool.HttpClientPool;
import com.yz.util.StringUtil;

public class HttpUtil {

	private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param, String contentType, HttpInterceptor interceptor) {
		String urlNameString = url + (StringUtil.hasValue(param) ? "?" + param : "");
		HttpGet httpGet = new HttpGet(urlNameString);
		httpGet.setHeader("accept", "*/*");
		httpGet.setHeader("connection", "Keep-Alive");
		httpGet.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		if (StringUtil.hasValue(contentType)) {
			httpGet.setHeader("content-type", contentType);
		}
		return HttpClientPool.getInstance().invoke(httpGet, interceptor);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param, HttpInterceptor interceptor) {
		return sendPost(url, param, null, interceptor);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param, String contentType, HttpInterceptor interceptor) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("accept", "*/*");
		httpPost.setHeader("connection", "Keep-Alive");
		httpPost.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		if (StringUtil.hasValue(contentType)) {
			httpPost.setHeader("content-type", contentType);
		}
		StringEntity entity = new StringEntity(param, DEFAULT_CHARSET);
		entity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(entity);
		return HttpClientPool.getInstance().invoke(httpPost, interceptor);
	}

	public static String sendGet(String url, HttpInterceptor interceptor) {
		return sendGet(url, null, interceptor);
	}

	public static String sendGet(String url, Map<String, String> param, HttpInterceptor interceptor) {
		return sendGet(url, paramString(param), "text/html;charset=utf-8", interceptor);
	}

	public static String sendPost(String url, Map<String, String> param, HttpInterceptor interceptor) {
		return sendPost(url, paramString(param), interceptor);
	}

	public static String sendPost(String url, Map<String, String> param, String contentType,
			HttpInterceptor interceptor) {
		return sendPost(url, paramString(param), contentType, interceptor);
	}

	private static String paramString(Map<String, String> param) {
		if (param == null || param.isEmpty()) {
			return null;
		}
		Set<String> keys = param.keySet();
		Iterator<String> it = keys.iterator();
		StringBuffer sb = new StringBuffer();
		while (it.hasNext()) {
			String key = it.next();
			sb.append(key).append("=").append(param.get(key)).append("&");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static String sendDelete(String url, Map<String, String> param, HttpInterceptor interceptor) {
		String p = paramString(param);
		String urlNameString = url + (StringUtil.hasValue(p) ? "?" + p : "");
		HttpDelete httpDelete = new HttpDelete(urlNameString);
		return HttpClientPool.getInstance().invoke(httpDelete, interceptor);
	}

}
