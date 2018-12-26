package com.yz.core.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.yz.core.constants.AppConstants;
import com.yz.core.util.RequestUtil;
import com.yz.exception.IRuntimeException;
import com.yz.model.communi.Communication;
import com.yz.model.communi.Request;
import com.yz.util.JsonUtil;
import com.yz.util.JWTUtil;
import com.yz.util.StringUtil;

/**
 * 交互报文加解密
 * 
 * @author cyf
 *
 */
public abstract class AbstractCryptStringConverter
		extends AbstractHttpMessageConverter<Communication> {
	
	private static final String CURRENT_MEDIA_TYPE = "_yz_current_mediaType";

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractCryptStringConverter.class);

	private static final ThreadLocal<String> KEYS = new ThreadLocal<String>();

	/**
	 * 判断请求信息是否需要转换
	 */
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		boolean canRead = supports(clazz) && isMatch(mediaType);
		RequestUtil.setAttribute(CURRENT_MEDIA_TYPE, mediaType);
		if (canRead) {
			KEYS.set(readKey());
		}
		return canRead;
	}

	private String readKey() {
		String auth = RequestUtil.getHeader("auth");
		if (StringUtil.hasValue(auth)) {
			JWTUtil.Claims claims = JWTUtil.parseJwt(auth);
			String key = claims.getKey();
			String jwtToken = claims.getJwtToken();
			RequestUtil.setAttribute(AppConstants.PUB_JWT_TOKEN, jwtToken);
			return key;
		}
		return null;
	}

	/**
	 * 判断响应信息是否需要转换
	 */
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		if(mediaType == null)
			return false;
		boolean canWrite = isMatch(RequestUtil.getAttribute(CURRENT_MEDIA_TYPE, MediaType.class));
		return canWrite && supports(clazz);
	}

	AbstractCryptStringConverter(List<MediaType> supportedMediaTypes) {
		setSupportedMediaTypes(supportedMediaTypes);
	}

	/**
	 * 转换请求信息
	 */
	@Override
	protected Communication readInternal(
			Class<? extends Communication> clazz,
			HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		InputStream is = inputMessage.getBody();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String request = br.readLine();

		if (!StringUtil.hasValue(request))
			throw new HttpMessageNotReadableException("请求数据不能为空");
		br.close();
		br = null;
		isr.close();
		isr = null;
		is.close();
		is = null;

		String releaseRequest = requestProcess(request, KEYS.get());

		logger.debug(" -------------------------------------[解析后请求报文] "
				+ releaseRequest);

		Communication hc = convertFrom(clazz, releaseRequest);

		return hc;
	}

	protected abstract String requestProcess(String readCommunicationString,
			String key)
			throws HttpMessageNotWritableException, IRuntimeException;

	/**
	 * 转换响应信息
	 */
	@Override
	protected void writeInternal(Communication t,
			HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		OutputStream os = outputMessage.getBody();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(osw);
		String response = stringForm(t);

		String packageResponce = responseProcess(response, KEYS.get());

		logger.debug("-------------------------------------[封装后响应报文] "
				+ packageResponce);

		bw.write(packageResponce);

		bw.close();
		bw = null;
		osw.close();
		osw = null;
		os.close();
		os = null;
	}

	protected String stringForm(Communication t)
			throws HttpMessageNotWritableException {
		String s = JsonUtil.object2String(t);

		if (!StringUtil.hasValue(s)) {
			throw new HttpMessageNotReadableException("返回数据不能为空！");
		}

		return s;
	}

	protected abstract String responseProcess(String response, String key)
			throws HttpMessageNotWritableException, IRuntimeException;

	protected Communication convertFrom(
			Class<? extends Communication> clazz, String request)
			throws HttpMessageNotWritableException {
		if (!StringUtil.hasValue(request))
			return new Request();

		Communication hc = null;

		try {
			hc = JsonUtil.str2Object(request, Request.class);
		} catch (Exception e) {
			throw new HttpMessageNotReadableException("转换对象失败，请求数据格式不正确");
		}
		return hc;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return Communication.class.isAssignableFrom(clazz);
	}
	
	public boolean isMatch(MediaType mediaType) {
		List<MediaType> myMediaTypes = getSupportedMediaTypes();
		if(myMediaTypes == null || myMediaTypes.isEmpty())
			return false;
		if(mediaType == null)
			return false;
		
		for(MediaType mt : myMediaTypes) {
			String iType = mt.getType();
			String iSubType = mt.getSubtype();	
			if(iType.equals(mediaType.getType()) && iSubType.equals(mediaType.getSubtype())) {
				return true; 
			}
		}
		
		return false;
	}

}
