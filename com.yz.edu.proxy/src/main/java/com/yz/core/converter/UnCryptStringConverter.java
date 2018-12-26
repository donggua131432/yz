package com.yz.core.converter;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class UnCryptStringConverter extends AbstractCryptStringConverter {

	public UnCryptStringConverter(List<MediaType> supportedMediaTypes) {
		super(supportedMediaTypes);
	}
	
	public UnCryptStringConverter(MediaType supportedMediaType) {
		super(Collections.singletonList(supportedMediaType));
	}

	@Override
	protected String requestProcess(String request, String key) throws HttpMessageNotWritableException {
		return request;
	}

	@Override
	protected String responseProcess(String response, String key) throws HttpMessageNotWritableException {
		return response;
	}

}
