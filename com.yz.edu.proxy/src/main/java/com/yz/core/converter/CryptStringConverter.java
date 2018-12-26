package com.yz.core.converter;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;

import com.yz.exception.CustomException;
import com.yz.util.CodeUtil;
import com.yz.util.StringUtil;


public class CryptStringConverter extends AbstractCryptStringConverter {

	public CryptStringConverter(MediaType supportedMediaType) {
		super(Collections.singletonList(supportedMediaType));
	}

	public CryptStringConverter(List<MediaType> supportedMediaTypes) {
		super(supportedMediaTypes);
	}

	@Override
	protected String requestProcess(String readCommunicationString,
			String key) {
		String requestString = null;
		try {
			if (StringUtil.hasValue(key))
				requestString = CodeUtil.DES.decrypt(readCommunicationString,
						key);
			else
				requestString = CodeUtil.DES.decrypt(readCommunicationString);
		} catch (Exception e) {
			throw new CustomException("请求数据解析失败", e);
		}

		return requestString;
	}

	@Override
	protected String responseProcess(String response, String key) {
		if (StringUtil.hasValue(key))
			return CodeUtil.DES.encrypt(response, key);
		else
			return CodeUtil.DES.encrypt(response);
	}
}
