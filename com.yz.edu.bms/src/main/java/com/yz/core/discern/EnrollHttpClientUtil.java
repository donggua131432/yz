package com.yz.core.discern;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.yz.util.StringUtil;


public class EnrollHttpClientUtil {
	private static HttpClientContext context = HttpClientContext.create();
	private static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(120000)
			.setSocketTimeout(60000).setConnectionRequestTimeout(60000).setCookieSpec(CookieSpecs.STANDARD_STRICT)
			.setExpectContinueEnabled(true)
			.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
			.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();

	private static CloseableHttpClient createHttpClient(CookieStore cookieStore) {
		if (cookieStore == null) {
			cookieStore = new BasicCookieStore();
		}

		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultCookieStore(cookieStore)
				.setDefaultRequestConfig(requestConfig).build();

		return httpClient;
	}

	private static CloseableHttpClient createHttpClient() {
		return createHttpClient(null);
	}

	/**
	 * http get
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public static String doGet(String url, String data) {
		CookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultCookieStore(cookieStore)
				.setDefaultRequestConfig(requestConfig).build();

		HttpGet httpGet = new HttpGet(url);

		try {
			return toString(httpClient.execute(httpGet, context));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * http post
	 * 
	 * @param url
	 * @param values
	 * @return
	 */
	public static String doPost(String url, List<NameValuePair> values) {

		return doPost(url, values, null);
	}

	/**
	 * http post
	 * 
	 * @param url
	 * @param values
	 * @return
	 */
	public static String doPost(String url, List<NameValuePair> values, Map<String, String> headers) {
		CookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultCookieStore(cookieStore)
				.setDefaultRequestConfig(requestConfig).build();

		HttpPost httpPost = new HttpPost(url);

		if (headers != null && !headers.isEmpty()) {
			Set<Entry<String, String>> s = headers.entrySet();
			Iterator<Entry<String, String>> it = s.iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}

		if (values != null) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Consts.UTF_8);
			httpPost.setEntity(entity);
		}

		try {
			return toString(httpClient.execute(httpPost, context));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String doXmlPost(String url, String data) {
		CookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				.setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultCookieStore(cookieStore)
				.setDefaultRequestConfig(requestConfig).build();

		HttpPost httpPost = new HttpPost(url);

		httpPost.addHeader("Content-type", "text/xml");

		StringEntity sEntity = new StringEntity(data, "UTF-8");
		httpPost.setEntity(sEntity);

		try {
			return toString(httpClient.execute(httpPost, context));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 直接把Response内的Entity内容转换成String
	 * 
	 * @param httpResponse
	 * @return
	 */
	private static String toString(HttpResponse httpResponse) {
		// 获取响应消息实体
		String result = null;

		try {
			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				((CloseableHttpResponse) httpResponse).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	static CookieStore cookieStore = null;

	/**
	 * 报名入口(可修改数据)
	 * @param values
	 * @param sessionId
	 * @return
	 */
	public static Result enroll(List<NameValuePair> values, String sessionId) {
		CloseableHttpClient httpClient = createHttpClient();

		HttpPost httpPost = new HttpPost(EnrollOnlineConstants.ENROLL_URL);

		if (values != null) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Charset.forName("GBK"));
			httpPost.setEntity(entity);
		}

		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		try {
			HttpResponse response = httpClient.execute(httpPost, context);
			
			return new Result(toString(response), getSessionId(response));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 报名结果查询页面
	 * 
	 * @param sessionId
	 * @return
	 */
	public static Result enrollResult(String sessionId) {
		CloseableHttpClient httpClient = createHttpClient(createCookieStore(sessionId));

		HttpGet httpGet = new HttpGet(EnrollOnlineConstants.ENROLL_RESULT_URL);

		try {
			HttpResponse response = httpClient.execute(httpGet, context);
			return new Result(toString(response), getSessionId(response));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 下载准考证
	 * @param sessionId
	 * @param savePrefix
	 * @param saveUrl
	 * @return
	 */
	public static boolean proveDownloadResult(String sessionId, String savePrefix, String saveUrl, String fileName) {
		CloseableHttpClient httpClient = createHttpClient(createCookieStore(sessionId));
		HttpGet httpGet = new HttpGet(EnrollOnlineConstants.DYZKZ_URL);

		InputStream in = null;
		FileOutputStream out = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, context);
			in = response.getEntity().getContent();
			long contextLength = response.getEntity().getContentLength();
			String contentType = response.getEntity().getContentType().getValue();
			System.out.println("返回数据类型："+contentType+ "===============返回数据长度："+contextLength);
			//Content-Type: application/pdf  Content-Type: text/html;charset=gbk
			if (!contentType.equals("application/pdf")) {
				System.out.println("预报名号【"+fileName+"】无法下载准考证！");
				return false;
			}

			File filePath = new File(savePrefix+saveUrl);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}

			File file = new File(savePrefix+saveUrl+File.separator+fileName+".pdf");
			if (!file.exists()) {
				file.createNewFile();
			}

			out = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int readLength = 0;
			while ((readLength = in.read(buffer)) > 0) {
				byte[] bytes = new byte[readLength];
				System.arraycopy(buffer, 0, bytes, 0, readLength);
				out.write(bytes);
			}
			out.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 学历认证结果查询页面
	 * 
	 * @param sessionId
	 * @return
	 */
	public static Result confirmResult(String sessionId) {
		CloseableHttpClient httpClient = createHttpClient(createCookieStore(sessionId));

		HttpGet httpGet = new HttpGet(EnrollOnlineConstants.CONFIRM_RESULT_URL);

		try {
			HttpResponse response = httpClient.execute(httpGet, context);
			return new Result(toString(response), sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param ybmh 已报名号
	 * @param xm 姓名
	 * @param zjh 证件号码
	 * @param sessionId
	 * @return
	 */
	public static Result confirm(String ybmh, String xm, String zjh, String sessionId) {
		List<NameValuePair> values = new ArrayList<NameValuePair>();

		values.add(new BasicNameValuePair("ybmh", ybmh));
		values.add(new BasicNameValuePair("xm", xm));
		values.add(new BasicNameValuePair("zjh", zjh));
		values.add(new BasicNameValuePair("xjxl", "1"));// 不知道是什么字段

		return confirm(values, sessionId);
	}

	/**
	 * 学历验证
	 * 
	 * @param values
	 * @param sessionId
	 * @return
	 */
	public static Result confirm(List<NameValuePair> values, String sessionId) {

		CloseableHttpClient httpClient = createHttpClient(createCookieStore(sessionId));

		HttpPost httpPost = new HttpPost(EnrollOnlineConstants.CONFIRM_URL);

		if (values != null) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Charset.forName("GBK"));
			httpPost.setEntity(entity);
		}

		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

		try {
			HttpResponse response = httpClient.execute(httpPost, context);

			return new Result(toString(response), sessionId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Result login(String username, String pwd) {
		
		CloseableHttpClient httpClient = createHttpClient();
		
		HttpPost httpPost = new HttpPost(EnrollOnlineConstants.LOGIN_DO_URL);
		
		String sessionId = home();
		
		byte[] imgByteArray = img(sessionId);
		
		DiscernUtil.init(null);//TODO 去掉
		
		String valiCode = DiscernUtil.getCode(imgByteArray);
		if(valiCode == null){
			return null;
		}
		
		List<NameValuePair> values = new ArrayList<NameValuePair>();
		
		values.add(new BasicNameValuePair("id", username));
		values.add(new BasicNameValuePair("pwd", pwd));
		values.add(new BasicNameValuePair("verifyCodeImg", valiCode));
		
		if (values != null) {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(values, Charset.forName("GBK"));
			httpPost.setEntity(entity);
		}
		
		/*httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");*/
		
		try {
			HttpResponse response = httpClient.execute(httpPost, context);
			
			return new Result(toString(response), sessionId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 现场确认结果
	 * @param sessionId
	 * @return
	 */
	public static Result localConfirm(String sessionId) {
		CloseableHttpClient httpClient = createHttpClient(createCookieStore(sessionId));

		HttpGet httpGet = new HttpGet(EnrollOnlineConstants.LOCAL_CONFIRM_URL);

		try {
			HttpResponse response = httpClient.execute(httpGet, context);
			return new Result(toString(response), sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取验证码图片字节数组
	 * 
	 * @param sessionId
	 * @return
	 */
	public static byte[] img(String sessionId) {
		CloseableHttpClient httpClient = createHttpClient(createCookieStore(sessionId));
		try {
			HttpGet httpGet = new HttpGet(EnrollOnlineConstants.VALI_CODE);
			httpGet.addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			httpGet.addHeader("Accept", "image/webp,image/apng,image/*,*/*");
			httpGet.addHeader("Referer", "http://www.ecogd.edu.cn/cr/cgbm/login.jsp");
			httpGet.addHeader("Host", "www.ecogd.edu.cn");
			httpGet.addHeader("content-type", "image/jpeg, image/png/, image/*");

			CloseableHttpResponse response = httpClient.execute(httpGet, context);

			HttpEntity entity = response.getEntity();

			if (entity != null && entity.isStreaming()) {
				return EntityUtils.toByteArray(entity);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 获取sessionId
	 * @return
	 */
	public static String home() {
		CloseableHttpClient httpClient = createHttpClient();

		HttpGet httpGet = new HttpGet(EnrollOnlineConstants.LOGIN_URL);

		try {
			return getSessionId(httpClient.execute(httpGet, context));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据sessionId创建Cookie
	 * 
	 * @param sessionId
	 * @return
	 */
	public static CookieStore createCookieStore(String sessionId) {
		if(StringUtil.isEmpty(sessionId))
			return null;
		CookieStore cookieStore = new BasicCookieStore();

		BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", sessionId);

		cookieStore.addCookie(cookie);
		return cookieStore;
	}

	/**
	 * 获取sessionId
	 * 
	 * @param httpResponse
	 * @return
	 */
	public static String getSessionId(HttpResponse httpResponse) {

		Header header = httpResponse.getFirstHeader("Set-Cookie");

		if (header == null)
			return null;
		String sessionStr = header.getValue();
		if (StringUtil.isEmpty(sessionStr))
			return null;

		return sessionStr.substring("JSESSIONID=".length(), sessionStr.indexOf(";"));
	}

	public static class Result {
		private String html;
		private String sessionId;

		public String getHtml() {
			return html;
		}

		public void setHtml(String html) {
			this.html = html;
		}

		public String getSessionId() {
			return sessionId;
		}

		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}

		public Result(String html, String sessionId) {
			this.html = html;
			this.sessionId = sessionId;
		}

		public Result() {
		}
	}

	
}
